package com.zusmart.network.buffer;

import java.nio.ByteBuffer;

import com.zusmart.basic.toolkit.Charset;

public interface BufferPutter {

	public Buffer put(byte b);

	public Buffer put(int index, byte b);

	public Buffer put(ByteBuffer src);

	public Buffer put(ByteBuffer src, int length);

	public Buffer put(int index, ByteBuffer src);

	public Buffer put(int index, ByteBuffer src, int length);

	public Buffer put(Buffer src);

	public Buffer put(Buffer src, int length);

	public Buffer put(int index, Buffer src);

	public Buffer put(int index, Buffer src, int length);

	public Buffer put(byte[] src);

	public Buffer put(byte[] src, int offset, int length);

	public Buffer put(int index, byte[] src);

	public Buffer put(int index, byte[] src, int offset, int length);

	public Buffer putChar(char c);

	public Buffer putChar(int index, char c);

	public Buffer putShort(short s);

	public Buffer putShort(int index, short s);

	public Buffer putInt(int i);

	public Buffer putInt(int index, int i);

	public Buffer putLong(long l);

	public Buffer putLong(int index, long l);

	public Buffer putFloat(float f);

	public Buffer putFloat(int index, float f);

	public Buffer putDouble(double d);

	public Buffer putDouble(int index, double d);

	public Buffer putUnsignedByte(short s);

	public Buffer putUnsignedByte(int index, short s);

	public Buffer putUnsignedShort(int i);

	public Buffer putUnsignedShort(int index, int i);

	public Buffer putUnsignedInt(long l);

	public Buffer putUnsignedInt(int index, long l);

	public Buffer putString(String s, Charset charset);

	public Buffer putString(int index, String s, Charset charset);

}