package com.zusmart.network.support;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.network.NetAddress;
import com.zusmart.network.NetServer;
import com.zusmart.network.NetServerSetting;
import com.zusmart.network.socket.SocketAcceptor;
import com.zusmart.network.socket.SocketBossEventLoopGroup;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.socket.SocketSessionManager;
import com.zusmart.network.socket.SocketSessionSequenceGenerator;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;
import com.zusmart.network.socket.support.DefaultSocketAcceptor;
import com.zusmart.network.socket.support.DefaultSocketBossEventLoopGroup;
import com.zusmart.network.socket.support.DefaultSocketSessionManager;
import com.zusmart.network.socket.support.DefaultSocketSessionSequenceGenerator;
import com.zusmart.network.socket.support.DefaultSocketWorkEventLoopGroup;
import com.zusmart.network.util.CloseUtils;

public class DefaultNetServer extends AbstractNetNode implements NetServer {
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultNetServer.class);
	
	private SocketAcceptor socketAcceptor;
	private SocketSessionManager socketSessionManager;
	private SocketBossEventLoopGroup socketBossEventLoopGroup;
	private SocketWorkEventLoopGroup socketWorkEventLoopGroup;

	public DefaultNetServer(NetAddress netAddress, NetServerSetting setting, SocketSessionAdapter socketSessionAdapter) {
		super(setting, socketSessionAdapter);
		this.socketSessionManager = this.createSocketSessionManager();
		this.socketBossEventLoopGroup = this.createSocketBossEventLoopGroup();
		this.socketWorkEventLoopGroup = this.createSocketWorkEventLoopGroup();
		this.socketAcceptor = this.createSocketAcceptor(netAddress);
	}

	@Override
	public NetServerSetting getSetting() {
		return (NetServerSetting) super.getSetting();
	}

	@Override
	public NetAddress getServerAddress() {
		return null != this.socketAcceptor ? this.socketAcceptor.getServerAddress() : null;
	}

	@Override
	public SocketSessionManager getSessionManager() {
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
	public SocketAcceptor getSocketAcceptor() {
		return this.socketAcceptor;
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
		this.socketAcceptor.start();
		logger.info("Net Server Start Success");
	}

	@Override
	protected void doClose() throws Exception {
		CloseUtils.close(this.socketAcceptor);
		CloseUtils.close(this.socketBossEventLoopGroup);
		CloseUtils.close(this.socketWorkEventLoopGroup);
		CloseUtils.close(this.socketSessionManager);
		this.socketSessionManager = null;
		this.socketWorkEventLoopGroup = null;
		this.socketBossEventLoopGroup = null;
		this.socketAcceptor = null;
		logger.info("Net Server Close Success");
	}

	protected SocketAcceptor createSocketAcceptor(NetAddress netAddress) {
		NetServerSetting setting = this.getSetting();
		return new DefaultSocketAcceptor(this.socketSessionManager, this.socketBossEventLoopGroup, this.socketWorkEventLoopGroup, netAddress, setting.getAcceptName(), setting.getBackLog());
	}

	protected SocketSessionSequenceGenerator createSocketSessionSequenceGenerator() {
		return new DefaultSocketSessionSequenceGenerator();
	}

	protected SocketSessionManager createSocketSessionManager() {
		NetServerSetting setting = this.getSetting();
		SocketSessionSequenceGenerator generator = this.createSocketSessionSequenceGenerator();
		return new DefaultSocketSessionManager(this.getSocketSessionAdapter(), generator, setting.getSessionTimeoutMillis(), setting.getReadBufferSize(), setting.isUseDirect(),setting.getSessionTimeoutCheckInterval());
	}

	protected SocketBossEventLoopGroup createSocketBossEventLoopGroup() {
		NetServerSetting setting = this.getSetting();
		return new DefaultSocketBossEventLoopGroup(setting.getBossEventLoopSize(), setting.getBossEventLoopName());
	}

	protected SocketWorkEventLoopGroup createSocketWorkEventLoopGroup() {
		NetServerSetting setting = this.getSetting();
		return new DefaultSocketWorkEventLoopGroup(setting.getWorkEventLoopSize(), setting.getWorkEventLoopName(), setting.getWorkThreadMinSize(), setting.getWorkThreadMaxSize(), setting.getWorkThreadQueueSize(), setting.getWorkThreadKeepAlive());
	}

}