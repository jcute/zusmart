package com.zusmart.network;

import com.zusmart.network.socket.SocketAcceptor;
import com.zusmart.network.socket.SocketSessionManager;

public interface NetServer extends NetNode {

	@Override
	public NetServerSetting getSetting();

	public SocketAcceptor getSocketAcceptor();

	public SocketSessionManager getSocketSessionManager();

}