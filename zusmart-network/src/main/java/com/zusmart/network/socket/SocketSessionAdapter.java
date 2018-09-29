package com.zusmart.network.socket;

import com.zusmart.network.handler.SocketSessionHandlerChain;
import com.zusmart.network.packet.MessageProtocol;

public interface SocketSessionAdapter {

	public MessageProtocol createMessageProtocol();

	public void initSocketSessionHandlerChain(SocketSessionHandlerChain chain);

}