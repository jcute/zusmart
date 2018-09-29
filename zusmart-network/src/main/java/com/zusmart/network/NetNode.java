package com.zusmart.network;

import com.zusmart.basic.toolkit.Executable;
import com.zusmart.network.socket.SocketBossEventLoopGroup;
import com.zusmart.network.socket.SocketSessionAdapter;
import com.zusmart.network.socket.SocketSessionManager;
import com.zusmart.network.socket.SocketWorkEventLoopGroup;

public interface NetNode extends Executable{
	
	public NetNodeSetting getSetting();
	
	public NetAddress getServerAddress();
	
	public SocketSessionManager getSessionManager();
	
	public SocketSessionAdapter getSocketSessionAdapter();
	
	public SocketBossEventLoopGroup getSocketBossEventLoopGroup();
	
	public SocketWorkEventLoopGroup getSocketWorkEventLoopGroup();
	
}