package com.zusmart.network.handler;

import com.zusmart.basic.handler.HandlerChain;
import com.zusmart.network.packet.Message;
import com.zusmart.network.socket.SocketSession;

public interface SocketSessionHandlerChain extends HandlerChain<SocketSessionHandler> {

	public SocketSession getSocketSession();

	public void fireOnRegister();

	public void fireUnRegister();

	public void fireOnMessage(Message message);

	public void fireOnTimeout();

	public void fireOnException(Throwable cause);

}