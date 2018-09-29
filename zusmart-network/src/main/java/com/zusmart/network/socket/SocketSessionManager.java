package com.zusmart.network.socket;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zusmart.basic.toolkit.Executable;

public interface SocketSessionManager extends Executable, Iterable<Entry<String, SocketSession>> {

	public SocketSessionSequenceGenerator getSocketSessionSequenceGenerator();

	public SocketSession createSocketSession(boolean isServerSide,SocketChannel socketChannel, SocketBossEventLoop socketBossEventLoop, SocketWorkEventLoop socketWorkEventLoop);

	public SocketSession getSocketSession(String socketSessionSequence);

	public Map<String, SocketSession> getAllSocketSessionAsMap();

	public Set<String> getAllSocketSessionSequence();

	public Set<SocketSession> getAllSocketSessionAsSet();

}