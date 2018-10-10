package com.zusmart.network.socket.support;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.toolkit.support.AbstractExecutable;
import com.zusmart.basic.util.StringUtils;
import com.zusmart.network.NetAddress;
import com.zusmart.network.NetServerSetting;
import com.zusmart.network.socket.SocketAcceptor;
import com.zusmart.network.socket.SocketBossEventLoop;
import com.zusmart.network.socket.SocketBossEventLoopGroup;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionManager;
import com.zusmart.network.socket.SocketWorkEventLoop;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;
import com.zusmart.network.util.CloseUtils;

public class DefaultSocketAcceptor extends AbstractExecutable implements SocketAcceptor, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(DefaultSocketAcceptor.class);

	private final NetAddress netAddress;
	private final NetServerSetting netServerSetting;
	private final SocketSessionManager socketSessionManager;
	private final SocketBossEventLoopGroup socketBossEventLoopGroup;
	private final SocketWorkEventLoopGroup socketWorkEventLoopGroup;

	private Thread monitor;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;

	private volatile boolean running = false;

	public DefaultSocketAcceptor(SocketSessionManager socketSessionManager, SocketBossEventLoopGroup socketBossEventLoopGroup, SocketWorkEventLoopGroup socketWorkEventLoopGroup, NetAddress netAddress, NetServerSetting netServerSetting) {
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
		if (null == netServerSetting) {
			throw new IllegalArgumentException("net server setting must not be null");
		}
		this.netAddress = netAddress;
		this.netServerSetting = netServerSetting;
		this.socketSessionManager = socketSessionManager;
		this.socketBossEventLoopGroup = socketBossEventLoopGroup;
		this.socketWorkEventLoopGroup = socketWorkEventLoopGroup;
		this.monitor = new Thread(this, this.netServerSetting.getAcceptName());
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
			return NetAddress.create((InetSocketAddress) this.serverSocketChannel.socket().getLocalSocketAddress());
		}
		return null;
	}

	@Override
	public boolean isOpen() {
		return null != this.serverSocketChannel && this.serverSocketChannel.isOpen();
	}

	@Override
	protected void doStart() throws Exception {
		if (this.running == true) {
			return;
		}
		this.running = true;
		this.selector = Selector.open();
		this.serverSocketChannel = ServerSocketChannel.open();
		this.serverSocketChannel.socket().setReuseAddress(this.netServerSetting.isReuseAddress());
		this.serverSocketChannel.socket().setReceiveBufferSize(this.netServerSetting.getReceiveBufferSize());
		this.serverSocketChannel.socket().setSoTimeout(this.netServerSetting.getSoTimeout());

		this.serverSocketChannel.socket().bind(this.netAddress.toSocketAddress(), this.netServerSetting.getBackLog());
		this.serverSocketChannel.configureBlocking(false);
		this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		this.monitor.start();
	}

	@Override
	protected void doClose() throws Exception {
		if (this.running == false) {
			return;
		}
		this.running = false;
		this.selector.wakeup();
	}

	@Override
	public void run() {

		for (; this.running;) {
			this.process();
		}

		this.doClear();
		CloseUtils.close(this.selector);
		CloseUtils.close(this.serverSocketChannel);
		this.selector = null;
		this.serverSocketChannel = null;
	}

	protected void doClear() {
		Set<SelectionKey> keys = this.selector.keys();
		for (SelectionKey key : keys) {
			CloseUtils.close(key);
		}
	}

	protected void process() {

		try {
			this.selector.select(2000L);
		} catch (IOException e) {
			logger.warn("Selector select error : {}", StringUtils.getExceptionMessage(e));
		}

		Set<SelectionKey> keys = this.selector.selectedKeys();
		try {

			for (SelectionKey key : keys) {
				if (key.isValid() && key.isAcceptable()) {
					try {
						this.processRegister(key);
					} catch (IOException e) {
						CloseUtils.close(key);
					}
				} else {
					CloseUtils.close(key);
				}
			}

		} finally {
			keys.clear();
		}

	}

	protected void processRegister(SelectionKey key) throws IOException {
		SocketBossEventLoop socketBossEventLoop = this.socketBossEventLoopGroup.getEventLoop();
		SocketWorkEventLoop socketWorkEventLoop = this.socketWorkEventLoopGroup.getEventLoop();
		SocketChannel socketChannel = this.serverSocketChannel.accept();
		SocketSession socketSession = this.socketSessionManager.createSocketSession(true, socketChannel, socketBossEventLoop, socketWorkEventLoop);
		socketBossEventLoop.doRegister(socketSession);
	}

}