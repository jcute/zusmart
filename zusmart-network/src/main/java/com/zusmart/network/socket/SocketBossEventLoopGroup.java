package com.zusmart.network.socket;

import com.zusmart.network.looper.EventLoopGroup;

public interface SocketBossEventLoopGroup extends EventLoopGroup{

	@Override
	public SocketBossEventLoop getEventLoop();
	
	@Override
	public SocketBossEventLoop getEventLoop(int index);
	
}
