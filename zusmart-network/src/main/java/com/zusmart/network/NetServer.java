package com.zusmart.network;

import com.zusmart.network.socket.SocketAcceptor;

public interface NetServer extends NetNode {

	@Override
	public NetServerSetting getSetting();

	public SocketAcceptor getSocketAcceptor();

}