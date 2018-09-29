package com.zusmart.network.socket.support;

import com.zusmart.basic.toolkit.SnowflakeIdWorker;
import com.zusmart.network.socket.SocketSessionSequenceGenerator;

public class DefaultSocketSessionSequenceGenerator implements SocketSessionSequenceGenerator {

	private static final SnowflakeIdWorker worker = new SnowflakeIdWorker(8, 8);

	@Override
	public String create() {
		return String.valueOf(worker.nextId());
	}

}