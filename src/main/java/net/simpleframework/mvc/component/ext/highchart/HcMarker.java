package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractMarker;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcMarker extends AbstractMarker<HcMarker> {

	private Integer height, width;

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

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getHeight()) != null) {
			kv.add("height", val);
		}
		if ((val = getWidth()) != null) {
			kv.add("width", val);
		}
		return kv;
	}
}
