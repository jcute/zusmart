package com.zusmart.network.socket;

import com.zusmart.network.looper.EventLoop;

public interface SocketBossEventLoop extends EventLoop{
	
	@Override
	public SocketBossEventLoopGroup getEventLoopGroup();
	
	public Thread getMonitor();
	
	public boolean inEventLoop();
	
	public boolean inEventLoop(Thread thread);
	
	public void doRegister(SocketSession socketSession);
	
	public void deRegister(SocketSession socketSession);
	
	public void doListener(SocketSession socketSession,int listener);
	
	public void doTimeout(SocketSession socketSession);
	
}