package com.zusmart.network.socket.support;

import com.zusmart.network.looper.support.AbstractEventLoopGroup;
import com.zusmart.network.socket.SocketBossEventLoop;
import com.zusmart.network.socket.SocketBossEventLoopGroup;

public class DefaultSocketBossEventLoopGroup extends AbstractEventLoopGroup implements SocketBossEventLoopGroup {

	public DefaultSocketBossEventLoopGroup(int eventLoopSize, String eventLoopName) {
		super(eventLoopSize, eventLoopName);
	}

	@Override
	public SocketBossEventLoop getEventLoop() {
		return (SocketBossEventLoop) super.getEventLoop();
	}

	@Override
	public SocketBossEventLoop getEventLoop(int index) {
		return (SocketBossEventLoop) super.getEventLoop(index);
	}

	@Override
	protected SocketBossEventLoop createEventLoop(String eventLoopName) {
		return new DefaultSocketBossEventLoop(eventLoopName, this);
	}

}