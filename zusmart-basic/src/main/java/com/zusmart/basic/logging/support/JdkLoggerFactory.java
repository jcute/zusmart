package com.zusmart.basic.logging.support;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;

public class JdkLoggerFactory extends LoggerFactory {

	@Override
	protected Logger newInstance(String name) {
		return new JdkLogger(java.util.logging.Logger.getLogger(name));
	}

}