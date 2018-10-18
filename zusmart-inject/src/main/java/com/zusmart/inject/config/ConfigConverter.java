package com.zusmart.inject.config;

import com.zusmart.inject.resource.Resource;

public interface ConfigConverter<R> {

	public R convert(Resource resource);

}