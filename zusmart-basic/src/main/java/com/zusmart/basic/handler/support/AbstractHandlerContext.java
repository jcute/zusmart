package com.zusmart.basic.handler.support;

import com.zusmart.basic.handler.Handler;
import com.zusmart.basic.handler.HandlerChain;
import com.zusmart.basic.handler.HandlerContext;

public abstract class AbstractHandlerContext<T extends Handler> implements HandlerContext<T> {

	protected final HandlerChain<T> chain;
	protected final String name;
	protected final T handler;

	protected volatile HandlerContext<T> next;
	protected volatile HandlerContext<T> prev;

	public AbstractHandlerContext(HandlerChain<T> chain, String name, T handler) {
		this.chain = chain;
		this.name = name;
		this.handler = handler;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public T getHandler() {
		return this.handler;
	}

	@Override
	public HandlerContext<T> getNext() {
		return this.next;
	}

	@Override
	public HandlerContext<T> getPrev() {
		return this.prev;
	}

	@Override
	public AbstractHandlerContext<T> setNext(HandlerContext<T> next) {
		this.next = next;
		return this;
	}

	@Override
	public AbstractHandlerContext<T> setPrev(HandlerContext<T> prev) {
		this.prev = prev;
		return this;
	}

	@Override
	public HandlerChain<T> getChain() {
		return this.chain;
	}

}