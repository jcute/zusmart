package com.zusmart.basic.logging.support;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;

public class Log4jLoggerFactory extends LoggerFactory {

	@Override
	protected Logger newInstance(String name) {
		return new Log4jLogger(org.apache.log4j.Logger.getLogger(name));
	}

}