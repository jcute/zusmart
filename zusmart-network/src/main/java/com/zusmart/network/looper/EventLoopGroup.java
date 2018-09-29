package com.zusmart.network.looper;

import com.zusmart.basic.toolkit.Executable;

public interface EventLoopGroup extends Executable{

	public EventLoop getEventLoop();

	public EventLoop getEventLoop(int index);

	public int getEventLoopSize();

	public String getEventLoopName();

}