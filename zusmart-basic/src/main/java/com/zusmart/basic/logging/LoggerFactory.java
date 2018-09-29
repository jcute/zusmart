package com.zusmart.basic.logging;

import com.zusmart.basic.logging.support.JdkLoggerFactory;
import com.zusmart.basic.logging.support.Log4j2LoggerFactory;
import com.zusmart.basic.logging.support.Log4jLoggerFactory;
import com.zusmart.basic.logging.support.Slf4jLoggerFactory;

public abstract class LoggerFactory {

	private static volatile LoggerFactory defaultLoggerFactory;

	private static LoggerFactory createDefaultLoggerFactory(String name) {
		LoggerFactory loggerFactory;
		try {
			loggerFactory = new Slf4jLoggerFactory();
			loggerFactory.newInstance(name).debug("Using SLF4J as the default logging framework");
		} catch (Throwable e) {
			try {
				loggerFactory = new Log4jLoggerFactory();
				loggerFactory.newInstance(name).debug("Using Log4J as the default logging framework");
			} catch (Throwable ee) {
				try {
					loggerFactory = new Log4j2LoggerFactory();
					loggerFactory.newInstance(name).debug("Using Log4J2 as the default logging framework");
				} catch (Throwable eee) {
					loggerFactory = new JdkLoggerFactory();
					loggerFactory.newInstance(name).debug("Using java.util.logging as the default logging framework");
				}
			}
		}
		return loggerFactory;
	}

	public static LoggerFactory getDefaultLoggerFactory() {
		if (null == defaultLoggerFactory) {
			defaultLoggerFactory = createDefaultLoggerFactory(LoggerFactory.class.getName());
		}
		return defaultLoggerFactory;
	}

	public static void setDefaultLoggerFactory(LoggerFactory defaultLoggerFactory) {
		if (null == defaultLoggerFactory) {
			throw new NullPointerException("defaultLoggerFactory");
		}
		LoggerFactory.defaultLoggerFactory = defaultLoggerFactory;
	}

	public static Logger getLogger(Class<?> classType) {
		return getLogger(classType.getName());
	}

	public static Logger getLogger(String name) {
		return getDefaultLoggerFactory().newInstance(name);
	}

	protected abstract Logger newInstance(String name);

}