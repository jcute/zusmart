package com.zusmart.network.protocol;

import com.zusmart.basic.toolkit.Charset;
import com.zusmart.network.buffer.Buffer;
import com.zusmart.network.buffer.BufferFactory;
import com.zusmart.network.packet.Message;
import com.zusmart.network.packet.MessageProtocol;

public class StringMessageProtocol implements MessageProtocol {

	private static final byte[] token = "\r\n\r\n".getBytes();

	@Override
	public Buffer encode(Message message) throws Exception {
		StringMessage msg = (StringMessage) message;
		byte[] data = msg.getContent().getBytes();
		Buffer buffer = BufferFactory.allocate(data.length);
		buffer.put(data);
		return buffer;
	}

	@Override
	public Message decode(Buffer buffer) throws Exception {
		int index = buffer.indexOf(token);
		if (index >= 0) {
			String content = buffer.getString(Charset.UTF8, index + token.length);
			StringMessage msg = new StringMessage();
			msg.setContent(content);
			return msg;
		}
		return null;

	}

}