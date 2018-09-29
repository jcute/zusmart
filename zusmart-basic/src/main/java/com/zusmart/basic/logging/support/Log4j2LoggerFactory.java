package com.zusmart.basic.logging.support;

import org.apache.logging.log4j.LogManager;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;

public class Log4j2LoggerFactory extends LoggerFactory {

	@Override
	protected Logger newInstance(String name) {
		return new Log4j2Logger(LogManager.getLogger(name));
	}

}