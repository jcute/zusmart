package com.zusmart.network.looper.support;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.toolkit.FixedAtomicInteger;
import com.zusmart.basic.toolkit.support.AbstractExecutable;
import com.zusmart.basic.util.StringUtils;
import com.zusmart.network.looper.EventLoop;
import com.zusmart.network.looper.EventLoopGroup;

public abstract class AbstractEventLoopGroup extends AbstractExecutable implements EventLoopGroup {

	private static final Logger logger = LoggerFactory.getLogger(AbstractEventLoopGroup.class);

	private final String eventLoopName;
	private final FixedAtomicInteger eventLoopIndex;
	private final EventLoop[] eventLoopPool;

	protected AbstractEventLoopGroup(int eventLoopSize, String eventLoopName) {
		this.eventLoopName = eventLoopName;
		this.eventLoopIndex = new FixedAtomicInteger(0, eventLoopSize - 1);
		this.eventLoopPool = new EventLoop[eventLoopSize];
	}

	@Override
	public EventLoop getEventLoop() {
		return this.getEventLoop(this.eventLoopIndex.getAndIncrement());
	}

	@Override
	public EventLoop getEventLoop(int index) {
		if (index < 0 || index > this.eventLoopPool.length) {
			throw new IllegalArgumentException(String.format("Event loop index must >= 0 and index <= %d", this.eventLoopPool.length - 1));
		}
		return this.eventLoopPool[index];
	}

	@Override
	public int getEventLoopSize() {
		return this.eventLoopPool.length;
	}

	@Override
	public String getEventLoopName() {
		return this.eventLoopName;
	}

	@Override
	protected void doStart() throws Exception {
		for (int i = 0, j = this.eventLoopPool.length; i < j; i++) {
			this.eventLoopPool[i] = this.createEventLoop(String.format("%s-%d", this.eventLoopName, i));
		}
		for (int i = 0, j = this.eventLoopPool.length; i < j; i++) {
			this.eventLoopPool[i].start();
		}
		logger.debug("Start event loop group success : {}", this);
	}

	@Override
	protected void doClose() throws Exception {
		for (int i = 0, j = this.eventLoopPool.length; i < j; i++) {
			try {
				this.eventLoopPool[i].close();
			} catch (Exception e) {
				logger.warn("Close event loop error : {}", StringUtils.getExceptionMessage(e));
			}
		}
		logger.debug("Close event loop group success : {}", this);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString()).append(" (");
		sb.append("eventLoopSize=").append(this.eventLoopPool.length).append(",");
		sb.append("eventLoopName=").append(this.eventLoopName);
		sb.append(")");
		return sb.toString();
	}

	protected abstract EventLoop createEventLoop(String eventLoopName);

}