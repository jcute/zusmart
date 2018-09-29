package com.zusmart.network.protocol;

import com.zusmart.network.packet.Message;

public class StringMessage implements Message {

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return this.content;
	}

}