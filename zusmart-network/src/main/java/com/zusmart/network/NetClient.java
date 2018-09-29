package com.zusmart.network;

import com.zusmart.network.socket.SocketConnector;
import com.zusmart.network.socket.SocketSessionManager;

public interface NetClient extends NetNode{

	@Override
	public NetClientSetting getSetting();
	
	public NetAddress getServerAddress();
	
	public NetAddress getClientAddress();
	
	public SocketConnector getSocketConnector();
	
	public SocketSessionManager getSocketSessionManager();
	
}