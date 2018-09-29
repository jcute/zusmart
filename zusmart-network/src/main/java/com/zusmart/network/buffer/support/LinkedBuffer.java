package com.zusmart.network.buffer.support;

import java.nio.ByteBuffer;

import com.zusmart.basic.toolkit.Charset;
import com.zusmart.network.buffer.Buffer;
import com.zusmart.network.buffer.BufferFactory;

public class LinkedBuffer extends AbstractBuffer {

	protected final Entry header;

	public LinkedBuffer(Buffer[] content) {
		super(0, 0);
		header = new Entry();
		header.previous = header.next = header;
		for (int i = 0; i < content.length; i++) {
			append(content[i].duplicate());
		}
		limit(capacity());
	}

	private LinkedBuffer(Entry header, int offset, int capacity) {
		super(offset, capacity);
		this.header = header;
	}

	private class DelegateReleaseBuffer extends LinkedBuffer {

		public DelegateReleaseBuffer(int offset, int capacity) {
			super(LinkedBuffer.this.header, offset, capacity);
		}

		public boolean isReleased() {
			return LinkedBuffer.this.isReleased();
		}

		public void release() {
			LinkedBuffer.this.release();
		}

		public boolean isPermanent() {
			return LinkedBuffer.this.isPermanent();
		}

		public Buffer setPermanent(boolean b) {
			return LinkedBuffer.this.setPermanent(b);
		}

	}

	public void appendBuffer(Buffer buffer) {
		this.append(buffer);
	}

	protected void append(Buffer buffer) {
		if (buffer != null) {
			Entry entry = new Entry();
			Entry last = header.previous;

			last.next = entry;
			entry.previous = last;
			entry.next = header;
			header.previous = entry;

			entry.buffer = buffer;
			entry.position = (last == header ? 0 : last.buffer.remaining()) + last.position;

			// only effect current buffer
			capacity(capacity() + buffer.remaining());
		}
	}

	protected void remove(Buffer buffer) {
		for (Entry e = header.next; e != header; e = e.next) {
			if (e.buffer == buffer) {
				Entry removedEntry = e;
				int removedSize = e.buffer.remaining();

				e.previous.next = e.next;
				e.next.previous = e.previous;
				while ((e = e.next) != header) {
					e.position -= removedSize;
				}

				removedEntry.previous = null;
				removedEntry.next = null;
				removedEntry.buffer = null;

				// only effect current buffer
				capacity(capacity() - removedSize);
				limit(capacity());
				position(0);
				return;
			}
		}
	}

	@Override
	public boolean isDirect() {
		for (Entry e = header.next; e != header; e = e.next) {
			if (!e.buffer.isDirect())
				return false;
		}
		return header.next != header; // is not empty
	}

	@Override
	public Buffer duplicate() {
		LinkedBuffer buffer = new DelegateReleaseBuffer(getIndex(0, 0), capacity());
		buffer.limit(limit()).position(position());
		buffer.mark(getMark());
		buffer.setReadOnly(isReadOnly());
		return buffer;
	}

	@Override
	public Buffer slice() {
		return new DelegateReleaseBuffer(getIndex(0), remaining()).setReadOnly(isReadOnly());
	}

