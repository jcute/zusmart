package com.zusmart.network.packet.support;

import com.zusmart.network.buffer.Buffer;
import com.zusmart.network.buffer.BufferFactory;

public class ByteArrayMessageProtocol extends SimpleMessageProtocol<ByteArrayMessage> {

	@Override
	protected Buffer doEncode(ByteArrayMessage message) throws Exception {
		byte[] content = message.getContent();
		Buffer buffer = null;
		if (null == content || content.length == 0) {
			buffer = BufferFactory.allocate(4);
			buffer.putInt(0);
			buffer.put(new byte[0]);
		} else {
			buffer = BufferFactory.allocate(content.length + 4);
			buffer.putInt(content.length);
			buffer.put(content);
		}
		return buffer;
	}

	@Override
	protected ByteArrayMessage doDecode(Buffer buffer) throws Exception {
		if (buffer.remaining() < 4) {
			return null;
		}
		int length = buffer.getInt();
		byte[] content = new byte[length];
		buffer.get(content);
		return new ByteArrayMessage(content);
	}

}