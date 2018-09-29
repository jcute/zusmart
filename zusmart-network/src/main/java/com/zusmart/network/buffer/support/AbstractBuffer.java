package com.zusmart.network.buffer.support;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.zusmart.basic.toolkit.Charset;
import com.zusmart.network.buffer.Buffer;
import com.zusmart.network.buffer.BufferReleasedException;

public abstract class AbstractBuffer implements Buffer {

	private final int offset;

	private boolean readOnly = false;
	private boolean bigEndian = true;
	private boolean permanent = false;
	private boolean released = false;

	private int mark = -1;
	private int position = 0;
	private int limit;
	private int capacity;

	protected AbstractBuffer(int offset, int capacity) {
		this.offset = offset;
		this.capacity = capacity;
		this.limit(capacity);
	}

	@Override
	public boolean isPermanent() {
		return this.permanent;
	}

	@Override
	public Buffer setPermanent(boolean permanent) {
		this.checkReleased();
		this.permanent = permanent;
		return this;
	}

	@Override
	public boolean isReleased() {
		return this.released;
	}

	@Override
	public synchronized void release() {
		if (!(this.permanent || this.released)) {
			try {
				this.doRelease();
			} finally {
				this.released = true;
			}
		}
	}

	@Override
	public boolean isReadOnly() {
		return this.readOnly;
	}

	@Override
	public boolean isBigEndian() {
		return this.bigEndian;
	}

	@Override
	public Buffer setBigEndian(boolean bigEndian) {
		this.bigEndian = bigEndian;
		return this;
	}

	@Override
	public Buffer skip(int size) {
		if (size != 0) {
			this.position(this.position + size);
		}
		return this;
	}

	@Override
	public int capacity() {
		return this.capacity;
	}

	@Override
	public int limit() {
		return this.limit;
	}

	@Override
	public Buffer limit(int limit) {
		if (limit > this.capacity || limit < 0) {
			throw new IllegalArgumentException();
		}
		this.limit = limit;
		return this.position(Math.min(position, limit));
	}

	@Override
	public int position() {
		return this.position;
	}

	@Override
	public Buffer position(int position) {
		if (position > this.limit || position < 0) {
			throw new IllegalArgumentException();
		}
		this.position = position;
		if (this.mark > this.position) {
			this.mark = -1;
		}
		return this;
	}

	@Override
	public Buffer mark() {
		return this.mark(this.position);
	}

	@Override
	public Buffer reset() {
		if (this.mark < 0) {
			throw new InvalidMarkException();
		}
		this.position = mark;
		return this;
	}

	@Override
	public Buffer clear() {
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
		return this;
	}

	@Override
	public Buffer flip() {
		this.limit = this.position;
		this.position = 0;
		this.mark = -1;
		return this;
	}

	@Override
	public Buffer rewind() {
		this.position = 0;
		this.mark = -1;
		return this;
	}

	@Override
	public int remaining() {
		return this.limit - this.position;
	}

	@Override
	public boolean hasRemaining() {
		return this.position < this.limit;
	}

	@Override
	public int indexOf(byte data) {
		return this.indexOf(new byte[] { data });
	}

	@Override
	public int indexOf(byte[] data) {
		if (data.length == 0) {
			return this.position;
		}
		int startIndex = this.getIndex(0);
		int endIndex = startIndex + this.remaining() - data.length;
		byte first = data[0];
		Label: for (int i = startIndex; i <= endIndex; i++) {
			if (this.doGet(i) == first) {
				for (int j = 1; j < data.length; j++) {
					if (this.doGet(i + j) != data[j]) {
						continue Label;
					}
				}
				return i - startIndex + this.position;
			}
		}
		return -1;
	}

	@Override
	public int write(WritableByteChannel channel) throws IOException {
		int count = 0;
		ByteBuffer buffer = this.asByteBuffer();
		try {
			count = channel.write(buffer);
		} finally {
			this.skip(count);
		}
		return count;
	}

	@Override
	public int read(ReadableByteChannel channel) throws IOException {
		int count = 0;
		try {
			count = channel.read(this.asByteBuffer());
		} finally {
			if (count > 0) {
				this.skip(count);
			}
		}
		return count;
	}

