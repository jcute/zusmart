package com.zusmart.basic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public abstract class StringUtils {

	private static final String NULL_OBJECT_NAME = "null_object";
	private static final char PACKAGE_SEPARATOR_CHAR = '.';

	public static String getSimpleClassName(Class<?> classType) {
		if (null == classType) {
			throw new NullPointerException("Class type must not be null");
		}
		String className = classType.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
		if (lastDotIndex > -1) {
			return className.substring(lastDotIndex + 1);
		}
		return className;
	}

	public static String getSimpleClassName(Object object) {
		if (null == object) {
			return NULL_OBJECT_NAME;
		} else {
			return getSimpleClassName(object.getClass());
		}
	}

	public static boolean isBlank(CharSequence charSequence) {
		int length;
		if (null == charSequence || (length = charSequence.length()) == 0) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(charSequence.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(CharSequence charSequence) {
		return !isBlank(charSequence);
	}

	public static String getExceptionMessage(Throwable cause) {
		if (isBlank(cause.getMessage())) {
			return cause.toString();
		} else {
			return cause.getMessage();
		}
	}

	/**
	 * 集合类转String数组,null或empty返回0长度数组
	 * 
	 * @param collection
	 * @return 返回拆分后的字符串数组,空为0长度数组
	 */
	public static String[] toArray(final Collection<String> collection) {
		if (null == collection || collection.size() == 0) {
			return new String[0];
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * 指定分隔符打断字符串为数组
	 * 
	 * @param value
	 * @param delimiters
	 * @param trimToken
	 * @param ignoreEmptyToken
	 * @return 返回拆分后的字符串数组,空为0长度数组
	 */
	public static String[] tokenToArray(final String value, final String delimiters, final boolean trimToken, final boolean ignoreEmptyToken) {
		if (null == value) {
			return new String[0];
		}
		StringTokenizer stringTokenizer = new StringTokenizer(value, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (trimToken) {
				token = token.trim();
			}
			if (!ignoreEmptyToken || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toArray(tokens);
	}

}