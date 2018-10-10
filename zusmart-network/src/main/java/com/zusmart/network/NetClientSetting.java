package com.zusmart.network;

public class NetClientSetting extends NetNodeSetting {

	private static final long serialVersionUID = 2443318011806583697L;

	private int bossEventLoopSize = Runtime.getRuntime().availableProcessors();
	private String bossEventLoopName = "boss";
	private int workEventLoopSize = 1;
	private String workEventLoopName = "work";
	private int workThreadMinSize = 5;
	private int workThreadMaxSize = 20;
	private int workThreadQueueSize = 512;
	private long workThreadKeepAlive = 60 * 60 * 30;

	private boolean keepAlive = true;
	private int receiveBufferSize = 8192;
	private int sendBufferSize = 8192;
	private int soTimeout = 0;
	private boolean tcpNpDelay = true;

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

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	public int getSendBufferSize() {
		return sendBufferSize;
	}

	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public boolean isTcpNpDelay() {
		return tcpNpDelay;
	}

	public void setTcpNpDelay(boolean tcpNpDelay) {
		this.tcpNpDelay = tcpNpDelay;
	}

}