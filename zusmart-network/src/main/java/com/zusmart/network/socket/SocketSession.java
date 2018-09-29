package com.zusmart.network.socket;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import com.zusmart.network.handler.SocketSessionHandlerChain;

public interface SocketSession extends SocketSessionContext {

	public static final int EVENT_READABLE = SelectionKey.OP_READ;
	public static final int EVENT_WRITABLE = SelectionKey.OP_WRITE;

	public String getSocketSessionSequence();

	public SocketBossEventLoop getSocketBossEventLoop();

	public SocketWorkEventLoop getSocketWorkEventLoop();

	public SocketSessionHandlerChain getSocketSessionHandlerChain();

	public long getRegistTime();

	public long getAcitveTime();

	public void fireOnRegister(Selector selector) throws Exception;

	public void fireOnReadable() throws Exception;

	public void fireOnWritable() throws Exception;

	public void fireOnListener(int events) throws Exception;

	public void fireUnRegister();

	public void fireOnTimeout();

	public void fireOnException(Throwable cause);

}