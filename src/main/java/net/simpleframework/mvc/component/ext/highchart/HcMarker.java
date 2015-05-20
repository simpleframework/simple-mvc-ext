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
public class HcMarker extends AbstractHcElement<HcMarker> {
	private Boolean enabled;

	private Integer height, width;
	private String fillColor;

	private String lineColor;
	private Integer lineWidth;

	private Integer radius;

	public Boolean getEnabled() {
		return enabled;
	}

	public HcMarker setEnabled(final Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public Integer getHeight() {
		return height;
	}

	public HcMarker setHeight(final Integer height) {
		this.height = height;
		return this;
	}

	public Integer getWidth() {
		return width;
	}

	public HcMarker setWidth(final Integer width) {
		this.width = width;
		return this;
	}

	public String getFillColor() {
		return fillColor;
	}

	public HcMarker setFillColor(final String fillColor) {
		this.fillColor = fillColor;
		return this;
	}

	public String getLineColor() {
		return lineColor;
	}

	public HcMarker setLineColor(final String lineColor) {
		this.lineColor = lineColor;
		return this;
	}

	public Integer getLineWidth() {
		return lineWidth;
	}

	public HcMarker setLineWidth(final Integer lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}

	public Integer getRadius() {
		return radius;
	}

	public HcMarker setRadius(final Integer radius) {
		this.radius = radius;
		return this;
	}

	public Object toVal() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getEnabled()) != null) {
			kv.add("enabled", val);
		}
		if ((val = getHeight()) != null) {
			kv.add("height", val);
		}
		if ((val = getWidth()) != null) {
			kv.add("width", val);
		}
		if ((val = getFillColor()) != null) {
			kv.add("fillColor", val);
		}
		if ((val = getLineColor()) != null) {
			kv.add("lineColor", val);
		}
		if ((val = getLineWidth()) != null) {
			kv.add("lineWidth", val);
		}
		if ((val = getRadius()) != null) {
			kv.add("radius", val);
		}
		return kv;
	}
}
