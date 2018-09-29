package com.zusmart.network.packet.support;

import com.zusmart.network.packet.Message;

public class ByteArrayMessage implements Message {

	private byte[] content;

	public ByteArrayMessage() {
	}

	public ByteArrayMessage(byte[] content) {
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}