package com.zusmart.network.looper;

import com.zusmart.basic.toolkit.Executable;

public interface EventLoop extends Executable {
	
	public String getEventLoopName();
	
	public EventLoopGroup getEventLoopGroup();
	
}