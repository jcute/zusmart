package com.zusmart.network;

public class NetServerSetting extends NetNodeSetting {

	private static final long serialVersionUID = 19706025406035937L;

	private int backLog;

	private String acceptName = "accept";

	private int bossEventLoopSize = Runtime.getRuntime().availableProcessors();
	private String bossEventLoopName = "boss";
	private int workEventLoopSize = 1;
	private String workEventLoopName = "work";
	private int workThreadMinSize = 5;
	private int workThreadMaxSize = 20;
	private int workThreadQueueSize = 512;
	private long workThreadKeepAlive = 60 * 60 * 30;

	private boolean reuseAddress = true;
	private int receiveBufferSize = 8192;
	private int soTimeout = 0;

	public int getBackLog() {
		return backLog;
	}

	public void setBackLog(int backLog) {
		this.backLog = backLog;
	}

	public String getAcceptName() {
		return acceptName;
	}

	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}

	public int getBossEventLoopSize() {
		return bossEventLoopSize;
	}

	public void setBossEventLoopSize(int bossEventLoopSize) {
		this.bossEventLoopSize = bossEventLoopSize;
	}

	public String getBossEventLoopName() {
		return bossEventLoopName;
	}

	public void setBossEventLoopName(String bossEventLoopName) {
		this.bossEventLoopName = bossEventLoopName;
	}

	public int getWorkEventLoopSize() {
		return workEventLoopSize;
	}

	public void setWorkEventLoopSize(int workEventLoopSize) {
		this.workEventLoopSize = workEventLoopSize;
	}

	public String getWorkEventLoopName() {
		return workEventLoopName;
	}

	public void setWorkEventLoopName(String workEventLoopName) {
		this.workEventLoopName = workEventLoopName;
	}

	public int getWorkThreadMinSize() {
		return workThreadMinSize;
	}

	public void setWorkThreadMinSize(int workThreadMinSize) {
		this.workThreadMinSize = workThreadMinSize;
	}

	public int getWorkThreadMaxSize() {
		return workThreadMaxSize;
	}

	public void setWorkThreadMaxSize(int workThreadMaxSize) {
		this.workThreadMaxSize = workThreadMaxSize;
	}

	public int getWorkThreadQueueSize() {
		return workThreadQueueSize;
	}

	public void setWorkThreadQueueSize(int workThreadQueueSize) {
		this.workThreadQueueSize = workThreadQueueSize;
	}

	public long getWorkThreadKeepAlive() {
		return workThreadKeepAlive;
	}

	public void setWorkThreadKeepAlive(long workThreadKeepAlive) {
		this.workThreadKeepAlive = workThreadKeepAlive;
	}

	public boolean isReuseAddress() {
		return reuseAddress;
	}

	public void setReuseAddress(boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

}