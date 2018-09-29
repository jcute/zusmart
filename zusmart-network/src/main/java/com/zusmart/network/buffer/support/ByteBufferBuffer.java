package com.zusmart.network.buffer.support;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.zusmart.network.buffer.Buffer;

public class ByteBufferBuffer extends AbstractBuffer {

	private static final ByteBuffer EMPTY_DIRECT_CONTENT = ByteBuffer.allocateDirect(0);
	private static final ByteBuffer EMPTY_HEAP_CONTENT = ByteBuffer.allocate(0);

	public static ByteBufferBuffer allocate(int capacity, boolean direct) {
		if (capacity < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteBufferBuffer(direct ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity), 0, capacity);
	}

	public static ByteBufferBuffer wrap(ByteBuffer buffer) {
		ByteBufferBuffer result = new ByteBufferBuffer(buffer.duplicate(), 0, buffer.capacity());
		result.limit(buffer.limit()).position(buffer.position());
		result.setReadOnly(buffer.isReadOnly());
		result.setBigEndian(buffer.order() == ByteOrder.BIG_ENDIAN);
		return result;
	}

	private ByteBuffer content;

	public ByteBufferBuffer(ByteBuffer content, int offset, int capacity) {
		super(offset, capacity);
		this.content = content;
	}

	@Override
	public boolean isDirect() {
		return content.isDirect();
	}

	@Override
	public ByteBuffer asByteBuffer() {
		int limit = content.limit();
		content.position(getIndex(0, 0));
		content.limit(content.position() + capacity());
		ByteBuffer buffer = content.slice();
		buffer.position(position());
		buffer.limit(limit());
		content.limit(limit);
		return isReadOnly() ? buffer.asReadOnlyBuffer() : buffer;
	}

	@Override
	public Buffer duplicate() {
		ByteBufferBuffer buffer = new DelegateByteBufferBuffer(getIndex(0, 0), capacity());
		buffer.limit(limit()).position(position());
		buffer.mark(getMark());
		buffer.setReadOnly(isReadOnly());
		return buffer;
	}

	@Override
	public Buffer slice() {
		return new DelegateByteBufferBuffer(getIndex(0), remaining()).setReadOnly(isReadOnly());
	}

	@Override
	public Buffer compact() {
		this.checkReadOnly();
		ByteBuffer duplicate = content.duplicate();
		duplicate.position(getIndex(0));
		duplicate.limit(duplicate.position() + remaining());
		content.position(getIndex(0, 0));
		content.put(duplicate);
		position(remaining()).limit(capacity());
		return this;
	}

	@Override
	protected void doRelease() {
		this.content = this.isDirect() ? EMPTY_DIRECT_CONTENT : EMPTY_HEAP_CONTENT;
	}

	@Override
	protected byte doGet(int index) {
		return this.content.get(index);
	}

	@Override
	protected void doPut(int index, byte b) {
		this.content.put(index, b);
	}

	@Override
	public int write(WritableByteChannel channel) throws IOException {
		int writeCount = 0;
		int limit = content.limit();
		int position = getIndex(0);
		content.position(position).limit(position + this.remaining());
		try {
			writeCount = channel.write(content);
		} finally {
			content.limit(limit);
			skip(writeCount);
		}
		return writeCount;
	}

	@Override
	public int read(ReadableByteChannel channel) throws IOException {
		this.checkReadOnly();
		int readCount = 0;
		int limit = content.limit();
		try {
			int position = getIndex(0);
			content.position(position).limit(position + remaining());
			readCount = channel.read(content);
		} finally {
			content.limit(limit);
			if (readCount > 0) {
				skip(readCount);
			}
		}
		return readCount;
	}

	@Override
	public Buffer get(byte[] dst, int offset, int length) {
		checkBounds(offset, length, dst.length);
		content.position(getIndex(length));
		content.get(dst, offset, length);
		return this;
	}

	@Override
	public Buffer get(int index, byte[] dst, int offset, int length) {
		checkBounds(offset, length, dst.length);
		content.position(getIndex(index, length));
		content.get(dst, offset, length);
		return this;
	}

	@Override
	public Buffer get(ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		content.position(getIndex(length));
		int limit = content.limit();
		content.limit(content.position() + length);
		dst.put(content);
		content.limit(limit);
		return this;
	}

	@Override
	public Buffer get(int index, ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		content.position(getIndex(index, length));
		int limit = content.limit();
		content.limit(content.position() + length);
		dst.put(content);
		content.limit(limit);
		return this;
	}

