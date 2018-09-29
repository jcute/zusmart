package com.zusmart.network.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface Buffer extends BufferPutter, BufferGetter {

	public boolean isPermanent();

	public boolean isReleased();

	public boolean isReadOnly();

	public boolean isBigEndian();

	public boolean isDirect();

	public boolean hasRemaining();

	public int capacity();

	public int position();

	public int limit();

	public int remaining();

	public int indexOf(byte[] data);

	public int indexOf(byte data);

	public int write(WritableByteChannel channel) throws IOException;

	public int read(ReadableByteChannel channel) throws IOException;

	public Buffer setBigEndian(boolean bigEndian);

	public Buffer setPermanent(boolean permanent);

	public Buffer position(int position);

	public Buffer limit(int limit);

	public Buffer skip(int size);

	public Buffer mark();

	public Buffer reset();

	public Buffer clear();

	public Buffer flip();

	public Buffer rewind();

	public Buffer compact();

	public Buffer slice();

	public Buffer duplicate();

	public Buffer asReadOnlyBuffer();

	public ByteBuffer asByteBuffer();

	public void release();

}