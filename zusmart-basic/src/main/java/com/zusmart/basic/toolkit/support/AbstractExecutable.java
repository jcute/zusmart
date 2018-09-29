package com.zusmart.basic.toolkit.support;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.basic.toolkit.Executable;
import com.zusmart.basic.util.StringUtils;

public abstract class AbstractExecutable implements Executable {

	private static final Logger logger = LoggerFactory.getLogger(AbstractExecutable.class);

	private static final byte STATUS_CLOSED = 0x01;
	private static final byte STATUS_CLOSING = 0x02;
	private static final byte STATUS_STARTING = 0x03;
	private static final byte STATUS_STARTED = 0x04;
	private static final byte STATUS_START_FAILED = 0x05;
	private static final byte STATUS_CLOSE_FAILED = 0x06;

	private volatile byte status = STATUS_CLOSED;

	private static String getStatusName(byte status) {
		switch (status) {
		case STATUS_CLOSED:
			return "STATUS_CLOSED";
		case STATUS_CLOSING:
			return "STATUS_CLOSING";
		case STATUS_STARTING:
			return "STATUS_STARTING";
		case STATUS_STARTED:
			return "STATUS_STARTED";
		case STATUS_START_FAILED:
			return "STATUS_START_FAILED";
		case STATUS_CLOSE_FAILED:
			return "STATUS_CLOSE_FAILED";
		default:
			return "STATUS_UNKNOW";
		}
	}

	@Override
	public boolean isClosed() {
		return this.status == STATUS_CLOSED;
	}

	@Override
	public boolean isStarted() {
		return this.status == STATUS_STARTED;
	}

	@Override
	public boolean isClosing() {
		return this.status == STATUS_CLOSING;
	}

	@Override
	public boolean isStarting() {
		return this.status == STATUS_STARTING;
	}

	@Override
	public boolean isStartFailed() {
		return this.status == STATUS_START_FAILED;
	}

	@Override
	public boolean isCloseFailed() {
		return this.status == STATUS_CLOSE_FAILED;
	}

	@Override
	public final void start() throws Exception {
		synchronized (this) {
			if (this.isStarting() || this.isStarted()) {
				logger.debug("{} was {}", StringUtils.getSimpleClassName(this), getStatusName(status));
				return;
			}
			try {
				this.status = STATUS_STARTING;
				this.doStart();
				this.status = STATUS_STARTED;
			} catch (Exception e) {
				this.status = STATUS_START_FAILED;
				throw e;
			}
		}
	}

	@Override
	public final void close() throws Exception {
		synchronized (this) {
			if (this.isClosing() || this.isClosed()) {
				logger.debug("{} was {}", StringUtils.getSimpleClassName(this), getStatusName(status));
				return;
			}
			try {
				this.status = STATUS_CLOSING;
				this.doClose();
				this.status = STATUS_CLOSED;
			} catch (Exception e) {
				this.status = STATUS_CLOSE_FAILED;
				throw e;
			}
		}
	}

	protected abstract void doStart() throws Exception;

	protected abstract void doClose() throws Exception;

}