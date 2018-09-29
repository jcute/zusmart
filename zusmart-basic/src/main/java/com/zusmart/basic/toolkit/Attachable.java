package com.zusmart.basic.toolkit;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * 属性附着基础类,子类可为其设置属性
 * 
 * @author koko
 * 
 */
public interface Attachable {

	public Map<Object, Object> getAllAttribute();

	public Set<Object> getAllAttributeName();

	public void clearAllAttribute();

	public void setAttribute(Object key, Object value);

	public Object getAttribute(Object key);

	public Object delAttribute(Object key);

	public String getStringAttribute(Object key);

	public Integer getIntegerAttribute(Object key);

	public Long getLongAttribute(Object key);

	public Short getShortAttribute(Object key);

	public Double getDoubleAttribute(Object key);

	public Float getFloatAttribute(Object key);

	public Boolean getBooleanAttribute(Object key);

	public BigDecimal getBigDecimalAttribute(Object key);

}