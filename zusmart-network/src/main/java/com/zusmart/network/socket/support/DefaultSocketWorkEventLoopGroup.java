package com.zusmart.network.socket.support;

import com.zusmart.network.looper.support.AbstractEventLoopGroup;
import com.zusmart.network.socket.SocketWorkEventLoop;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;

public class DefaultSocketWorkEventLoopGroup extends AbstractEventLoopGroup implements SocketWorkEventLoopGroup {

	private final int threadMinSize;
	private final int threadMaxSize;
	private final int threadQueueSize;
	private final long threadKeepAlive;

	public DefaultSocketWorkEventLoopGroup(int eventLoopSize, String eventLoopName, int threadMinSize, int threadMaxSize, int threadQueueSize, long threadKeepAlive) {
		super(eventLoopSize, eventLoopName);
		this.threadMinSize = threadMinSize;
		this.threadMaxSize = threadMaxSize;
		this.threadQueueSize = threadQueueSize;
		this.threadKeepAlive = threadKeepAlive;
	}

	@Override
	public SocketWorkEventLoop getEventLoop() {
		return (SocketWorkEventLoop) super.getEventLoop();
	}

	@Override
	public SocketWorkEventLoop getEventLoop(int index) {
		return (SocketWorkEventLoop) super.getEventLoop(index);
	}

	@Override
	public int getThreadMinSize() {
		return this.threadMinSize;
	}

	@Override
	public int getThreadMaxSize() {
		return this.threadMaxSize;
	}

	@Override
	public int getThreadQueueSize() {
		return this.threadQueueSize;
	}

	@Override
	public long getThreadKeepAlive() {
		return this.threadKeepAlive;
	}

	@Override
	protected SocketWorkEventLoop createEventLoop(String eventLoopName) {
		return new DefaultSocketWorkEventLoop(eventLoopName, this);
	}

}