package com.zusmart.network.socket.support;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.util.StringUtils;
import com.zusmart.network.looper.support.AbstractEventLoop;
import com.zusmart.network.socket.SocketBossEventLoop;
import com.zusmart.network.socket.SocketBossEventLoopGroup;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.util.CloseUtils;

public class DefaultSocketBossEventLoop extends AbstractEventLoop implements SocketBossEventLoop, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(DefaultSocketBossEventLoop.class);

	private final Queue<SocketSession> onRegisterQueue = new ConcurrentLinkedQueue<SocketSession>();
	private final Queue<SocketSession> unRegisterQueue = new ConcurrentLinkedQueue<SocketSession>();
	private final Queue<SocketSession> onTimeoutQueue = new ConcurrentLinkedQueue<SocketSession>();
	private final Queue<SocketSessionUpdater> onListenerQueue = new ConcurrentLinkedQueue<SocketSessionUpdater>();

	private final Thread monitor;

	private volatile boolean running = false;
	private Selector selector;

	public DefaultSocketBossEventLoop(String eventLoopName, SocketBossEventLoopGroup eventLoopGroup) {
		super(eventLoopName, eventLoopGroup);
		this.monitor = new Thread(this, eventLoopName);
	}

	@Override
	public SocketBossEventLoopGroup getEventLoopGroup() {
		return (SocketBossEventLoopGroup) super.getEventLoopGroup();
	}

	@Override
	public Thread getMonitor() {
		return this.monitor;
	}

	@Override
	public boolean inEventLoop() {
		return this.inEventLoop(Thread.currentThread());
	}

	@Override
	public boolean inEventLoop(Thread thread) {
		return thread == this.monitor;
	}

	@Override
	public void doRegister(SocketSession socketSession) {
		if (this.inEventLoop()) {
			this.handlerOnRegisterNow(socketSession);
		} else {
			this.onRegisterQueue.add(socketSession);
			this.wakeup();
		}
	}

	@Override
	public void deRegister(SocketSession socketSession) {
		if (this.inEventLoop()) {
			this.handlerUnRegisterNow(socketSession);
		} else {
			this.unRegisterQueue.add(socketSession);
			this.wakeup();
		}
	}

	@Override
	public void doListener(SocketSession socketSession, int events) {
		if (this.inEventLoop()) {
			this.handlerOnListenerNow(socketSession, events);
		}else{
			this.onListenerQueue.add(new SocketSessionUpdater(socketSession, events));
			this.wakeup();
		}
	}

	@Override
	public void doTimeout(SocketSession socketSession) {
		if (this.inEventLoop()) {
			this.handlerOnTimeoutNow(socketSession);
		} else {
			this.onTimeoutQueue.add(socketSession);
			this.wakeup();
		}
	}

	@Override
	protected void doStart() throws Exception {
		if (this.running == true) {
			return;
		}
		this.running = true;
		this.selector = Selector.open();
		this.monitor.start();
	}

	@Override
	protected void doClose() throws Exception {
		if (this.running == false) {
			return;
		}
		this.running = false;
		this.wakeup();
	}

	@Override
	public void run() {

		for (; this.running;) {
			this.process();
		}

		this.doClear();
		CloseUtils.close(this.selector);
		this.selector = null;

	}

	protected void wakeup() {
		this.selector.wakeup();
	}

	protected void process() {
		this.processOnRegister();
		this.processUnRegister();
		this.processOnListener();
		this.processOnTimeout();
		this.processOnEvents();
	}

	protected void processOnRegister() {
		SocketSession socketSession = null;
		while ((socketSession = this.onRegisterQueue.poll()) != null) {
			this.handlerOnRegisterNow(socketSession);
		}
	}

	protected void processUnRegister() {
		SocketSession socketSession = null;
		while ((socketSession = this.unRegisterQueue.poll()) != null) {
			this.handlerUnRegisterNow(socketSession);
		}
	}

	protected void processOnListener() {
		SocketSessionUpdater socketSessionUpdater = null;
		while ((socketSessionUpdater = this.onListenerQueue.poll()) != null) {
			this.handlerOnListenerNow(socketSessionUpdater.socketSession, socketSessionUpdater.events);
		}
	}

	protected void processOnTimeout() {
		SocketSession socketSession = null;
		while ((socketSession = this.onTimeoutQueue.poll()) != null) {
			this.handlerOnTimeoutNow(socketSession);
		}
	}

	protected void processOnEvents() {

		try {
			this.selector.select(4000L);
		} catch (IOException e) {
			logger.warn("Selector select error : {}", StringUtils.getExceptionMessage(e));
		}

		Set<SelectionKey> keys = this.selector.selectedKeys();
		try {
			for (SelectionKey key : keys) {
				SocketSession socketSession = (SocketSession) key.attachment();
				if (null == socketSession) {
					CloseUtils.close(key);
					continue;
				}
				if (key.isValid() && key.isReadable()) {
					try {
						socketSession.fireOnReadable();
					} catch (Exception e) {
						socketSession.fireOnException(e);
					}
				} else if (key.isValid() && key.isWritable()) {
					try {
						socketSession.fireOnWritable();
					} catch (Exception e) {
						socketSession.fireOnException(e);
					}
				} else {
					CloseUtils.close(key);
					socketSession.fireUnRegister();
				}
			}
		} finally {
			keys.clear();
		}

	}

	protected void handlerOnRegisterNow(SocketSession socketSession) {
		try {
			socketSession.fireOnRegister(this.selector);
		} catch (Exception e) {
			socketSession.fireUnRegister();
		}
	}

	protected void handlerUnRegisterNow(SocketSession socketSession) {
		socketSession.fireUnRegister();
	}

	protected void handlerOnListenerNow(SocketSession socketSession, int events) {
		try {
			socketSession.fireOnListener(events);
		} catch (Exception e) {
			socketSession.fireUnRegister();
		}
	}

	protected void handlerOnTimeoutNow(SocketSession socketSession) {
		socketSession.fireOnTimeout();
		socketSession.fireUnRegister();
	}

	protected static class SocketSessionUpdater {
		protected SocketSession socketSession;
		protected int events;

		protected SocketSessionUpdater(SocketSession socketSession, int events) {
			this.socketSession = socketSession;
			this.events = events;
		}
	}

	protected void doClear() {

		Set<SelectionKey> keys = this.selector.keys();
		for (SelectionKey key : keys) {
			SocketSession socketSession = (SocketSession) key.attachment();
			if (null != socketSession) {
				try {
					socketSession.fireUnRegister();
				} catch (Exception e) {
					logger.warn("Socket session close error : {}", StringUtils.getExceptionMessage(e));
				}
			}
			CloseUtils.close(key);
		}

	}

}