package com.zusmart.network.buffer;

import java.nio.ByteBuffer;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;
import com.zusmart.network.buffer.support.ByteArrayBuffer;
import com.zusmart.network.buffer.support.ByteBufferBuffer;
import com.zusmart.network.buffer.support.DefaultBufferPool;
import com.zusmart.network.buffer.support.LinkedBuffer;

public class BufferFactory {

	private static final Logger logger = LoggerFactory.getLogger(BufferFactory.class);

	public static Buffer wrap(byte[] array) {
		return ByteArrayBuffer.wrap(array);
	}

	public static Buffer wrap(byte[] array, int offset, int length) {
		return ByteArrayBuffer.wrap(array, offset, length);
	}

	public static Buffer wrap(ByteBuffer buffer) {
		return ByteBufferBuffer.wrap(buffer);
	}

	public static Buffer wrap(Buffer[] buffers) {
		return new LinkedBuffer(buffers);
	}

	private static final BufferPool pool;
	private static final boolean useDirectBuffer = false;

	static {
		String className = DefaultBufferPool.class.getName();
		BufferPool tempPool = null;
		if (className != null)
			try {
				tempPool = (BufferPool) Class.forName(className).newInstance();
			} catch (Exception e) {
				logger.error(e);
			}
		pool = tempPool == null ? new DefaultBufferPool() : tempPool;
	}

	public static Buffer allocate(int capacity) {
		return allocate(capacity, useDirectBuffer);
	}

	public static Buffer allocate(int capacity, boolean direct) {
		if (capacity < 0) {
			throw new IllegalArgumentException();
		}
		return pool.allocate(capacity, direct);
	}
}