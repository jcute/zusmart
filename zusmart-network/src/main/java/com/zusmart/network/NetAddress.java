package com.zusmart.network;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class NetAddress implements Serializable {

	private static final long serialVersionUID = -5232790807159911111L;

	private static final String DEF_HOST = "0.0.0.0";
	private static final int DEF_PORT = 0;

	private final String host;
	private final int port;

	public NetAddress(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public InetSocketAddress toSocketAddress() {
		return new InetSocketAddress(this.host, this.port);
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (obj instanceof NetAddress) {
			return obj.toString().equals(this.toString());
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("%s:%d", this.host, this.port);
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	public static NetAddress create(String host, int port) {
		return new NetAddress(host, port);
	}

	public static NetAddress create(String host) {
		return new NetAddress(host, DEF_PORT);
	}

	public static NetAddress create(int port) {
		return new NetAddress(DEF_HOST, port);
	}

	public static NetAddress create() {
		return new NetAddress(DEF_HOST, DEF_PORT);
	}

	public static NetAddress create(InetSocketAddress address) {
		return new NetAddress(address.getHostName(), address.getPort());
	}

	public static NetAddress create(InetAddress address) {
		return new NetAddress(address.getHostAddress(), DEF_PORT);
	}

}