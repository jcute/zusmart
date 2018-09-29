package com.zusmart.network.handler.support;

import com.zusmart.network.handler.SocketSessionHandler;
import com.zusmart.network.packet.Message;
import com.zusmart.network.socket.SocketSession;

public class DefaultSocketSessionHandler implements SocketSessionHandler {

	@Override
	public void onRegister(SocketSession session) {

	}

	@Override
	public void unRegister(SocketSession session) {

	}

	@Override
	public void onMessage(SocketSession session, Message message) {

	}

	@Override
	public void onTimeout(SocketSession session) {

	}

	@Override
	public void onException(SocketSession session, Throwable cause) {

	}

}