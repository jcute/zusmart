package com.zusmart.basic.logging.support;

import org.slf4j.helpers.NOPLoggerFactory;

import com.zusmart.basic.logging.Logger;
import com.zusmart.basic.logging.LoggerFactory;

public class Slf4jLoggerFactory extends LoggerFactory {

	public Slf4jLoggerFactory() {
		if (org.slf4j.LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
			throw new NoClassDefFoundError("NOPLoggerFactory not supported");
		}
	}

	@Override
	protected Logger newInstance(String name) {
		return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(name));
	}

}