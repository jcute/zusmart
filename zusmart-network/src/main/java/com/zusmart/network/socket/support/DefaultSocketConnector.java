package com.zusmart.network.socket.support;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

import com.zusmart.basic.toolkit.support.AbstractExecutable;
import com.zusmart.network.NetAddress;
import com.zusmart.network.NetClientSetting;
import com.zusmart.network.handler.support.DefaultSocketSessionHandler;
import com.zusmart.network.socket.SocketBossEventLoop;
import com.zusmart.network.socket.SocketBossEventLoopGroup;
import com.zusmart.network.socket.SocketConnector;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionManager;
import com.zusmart.network.socket.SocketWorkEventLoop;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;
import com.zusmart.network.util.CloseUtils;

public class DefaultSocketConnector extends AbstractExecutable implements SocketConnector {

	private final NetAddress netAddress;
	private final NetClientSetting netClientSetting;
	private final SocketSessionManager socketSessionManager;
	private final SocketBossEventLoopGroup socketBossEventLoopGroup;
	private final SocketWorkEventLoopGroup socketWorkEventLoopGroup;

	private SocketChannel socketChannel;
	private SocketSession socketSession;

	private volatile boolean running = false;

	public DefaultSocketConnector(SocketSessionManager socketSessionManager, SocketBossEventLoopGroup socketBossEventLoopGroup, SocketWorkEventLoopGroup socketWorkEventLoopGroup, NetAddress netAddress, NetClientSetting netClientSetting) {
		if (null == netAddress) {
			throw new IllegalArgumentException("net address must not be null");
		}
		if (null == socketSessionManager) {
			throw new IllegalArgumentException("socket session manager must not be null");
		}
		if (null == socketBossEventLoopGroup) {
			throw new IllegalArgumentException("socket boss event loop group must not be null");
		}
		if (null == socketWorkEventLoopGroup) {
			throw new IllegalArgumentException("socket work event loop group must not be null");
		}
		if (null == netClientSetting) {
			throw new IllegalArgumentException("net client setting must not be null");
		}
		this.netAddress = netAddress;
		this.netClientSetting = netClientSetting;
		this.socketSessionManager = socketSessionManager;
		this.socketBossEventLoopGroup = socketBossEventLoopGroup;
		this.socketWorkEventLoopGroup = socketWorkEventLoopGroup;
	}

	@Override
	public SocketSessionManager getSocketSessionManager() {
		return this.socketSessionManager;
	}

	@Override
	public SocketBossEventLoopGroup getSocketBossEventLoopGroup() {
		return this.socketBossEventLoopGroup;
	}

	@Override
	public SocketWorkEventLoopGroup getSocketWorkEventLoopGroup() {
		return this.socketWorkEventLoopGroup;
	}

	@Override
	public NetAddress getServerAddress() {
		if (this.isOpen()) {
			return NetAddress.create((InetSocketAddress) this.socketChannel.socket().getRemoteSocketAddress());
		}
		return null;
	}

	@Override
	public NetAddress getClientAddress() {
		if (this.isOpen()) {
			return NetAddress.create((InetSocketAddress) this.socketChannel.socket().getLocalSocketAddress());
		}
		return null;
	}

	@Override
	public SocketSession getSocketSession() {
		return this.socketSession;
	}

	@Override
	public boolean isOpen() {
		return null != this.socketChannel && this.socketChannel.isOpen();
	}

	@Override
	protected void doStart() throws Exception {
		if (this.running == true) {
			return;
		}
		final CountDownLatch latch = new CountDownLatch(1);
		this.running = true;
		this.socketChannel = SocketChannel.open();

		this.socketChannel.socket().setKeepAlive(this.netClientSetting.isKeepAlive());
		this.socketChannel.socket().setReceiveBufferSize(this.netClientSetting.getReceiveBufferSize());
		this.socketChannel.socket().setSendBufferSize(this.netClientSetting.getSendBufferSize());
		this.socketChannel.socket().setSoTimeout(this.netClientSetting.getSoTimeout());
		this.socketChannel.socket().setTcpNoDelay(this.netClientSetting.isTcpNpDelay());

		this.socketChannel.connect(this.netAddress.toSocketAddress());
		this.socketChannel.finishConnect();
		this.socketSession = this.createSocketSession(this.socketChannel);
		this.socketSession.getSocketSessionHandlerChain().addLast("INTERNAL_WATTING", new DefaultSocketSessionHandler() {
			@Override
			public void onRegister(SocketSession session) {
				latch.countDown();
			}
		});
		this.socketSession.getSocketBossEventLoop().doRegister(this.socketSession);
		latch.await();
	}

	@Override
	protected void doClose() throws Exception {
		if (this.running == false) {
			return;
		}
		this.running = false;
		this.socketSession.close();
		CloseUtils.close(this.socketChannel);
		this.socketSession = null;
		this.socketChannel = null;
	}

	protected SocketSession createSocketSession(SocketChannel socketChannel) {
		SocketBossEventLoop socketBossEventLoop = this.socketBossEventLoopGroup.getEventLoop();
		SocketWorkEventLoop socketWorkEventLoop = this.socketWorkEventLoopGroup.getEventLoop();
		return this.socketSessionManager.createSocketSession(false, socketChannel, socketBossEventLoop, socketWorkEventLoop);
	}

}