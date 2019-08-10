package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractHcLabels;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcDataLabels extends AbstractHcLabels<HcDataLabels> {
	private static final long serialVersionUID = 7878889517727070241L;

	private String color;

	private int borderWidth = 1;

	private boolean useHTML;

	public HcDataLabels() {
	}

	public String getColor() {
		return color;
	}

	public HcDataLabels setColor(final String color) {
		this.color = color;
		return this;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public HcDataLabels setBorderWidth(final int borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}

	public boolean isUseHTML() {
		return useHTML;
	}

	public HcDataLabels setUseHTML(final boolean useHTML) {
		this.useHTML = useHTML;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getColor()) != null) {
			kv.add("color", val);
		}
		kv.add("borderWidth", getBorderWidth());
		kv.add("useHTML", isUseHTML());
		return kv;
	}
}
