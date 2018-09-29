package com.zusmart.network.socket.support;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zusmart.basic.toolkit.NamedThreadFactory;
import com.zusmart.network.looper.support.AbstractEventLoop;
import com.zusmart.network.socket.SocketWorkEventLoop;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;

public class DefaultSocketWorkEventLoop extends AbstractEventLoop implements SocketWorkEventLoop {

	private final int threadMinSize;
	private final int threadMaxSize;
	private final int threadQueueSize;
	private final long threadKeepAlive;

	private final NamedThreadFactory namedThreadFactory;

	private ThreadPoolExecutor threadPoolExecutor;

	private volatile boolean running = false;

	public DefaultSocketWorkEventLoop(String eventLoopName, SocketWorkEventLoopGroup eventLoopGroup) {
		super(eventLoopName, eventLoopGroup);
		this.namedThreadFactory = new NamedThreadFactory(eventLoopName);
		this.threadMinSize = eventLoopGroup.getThreadMinSize();
		this.threadMaxSize = eventLoopGroup.getThreadMaxSize();
		this.threadQueueSize = eventLoopGroup.getThreadQueueSize();
		this.threadKeepAlive = eventLoopGroup.getThreadKeepAlive();
	}

	@Override
	public SocketWorkEventLoopGroup getEventLoopGroup() {
		return (SocketWorkEventLoopGroup) super.getEventLoopGroup();
	}

	@Override
	public Future<?> submit(Runnable task) {
		return this.threadPoolExecutor.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return this.threadPoolExecutor.submit(task, result);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return this.threadPoolExecutor.submit(task);
	}

	@Override
	protected void doStart() throws Exception {
		if (this.running == true) {
			return;
		}
		this.running = true;
		this.threadPoolExecutor = new ThreadPoolExecutor(this.threadMinSize, this.threadMaxSize, this.threadKeepAlive, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(this.threadQueueSize), this.namedThreadFactory);
	}

	@Override
	protected void doClose() throws Exception {
		if (this.running == false) {
			return;
		}
		this.running = false;
		this.threadPoolExecutor.shutdown();
		this.threadPoolExecutor = null;
	}

}