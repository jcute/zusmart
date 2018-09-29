package com.zusmart.network.packet;

import com.zusmart.network.buffer.Buffer;

public interface MessageProtocol {
	
	public Buffer encode(Message message) throws Exception;

	public Message decode(Buffer buffer) throws Exception;

}