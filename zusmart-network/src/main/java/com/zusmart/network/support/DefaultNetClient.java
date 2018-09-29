package com.zusmart.network.support;

import com.zusmart.network.NetAddress;
import com.zusmart.network.NetClient;
import com.zusmart.network.NetClientSetting;
import com.zusmart.network.socket.SocketBossEventLoopGroup;
import com.zusmart.network.socket.SocketConnector;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.socket.SocketSessionManager;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;

public class DefaultNetClient extends AbstractNetNode implements NetClient {

	public DefaultNetClient(NetClientSetting setting, SocketSessionAdapter socketSessionAdapter) {
		super(setting, socketSessionAdapter);
	}

	@Override
	public NetClientSetting getSetting() {
		return (NetClientSetting) super.getSetting();
	}

	@Override
	public SocketSessionManager getSessionManager() {
		return null;
	}

	@Override
	public SocketBossEventLoopGroup getSocketBossEventLoopGroup() {
		return null;
	}

	@Override
	public SocketWorkEventLoopGroup getSocketWorkEventLoopGroup() {
		return null;
	}

	@Override
	public NetAddress getServerAddress() {
		return null;
	}

	@Override
	public NetAddress getClientAddress() {
		return null;
	}

	@Override
	public SocketConnector getSocketConnector() {
		return null;
	}

	@Override
	public SocketSessionManager getSocketSessionManager() {
		return null;
	}

	@Override
	protected void doStart() throws Exception {

	}

	@Override
	protected void doClose() throws Exception {

	}

}