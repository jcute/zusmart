package com.zusmart.network.looper.support;

import com.zusmart.basic.toolkit.support.AbstractExecutable;
import com.zusmart.basic.util.StringUtils;
import com.zusmart.network.looper.EventLoop;
import com.zusmart.network.looper.EventLoopGroup;

public abstract class AbstractEventLoop extends AbstractExecutable implements EventLoop{
	
	private final String eventLoopName;
	private final EventLoopGroup eventLoopGroup;
	
	protected AbstractEventLoop(String eventLoopName,EventLoopGroup eventLoopGroup){
		if(StringUtils.isBlank(eventLoopName)){
			throw new IllegalArgumentException("Event loop name must not be null");
		}
		if(null == eventLoopGroup){
			throw new IllegalArgumentException("Event loop group must not be null");
		}
		this.eventLoopName = eventLoopName;
		this.eventLoopGroup = eventLoopGroup;
	}
	
	@Override
	public String getEventLoopName() {
		return this.eventLoopName;
	}

	@Override
	public EventLoopGroup getEventLoopGroup() {
		return this.eventLoopGroup;
	}
	
}