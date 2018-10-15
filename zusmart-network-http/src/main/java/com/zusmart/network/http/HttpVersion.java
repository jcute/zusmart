package com.zusmart.network.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zusmart.basic.toolkit.Charset;
import com.zusmart.basic.util.StringUtils;

public class HttpVersion implements Comparable<HttpVersion> {

	private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");

	public static final String HTTP_1_0_STRING = "HTTP/1.0";
	public static final String HTTP_1_1_String = "HTTP/1.1";

	public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false, true);
	public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true, true);

	private final String protocolName;
	private final int majorVersion;
	private final int minorVersion;
	private final String text;
	private final boolean keepAliveDefault;
	private final byte[] bytes;

	public HttpVersion(String text, boolean keepAliveDefault) {
		if (StringUtils.isBlank(text)) {
			throw new IllegalArgumentException("text must not be empty");
		}
		text = text.trim().toUpperCase();
		Matcher matcher = VERSION_PATTERN.matcher(text);
		if (!matcher.matches()) {
			throw new IllegalArgumentException(String.format("invalid version format : %s", text));
		}
		this.protocolName = matcher.group(1);
		this.majorVersion = Integer.parseInt(matcher.group(2));
		this.minorVersion = Integer.parseInt(matcher.group(3));
		this.text = String.format("%s/%s.%s", this.protocolName, this.majorVersion, this.minorVersion);
		this.keepAliveDefault = keepAliveDefault;
		this.bytes = null;
	}

	public HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault, boolean bytes) {
		if (StringUtils.isBlank(protocolName)) {
			throw new IllegalArgumentException("protocol name must not be empty");
		}
		if (majorVersion < 0) {
			throw new IllegalArgumentException("negative majorVersion");
		}
		if (minorVersion < 0) {
			throw new IllegalArgumentException("negative minorVersion");
		}
		this.protocolName = protocolName.toUpperCase();
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.text = String.format("%s/%s.%s", this.protocolName, this.majorVersion, this.minorVersion);
		this.keepAliveDefault = keepAliveDefault;
		if (bytes) {
			this.bytes = text.getBytes(Charset.ASCII.getJavaCharset());
		} else {
			this.bytes = null;
		}
	}

	public String getProtocolName() {
		return protocolName;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public String getText() {
		return text;
	}

	public boolean isKeepAliveDefault() {
		return keepAliveDefault;
	}

	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public int hashCode() {
		return this.getProtocolName().hashCode() * 31 + this.getMajorVersion() * 31 + this.getMinorVersion();
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (obj instanceof HttpVersion) {
			HttpVersion v = (HttpVersion) obj;
			return this.getMinorVersion() == v.getMinorVersion() && this.getMajorVersion() == v.getMajorVersion() && this.getProtocolName().equals(v.getProtocolName());
		}
		return false;
	}

	@Override
	public String toString() {
		return this.getText();
	}

	@Override
	public int compareTo(HttpVersion o) {
		int v = this.getProtocolName().compareTo(o.getProtocolName());
		if (v != 0) {
			return v;
		}
		v = this.getMajorVersion() - o.getMajorVersion();
		if (v != 0) {
			return v;
		}
		return this.getMinorVersion() - o.getMinorVersion();
	}

}