package com.zusmart.network.socket;

import com.zusmart.network.looper.EventLoopGroup;

public interface SocketWorkEventLoopGroup extends EventLoopGroup{
	
	@Override
	public SocketWorkEventLoop getEventLoop();
	
	@Override
	public SocketWorkEventLoop getEventLoop(int index);
	
	public int getThreadMinSize();
	
	public int getThreadMaxSize();
	
	public int getThreadQueueSize();
	
	public long getThreadKeepAlive();
	
}