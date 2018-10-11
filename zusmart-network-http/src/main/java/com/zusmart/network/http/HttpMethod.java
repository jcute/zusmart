package com.zusmart.network.http;

public enum HttpMethod {

	GET("GET"), HEAD("HEAD"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE"), TRACE("TRACE"), CONNECT("CONNECT");

	private String value;

	HttpMethod(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	private static final HttpMethod DEFAULT = HttpMethod.GET;

	public static HttpMethod parse(String value) {
		for (HttpMethod method : HttpMethod.values()) {
			if (method.getValue().equals(value)) {
				return method;
			}
		}
		return DEFAULT;
	}

}