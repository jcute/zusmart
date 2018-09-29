package com.zusmart.network.buffer.support;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.zusmart.network.buffer.Buffer;

public class ByteArrayBuffer extends AbstractBuffer {

	private static final byte[] EMPTY_CONTENT = new byte[0];

	public static ByteArrayBuffer allocate(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayBuffer(new byte[capacity], 0, capacity);
	}

	public static ByteArrayBuffer wrap(byte[] array) {
		return wrap(array, 0, array.length);
	}

	public static ByteArrayBuffer wrap(byte[] array, int offset, int length) {
		checkBounds(offset, length, array.length);
		ByteArrayBuffer buffer = new ByteArrayBuffer(array, 0, array.length);
		buffer.limit(offset + length);
		buffer.position(offset);
		return buffer;
	}

	private byte[] content;

	public ByteArrayBuffer(byte[] content, int offset, int capacity) {
		super(offset, capacity);
		this.content = content;
	}

	@Override
	public boolean isDirect() {
		return false;
	}

	@Override
	public Buffer compact() {
		this.checkReadOnly();
		System.arraycopy(content, this.getIndex(0), content, getIndex(0, 0), this.remaining());
		this.position(this.remaining()).limit(this.capacity());
		return this;
	}

	@Override
	public Buffer slice() {
		return new DelegateByteArrayBuffer(this.getIndex(0), this.remaining());
	}

	@Override
	public Buffer duplicate() {
		ByteArrayBuffer buffer = new DelegateByteArrayBuffer(getIndex(0, 0), capacity());
		buffer.limit(this.limit()).position(this.position());
		buffer.mark(this.getMark());
		buffer.setReadOnly(this.isReadOnly());
		return buffer;
	}

	@Override
	public ByteBuffer asByteBuffer() {
		ByteBuffer buffer = ByteBuffer.wrap(content, getIndex(0, 0), capacity()).slice();
		buffer.position(position()).limit(limit());
		return this.isReadOnly() ? buffer.asReadOnlyBuffer() : buffer;
	}

	@Override
	protected void doRelease() {
		this.content = EMPTY_CONTENT;
	}

	@Override
	protected byte doGet(int index) {
		return this.content[index];
	}

	@Override
	protected void doPut(int index, byte b) {
		this.content[index] = b;
	}

	@Override
	public int write(WritableByteChannel channel) throws IOException {
		int count = 0;
		try {
			count = channel.write(ByteBuffer.wrap(this.content, this.getIndex(0), this.remaining()));
		} finally {
			this.skip(count);
		}
		return count;
	}

	@Override
	public int read(ReadableByteChannel channel) throws IOException {
		this.checkReadOnly();
		int count = 0;
		try {
			count = channel.read(ByteBuffer.wrap(this.content, this.getIndex(0), remaining()));
		} finally {
			if (count > 0) {
				this.skip(count);
			}
		}
		return count;
	}

	@Override
	public Buffer get(byte[] dst, int offset, int length) {
		checkBounds(offset, length, dst.length);
		System.arraycopy(this.content, this.getIndex(length), dst, offset, length);
		return this;
	}

	@Override
	public Buffer get(int index, byte[] dst, int offset, int length) {
		checkBounds(offset, length, dst.length);
		System.arraycopy(this.content, this.getIndex(index, length), dst, offset, length);
		return this;
	}

	@Override
	public Buffer get(ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		dst.put(this.content, getIndex(length), length);
		return this;
	}

	@Override
	public Buffer get(int index, ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		dst.put(this.content, this.getIndex(index, length), length);
		return this;
	}

	@Override
	public Buffer get(Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		dst.put(this.content, this.getIndex(length), length);
		return this;
	}

	@Override
	public Buffer get(int index, Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		dst.put(this.content, this.getIndex(index, length), length);
		return this;
	}

	@Override
	public Buffer put(byte[] src, int offset, int length) {
		checkBounds(offset, length, src.length);
		System.arraycopy(src, offset, this.content, this.putIndex(length), length);
		return this;
	}

	@Override
	public Buffer put(int index, byte[] src, int offset, int length) {
		checkBounds(offset, length, src.length);
		System.arraycopy(src, offset, this.content, this.putIndex(index, length), length);
		return this;
	}

	@Override
	public Buffer put(ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		src.get(this.content, this.putIndex(length), length);
		return this;
	}

	@Override
	public Buffer put(int index, ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		src.get(this.content, this.putIndex(index, length), length);
		return this;
	}

	@Override
	public Buffer put(Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		src.get(this.content, this.putIndex(length), length);
		return this;
	}

	@Override
	public Buffer put(int index, Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		src.get(this.content, this.putIndex(index, length), length);
		return this;
	}

	private class DelegateByteArrayBuffer extends ByteArrayBuffer {
		public DelegateByteArrayBuffer(int offset, int capacity) {
			super(content, offset, capacity);
		}

		public boolean isReleased() {
			return ByteArrayBuffer.this.isReleased();
		}

		public void release() {
			ByteArrayBuffer.this.release();
		}

		public boolean isPermanent() {
			return ByteArrayBuffer.this.isPermanent();
		}

		public Buffer setPermanent(boolean b) {
			return ByteArrayBuffer.this.setPermanent(b);
		}
	}

}