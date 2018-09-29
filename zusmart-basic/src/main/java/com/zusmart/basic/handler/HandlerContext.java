package com.zusmart.basic.handler;

public interface HandlerContext<T extends Handler> {

	public String getName();

	public T getHandler();

	public HandlerContext<T> getNext();

	public HandlerContext<T> getPrev();

	public HandlerContext<T> setNext(HandlerContext<T> context);

	public HandlerContext<T> setPrev(HandlerContext<T> context);

	public HandlerChain<T> getChain();

}