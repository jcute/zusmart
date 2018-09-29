package com.zusmart.network.support;

import com.zusmart.basic.toolkit.support.AbstractExecutable;
import com.zusmart.network.NetNode;
import com.zusmart.network.NetNodeSetting;
import com.zusmart.network.socket.SocketSessionAdapter;

public abstract class AbstractNetNode extends AbstractExecutable implements NetNode {

	private final NetNodeSetting netNodeSetting;
	private final SocketSessionAdapter socketSessionAdapter;
	
	protected AbstractNetNode(NetNodeSetting netNodeSetting, SocketSessionAdapter socketSessionAdapter) {
		if (null == netNodeSetting) {
			throw new IllegalArgumentException("setting must not be null");
		}
		if (null == socketSessionAdapter) {
			throw new IllegalArgumentException("socket session adapter must not be null");
		}
		this.netNodeSetting = netNodeSetting;
		this.socketSessionAdapter = socketSessionAdapter;
	}

	@Override
	public NetNodeSetting getSetting() {
		return this.netNodeSetting;
	}

	@Override
	public SocketSessionAdapter getSocketSessionAdapter() {
		return this.socketSessionAdapter;
	}

}