package com.zusmart.network.buffer.support;

import com.zusmart.network.buffer.Buffer;

class AbstractBufferUtil {

	static Buffer encodeShort(AbstractBuffer buffer, int index, short s) {
		byte b1, b2;
		if (buffer.isBigEndian()) {
			b1 = (byte) (s >> 8);
			b2 = (byte) (s >> 0);
		} else {
			b1 = (byte) (s >> 0);
			b2 = (byte) (s >> 8);
		}
		buffer.doPut(index + 0, b1);
		buffer.doPut(index + 1, b2);
		return buffer;
	}

	static short decodeShort(AbstractBuffer buffer, int index) {
		byte b1, b2;
		if (buffer.isBigEndian()) {
			b1 = buffer.doGet(index + 0);
			b2 = buffer.doGet(index + 1);
		} else {
			b1 = buffer.doGet(index + 1);
			b2 = buffer.doGet(index + 0);
		}
		return (short) ((b1 << 8) | (b2 & 0xff));
	}

	static Buffer encodeInt(AbstractBuffer buffer, int index, int i) {
		byte b1, b2, b3, b4;
		if (buffer.isBigEndian()) {
			b1 = (byte) (i >> 24);
			b2 = (byte) (i >> 16);
			b3 = (byte) (i >> 8);
			b4 = (byte) (i >> 0);
		} else {
			b1 = (byte) (i >> 0);
			b2 = (byte) (i >> 8);
			b3 = (byte) (i >> 16);
			b4 = (byte) (i >> 24);
		}
		buffer.doPut(index + 0, b1);
		buffer.doPut(index + 1, b2);
		buffer.doPut(index + 2, b3);
		buffer.doPut(index + 3, b4);
		return buffer;
	}

	static int decodeInt(AbstractBuffer buffer, int index) {
		byte b1, b2, b3, b4;
		if (buffer.isBigEndian()) {
			b1 = buffer.doGet(index + 0);
			b2 = buffer.doGet(index + 1);
			b3 = buffer.doGet(index + 2);
			b4 = buffer.doGet(index + 3);
		} else {
			b1 = buffer.doGet(index + 3);
			b2 = buffer.doGet(index + 2);
			b3 = buffer.doGet(index + 1);
			b4 = buffer.doGet(index + 0);
		}
		return (int) ((((b1 & 0xff) << 24) | ((b2 & 0xff) << 16) | ((b3 & 0xff) << 8) | ((b4 & 0xff) << 0)));
	}

	static Buffer encodeLong(AbstractBuffer buffer, int index, long l) {
		byte b1, b2, b3, b4, b5, b6, b7, b8;
		if (buffer.isBigEndian()) {
			b1 = (byte) (l >> 56);
			b2 = (byte) (l >> 48);
			b3 = (byte) (l >> 40);
			b4 = (byte) (l >> 32);
			b5 = (byte) (l >> 24);
			b6 = (byte) (l >> 16);
			b7 = (byte) (l >> 8);
			b8 = (byte) (l >> 0);
		} else {
			b1 = (byte) (l >> 0);
			b2 = (byte) (l >> 8);
			b3 = (byte) (l >> 16);
			b4 = (byte) (l >> 24);
			b5 = (byte) (l >> 32);
			b6 = (byte) (l >> 40);
			b7 = (byte) (l >> 48);
			b8 = (byte) (l >> 56);
		}
		buffer.doPut(index + 0, b1);
		buffer.doPut(index + 1, b2);
		buffer.doPut(index + 2, b3);
		buffer.doPut(index + 3, b4);
		buffer.doPut(index + 4, b5);
		buffer.doPut(index + 5, b6);
		buffer.doPut(index + 6, b7);
		buffer.doPut(index + 7, b8);
		return buffer;
	}

	static long decodeLong(AbstractBuffer buffer, int index) {
		byte b1, b2, b3, b4, b5, b6, b7, b8;
		if (buffer.isBigEndian()) {
			b1 = buffer.doGet(index + 0);
			b2 = buffer.doGet(index + 1);
			b3 = buffer.doGet(index + 2);
			b4 = buffer.doGet(index + 3);
			b5 = buffer.doGet(index + 4);
			b6 = buffer.doGet(index + 5);
			b7 = buffer.doGet(index + 6);
			b8 = buffer.doGet(index + 7);
		} else {
			b1 = buffer.doGet(index + 7);
			b2 = buffer.doGet(index + 6);
			b3 = buffer.doGet(index + 5);
			b4 = buffer.doGet(index + 4);
			b5 = buffer.doGet(index + 3);
			b6 = buffer.doGet(index + 2);
			b7 = buffer.doGet(index + 1);
			b8 = buffer.doGet(index + 0);
		}
		return (((long) b1 & 0xff) << 56) | (((long) b2 & 0xff) << 48) | (((long) b3 & 0xff) << 40) | (((long) b4 & 0xff) << 32) | (((long) b5 & 0xff) << 24) | (((long) b6 & 0xff) << 16)
				| (((long) b7 & 0xff) << 8) | (((long) b8 & 0xff) << 0);
	}

}
