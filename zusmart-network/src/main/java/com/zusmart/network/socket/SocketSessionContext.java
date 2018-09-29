package com.zusmart.network.socket;

import com.zusmart.network.NetAddress;
import com.zusmart.network.packet.Message;

public interface SocketSessionContext {

	public void write(Message message);
	
	public void flush();
	
	public void writeAndFlush(Message message);

	public void close();
	
	public boolean isOpen();
	
	public NetAddress getServerAddress();
	
	public NetAddress getClientAddress();
	
}