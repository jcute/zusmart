package com.zusmart.basic.logging.support;

import java.io.Serializable;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerLevel;

public abstract class AbstractLogger implements Logger, Serializable {

	private static final long serialVersionUID = -1148319155814739280L;
	private static final String EXCEPTION_MESSAGE = "Unexpected exception";

	private final String name;

	protected AbstractLogger(String name) {
		if (null == name) {
			throw new NullPointerException("name");
		}
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isEnabled(LoggerLevel level) {
		if (null == level) {
			throw new Error("Logger level must not be null");
		}
		if (level == LoggerLevel.TRACE) {
			return this.isTraceEnabled();
		} else if (level == LoggerLevel.DEBUG) {
			return this.isDebugEnabled();
		} else if (level == LoggerLevel.INFO) {
			return this.isInfoEnabled();
		} else if (level == LoggerLevel.WARN) {
			return this.isWarnEnabled();
		} else if (level == LoggerLevel.ERROR) {
			return this.isErrorEnabled();
		}
		return false;
	}

	@Override
	public void trace(Throwable t) {
		this.trace(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void debug(Throwable t) {
		this.debug(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void info(Throwable t) {
		this.info(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void warn(Throwable t) {
		this.warn(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void error(Throwable t) {
		this.error(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void log(LoggerLevel level, String msg) {
		if (null == level) {
			throw new Error("Logger level must not be null");
		}
		if (level == LoggerLevel.TRACE) {
			this.trace(msg);
		} else if (level == LoggerLevel.DEBUG) {
			this.debug(msg);
		} else if (level == LoggerLevel.INFO) {
			this.info(msg);
		} else if (level == LoggerLevel.WARN) {
			this.warn(msg);
		} else if (level == LoggerLevel.ERROR) {
			this.error(msg);
		}
	}

	@Override
	public void log(LoggerLevel level, String msg, Object arg) {
		if (null == level) {
			throw new Error("Logger level must not be null");
		}
		if (level == LoggerLevel.TRACE) {
			this.trace(msg, arg);
		} else if (level == LoggerLevel.DEBUG) {
			this.debug(msg, arg);
		} else if (level == LoggerLevel.INFO) {
			this.info(msg, arg);
		} else if (level == LoggerLevel.WARN) {
			this.warn(msg, arg);
		} else if (level == LoggerLevel.ERROR) {
			this.error(msg, arg);
		}
	}

	@Override
	public void log(LoggerLevel level, String msg, Object argA, Object argB) {
		if (null == level) {
			throw new Error("logger level must not be null");
		}
		if (level == LoggerLevel.TRACE) {
			this.trace(msg, argA, argB);
		} else if (level == LoggerLevel.DEBUG) {
			this.debug(msg, argA, argB);
		} else if (level == LoggerLevel.INFO) {
			this.info(msg, argA, argB);
		} else if (level == LoggerLevel.WARN) {
			this.warn(msg, argA, argB);
		} else if (level == LoggerLevel.ERROR) {
			this.error(msg, argA, argB);
		}
	}

	@Override
	public void log(LoggerLevel level, String msg, Object... arguments) {
		if (null == level) {
			throw new Error("logger level must not be null");
		}
		if (level == LoggerLevel.TRACE) {
			this.trace(msg, arguments);
		} else if (level == LoggerLevel.DEBUG) {
			this.debug(msg, arguments);
		} else if (level == LoggerLevel.INFO) {
			this.info(msg, arguments);
		} else if (level == LoggerLevel.WARN) {
			this.warn(msg, arguments);
		} else if (level == LoggerLevel.ERROR) {
			this.error(msg, arguments);
		}
	}

	@Override
	public void log(LoggerLevel level, String msg, Throwable t) {
		if (null == level) {
			throw new Error("Logger level must not be null");
		}
		if (level == LoggerLevel.TRACE) {
			this.trace(msg, t);
		} else if (level == LoggerLevel.DEBUG) {
			this.debug(msg, t);
		} else if (level == LoggerLevel.INFO) {
			this.info(msg, t);
		} else if (level == LoggerLevel.WARN) {
			this.warn(msg, t);
		} else if (level == LoggerLevel.ERROR) {
			this.error(msg, t);
		}
	}

	@Override
	public void log(LoggerLevel level, Throwable t) {
		if (null == level) {
			throw new Error("Logger level must not be null");
		}
		if (level == LoggerLevel.TRACE) {
			this.trace(t);
		} else if (level == LoggerLevel.DEBUG) {
			this.debug(t);
		} else if (level == LoggerLevel.INFO) {
			this.info(t);
		} else if (level == LoggerLevel.WARN) {
			this.warn(t);
		} else if (level == LoggerLevel.ERROR) {
			this.error(t);
		}
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", this.getClass().getSimpleName(), this.getName());
	}

}