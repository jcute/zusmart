package com.zusmart.network.http;

import java.util.HashMap;
import java.util.Map;

import com.zusmart.basic.util.StringUtils;

public class HttpMethod implements Comparable<HttpMethod> {

	public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
	public static final HttpMethod GET = new HttpMethod("GET");
	public static final HttpMethod PUT = new HttpMethod("PUT");
	public static final HttpMethod POST = new HttpMethod("POST");
	public static final HttpMethod HEAD = new HttpMethod("HEAD");
	public static final HttpMethod PATCH = new HttpMethod("PATCH");
	public static final HttpMethod DELETE = new HttpMethod("DELETE");
	public static final HttpMethod TRACE = new HttpMethod("TRACE");
	public static final HttpMethod CONNECT = new HttpMethod("CONNECT");

	public static final Map<String, HttpMethod> MAPPING = new HashMap<String, HttpMethod>();

	static {
		MAPPING.put(OPTIONS.getName(), OPTIONS);
		MAPPING.put(GET.getName(), GET);
		MAPPING.put(PUT.getName(), PUT);
		MAPPING.put(POST.getName(), POST);
		MAPPING.put(HEAD.getName(), HEAD);
		MAPPING.put(PATCH.getName(), PATCH);
		MAPPING.put(DELETE.getName(), DELETE);
		MAPPING.put(TRACE.getName(), TRACE);
		MAPPING.put(CONNECT.getName(), CONNECT);
	}

	public static HttpMethod valueOf(String name) {
		return MAPPING.get(name);
	}

	private final String name;

	public HttpMethod(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Http method name must not be null");
		}
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int compareTo(HttpMethod o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (obj instanceof HttpMethod) {
			return obj.toString().equals(this.toString());
		}
		return false;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}