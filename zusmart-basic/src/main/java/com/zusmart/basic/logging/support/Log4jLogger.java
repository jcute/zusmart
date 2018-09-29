package com.zusmart.basic.logging.support;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.zusmart.basic.logging.toolkit.Formatter;
import com.zusmart.basic.logging.toolkit.FormatterTuple;

public class Log4jLogger extends AbstractLogger {

	private static final long serialVersionUID = -5231365406021836047L;
	private static final String FQCN = Log4jLogger.class.getName();

	private final transient Logger logger;
	private final boolean traceCapable;

	public Log4jLogger(Logger logger) {
		super(logger.getName());
		this.logger = logger;
		this.traceCapable = this.isTraceCapable();
	}

	@Override
	public boolean isTraceEnabled() {
		if (this.traceCapable) {
			return this.logger.isTraceEnabled();
		} else {
			return this.logger.isDebugEnabled();
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return this.logger.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return this.logger.isEnabledFor(Level.WARN);
	}

	@Override
	public boolean isErrorEnabled() {
		return this.logger.isEnabledFor(Level.ERROR);
	}

	@Override
	public void trace(String msg) {
		this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, msg, null);
	}

	@Override
	public void trace(String msg, Object arg) {
		if (this.isTraceEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.logger.log(FQCN, traceCapable ? Level.TRACE : Level.DEBUG, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void trace(String msg, Object argA, Object argB) {
		if (this.isTraceEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.logger.log(FQCN, traceCapable ? Level.TRACE : Level.DEBUG, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void trace(String msg, Object... arguments) {
		if (this.isTraceEnabled()) {
			FormatterTuple formatterTuple = Formatter.arrayFormat(msg, arguments);
			this.logger.log(FQCN, traceCapable ? Level.TRACE : Level.DEBUG, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void trace(String msg, Throwable t) {
		this.logger.log(FQCN, traceCapable ? Level.TRACE : Level.DEBUG, msg, t);
	}

	@Override
	public void debug(String msg) {
		this.logger.debug(msg, null);
	}

	@Override
	public void debug(String msg, Object arg) {
		if (this.isDebugEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.logger.log(FQCN, Level.DEBUG, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void debug(String msg, Object argA, Object argB) {
		if (this.isDebugEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.logger.log(FQCN, Level.DEBUG, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void debug(String msg, Object... arguments) {
		if (this.isDebugEnabled()) {
			FormatterTuple formatterTuple = Formatter.arrayFormat(msg, arguments);
			this.logger.log(FQCN, Level.DEBUG, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void debug(String msg, Throwable t) {
		this.logger.log(FQCN, Level.DEBUG, msg, t);
	}

	@Override
	public void info(String msg) {
		this.logger.info(msg, null);
	}

	@Override
	public void info(String msg, Object arg) {
		if (this.isInfoEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.logger.log(FQCN, Level.INFO, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void info(String msg, Object argA, Object argB) {
		if (this.isInfoEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.logger.log(FQCN, Level.INFO, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void info(String msg, Object... arguments) {
		if (this.isInfoEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arguments);
			this.logger.log(FQCN, Level.INFO, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void info(String msg, Throwable t) {
		this.logger.log(FQCN, Level.INFO, msg, t);
	}

	@Override
	public void warn(String msg) {
		this.logger.warn(msg, null);
	}

	@Override
	public void warn(String msg, Object arg) {
		if (this.isWarnEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.logger.log(FQCN, Level.WARN, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void warn(String msg, Object argA, Object argB) {
		if (this.isWarnEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.logger.log(FQCN, Level.WARN, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void warn(String msg, Object... arguments) {
		if (this.isWarnEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arguments);
			this.logger.log(FQCN, Level.WARN, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void warn(String msg, Throwable t) {
		this.logger.warn(msg, t);
	}

	@Override
	public void error(String msg) {
		this.logger.error(msg, null);
	}

	@Override
	public void error(String msg, Object arg) {
		if (this.isErrorEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arg);
			this.logger.log(FQCN, Level.ERROR, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void error(String msg, Object argA, Object argB) {
		if (this.isErrorEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, argA, argB);
			this.logger.log(FQCN, Level.ERROR, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void error(String msg, Object... arguments) {
		if (this.isErrorEnabled()) {
			FormatterTuple formatterTuple = Formatter.format(msg, arguments);
			this.logger.log(FQCN, Level.ERROR, formatterTuple.getMessage(), formatterTuple.getThrowable());
		}
	}

	@Override
	public void error(String msg, Throwable t) {
		this.logger.error(msg, t);
	}

	private boolean isTraceCapable() {
		try {
			this.logger.isTraceEnabled();
			return true;
		} catch (NoSuchMethodError e) {
			return false;
		}
	}

}