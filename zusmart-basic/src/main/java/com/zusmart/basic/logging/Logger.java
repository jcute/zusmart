package com.zusmart.basic.logging;

/**
 * 日志对象封装,自动适配
 * 
 * @author koko
 * 
 */
public interface Logger {

	public String getName();

	public boolean isTraceEnabled();

	public boolean isDebugEnabled();

	public boolean isInfoEnabled();

	public boolean isWarnEnabled();

	public boolean isErrorEnabled();

	public boolean isEnabled(LoggerLevel level);

	public void trace(String msg);

	public void trace(String msg, Object arg);

	public void trace(String msg, Object argA, Object argB);

	public void trace(String msg, Object... arguments);

	public void trace(String msg, Throwable t);

	public void trace(Throwable t);

	public void debug(String msg);

	public void debug(String msg, Object arg);

	public void debug(String msg, Object argA, Object argB);

	public void debug(String msg, Object... arguments);

	public void debug(String msg, Throwable t);

	public void debug(Throwable t);

	public void info(String msg);

	public void info(String msg, Object arg);

	public void info(String msg, Object argA, Object argB);

	public void info(String msg, Object... arguments);

	public void info(String msg, Throwable t);

	public void info(Throwable t);

	public void warn(String msg);

	public void warn(String msg, Object arg);

	public void warn(String msg, Object argA, Object argB);

	public void warn(String msg, Object... arguments);

	public void warn(String msg, Throwable t);

	public void warn(Throwable t);

	public void error(String msg);

	public void error(String msg, Object arg);

	public void error(String msg, Object argA, Object argB);

	public void error(String msg, Object... arguments);

	public void error(String msg, Throwable t);

	public void error(Throwable t);

	public void log(LoggerLevel level, String msg);

	public void log(LoggerLevel level, String msg, Object arg);

	public void log(LoggerLevel level, String msg, Object argA, Object argB);

	public void log(LoggerLevel level, String msg, Object... arguments);

	public void log(LoggerLevel level, String msg, Throwable t);

	public void log(LoggerLevel level, Throwable t);

}