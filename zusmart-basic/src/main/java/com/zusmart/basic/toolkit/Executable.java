package com.zusmart.basic.toolkit;

public interface Executable {

	public boolean isClosed();

	public boolean isStarted();

	public boolean isClosing();

	public boolean isStarting();

	public boolean isStartFailed();

	public boolean isCloseFailed();

	public void start() throws Exception;

	public void close() throws Exception;

}