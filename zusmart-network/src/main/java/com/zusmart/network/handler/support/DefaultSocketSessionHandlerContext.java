package com.zusmart.network.handler.support;

import com.zusmart.basic.handler.HandlerChain;
import com.zusmart.basic.handler.support.AbstractHandlerContext;
import com.zusmart.network.handler.SocketSessionHandler;
import com.zusmart.network.handler.SocketSessionHandlerContext;

public class DefaultSocketSessionHandlerContext extends AbstractHandlerContext<SocketSessionHandler> implements SocketSessionHandlerContext {

	public DefaultSocketSessionHandlerContext(HandlerChain<SocketSessionHandler> chain, String name, SocketSessionHandler handler) {
		super(chain, name, handler);
	}

}