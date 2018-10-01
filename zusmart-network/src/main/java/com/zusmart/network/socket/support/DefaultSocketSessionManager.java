package com.zusmart.network.socket.support;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.toolkit.support.AbstractExecutable;
import com.zusmart.basic.util.StringUtils;
import com.zusmart.network.handler.support.DefaultSocketSessionHandler;
import com.zusmart.network.socket.SocketBossEventLoop;
import com.zusmart.network.socket.SocketSession;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.socket.SocketSessionManager;
import com.zusmart.network.socket.SocketSessionSequenceGenerator;
import com.zusmart.network.socket.SocketWorkEventLoop;

public class DefaultSocketSessionManager extends AbstractExecutable implements SocketSessionManager, Runnable {

	private static final Logger logger = LoggerFactory.getLogger(DefaultSocketSessionManager.class);
	private static final String thread_name = "session-manager";

	private final long sessionTimeoutMillis;
	private final int readBufferSize;
	private final int checkInterval;
	private final boolean useDirect;
	private final Map<String, SocketSession> sessions;
	private final SocketSessionSequenceGenerator generator;
	private final SocketSessionAdapter socketSessionAdapter;

	private final Queue<SocketSession> onTimeoutQueue;
	private final Queue<SocketSession> onMonitorQueue;

	private Thread monitor;
	private volatile boolean running = false;

	public DefaultSocketSessionManager(SocketSessionAdapter socketSessionAdapter, SocketSessionSequenceGenerator generator, long sessionTimeoutMillis, int readBufferSize, boolean useDirect, int checkInterval) {
		this.sessionTimeoutMillis = sessionTimeoutMillis;
		this.readBufferSize = readBufferSize;
		this.useDirect = useDirect;
		this.checkInterval = checkInterval;
		this.sessions = new HashMap<String, SocketSession>();
		this.onTimeoutQueue = new LinkedList<SocketSession>();
		this.onMonitorQueue = new LinkedList<SocketSession>();
		this.socketSessionAdapter = socketSessionAdapter;
		this.generator = generator;
	}

	@Override
	public SocketSessionSequenceGenerator getSocketSessionSequenceGenerator() {
		return this.generator;
	}

	@Override
	public SocketSession createSocketSession(boolean isServerSide, SocketChannel socketChannel, SocketBossEventLoop socketBossEventLoop, SocketWorkEventLoop socketWorkEventLoop) {
		SocketSession socketSession = this.doCreateSocketSession(isServerSide, socketChannel, socketBossEventLoop, socketWorkEventLoop);
		socketSession.getSocketSessionHandlerChain().addLast("INTERNAL_SESSION_MANAGER", new DefaultSocketSessionHandler() {

			@Override
			public void onRegister(SocketSession session) {
				onMonitorQueue.add(session);
				try {
					DefaultSocketSessionManager.this.notify();
				} catch (Exception e) {
				}
			}

			@Override
			public void unRegister(SocketSession session) {
				onTimeoutQueue.add(session);
				try {
					DefaultSocketSessionManager.this.notify();
				} catch (Exception e) {
				}
			}

		});
		return socketSession;
	}

	@Override
	public SocketSession getSocketSession(String socketSessionSequence) {
		return this.sessions.get(socketSessionSequence);
	}

	@Override
	public Map<String, SocketSession> getAllSocketSessionAsMap() {
		return Collections.unmodifiableMap(this.sessions);
	}

	@Override
	public Set<String> getAllSocketSessionSequence() {
		return Collections.unmodifiableSet(this.sessions.keySet());
	}

	@Override
	public Set<SocketSession> getAllSocketSessionAsSet() {
		return Collections.unmodifiableSet(new HashSet<SocketSession>(this.sessions.values()));
	}

	@Override
	public Iterator<Entry<String, SocketSession>> iterator() {
		return this.sessions.entrySet().iterator();
	}

	@Override
	protected void doStart() throws Exception {
		if (this.running == true) {
			return;
		}
		this.running = true;
		this.monitor = new Thread(this, thread_name);
		this.monitor.start();
		logger.debug("Socket session manager start success");
	}

	@Override
	protected void doClose() throws Exception {
		if (this.running == false) {
			return;
		}
		this.running = false;
		this.notify();
		logger.debug("Socket session manager close success");
	}

	@Override
	public void run() {

		while (this.running) {

			SocketSession socketSession = null;
			while ((socketSession = this.onMonitorQueue.poll()) != null) {
				this.sessions.put(socketSession.getSocketSessionSequence(), socketSession);
				logger.debug("Attach session success [{}]", socketSession.toString());
			}
			while ((socketSession = this.onTimeoutQueue.poll()) != null) {
				String key = socketSession.getSocketSessionSequence();
				if (this.sessions.containsKey(key)) {
					this.sessions.remove(socketSession.getSocketSessionSequence());
					logger.debug("Detach session success [{}]", socketSession.toString());
				}
			}

			try {
				this.checkSessionTimeout();
				synchronized (this) {
					this.wait(this.checkInterval);
				}
			} catch (Exception e) {
				logger.warn("Check session timeout thread error : {}", StringUtils.getExceptionMessage(e));
			}

		}

		this.doClear();

	}

	protected void doClear() {
		for (Entry<String, SocketSession> entry : this.sessions.entrySet()) {
			try {
				entry.getValue().close();
			} catch (Exception e) {
				logger.warn("Close socket session error : {}", StringUtils.getExceptionMessage(e));
			}
		}
	}

	protected void checkSessionTimeout() {
		for (Entry<String, SocketSession> entry : this.sessions.entrySet()) {

			SocketSession socketSession = entry.getValue();
			long lastActiveTime = socketSession.getAcitveTime();
			long currActiveTime = System.currentTimeMillis();

			if (currActiveTime - lastActiveTime > this.sessionTimeoutMillis) {
				socketSession.getSocketBossEventLoop().doTimeout(socketSession);
			}
		}
	}

	protected SocketSession doCreateSocketSession(boolean isServerSide, SocketChannel socketChannel, SocketBossEventLoop socketBossEventLoop, SocketWorkEventLoop socketWorkEventLoop) {
		String socketSessionSequence = this.generator.create();
		return new DefaultSocketSession(isServerSide, socketSessionSequence, socketChannel, socketBossEventLoop, socketWorkEventLoop, this.socketSessionAdapter, this.readBufferSize, this.useDirect);
	}

}