package com.zusmart.network.handler.support;

import com.zusmart.basic.handler.HandlerChain;
import com.zusmart.basic.handler.support.AbstractHandlerChain;
import com.zusmart.network.handler.SocketSessionHandler;
import com.zusmart.network.handler.SocketSessionHandlerChain;
import com.zusmart.network.handler.SocketSessionHandlerContext;
import com.zusmart.network.packet.Message;
import com.zusmart.network.socket.SocketSession;

public class DefaultSocketSessionHandlerChain extends AbstractHandlerChain<SocketSessionHandler> implements SocketSessionHandlerChain {

	private final SocketSession socketSession;

	public DefaultSocketSessionHandlerChain(SocketSession socketSession) {
		if (null == socketSession) {
			throw new IllegalArgumentException("Socket session must not be null");
		}
		this.socketSession = socketSession;
	}

	@Override
	public SocketSession getSocketSession() {
		return this.socketSession;
	}

	@Override
	public void fireOnRegister() {
		SocketSessionHandlerContext context = (SocketSessionHandlerContext) this.head.getNext();
		while (context != this.foot) {
			context.getHandler().onRegister(this.socketSession);
			context = (SocketSessionHandlerContext) context.getNext();
		}
	}

	@Override
	public void fireUnRegister() {
		SocketSessionHandlerContext context = (SocketSessionHandlerContext) this.head.getNext();
		while (context != this.foot) {
			context.getHandler().unRegister(this.socketSession);
			context = (SocketSessionHandlerContext) context.getNext();
		}
	}

	@Override
	public void fireOnMessage(Message message) {
		SocketSessionHandlerContext context = (SocketSessionHandlerContext) this.head.getNext();
		while (context != this.foot) {
			context.getHandler().onMessage(this.socketSession, message);
			context = (SocketSessionHandlerContext) context.getNext();
		}
	}

	@Override
	public void fireOnTimeout() {
		SocketSessionHandlerContext context = (SocketSessionHandlerContext) this.head.getNext();
		while (context != this.foot) {
			context.getHandler().onTimeout(this.socketSession);
			context = (SocketSessionHandlerContext) context.getNext();
		}
	}

	@Override
	public void fireOnException(Throwable cause) {
		SocketSessionHandlerContext context = (SocketSessionHandlerContext) this.head.getNext();
		while (context != this.foot) {
			context.getHandler().onException(this.socketSession, cause);
			context = (SocketSessionHandlerContext) context.getNext();
		}
	}

	@Override
	protected SocketSessionHandlerContext createHeadHandlerContext() {
		return new DefaultSocketSessionHeadContext(this);
	}

	@Override
	protected SocketSessionHandlerContext createFootHandlerContext() {
		return new DefaultSocketSessionFootContext(this);
	}

	@Override
	protected SocketSessionHandlerContext createHandlerContext(String name, SocketSessionHandler handler) {
		return new DefaultSocketSessionHandlerContext(this, name, handler);
	}

	private static class DefaultSocketSessionHeadContext extends DefaultSocketSessionHandlerContext implements SocketSessionHandler {

		private static final String NAME = "__HEAD__";

		public DefaultSocketSessionHeadContext(HandlerChain<SocketSessionHandler> chain) {
			super(chain, NAME, null);
		}

		@Override
		public SocketSessionHandler getHandler() {
			return this;
		}

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

	private static class DefaultSocketSessionFootContext extends DefaultSocketSessionHandlerContext implements SocketSessionHandler {

		private static final String NAME = "__FOOT__";

		public DefaultSocketSessionFootContext(HandlerChain<SocketSessionHandler> chain) {
			super(chain, NAME, null);
		}

		@Override
		public SocketSessionHandler getHandler() {
			return this;
		}

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

}