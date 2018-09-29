package com.zusmart.network.buffer.support;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.zusmart.basic.toolkit.Charset;
import com.zusmart.network.buffer.Buffer;

public class DelegateBuffer implements Buffer {

	protected final Buffer delegate;

	public DelegateBuffer(Buffer delegate) {
		this.delegate = delegate;
	}

	public Buffer getDelegate() {
		return this.delegate;
	}

	@Override
	public ByteBuffer asByteBuffer() {
		return this.delegate.asByteBuffer();
	}

	@Override
	public Buffer asReadOnlyBuffer() {
		return this.delegate.asReadOnlyBuffer();
	}

	@Override
	public int capacity() {
		return this.delegate.capacity();
	}

	@Override
	public Buffer clear() {
		return this.delegate.clear();
	}

	@Override
	public Buffer compact() {
		return this.delegate.compact();
	}

	@Override
	public Buffer duplicate() {
		return this.delegate.duplicate();
	}

	@Override
	public Buffer flip() {
		return this.delegate.flip();
	}

	@Override
	public byte get() {
		return this.delegate.get();
	}

	@Override
	public Buffer get(Buffer dst, int length) {
		return this.delegate.get(dst, length);
	}

	@Override
	public Buffer get(Buffer dst) {
		return this.delegate.get(dst);
	}

	@Override
	public Buffer get(byte[] dst, int offset, int length) {
		return this.delegate.get(dst, offset, length);
	}

	@Override
	public Buffer get(byte[] dst) {
		return this.delegate.get(dst);
	}

	@Override
	public Buffer get(ByteBuffer dst, int length) {
		return this.delegate.get(dst, length);
	}

	@Override
	public Buffer get(ByteBuffer dst) {
		return this.delegate.get(dst);
	}

	@Override
	public Buffer get(int index, Buffer dst, int length) {
		return this.delegate.get(index, dst, length);
	}

	@Override
	public Buffer get(int index, Buffer dst) {
		return this.delegate.get(index, dst);
	}

	@Override
	public Buffer get(int index, byte[] dst, int offset, int length) {
		return this.delegate.get(index, dst, offset, length);
	}

	@Override
	public Buffer get(int index, byte[] dst) {
		return this.delegate.get(index, dst);
	}

	@Override
	public Buffer get(int index, ByteBuffer dst, int length) {
		return this.delegate.get(index, dst, length);
	}

	@Override
	public Buffer get(int index, ByteBuffer dst) {
		return this.delegate.get(index, dst);
	}

	@Override
	public byte get(int index) {
		return this.delegate.get(index);
	}

	@Override
	public char getChar() {
		return this.delegate.getChar();
	}

	@Override
	public char getChar(int index) {
		return this.delegate.getChar(index);
	}

	@Override
	public double getDouble() {
		return this.delegate.getDouble();
	}

	@Override
	public double getDouble(int index) {
		return this.delegate.getDouble(index);
	}

	@Override
	public float getFloat() {
		return this.delegate.getFloat();
	}

	@Override
	public float getFloat(int index) {
		return this.delegate.getFloat(index);
	}

	@Override
	public int getInt() {
		return this.delegate.getInt();
	}

	@Override
	public int getInt(int index) {
		return this.delegate.getInt(index);
	}

	@Override
	public long getLong() {
		return this.delegate.getLong();
	}

	@Override
	public long getLong(int index) {
		return this.delegate.getLong(index);
	}

	@Override
	public short getShort() {
		return this.delegate.getShort();
	}

	@Override
	public short getShort(int index) {
		return this.delegate.getShort(index);
	}

	@Override
	public String getString(Charset charset, int bufferLen) {
		return this.delegate.getString(charset, bufferLen);
	}

	@Override
	public String getString(int index, Charset charset, int bufferLen) {
		return this.delegate.getString(index, charset, bufferLen);
	}

	@Override
	public short getUnsignedByte() {
		return this.delegate.getUnsignedByte();
	}

	@Override
	public short getUnsignedByte(int index) {
		return this.delegate.getUnsignedByte(index);
	}

	@Override
	public long getUnsignedInt() {
		return this.delegate.getUnsignedInt();
	}

	@Override
	public long getUnsignedInt(int index) {
		return this.delegate.getUnsignedInt(index);
	}

	@Override
	public int getUnsignedShort() {
		return this.delegate.getUnsignedShort();
	}

	@Override
	public int getUnsignedShort(int index) {
		return this.delegate.getUnsignedShort(index);
	}

	@Override
	public boolean hasRemaining() {
		return this.delegate.hasRemaining();
	}

	@Override
	public int indexOf(byte data) {
		return this.indexOf(new byte[] { data });
	}

	@Override
	public int indexOf(byte[] b) {
		return this.delegate.indexOf(b);
	}

	@Override
	public boolean isBigEndian() {
		return this.delegate.isBigEndian();
	}

	@Override
	public boolean isDirect() {
		return this.delegate.isDirect();
	}

	@Override
	public boolean isPermanent() {
		return this.delegate.isPermanent();
	}

	@Override
	public boolean isReadOnly() {
		return this.delegate.isReadOnly();
	}