	@Override
	public Buffer get(Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		content.position(getIndex(length));
		dst.put(content, length);
		return this;
	}

	@Override
	public Buffer get(int index, Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		content.position(getIndex(index, length));
		dst.put(content, length);
		return this;
	}

	@Override
	public Buffer put(byte[] src, int offset, int length) {
		checkBounds(offset, length, src.length);
		content.position(putIndex(length));
		content.put(src, offset, length);
		return this;
	}

	@Override
	public Buffer put(int index, byte[] src, int offset, int length) {
		checkBounds(offset, length, src.length);
		content.position(putIndex(index, length));
		content.put(src, offset, length);
		return this;
	}

	@Override
	public Buffer put(ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		content.position(putIndex(length));
		int limit = src.limit();
		src.limit(src.position() + length);
		content.put(src);
		src.limit(limit);
		return this;
	}

	@Override
	public Buffer put(int index, ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		content.position(putIndex(index, length));
		int limit = src.limit();
		src.limit(src.position() + length);
		content.put(src);
		src.limit(limit);
		return this;
	}

	@Override
	public Buffer put(Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		content.position(putIndex(length));
		src.get(content, length);
		return this;
	}

	@Override
	public Buffer put(int index, Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		content.position(putIndex(index, length));
		src.get(content, length);
		return this;
	}

	@Override
	public char getChar() {
		return content.getChar(getIndex(2));
	}

	@Override
	public char getChar(int index) {
		return content.getChar(getIndex(index, 2));
	}

	@Override
	public Buffer putChar(char c) {
		content.putChar(putIndex(2), c);
		return this;
	}

	@Override
	public Buffer putChar(int index, char c) {
		content.putChar(putIndex(index, 2), c);
		return this;
	}

	@Override
	public short getShort() {
		return content.getShort(getIndex(2));
	}

	@Override
	public short getShort(int index) {
		return content.getShort(getIndex(index, 2));
	}

	@Override
	public Buffer putShort(short s) {
		content.putShort(putIndex(2), s);
		return this;
	}

	@Override
	public Buffer putShort(int index, short s) {
		content.putShort(putIndex(index, 2), s);
		return this;
	}

	@Override
	public int getInt() {
		return content.getInt(getIndex(4));
	}

	@Override
	public int getInt(int index) {
		return content.getInt(getIndex(index, 4));
	}

	@Override
	public Buffer putInt(int i) {
		content.putInt(putIndex(4), i);
		return this;
	}

	@Override
	public Buffer putInt(int index, int i) {
		content.putInt(putIndex(index, 4), i);
		return this;
	}

	@Override
	public long getLong() {
		return content.getLong(getIndex(8));
	}

	@Override
	public long getLong(int index) {
		return content.getLong(getIndex(index, 8));
	}

	@Override
	public Buffer putLong(long l) {
		content.putLong(putIndex(8), l);
		return this;
	}

	@Override
	public Buffer putLong(int index, long l) {
		content.putLong(putIndex(index, 8), l);
		return this;
	}

	@Override
	public float getFloat() {
		return content.getFloat(getIndex(4));
	}

	@Override
	public float getFloat(int index) {
		return content.getFloat(getIndex(index, 4));
	}

	@Override
	public Buffer putFloat(float f) {
		content.putFloat(putIndex(4), f);
		return this;
	}

	@Override
	public Buffer putFloat(int index, float f) {
		content.putFloat(putIndex(index, 4), f);
		return this;
	}

	@Override
	public double getDouble() {
		return content.getDouble(getIndex(8));
	}

	@Override
	public double getDouble(int index) {
		return content.getDouble(getIndex(index, 8));
	}

	@Override
	public Buffer putDouble(double d) {
		content.putDouble(putIndex(8), d);
		return this;
	}

	@Override
	public Buffer putDouble(int index, double d) {
		content.putDouble(putIndex(index, 8), d);
		return this;
	}

	private class DelegateByteBufferBuffer extends ByteBufferBuffer {

		public DelegateByteBufferBuffer(int offset, int capacity) {
			super(content, offset, capacity);
		}

		public boolean isReleased() {
			return ByteBufferBuffer.this.isReleased();
		}

		public void release() {
			ByteBufferBuffer.this.release();
		}

		public boolean isPermanent() {
			return ByteBufferBuffer.this.isPermanent();
		}

		public Buffer setPermanent(boolean b) {
			return ByteBufferBuffer.this.setPermanent(b);
		}

	}

}