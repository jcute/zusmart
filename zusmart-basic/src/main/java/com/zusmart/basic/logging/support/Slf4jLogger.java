package com.zusmart.basic.logging.support;

import org.slf4j.Logger;

public class Slf4jLogger extends AbstractLogger {

	private static final long serialVersionUID = -263870631309434660L;

	private final transient Logger logger;

	public Slf4jLogger(Logger logger) {
		super(logger.getName());
		this.logger = logger;
	}

	@Override
	public boolean isTraceEnabled() {
		return this.logger.isTraceEnabled();
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
		return this.logger.isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return this.logger.isErrorEnabled();
	}

	@Override
	public void trace(String msg) {
		this.logger.trace(msg);
	}

	@Override
	public void trace(String msg, Object arg) {
		this.logger.trace(msg, arg);
	}

	@Override
	public void trace(String msg, Object argA, Object argB) {
		this.logger.trace(msg, argA, argB);
	}

	@Override
	public void trace(String msg, Object... arguments) {
		this.logger.trace(msg, arguments);
	}

	@Override
	public void trace(String msg, Throwable t) {
		this.logger.trace(msg, t);
	}

	@Override
	public void debug(String msg) {
		this.logger.debug(msg);
	}

	@Override
	public void debug(String msg, Object arg) {
		this.logger.debug(msg, arg);
	}

	@Override
	public void debug(String msg, Object argA, Object argB) {
		this.logger.debug(msg, argA, argB);
	}

	@Override
	public void debug(String msg, Object... arguments) {
		this.logger.debug(msg, arguments);
	}

	@Override
	public void debug(String msg, Throwable t) {
		this.logger.debug(msg, t);
	}

	@Override
	public void info(String msg) {
		this.logger.info(msg);
	}

	@Override
	public void info(String msg, Object arg) {
		this.logger.info(msg, arg);
	}

	@Override
	public void info(String msg, Object argA, Object argB) {
		this.logger.info(msg, argA, argB);
	}

	@Override
	public void info(String msg, Object... arguments) {
		this.logger.info(msg, arguments);
	}

	@Override
	public void info(String msg, Throwable t) {
		this.logger.info(msg, t);
	}

	@Override
	public void warn(String msg) {
		this.logger.warn(msg);
	}

	@Override
	public void warn(String msg, Object arg) {
		this.logger.warn(msg, arg);
	}

	@Override
	public void warn(String msg, Object argA, Object argB) {
		this.logger.warn(msg, argA, argB);
	}

	@Override
	public void warn(String msg, Object... arguments) {
		this.logger.warn(msg, arguments);
	}

	@Override
	public void warn(String msg, Throwable t) {
		this.logger.warn(msg, t);
	}

	@Override
	public void error(String msg) {
		this.logger.error(msg);
	}

	@Override
	public void error(String msg, Object arg) {
		this.logger.error(msg, arg);
	}

	@Override
	public void error(String msg, Object argA, Object argB) {
		this.logger.error(msg, argA, argB);
	}

	@Override
	public void error(String msg, Object... arguments) {
		this.logger.error(msg, arguments);
	}

	@Override
	public void error(String msg, Throwable t) {
		this.logger.error(msg, t);
	}

}