package com.zusmart.network.socket;

import com.zusmart.basic.toolkit.Executable;
import com.zusmart.network.NetAddress;

public interface SocketAcceptor extends Executable{
	
	public SocketSessionManager getSocketSessionManager();
	
	public SocketBossEventLoopGroup getSocketBossEventLoopGroup();
	
	public SocketWorkEventLoopGroup getSocketWorkEventLoopGroup();
	
	public NetAddress getServerAddress();
	
	public boolean isOpen();
	
}