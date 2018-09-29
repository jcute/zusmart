package com.zusmart.basic.toolkit;

import java.util.concurrent.atomic.AtomicInteger;

public final class FixedAtomicInteger {

	private AtomicInteger counter;
	private int maxValue;
	private int minValue;

	public FixedAtomicInteger(int min, int max) {
		this.minValue = min;
		this.maxValue = max;
		this.counter = new AtomicInteger(this.minValue);
	}

	public FixedAtomicInteger(int max) {
		this(0, max);
	}

	public FixedAtomicInteger() {
		this(0, Integer.MAX_VALUE);
	}

	public final int getAndIncrement() {
		for (;;) {
			int current = this.counter.get();
			int next;
			if (current == this.maxValue) {
				next = this.minValue;
			} else {
				next = current + 1;
			}
			if (this.counter.compareAndSet(current, next)) {
				return current;
			}
		}
	}

	public final int getAndDecrement() {
		for (;;) {
			int current = this.counter.get();
			int next;
			if (current == this.minValue) {
				next = this.maxValue;
			} else {
				next = current - 1;
			}
			if (this.counter.compareAndSet(current, next)) {
				return current;
			}
		}
	}

	public final int incrementAndGet() {
		for (;;) {
			int current = this.counter.get();
			int next;
			if (current == this.maxValue) {
				next = this.minValue;
			} else {
				next = current + 1;
			}
			if (this.counter.compareAndSet(current, next)) {
				return next;
			}
		}
	}

	public final int decrementAndGet() {
		for (;;) {
			int current = this.counter.get();
			int next;
			if (current == this.minValue) {
				next = this.maxValue;
			} else {
				next = current - 1;
			}
			if (this.counter.compareAndSet(current, next)) {
				return next;
			}
		}
	}

	public boolean compareAndSet(int expect, int update) {
		return this.counter.compareAndSet(expect, update);
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	public int getMinValue() {
		return this.minValue;
	}

}