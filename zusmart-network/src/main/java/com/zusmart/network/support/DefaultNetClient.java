package com.zusmart.network.support;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.network.NetAddress;
import com.zusmart.network.NetClient;
import com.zusmart.network.NetClientSetting;
import com.zusmart.network.handler.support.DefaultSocketSessionHandler;
import com.zusmart.network.socket.SocketBossEventLoopGroup;
import com.zusmart.network.socket.SocketConnector;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.socket.SocketSessionManager;
import com.zusmart.network.socket.SocketSessionSequenceGenerator;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;
import com.zusmart.network.socket.support.DefaultSocketBossEventLoopGroup;
import com.zusmart.network.socket.support.DefaultSocketConnector;
import com.zusmart.network.socket.support.DefaultSocketSessionManager;
import com.zusmart.network.socket.support.DefaultSocketSessionSequenceGenerator;
import com.zusmart.network.socket.support.DefaultSocketWorkEventLoopGroup;
import com.zusmart.network.util.CloseUtils;

public class DefaultNetClient extends AbstractNetNode implements NetClient {

	private static final Logger logger = LoggerFactory.getLogger(DefaultNetClient.class);

	private SocketConnector socketConnector;
	private SocketSessionManager socketSessionManager;
	private SocketBossEventLoopGroup socketBossEventLoopGroup;
	private SocketWorkEventLoopGroup socketWorkEventLoopGroup;

	public DefaultNetClient(NetAddress netAddress, NetClientSetting setting, SocketSessionAdapter socketSessionAdapter) {
		super(setting, socketSessionAdapter);
		this.socketSessionManager = this.createSocketSessionManager();
		this.socketBossEventLoopGroup = this.createSocketBossEventLoopGroup();
		this.socketWorkEventLoopGroup = this.createSocketWorkEventLoopGroup();
		this.socketConnector = this.createSocketConnector(netAddress);
	}

	@Override
	public NetClientSetting getSetting() {
		return (NetClientSetting) super.getSetting();
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
		return this.socketConnector.getServerAddress();
	}

	@Override
	public NetAddress getClientAddress() {
		return this.socketConnector.getClientAddress();
	}

	@Override
	public SocketConnector getSocketConnector() {
		return this.socketConnector;
	}

	@Override
	public SocketSessionManager getSocketSessionManager() {
		return this.socketSessionManager;
	}

	@Override
	protected void doStart() throws Exception {
		this.socketSessionManager.start();
		this.socketWorkEventLoopGroup.start();
		this.socketBossEventLoopGroup.start();
		this.socketConnector.start();
		this.initSocketSessionListener(this.socketConnector.getSocketSession());
		logger.info("Net Client Start Success");
	}

	@Override
	protected void doClose() throws Exception {
		CloseUtils.close(this.socketConnector);
		CloseUtils.close(this.socketBossEventLoopGroup);
		CloseUtils.close(this.socketWorkEventLoopGroup);
		CloseUtils.close(this.socketSessionManager);
		this.socketSessionManager = null;
		this.socketWorkEventLoopGroup = null;
		this.socketBossEventLoopGroup = null;
		this.socketConnector = null;
		logger.info("Net Client Close Success");
	}

	protected void initSocketSessionListener(SocketSession socketSession) {
		socketSession.getSocketSessionHandlerChain().addLast("listener", new DefaultSocketSessionHandler() {
			@Override
			public void unRegister(SocketSession session) {
				try {
					DefaultNetClient.this.close();
				} catch (Exception e) {
					// IGNORE
				}
			}
		});
	}

	protected SocketConnector createSocketConnector(NetAddress netAddress) {
		return new DefaultSocketConnector(this.socketSessionManager, this.socketBossEventLoopGroup, this.socketWorkEventLoopGroup, netAddress);
	}

	protected SocketSessionSequenceGenerator createSocketSessionSequenceGenerator() {
		return new DefaultSocketSessionSequenceGenerator();
	}

	protected SocketSessionManager createSocketSessionManager() {
		NetClientSetting setting = this.getSetting();
		SocketSessionSequenceGenerator generator = this.createSocketSessionSequenceGenerator();
		return new DefaultSocketSessionManager(this.getSocketSessionAdapter(), generator, setting.getSessionTimeoutMillis(), setting.getReadBufferSize(), setting.isUseDirect(), setting.getSessionTimeoutCheckInterval());
	}

	protected SocketBossEventLoopGroup createSocketBossEventLoopGroup() {
		NetClientSetting setting = this.getSetting();
		return new DefaultSocketBossEventLoopGroup(setting.getBossEventLoopSize(), setting.getBossEventLoopName());
	}

	protected SocketWorkEventLoopGroup createSocketWorkEventLoopGroup() {
		NetClientSetting setting = this.getSetting();
		return new DefaultSocketWorkEventLoopGroup(setting.getWorkEventLoopSize(), setting.getWorkEventLoopName(), setting.getWorkThreadMinSize(), setting.getWorkThreadMaxSize(), setting.getWorkThreadQueueSize(), setting.getWorkThreadKeepAlive());
	}

}