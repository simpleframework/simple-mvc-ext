package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HcDataLabels extends AbstractHcLabels<HcDataLabels> {

	private String color;

	public HcDataLabels(final XmlElement beanElement) {
		super(beanElement);
	}

	public HcDataLabels() {
		this(null);
	}

	public String getColor() {
		return color;
	}

	public HcDataLabels setColor(final String color) {
		this.color = color;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getColor()) != null) {
			kv.add("color", val);
		}
		return kv;
	}
}
