package com.zusmart.basic.logging.support;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerLevel;

public class Log4j2Logger extends ExtendedLoggerWrapper implements Logger {

	private static final long serialVersionUID = 597951761499805549L;
	private static final String EXCEPTION_MESSAGE = "Unexpected exception:";

	public Log4j2Logger(org.apache.logging.log4j.Logger logger) {
		super((ExtendedLogger) logger, logger.getName(), logger.getMessageFactory());
	}

	@Override
	public boolean isEnabled(LoggerLevel level) {
		return this.isEnabled(this.toLevel(level));
	}

	@Override
	public void trace(Throwable t) {
		this.log(Level.TRACE, EXCEPTION_MESSAGE, t);
	}

	@Override
	public void debug(Throwable t) {
		this.log(Level.DEBUG, EXCEPTION_MESSAGE, t);
	}

	@Override
	public void info(Throwable t) {
		this.log(Level.INFO, EXCEPTION_MESSAGE, t);
	}

	@Override
	public void warn(Throwable t) {
		this.log(Level.WARN, EXCEPTION_MESSAGE, t);
	}

	@Override
	public void error(Throwable t) {
		this.log(Level.ERROR, EXCEPTION_MESSAGE, t);
	}

	@Override
	public void trace(String msg, Object arg) {
		this.log(Level.TRACE, msg, arg);
	}

	@Override
	public void trace(String msg, Object argA, Object argB) {
		this.log(Level.TRACE, msg, argA, argB);
	}

	@Override
	public void debug(String msg, Object arg) {
		this.log(Level.DEBUG, msg, arg);
	}

	@Override
	public void debug(String msg, Object argA, Object argB) {
		this.log(Level.DEBUG, msg, argA, argB);
	}

	@Override
	public void info(String msg, Object arg) {
		this.log(Level.INFO, msg, arg);
	}

	@Override
	public void info(String msg, Object argA, Object argB) {
		this.log(Level.INFO, msg, argA, argB);
	}

	@Override
	public void warn(String msg, Object arg) {
		this.log(Level.WARN, msg, arg);
	}

	@Override
	public void warn(String msg, Object argA, Object argB) {
		this.log(Level.WARN, msg, argA, argB);
	}

	@Override
	public void error(String msg, Object arg) {
		this.log(Level.ERROR, msg, arg);
	}

	@Override
	public void error(String msg, Object argA, Object argB) {
		this.log(Level.ERROR, msg, argA, argB);
	}

	@Override
	public void log(LoggerLevel level, String msg) {
		this.log(this.toLevel(level), msg);
	}

	@Override
	public void log(LoggerLevel level, String msg, Object arg) {
		this.log(this.toLevel(level), msg, arg);
	}

	@Override
	public void log(LoggerLevel level, String msg, Object argA, Object argB) {
		this.log(this.toLevel(level), msg, argA, argB);
	}

	@Override
	public void log(LoggerLevel level, String msg, Object... arguments) {
		this.log(this.toLevel(level), msg, arguments);
	}

	@Override
	public void log(LoggerLevel level, String msg, Throwable t) {
		this.log(this.toLevel(level), msg, t);
	}

	@Override
	public void log(LoggerLevel level, Throwable t) {
		this.log(this.toLevel(level), EXCEPTION_MESSAGE, t);
	}

	private Level toLevel(LoggerLevel level) {
		if (null == level) {
			throw new Error();
		}
		if (level == LoggerLevel.TRACE) {
			return Level.TRACE;
		} else if (level == LoggerLevel.DEBUG) {
			return Level.DEBUG;
		} else if (level == LoggerLevel.INFO) {
			return Level.INFO;
		} else if (level == LoggerLevel.WARN) {
			return Level.WARN;
		} else if (level == LoggerLevel.ERROR) {
			return Level.ERROR;
		}
		throw new Error();
	}

}