	@Override
	public Buffer asReadOnlyBuffer() {
		return ((AbstractBuffer) this.duplicate()).setReadOnly(true);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Buffer)) {
			return false;
		}
		Buffer buffer = (Buffer) obj;
		if (this.remaining() != buffer.remaining()) {
			return false;
		}
		int start = this.getIndex(0);
		int end = start + this.remaining();
		for (int i = start, j = buffer.position(); i < end; i++, j++) {
			if (this.doGet(i) != buffer.get(j)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName());
		sb.append("[position=").append(position);
		sb.append(", limit=").append(limit);
		sb.append(", capacity=").append(capacity);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public byte get() {
		return this.doGet(getIndex(1));
	}

	@Override
	public byte get(int index) {
		return this.doGet(getIndex(index, 1));
	}

	@Override
	public Buffer get(byte[] dst) {
		return this.get(dst, 0, dst.length);
	}

	@Override
	public Buffer get(int index, byte[] dst) {
		return this.get(index, dst, 0, dst.length);
	}

	@Override
	public Buffer get(byte[] dst, int offset, int length) {
		return this.get(ByteBuffer.wrap(dst, offset, length));
	}

	@Override
	public Buffer get(int index, byte[] dst, int offset, int length) {
		return this.get(index, ByteBuffer.wrap(dst, offset, length));
	}

	@Override
	public Buffer get(ByteBuffer dst) {
		return this.get(dst, dst.remaining());
	}

	@Override
	public Buffer get(int index, ByteBuffer dst) {
		return this.get(index, dst, dst.remaining());
	}

	@Override
	public Buffer get(ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		this.getIndex(length);
		ByteBuffer buffer = asByteBuffer();
		buffer.limit(buffer.position());
		buffer.position(buffer.position() - length);
		dst.put(buffer);
		return this;
	}

	@Override
	public Buffer get(int index, ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		this.getIndex(index, length);
		ByteBuffer buffer = asByteBuffer();
		buffer.position(index);
		buffer.limit(index + length);
		dst.put(buffer);
		return this;
	}

	@Override
	public Buffer get(Buffer dst) {
		return this.get(dst, dst.remaining());
	}

	@Override
	public Buffer get(int index, Buffer dst) {
		return this.get(index, dst, dst.remaining());
	}

	@Override
	public Buffer get(Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		this.getIndex(length);
		ByteBuffer buffer = asByteBuffer();
		buffer.limit(buffer.position());
		buffer.position(buffer.position() - length);
		dst.put(buffer);
		return this;
	}

	@Override
	public Buffer get(int index, Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		this.getIndex(index, length);
		ByteBuffer buffer = asByteBuffer();
		buffer.position(index);
		buffer.limit(index + length);
		dst.put(buffer);
		return this;
	}

	@Override
	public Buffer put(byte b) {
		this.doPut(putIndex(1), b);
		return this;
	}

	@Override
	public Buffer put(int index, byte b) {
		this.doPut(putIndex(index, 1), b);
		return this;
	}

	@Override
	public Buffer put(byte[] src) {
		return this.put(src, 0, src.length);
	}

	@Override
	public Buffer put(int index, byte[] src) {
		return this.put(index, src, 0, src.length);
	}

	@Override
	public Buffer put(byte[] src, int offset, int length) {
		return this.put(ByteBuffer.wrap(src, offset, length));
	}

	@Override
	public Buffer put(int index, byte[] src, int offset, int length) {
		return this.put(index, ByteBuffer.wrap(src, offset, length));
	}

	@Override
	public Buffer put(ByteBuffer src) {
		return this.put(src, src.remaining());
	}

	@Override
	public Buffer put(int index, ByteBuffer src) {
		return this.put(index, src, src.remaining());
	}

	@Override
	public Buffer put(ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		int pos = putIndex(length);
		for (int i = 0; i < length; i++) {
			this.doPut(pos + i, src.get());
		}
		return this;
	}

	@Override
	public Buffer put(int index, ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		int pos = putIndex(index, length);
		for (int i = 0; i < length; i++) {
			this.doPut(pos + i, src.get());
		}
		return this;
	}

	@Override
	public Buffer put(Buffer src) {
		return this.put(src, src.remaining());
	}

	@Override
	public Buffer put(int index, Buffer src) {
		return this.put(index, src, src.remaining());
	}

	@Override
	public Buffer put(Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		int pos = putIndex(length);
		for (int i = 0; i < length; i++) {
			this.doPut(pos + i, src.get());
		}
		return this;
	}

	@Override
	public Buffer put(int index, Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		int pos = putIndex(index, length);
		for (int i = 0; i < length; i++) {
			this.doPut(pos + i, src.get());
		}
		return this;
	}

	@Override
	public char getChar() {
		return (char) getShort();
	}

	@Override
	public char getChar(int index) {
		return (char) getShort(index);
	}

	@Override
	public Buffer putChar(char c) {
		return putShort((short) c);
	}

	@Override
	public Buffer putChar(int index, char c) {
		return putShort(index, (short) c);
	}

	@Override
	public short getShort() {
		return AbstractBufferUtil.decodeShort(this, getIndex(2));
	}

	@Override
	public short getShort(int index) {
		return AbstractBufferUtil.decodeShort(this, getIndex(index, 2));
	}

	@Override
	public Buffer putShort(short s) {
		return AbstractBufferUtil.encodeShort(this, putIndex(2), s);
	}

	@Override
	public Buffer putShort(int index, short s) {
		return AbstractBufferUtil.encodeShort(this, putIndex(index, 2), s);
	}

	@Override
	public int getInt() {
		return AbstractBufferUtil.decodeInt(this, getIndex(4));
	}

	@Override
	public int getInt(int index) {
		return AbstractBufferUtil.decodeInt(this, getIndex(index, 4));
	}

	@Override
	public Buffer putInt(int i) {
		return AbstractBufferUtil.encodeInt(this, putIndex(4), i);
	}

	@Override
	public Buffer putInt(int index, int i) {
		return AbstractBufferUtil.encodeInt(this, putIndex(index, 4), i);
	}

	@Override
	public long getLong() {
		return AbstractBufferUtil.decodeLong(this, getIndex(8));
	}

	@Override
	public long getLong(int index) {
		return AbstractBufferUtil.decodeLong(this, getIndex(index, 8));
	}

	@Override
	public Buffer putLong(long l) {
		return AbstractBufferUtil.encodeLong(this, putIndex(8), l);
	}

	@Override
	public Buffer putLong(int index, long l) {
		return AbstractBufferUtil.encodeLong(this, putIndex(index, 8), l);
	}

	@Override
	public float getFloat() {
		return Float.intBitsToFloat(getInt());
	}

	@Override
	public float getFloat(int index) {
		return Float.intBitsToFloat(getInt(index));
	}

	@Override
	public Buffer putFloat(float f) {
		return putInt(Float.floatToRawIntBits(f));
	}

	@Override
	public Buffer putFloat(int index, float f) {
		return putInt(index, Float.floatToRawIntBits(f));
	}

	@Override
	public double getDouble() {
		return Double.longBitsToDouble(getLong());
	}

	@Override
	public double getDouble(int index) {
		return Double.longBitsToDouble(getLong(index));
	}

	@Override
	public Buffer putDouble(double d) {
		return putLong(Double.doubleToRawLongBits(d));
	}

	@Override
	public Buffer putDouble(int index, double d) {
		return putLong(index, Double.doubleToRawLongBits(d));
	}

	@Override
	public short getUnsignedByte() {
		return (short) (get() & 0xff);
	}

	@Override
	public short getUnsignedByte(int index) {
		return (short) (get(index) & 0xff);
	}

	@Override
	public Buffer putUnsignedByte(short s) {
		if (s < 0 || s > 0xff) {
			throw new IllegalArgumentException();
		}
		return put((byte) s);
	}

	@Override
	public Buffer putUnsignedByte(int index, short s) {
		if (s < 0 || s > 0xff) {
			throw new IllegalArgumentException();
		}
		return put(index, (byte) s);
	}

	@Override
	public int getUnsignedShort() {
		return getShort() & 0xffff;
	}

	@Override
	public int getUnsignedShort(int index) {
		return getShort(index) & 0xffff;
	}

	@Override
	public Buffer putUnsignedShort(int i) {
		if (i < 0 || i > 0xffff) {
			throw new IllegalArgumentException();
		}
		return putShort((short) i);
	}

	@Override
	public Buffer putUnsignedShort(int index, int i) {
		if (i < 0 || i > 0xffff) {
			throw new IllegalArgumentException();
		}
		return putShort(index, (short) i);
	}

	@Override
	public long getUnsignedInt() {
		return getInt() & 0xffffffffL;
	}

	@Override
	public long getUnsignedInt(int index) {
		return getInt(index) & 0xffffffffL;
	}

	@Override
	public Buffer putUnsignedInt(long l) {
		if (l < 0 || l > 0xffffffff) {
			throw new IllegalArgumentException();
		}
		return putInt((int) l);
	}

	@Override
	public Buffer putUnsignedInt(int index, long l) {
		if (l < 0 || l > 0xffffffff) {
			throw new IllegalArgumentException();
		}
		return putInt(index, (int) l);
	}

	@Override
	public String getString(Charset charset, int bufferLen) {
		this.getIndex(bufferLen);
		ByteBuffer buffer = asByteBuffer();
		buffer.limit(buffer.position());
		buffer.position(buffer.limit() - bufferLen);
		return charset.decode(buffer);
	}

	@Override
	public String getString(int index, Charset charset, int bufferLen) {
		this.getIndex(index, bufferLen);
		ByteBuffer buffer = asByteBuffer();
		buffer.position(index);
		buffer.limit(index + bufferLen);
		return charset.decode(buffer);
	}

	@Override
	public Buffer putString(String s, Charset charset) {
		return this.put(charset.encode(s));
	}

	@Override
	public Buffer putString(int index, String s, Charset charset) {
		return this.put(index, charset.encode(s));
	}

	protected abstract void doRelease();

	protected abstract byte doGet(int index);

	protected abstract void doPut(int index, byte b);

	protected final int getIndex(int i, int length) {
		this.checkReleased();
		if (length < 0 || i < 0 || length > this.limit - i) {
			throw new IndexOutOfBoundsException();
		}
		return i + this.offset;
	}

	protected final int getIndex(int length) {
		this.checkReleased();
		if (this.limit - this.position < length) {
			throw new BufferUnderflowException();
		}
		int position = this.position + offset;
		this.position += length;
		return position;
	}

	protected final int putIndex(int i, int length) {
		this.checkReleased();
		this.checkReadOnly();
		if (length < 0 || i < 0 || length > this.limit - i) {
			throw new IndexOutOfBoundsException();
		}
		return i + this.offset;
	}

	protected final int putIndex(int length) {
		this.checkReleased();
		this.checkReadOnly();
		if (this.limit - this.position < length) {
			throw new BufferOverflowException();
		}
		int position = this.position + this.offset;
		this.position += length;
		return position;
	}

	protected int getMark() {
		return this.mark;
	}

	protected Buffer mark(int mark) {
		if (mark < 0) {
			this.mark = -1;
		} else {
			if (mark > position) {
				throw new IndexOutOfBoundsException();
			}
			this.mark = mark;
		}
		return this;
	}

	protected Buffer capacity(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException();
		}
		this.capacity = capacity;
		return this.limit(Math.min(limit, capacity));
	}

	protected Buffer setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	protected final void checkReadOnly() {
		if (this.isReadOnly()) {
			throw new ReadOnlyBufferException();
		}
	}

	protected final void checkReleased() {
		if (this.isReleased()) {
			throw new BufferReleasedException();
		}
	}

	protected final static void checkBounds(int offset, int length, int size) {
		if ((offset | length | (offset + length) | (size - (offset + length))) < 0) {
			throw new IndexOutOfBoundsException();
		}
	}

}