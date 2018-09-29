package com.zusmart.basic.toolkit;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.util.StringUtils;

public class NamedThreadFactory implements ThreadFactory {

	private static final String DEFUALT_THREAD_NAME_PREFIX = "thread";
	private static final Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);

	// 线程计数器
	private final AtomicInteger threadNumber;
	// 线程分组
	private final ThreadGroup threadGroup;
	// 线程名前缀,如果为空默认为thread
	private final String threadNamePrefix;

	public NamedThreadFactory() {
		this(null);
	}

	public NamedThreadFactory(String threadNamePrefix) {
		SecurityManager securityManager = System.getSecurityManager();
		this.threadNumber = new AtomicInteger(1);
		this.threadGroup = null == securityManager ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
		this.threadNamePrefix = StringUtils.isBlank(threadNamePrefix) ? DEFUALT_THREAD_NAME_PREFIX : threadNamePrefix;
	}

	@Override
	public Thread newThread(Runnable task) {
		String threadName = String.format("%s-%d", this.threadNamePrefix, this.threadNumber.getAndIncrement());
		Thread thread = new Thread(this.threadGroup, task, threadName, 0);
		if (thread.isDaemon()) {
			thread.setDaemon(false);// 设置为非守护线程
		}
		if (thread.getPriority() != Thread.NORM_PRIORITY) {
			thread.setPriority(Thread.NORM_PRIORITY);// 线程优先级使用默认优先级
		}
		logger.debug("Create thread start success {}", threadName);
		return thread;
	}

}