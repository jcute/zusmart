package com.zusmart.basic.util;

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

}