	@Override
	public boolean isReleased() {
		return this.delegate.isReleased();
	}

	@Override
	public int limit() {
		return this.delegate.limit();
	}

	@Override
	public Buffer limit(int limit) {
		return this.delegate.limit(limit);
	}

	@Override
	public Buffer mark() {
		return this.delegate.mark();
	}

	@Override
	public int position() {
		return this.delegate.position();
	}

	@Override
	public Buffer position(int position) {
		return this.delegate.position(position);
	}

	@Override
	public Buffer put(Buffer src, int length) {
		return this.delegate.put(src, length);
	}

	@Override
	public Buffer put(Buffer src) {
		return this.delegate.put(src);
	}

	@Override
	public Buffer put(byte b) {
		return this.delegate.put(b);
	}

	@Override
	public Buffer put(byte[] src, int offset, int length) {
		return this.delegate.put(src, offset, length);
	}

	@Override
	public Buffer put(byte[] src) {
		return this.delegate.put(src);
	}

	@Override
	public Buffer put(ByteBuffer src, int length) {
		return this.delegate.put(src, length);
	}

	@Override
	public Buffer put(ByteBuffer src) {
		return this.delegate.put(src);
	}

	@Override
	public Buffer put(int index, Buffer src, int length) {
		return this.delegate.put(index, src, length);
	}

	@Override
	public Buffer put(int index, Buffer src) {
		return this.delegate.put(index, src);
	}

	@Override
	public Buffer put(int index, byte b) {
		return this.delegate.put(index, b);
	}

	@Override
	public Buffer put(int index, byte[] src, int offset, int length) {
		return this.delegate.put(index, src, offset, length);
	}

	@Override
	public Buffer put(int index, byte[] src) {
		return this.delegate.put(index, src);
	}

	@Override
	public Buffer put(int index, ByteBuffer src, int length) {
		return this.delegate.put(index, src, length);
	}

	@Override
	public Buffer put(int index, ByteBuffer src) {
		return this.delegate.put(index, src);
	}

	@Override
	public Buffer putChar(char c) {
		return this.delegate.putChar(c);
	}

	@Override
	public Buffer putChar(int index, char c) {
		return this.delegate.putChar(index, c);
	}

	@Override
	public Buffer putDouble(double d) {
		return this.delegate.putDouble(d);
	}

	@Override
	public Buffer putDouble(int index, double d) {
		return this.delegate.putDouble(index, d);
	}

	@Override
	public Buffer putFloat(float f) {
		return this.delegate.putFloat(f);
	}

	@Override
	public Buffer putFloat(int index, float f) {
		return this.delegate.putFloat(index, f);
	}

	@Override
	public Buffer putInt(int index, int i) {
		return this.delegate.putInt(index, i);
	}

	@Override
	public Buffer putInt(int i) {
		return this.delegate.putInt(i);
	}

	@Override
	public Buffer putLong(int index, long l) {
		return this.delegate.putLong(index, l);
	}

	@Override
	public Buffer putLong(long l) {
		return this.delegate.putLong(l);
	}

	@Override
	public Buffer putShort(int index, short s) {
		return this.delegate.putShort(index, s);
	}

	@Override
	public Buffer putShort(short s) {
		return this.delegate.putShort(s);
	}

	@Override
	public Buffer putString(int index, String s, Charset charset) {
		return this.delegate.putString(index, s, charset);
	}

	@Override
	public Buffer putString(String s, Charset charset) {
		return this.delegate.putString(s, charset);
	}

	@Override
	public Buffer putUnsignedByte(int index, short s) {
		return this.delegate.putUnsignedByte(index, s);
	}

	@Override
	public Buffer putUnsignedByte(short s) {
		return this.delegate.putUnsignedByte(s);
	}

	@Override
	public Buffer putUnsignedInt(int index, long l) {
		return this.delegate.putUnsignedInt(index, l);
	}

	@Override
	public Buffer putUnsignedInt(long l) {
		return this.delegate.putUnsignedInt(l);
	}

	@Override
	public Buffer putUnsignedShort(int index, int i) {
		return this.delegate.putUnsignedShort(index, i);
	}

	@Override
	public Buffer putUnsignedShort(int i) {
		return this.delegate.putUnsignedShort(i);
	}

	@Override
	public int read(ReadableByteChannel channel) throws IOException {
		return this.delegate.read(channel);
	}

	@Override
	public void release() {
		this.delegate.release();
	}

	@Override
	public int remaining() {
		return this.delegate.remaining();
	}

	@Override
	public Buffer reset() {
		return this.delegate.reset();
	}

	@Override
	public Buffer rewind() {
		return this.delegate.rewind();
	}

	@Override
	public Buffer setBigEndian(boolean b) {
		return this.delegate.setBigEndian(b);
	}

	@Override
	public Buffer setPermanent(boolean b) {
		return this.delegate.setPermanent(b);
	}

	@Override
	public Buffer skip(int size) {
		return this.delegate.skip(size);
	}

	@Override
	public Buffer slice() {
		return this.delegate.slice();
	}

	@Override
	public int write(WritableByteChannel channel) throws IOException {
		return this.delegate.write(channel);
	}

}