package com.zusmart.basic.toolkit.support;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zusmart.basic.toolkit.Attachable;

public abstract class AbstractAttachable implements Attachable {

	private Map<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();

	@Override
	public Map<Object, Object> getAllAttribute() {
		return Collections.unmodifiableMap(this.attributes);
	}

	@Override
	public Set<Object> getAllAttributeName() {
		return this.attributes.keySet();
	}

	@Override
	public void clearAllAttribute() {
		this.attributes.clear();
	}

	@Override
	public void setAttribute(Object key, Object value) {
		this.attributes.put(key, value);
	}

	@Override
	public Object getAttribute(Object key) {
		return this.attributes.get(key);
	}

	@Override
	public Object delAttribute(Object key) {
		return this.attributes.remove(key);
	}

	@Override
	public String getStringAttribute(Object key) {
		return (String) this.getAttribute(key);
	}

	@Override
	public Integer getIntegerAttribute(Object key) {
		Object value = this.getAttribute(key);
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		try {
			return Integer.parseInt((String) value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Long getLongAttribute(Object key) {
		Object value = this.getAttribute(key);
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		try {
			return Long.parseLong((String) value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Short getShortAttribute(Object key) {
		Object value = this.getAttribute(key);
		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}
		try {
			return Short.parseShort((String) value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Double getDoubleAttribute(Object key) {
		Object value = this.getAttribute(key);
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		try {
			return Double.parseDouble((String) value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Float getFloatAttribute(Object key) {
		Object value = this.getAttribute(key);
		if (value instanceof Number) {
			return ((Number) value).floatValue();
		}
		try {
			return Float.parseFloat((String) value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Boolean getBooleanAttribute(Object key) {
		Object value = this.getAttribute(key);
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof String) {
			if ("yes".equals(value) || "1".equals(value) || "true".equals(value)) {
				return true;
			}
			if ("no".equals(value) || "0".equals(value) || "false".equals(value)) {
				return false;
			}
		}
		if (value instanceof Number) {
			int v = ((Number) value).intValue();
			if (v == 1) {
				return true;
			} else if (v == 0) {
				return false;
			}
		}
		try {
			return Boolean.parseBoolean((String) value);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public BigDecimal getBigDecimalAttribute(Object key) {
		Object value = this.getAttribute(key);
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof Number) {
			return new BigDecimal(((Number) value).doubleValue());
		}
		try {
			return new BigDecimal((String) value);
		} catch (Exception e) {
			return null;
		}
	}

}