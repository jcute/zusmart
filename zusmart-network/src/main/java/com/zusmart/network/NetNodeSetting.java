package com.zusmart.network;

import java.io.Serializable;

public class NetNodeSetting implements Serializable {

	private static final long serialVersionUID = -7869231238827731684L;

	private int readBufferSize = 8192;
	private boolean useDirect = false;
	private int sessionTimeoutMillis = 60 * 1000 * 30;
	private int sessionTimeoutCheckInterval = 5000;

	public int getReadBufferSize() {
		return readBufferSize;
	}

	public void setReadBufferSize(int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}

	public boolean isUseDirect() {
		return useDirect;
	}

	public void setUseDirect(boolean useDirect) {
		this.useDirect = useDirect;
	}

	public int getSessionTimeoutMillis() {
		return sessionTimeoutMillis;
	}

	public void setSessionTimeoutMillis(int sessionTimeoutMillis) {
		this.sessionTimeoutMillis = sessionTimeoutMillis;
	}

	public int getSessionTimeoutCheckInterval() {
		return sessionTimeoutCheckInterval;
	}

	public void setSessionTimeoutCheckInterval(int sessionTimeoutCheckInterval) {
		this.sessionTimeoutCheckInterval = sessionTimeoutCheckInterval;
	}

}