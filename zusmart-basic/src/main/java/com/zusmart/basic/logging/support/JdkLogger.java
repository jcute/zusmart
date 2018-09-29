package com.zusmart.basic.logging.support;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.zusmart.basic.logging.toolkit.Formatter;
import com.zusmart.basic.logging.toolkit.FormatterTuple;

public class JdkLogger extends AbstractLogger {

	private static final long serialVersionUID = 1687692606885305826L;
	private static final String SELF = JdkLogger.class.getName();
	private static final String SUPE = AbstractLogger.class.getName();

	private final transient Logger logger;

	public JdkLogger(Logger logger) {
		super(logger.getName());
		this.logger = logger;
	}

	@Override
	public boolean isTraceEnabled() {
		return this.logger.isLoggable(Level.FINEST);
	}

	@Override
	public boolean isDebugEnabled() {
		return this.logger.isLoggable(Level.FINE);
	}

	@Override
	public boolean isInfoEnabled() {
		return this.logger.isLoggable(Level.INFO);
	}

	@Override
	public boolean isWarnEnabled() {
		return this.logger.isLoggable(Level.WARNING);
	}

	@Override
	public boolean isErrorEnabled() {
		return this.logger.isLoggable(Level.SEVERE);
	}

	@Override
	public void trace(String msg) {
		if (this.isTraceEnabled()) {
			this.log(SELF, Level.FINEST, msg, null);
		}
	}

	@Override
	public void trace(String msg, Object arg) {
		if (this.isTraceEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.log(SELF, Level.FINEST, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void trace(String msg, Object argA, Object argB) {
		if (this.isTraceEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.log(SELF, Level.FINEST, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void trace(String msg, Object... arguments) {
		if (this.isTraceEnabled()) {
			FormatterTuple formatterTuple = Formatter.arrayFormat(msg, arguments);
			this.log(SELF, Level.FINEST, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void trace(String msg, Throwable t) {
		if (this.isTraceEnabled()) {
			this.log(SELF, Level.FINEST, msg, t);
		}
	}

	@Override
	public void debug(String msg) {
		if (this.isDebugEnabled()) {
			this.log(SELF, Level.FINE, msg, null);
		}
	}

	@Override
	public void debug(String msg, Object arg) {
		if (this.isDebugEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.log(SELF, Level.FINE, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void debug(String msg, Object argA, Object argB) {
		if (this.isDebugEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.log(SELF, Level.FINE, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void debug(String msg, Object... arguments) {
		if (this.isDebugEnabled()) {
			FormatterTuple formatterTuple = Formatter.arrayFormat(msg, arguments);
			this.log(SELF, Level.FINE, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void debug(String msg, Throwable t) {
		if (this.isDebugEnabled()) {
			this.log(SELF, Level.FINE, msg, t);
		}
	}

	@Override
	public void info(String msg) {
		if (this.isInfoEnabled()) {
			this.log(SELF, Level.INFO, msg, null);
		}
	}

	@Override
	public void info(String msg, Object arg) {
		if (this.isInfoEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.log(SELF, Level.INFO, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void info(String msg, Object argA, Object argB) {
		if (this.isInfoEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.log(SELF, Level.INFO, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void info(String msg, Object... arguments) {
		if (this.isInfoEnabled()) {
			FormatterTuple formatterTuple = Formatter.arrayFormat(msg, arguments);
			this.log(SELF, Level.INFO, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void info(String msg, Throwable t) {
		if (this.isInfoEnabled()) {
			this.log(SELF, Level.INFO, msg, t);
		}
	}

	@Override
	public void warn(String msg) {
		if (this.isWarnEnabled()) {
			this.log(SELF, Level.WARNING, msg, null);
		}
	}

	@Override
	public void warn(String msg, Object arg) {
		if (this.isWarnEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.log(SELF, Level.WARNING, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void warn(String msg, Object argA, Object argB) {
		if (this.isWarnEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.log(SELF, Level.WARNING, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void warn(String msg, Object... arguments) {
		if (this.isWarnEnabled()) {
			FormatterTuple formatterTuple = Formatter.arrayFormat(msg, arguments);
			this.log(SELF, Level.WARNING, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void warn(String msg, Throwable t) {
		if (this.isWarnEnabled()) {
			this.log(SELF, Level.WARNING, msg, t);
		}
	}

	@Override
	public void error(String msg) {
		if (this.isErrorEnabled()) {
			this.log(SELF, Level.SEVERE, msg, null);
		}
	}

	@Override
	public void error(String msg, Object arg) {
		if (this.isErrorEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.log(SELF, Level.SEVERE, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void error(String msg, Object argA, Object argB) {
		if (this.isErrorEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.log(SELF, Level.SEVERE, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void error(String msg, Object... arguments) {
		if (this.isErrorEnabled()) {
			FormatterTuple formatterTuple = Formatter.arrayFormat(msg, arguments);
			this.log(SELF, Level.SEVERE, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void error(String msg, Throwable t) {
		if (this.isErrorEnabled()) {
			this.log(SELF, Level.SEVERE, msg, t);
		}
	}

	private void log(String caller, Level level, String msg, Throwable t) {
		LogRecord record = new LogRecord(level, msg);
		record.setLoggerName(this.getName());
		record.setThrown(t);
		fillCallerData(caller, record);
		this.logger.log(record);
	}

	private static void fillCallerData(String caller, LogRecord record) {
		StackTraceElement[] stackTraceArray = new Throwable().getStackTrace();
		int selfIndex = -1;
		for (int i = 0; i < stackTraceArray.length; i++) {
			final String className = stackTraceArray[i].getClassName();
			if (className.equals(caller) || caller.equals(SUPE)) {
				selfIndex = i;
				break;
			}
		}
		int found = -1;
		for (int i = selfIndex + 1; i < stackTraceArray.length; i++) {
			final String className = stackTraceArray[i].getClassName();
			if (!(className.equals(caller) || className.equals(SUPE))) {
				found = i;
				break;
			}
		}
		if (found != -1) {
			StackTraceElement ste = stackTraceArray[found];
			record.setSourceClassName(ste.getClassName());
			record.setSourceMethodName(ste.getMethodName());
		}
	}

}