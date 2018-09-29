package com.zusmart.network.buffer.support;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zusmart.network.buffer.Buffer;
import com.zusmart.network.buffer.BufferPool;

public class DefaultBufferPool implements BufferPool {

	private static final int MIN_CACHABLE_LENGTH = 512;
	private static final int MAX_CACHABLE_LENGTH = Integer.MAX_VALUE;
	private static final int POSITIVE_INTEGER_SIZE = 31;

	private final BufferSoftReference[] directPools = new BufferSoftReference[POSITIVE_INTEGER_SIZE];
	private final BufferSoftReference[] heapPools = new BufferSoftReference[POSITIVE_INTEGER_SIZE];

	private final Lock[] directLocks = new ReentrantLock[POSITIVE_INTEGER_SIZE];
	private final Lock[] heapLocks = new ReentrantLock[POSITIVE_INTEGER_SIZE];

	private final AtomicInteger getCount = new AtomicInteger();
	private final AtomicInteger hitCount = new AtomicInteger();

	public DefaultBufferPool() {
		for (int i = 0; i < POSITIVE_INTEGER_SIZE; i++) {
			directLocks[i] = new ReentrantLock();
			heapLocks[i] = new ReentrantLock();
		}
	}

	private final int indexFor(int capacity) {
		if (capacity > MAX_CACHABLE_LENGTH || capacity < MIN_CACHABLE_LENGTH)
			return -1;
		for (int i = POSITIVE_INTEGER_SIZE - 1; i > 0; i--) {
			if ((capacity >>> i) > 0)
				return i;
		}
		return 0;
	}

	private final TreeMap<Integer, Entry> getPool(BufferSoftReference reference) {
		return reference == null ? null : (TreeMap<Integer, Entry>) reference.get();
	}

	private final TreeMap<Integer, Entry> createPool(BufferSoftReference[] references, int index) {
		BufferSoftReference reference = references[index];
		TreeMap<Integer, Entry> pool = reference == null ? null : (TreeMap<Integer, Entry>) reference.get();
		if (pool == null) {
			pool = new TreeMap<Integer, Entry>();
			references[index] = new BufferSoftReference(pool);
		}
		return pool;
	}

	public Buffer allocate(int capacity, boolean direct) {
		getCount.incrementAndGet();

		int index = indexFor(capacity);
		if (index >= 0) {
			Integer key = new Integer(capacity);
			Object obj = get(direct, index, key);
			if (obj == null && ++index != POSITIVE_INTEGER_SIZE)
				obj = get(direct, index, key);

			if (obj != null) {
				hitCount.incrementAndGet();
				if (direct) {
					ByteBuffer content = (ByteBuffer) obj;
					content.clear();
					return new DirectBuffer(content, capacity);
				} else {
					return new HeapBuffer((byte[]) obj, capacity);
				}
			}
		}

		try {
			return allocateNew(capacity, direct);
		} catch (OutOfMemoryError e) {
			clear();
			return allocateNew(capacity, direct);
		}
	}

	private Object get(boolean direct, int index, Integer key) {
		Lock lock = direct ? directLocks[index] : heapLocks[index];
		lock.lock();
		try {
			TreeMap<Integer, Entry> pool = getPool(direct ? directPools[index] : heapPools[index]);
			if (pool != null) {
				for (Iterator<Entry> iter = pool.tailMap(key).values().iterator(); iter.hasNext();) {
					Entry first = (Entry) iter.next();
					Entry next = first.next;
					if (next != null) {
						first.next = next.next;
						next.next = null;
						return next.obj;
					} else {
						iter.remove();
						return first.obj;
					}
				}
			}
		} finally {
			lock.unlock();
		}
		return null;
	}

	private Buffer allocateNew(int capacity, boolean direct) {
		return direct ? (Buffer) new DirectBuffer(capacity) : new HeapBuffer(capacity);
	}

	public void clear() {
		for (int i = 0; i < POSITIVE_INTEGER_SIZE; i++) {
			clear(heapLocks[i], heapPools[i]);
			clear(directLocks[i], directPools[i]);
		}
	}

	private void clear(Lock lock, BufferSoftReference reference) {
		lock.lock();
		try {
			TreeMap<Integer, Entry> pool = getPool(reference);
			if (pool != null)
				pool.clear();
		} finally {
			lock.unlock();
		}
	}

	public double getHitRate() {
		return (double) hitCount.get() / getCount.get();
	}

	public String toString() {
		return super.toString() + " [hitRate] " + getHitRate();
	}

	private final class DirectBuffer extends ByteBufferBuffer {

		private ByteBuffer content;

		private DirectBuffer(ByteBuffer content, int capacity) {
			super(content, 0, capacity);
			this.content = content;
		}

		private DirectBuffer(int capacity) {
			this(ByteBuffer.allocateDirect(capacity), capacity);
		}

		@Override
		protected void doRelease() {
			int index = indexFor(content.capacity());
			if (index >= 0) {
				Lock lock = directLocks[index];
				Integer key = new Integer(content.capacity());
				Entry value = new Entry(content);

				lock.lock();
				try {
					value.next = (Entry) createPool(directPools, index).put(key, value);
				} finally {
					lock.unlock();
				}
			}
			content = null;
			super.doRelease();
		}

	}

	private final class HeapBuffer extends ByteArrayBuffer {

		private byte[] content;

		private HeapBuffer(byte[] content, int capacity) {
			super(content, 0, capacity);
			this.content = content;
		}

		private HeapBuffer(int capacity) {
			this(new byte[capacity], capacity);
		}

		@Override
		protected void doRelease() {
			int index = indexFor(content.length);
			if (index >= 0) {
				Lock lock = heapLocks[index];
				Integer key = new Integer(content.length);
				Entry value = new Entry(content);

				lock.lock();
				try {
					value.next = (Entry) createPool(heapPools, index).put(key, value);
				} finally {
					lock.unlock();
				}
			}
			content = null;
			super.doRelease();
		}
	}

	private static class Entry {
		public final Object obj;
		public Entry next;

		public Entry(Object obj) {
			assert obj != null;
			this.obj = obj;
		}
	}

	private static class BufferSoftReference extends SoftReference<TreeMap<Integer, Entry>> {

		public BufferSoftReference(TreeMap<Integer, Entry> referent, ReferenceQueue<? super TreeMap<Integer, Entry>> q) {
			super(referent, q);
		}

		public BufferSoftReference(TreeMap<Integer, Entry> referent) {
			super(referent);
		}

	}

}