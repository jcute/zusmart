package com.zusmart.network.socket;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.zusmart.network.looper.EventLoop;

public interface SocketWorkEventLoop extends EventLoop {

	@Override
	public SocketWorkEventLoopGroup getEventLoopGroup();

	public Future<?> submit(Runnable task);

	public <T> Future<T> submit(Runnable task, T result);

	public <T> Future<T> submit(Callable<T> task);

}