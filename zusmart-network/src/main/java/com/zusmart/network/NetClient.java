package com.zusmart.network;

import com.zusmart.network.socket.SocketConnector;

public interface NetClient extends NetNode{

	@Override
	public NetClientSetting getSetting();
	
	public NetAddress getServerAddress();
	
	public NetAddress getClientAddress();
	
	public SocketConnector getSocketConnector();
	
}