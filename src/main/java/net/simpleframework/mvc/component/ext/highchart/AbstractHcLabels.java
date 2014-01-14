package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("unchecked")
public class AbstractHcLabels<T extends AbstractHcLabels<T>> extends AbstractHcElement<T> {
	private Boolean enabled;

	private Integer x, y;

	private String format, formatter;

	private Integer rotation;

	public Boolean getEnabled() {
		return enabled;
	}

	public T setEnabled(final Boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	public Integer getX() {
		return x;
	}

	public T setX(final Integer x) {
		this.x = x;
		return (T) this;
	}

	public Integer getY() {
		return y;
	}

	public T setY(final Integer y) {
		this.y = y;
		return (T) this;
	}

	public Integer getRotation() {
		return rotation;
	}

	public T setRotation(final Integer rotation) {
		this.rotation = rotation;
		return (T) this;
	}

	/**
	 * this.percentage Stacked series and pies only. The point's percentage of
	 * the total.
	 * 
	 * this.point The point object. The point name, if defined, is available
	 * through this.point.name.
	 * 
	 * this.series: The series object. The series name is available through
	 * this.series.name.
	 * 
	 * this.total Stacked series only. The total value at this point's x value.
	 * 
	 * this.x: The x value.
	 * 
	 * this.y: The y value.
	 * 
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	public T setFormat(final String format) {
		this.format = format;
		return (T) this;
	}

	public String getFormatter() {
		return formatter;
	}

	public T setFormatter(final String formatter) {
		this.formatter = formatter;
		return (T) this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getEnabled()) != null) {
			kv.add("enabled", val);
		}
		if ((val = getFormat()) != null) {
			kv.add("format", val);
		}
		if ((val = getFormatter()) != null) {
			kv.add("formatter", val);
		}
		if ((val = getX()) != null) {
			kv.add("x", val);
		}
		if ((val = getY()) != null) {
			kv.add("y", val);
		}
		if ((val = getRotation()) != null) {
			kv.add("rotation", val);
		}
		return kv;
	}
}
