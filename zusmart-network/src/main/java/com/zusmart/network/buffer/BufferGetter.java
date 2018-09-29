package com.zusmart.network.buffer;

import java.nio.ByteBuffer;

import com.zusmart.basic.toolkit.Charset;

public interface BufferGetter {

	public byte get();

	public byte get(int index);

	public Buffer get(byte[] dst);

	public Buffer get(int index, byte[] dst);

	public Buffer get(byte[] dst, int offset, int length);

	public Buffer get(int index, byte[] dst, int offset, int length);

	public Buffer get(ByteBuffer dst);

	public Buffer get(ByteBuffer dst, int length);

	public Buffer get(int index, ByteBuffer dst);

	public Buffer get(int index, ByteBuffer dst, int length);

	public Buffer get(Buffer dst);

	public Buffer get(Buffer dst, int length);

	public Buffer get(int index, Buffer dst);

	public Buffer get(int index, Buffer dst, int length);

	public char getChar();

	public char getChar(int index);

	public short getShort();

	public short getShort(int index);

	public int getInt();

	public int getInt(int index);

	public long getLong();

	public long getLong(int index);

	public float getFloat();

	public float getFloat(int index);

	public double getDouble();

	public double getDouble(int index);

	public short getUnsignedByte();

	public short getUnsignedByte(int index);

	public int getUnsignedShort();

	public int getUnsignedShort(int index);

	public long getUnsignedInt();

	public long getUnsignedInt(int index);

	public String getString(Charset charset, int bufferLength);

	public String getString(int index, Charset charset, int bufferLength);

}