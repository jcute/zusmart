package com.zusmart.network.packet.support;

import com.zusmart.network.buffer.Buffer;
import com.zusmart.network.packet.Message;
import com.zusmart.network.packet.MessageProtocol;

public abstract class SimpleMessageProtocol<T extends Message> implements MessageProtocol {

	@SuppressWarnings("unchecked")
	@Override
	public final Buffer encode(Message message) throws Exception {
		try {
			return this.doEncode((T) message);
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public final Message decode(Buffer buffer) throws Exception {
		return this.doDecode(buffer);
	}

	protected abstract Buffer doEncode(T message) throws Exception;

	protected abstract T doDecode(Buffer buffer) throws Exception;

}