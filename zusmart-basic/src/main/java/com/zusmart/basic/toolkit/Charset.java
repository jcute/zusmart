package com.zusmart.basic.toolkit;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Collection;

public final class Charset {

	public static final Charset SYST = new Charset(System.getProperty("file.encoding"));
	public static final Charset UTF8 = new Charset("UTF-8");

	private final java.nio.charset.Charset charset;
	private final ThreadLocal<SoftReference<CharsetEncoder>> encoderCache;
	private final ThreadLocal<SoftReference<CharsetDecoder>> decoderCache;

	public Charset(String charsetName) {
		this.charset = java.nio.charset.Charset.forName(charsetName);
		this.encoderCache = new ThreadLocal<SoftReference<CharsetEncoder>>();
		this.decoderCache = new ThreadLocal<SoftReference<CharsetDecoder>>();
	}

	public String getCharsetName() {
		return this.charset.name();
	}

	public java.nio.charset.Charset getJavaCharset() {
		return this.charset;
	}

	public byte[] encodeToArray(CharSequence sequence) {
		ByteBuffer buffer = this.encode(sequence);
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
		return result;
	}

	public ByteBuffer encode(CharSequence sequence) {
		CharBuffer buffer = CharBuffer.wrap(sequence);
		CharsetEncoder encoder = this.getEncoder();
		int number = 0;
		if (buffer.remaining() > 0) {
			number = (int) (buffer.remaining() * encoder.averageBytesPerChar());
			if (number == 0) {
				number = (int) (buffer.remaining() * encoder.maxBytesPerChar());
			}
		}
		ByteBuffer result = ByteBuffer.allocate(number);
		if (number == 0) {
			return result;
		}
		encoder.reset();
		while (true) {
			CoderResult cr = buffer.hasArray() ? encoder.encode(buffer, result, true) : encoder.flush(result);
			if (cr.isUnderflow()) {
				break;
			} else if (cr.isOverflow()) {
				number *= 2;
				result.flip();
				result = ByteBuffer.allocate(number).put(result);
				continue;
			}
		}
		result.flip();
		return result;
	}

	public ByteBuffer[] encode(CharSequence sequence, int capacity) {
		CharBuffer buffer = CharBuffer.wrap(sequence);
		CharsetEncoder encoder = this.getEncoder();
		encoder.reset();
		Collection<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
		while (true) {
			ByteBuffer out = ByteBuffer.allocate(capacity);
			CoderResult cr = encoder.encode(buffer, out, true);
			if (cr.isUnderflow()) {
				encoder.flush(out);
				out.flip();
				buffers.add(out);
				break;
			}
			if (cr.isOverflow()) {
				if (out.position() == 0) {
					throw new IllegalArgumentException("buffer capacity too small");
				}
				out.flip();
				buffers.add(out);
				continue;
			}
		}
		return buffers.toArray(new ByteBuffer[0]);
	}

	public String decode(byte[] data) {
		return this.decode(ByteBuffer.wrap(data));
	}
	
	public String decode(ByteBuffer buffer) {
		CharsetDecoder decoder = this.getDecoder();
		int number = 0;
		if (buffer.remaining() > 0) {
			number = (int) (buffer.remaining() * decoder.averageCharsPerByte());
			if (number == 0) {
				number = (int) (buffer.remaining() * decoder.maxCharsPerByte());
			}
		}
		if (number == 0) {
			return "";
		}
		CharBuffer result = CharBuffer.allocate(number);
		decoder.reset();
		while (true) {
			CoderResult cr = buffer.hasRemaining() ? decoder.decode(buffer, result, true) : decoder.flush(result);
			if (cr.isUnderflow()) {
				break;
			} else if (cr.isOverflow()) {
				number *= 2;
				result.flip();
				result = CharBuffer.allocate(number).put(result);
				continue;
			}
		}
		result.flip();
		return result.toString();
	}

	private CharsetEncoder getEncoder() {
		CharsetEncoder encoder = getReference(this.encoderCache);
		if (null == encoder) {
			encoder = this.charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
			setReference(encoderCache, encoder);
		}
		return encoder;
	}

	private CharsetDecoder getDecoder() {
		CharsetDecoder decoder = getReference(decoderCache);
		if (null == decoder) {
			decoder = this.charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
			setReference(decoderCache, decoder);
		}
		return decoder;
	}

	private static <T> T getReference(ThreadLocal<SoftReference<T>> threadLocal) {
		SoftReference<T> softReference = threadLocal.get();
		if (null != softReference) {
			return softReference.get();
		}
		return null;
	}

	private static <T> void setReference(ThreadLocal<SoftReference<T>> threadLocal, T object) {
		threadLocal.set(new SoftReference<T>(object));
	}

	private static ThreadLocal<SoftReference<Charset>> charsetCache = new ThreadLocal<SoftReference<Charset>>();

	private static Charset getCharset(String charsetName) {
		Charset charset = getReference(charsetCache);
		if (null == charset || !(charset.getCharsetName().equalsIgnoreCase(charsetName))) {
			charset = new Charset(charsetName);
			setReference(charsetCache, charset);
		}
		return charset;
	}

	public static ByteBuffer encode(String charsetName, CharSequence sequence) {
		return getCharset(charsetName).encode(sequence);
	}

	public static String decode(String charsetName, ByteBuffer buffer) {
		return getCharset(charsetName).decode(buffer);
	}

}