	@Override
	public ByteBuffer asByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(capacity());
		batch(true, getIndex(0, 0), buffer, buffer.capacity());
		buffer.position(position());
		buffer.limit(limit());
		return buffer;
	}

	@Override
	public Buffer compact() {
		checkReadOnly();

		Buffer buffer = BufferFactory.allocate(remaining());
		batch(true, getIndex(0), buffer, buffer.capacity());
		buffer.position(0);

		int index = getIndex(0, 0);
		Entry entry = getEntry(index);
		int offset = index - entry.position;

		do {
			Buffer content = entry.buffer;
			buffer.limit(Math.min(buffer.capacity(), buffer.position() + content.remaining() - offset));
			content.put(content.position() + offset, buffer);
			if (buffer.position() == buffer.capacity())
				break;
			offset = 0;
		} while ((entry = entry.next) != header);

		buffer.release();

		position(remaining());
		limit(capacity());
		return this;
	}

	@Override
	public Buffer get(byte[] dst, int offset, int length) {
		checkBounds(offset, length, dst.length);
		return batch(true, getIndex(length), dst, offset, length);
	}

	@Override
	public Buffer get(int index, byte[] dst, int offset, int length) {
		checkBounds(offset, length, dst.length);
		return batch(true, getIndex(index, length), dst, offset, length);
	}

	@Override
	public Buffer get(ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		return batch(true, getIndex(length), dst, length);
	}

	@Override
	public Buffer get(int index, ByteBuffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		return batch(true, getIndex(index, length), dst, length);
	}

	@Override
	public Buffer get(Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		return batch(true, getIndex(length), dst, length);
	}

	@Override
	public Buffer get(int index, Buffer dst, int length) {
		checkBounds(0, length, dst.remaining());
		return batch(true, getIndex(index, length), dst, length);
	}

	@Override
	public Buffer put(byte[] src, int offset, int length) {
		checkBounds(offset, length, src.length);
		return batch(false, putIndex(length), src, offset, length);
	}

	@Override
	public Buffer put(int index, byte[] src, int offset, int length) {
		checkBounds(offset, length, src.length);
		return batch(false, putIndex(index, length), src, offset, length);
	}

	@Override
	public Buffer put(ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		return batch(false, putIndex(length), src, length);
	}

	@Override
	public Buffer put(int index, ByteBuffer src, int length) {
		checkBounds(0, length, src.remaining());
		return batch(false, putIndex(index, length), src, length);
	}

	@Override
	public Buffer put(Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		return batch(false, putIndex(length), src, length);
	}

	@Override
	public Buffer put(int index, Buffer src, int length) {
		checkBounds(0, length, src.remaining());
		return batch(false, putIndex(index, length), src, length);
	}

	private short decodeShort(int index) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;

		int idx = buffer.position() + index - entry.position;
		if (buffer.limit() - idx >= 2)
			return buffer.getShort(idx);
		return AbstractBufferUtil.decodeShort(this, index);
	}

	private Buffer encodeShort(int index, short s) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;

		int idx = buffer.position() + index - entry.position;
		if (buffer.limit() - idx >= 2)
			buffer.putShort(idx, s);
		else
			AbstractBufferUtil.encodeShort(this, index, s);
		return this;
	}

	@Override
	public short getShort() {
		return decodeShort(getIndex(2));
	}

	@Override
	public short getShort(int index) {
		return decodeShort(getIndex(index, 2));
	}

	@Override
	public Buffer putShort(short s) {
		return encodeShort(putIndex(2), s);
	}

	@Override
	public Buffer putShort(int index, short s) {
		return encodeShort(putIndex(index, 2), s);
	}

	@Override
	public int getInt() {
		return decodeInt(getIndex(4));
	}

	@Override
	public int getInt(int index) {
		return decodeInt(getIndex(index, 4));
	}

	@Override
	public Buffer putInt(int i) {
		return encodeInt(putIndex(4), i);
	}

	@Override
	public Buffer putInt(int index, int i) {
		return encodeInt(putIndex(index, 4), i);
	}

	private long decodeLong(int index) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;

		int idx = buffer.position() + index - entry.position;
		if (buffer.limit() - idx >= 8)
			return buffer.getLong(idx);
		return AbstractBufferUtil.decodeLong(this, index);
	}

	private Buffer encodeLong(int index, long l) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;

		int idx = buffer.position() + index - entry.position;
		if (buffer.limit() - idx >= 8)
			buffer.putLong(idx, l);
		else
			AbstractBufferUtil.encodeLong(this, index, l);
		return this;
	}

	@Override
	public long getLong() {
		return decodeLong(getIndex(8));
	}

	@Override
	public long getLong(int index) {
		return decodeLong(getIndex(index, 8));
	}

	@Override
	public Buffer putLong(long l) {
		return encodeLong(putIndex(8), l);
	}

	@Override
	public Buffer putLong(int index, long l) {
		return encodeLong(putIndex(index, 8), l);
	}

	@Override
	public String getString(Charset charset, int bufferLen) {
		return doGetString(getIndex(bufferLen), charset, bufferLen);
	}

	@Override
	public String getString(int index, Charset charset, int bufferLen) {
		return doGetString(getIndex(index, bufferLen), charset, bufferLen);
	}

	@Override
	protected void doRelease() {
		for (Entry e = header.next; e != header;) {
			e.buffer.release();
			e.buffer = null;
			e.previous = null;
			e = e.next;
			e.previous.next = null;
		}
		header.next = header.previous = header;
	}

	@Override
	protected byte doGet(int index) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;
		return buffer.get(buffer.position() + index - entry.position);
	}

	@Override
	protected void doPut(int index, byte b) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;
		buffer.put(buffer.position() + index - entry.position, b);
	}

	protected static final class Entry {

		public Buffer buffer;
		public int position;

		public Entry next;
		public Entry previous;

	}

	private Entry getEntry(int index) {
		for (Entry e = header.previous; e != header; e = e.previous) {
			if (index >= e.position)
				return e;
		}
		return null;
	}

	protected Buffer batch(boolean get, int index, byte[] array, int offset, int length) {
		Entry entry = getEntry(index);
		int off = index - entry.position;
		do {
			Buffer buffer = entry.buffer;
			int len = Math.min(buffer.remaining() - off, length);

			if (get)
				buffer.get(buffer.position() + off, array, offset, len);
			else
				buffer.put(buffer.position() + off, array, offset, len);

			offset += len;
			length -= len;
			if (length <= 0)
				break;
			off = 0;
		} while ((entry = entry.next) != header);
		return this;
	}

	protected Buffer batch(boolean get, int index, ByteBuffer buffer, int length) {
		Entry entry = getEntry(index);
		int off = index - entry.position;
		do {
			Buffer content = entry.buffer;
			int len = Math.min(content.remaining() - off, length);

			if (get)
				content.get(content.position() + off, buffer, len);
			else {
				content.put(content.position() + off, buffer, len);
			}

			length -= len;
			if (length <= 0)
				break;
			off = 0;
		} while ((entry = entry.next) != header);
		return this;
	}

	protected Buffer batch(boolean get, int index, Buffer buffer, int length) {
		Entry entry = getEntry(index);
		int off = index - entry.position;
		do {
			Buffer content = entry.buffer;
			int len = Math.min(content.remaining() - off, length);

			if (get)
				content.get(content.position() + off, buffer, len);
			else
				content.put(content.position() + off, buffer, len);

			length -= len;
			if (length <= 0)
				break;
			off = 0;
		} while ((entry = entry.next) != header);
		return this;
	}

	private int decodeInt(int index) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;
		int idx = buffer.position() + index - entry.position;
		if (buffer.limit() - idx >= 4)
			return buffer.getInt(idx);
		return AbstractBufferUtil.decodeInt(this, index);
	}

	private Buffer encodeInt(int index, int i) {
		Entry entry = getEntry(index);
		Buffer buffer = entry.buffer;

		int idx = buffer.position() + index - entry.position;
		if (buffer.limit() - idx >= 4)
			buffer.putInt(idx, i);
		else
			AbstractBufferUtil.encodeInt(this, index, i);
		return this;
	}

	private String doGetString(int i, Charset charset, int bufferLen) {
		Buffer buffer = BufferFactory.allocate(bufferLen);
		batch(true, i, buffer, bufferLen);
		buffer.position(0);
		try {
			return charset.decode(buffer.asByteBuffer());
		} finally {
			buffer.release();
		}
	}

}