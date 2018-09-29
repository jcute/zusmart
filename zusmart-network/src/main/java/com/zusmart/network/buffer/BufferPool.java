package com.zusmart.network.buffer;

public interface BufferPool {

	public Buffer allocate(int capacity, boolean direct);

}