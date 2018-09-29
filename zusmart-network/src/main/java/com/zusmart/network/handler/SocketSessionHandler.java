package com.zusmart.network.handler;

import com.zusmart.basic.handler.Handler;
import com.zusmart.network.packet.Message;
import com.zusmart.network.socket.SocketSession;

public interface SocketSessionHandler extends Handler {
	
	public void onRegister(SocketSession session);
	
	public void unRegister(SocketSession session);
	
	public void onMessage(SocketSession session,Message message);
	
	public void onTimeout(SocketSession session);
	
	public void onException(SocketSession session,Throwable cause);
	
}