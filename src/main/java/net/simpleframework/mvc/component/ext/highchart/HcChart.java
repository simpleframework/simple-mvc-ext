package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.component.ext.highchart.EChart.EHcType;
import net.simpleframework.mvc.component.ext.highchart.EChart.EZoomType;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcChart extends AbstractHcElement<HcChart> {

	private EHcType type;

	private String backgroundColor;

	private Integer marginTop, marginRight, marginBottom, marginLeft;

	private EZoomType zoomType;

	private Integer height;

	public HcChart(final XmlElement beanElement) {
		super(beanElement);
	}

	public HcChart() {
		this(null);
	}

	public EHcType getType() {
		return type;
	}

	public HcChart setType(final EHcType type) {
		this.type = type;
		return this;
	}

	public Integer getMarginTop() {
		return marginTop;
	}

	public HcChart setMarginTop(final Integer marginTop) {
		this.marginTop = marginTop;
		return this;
	}

	public Integer getMarginRight() {
		return marginRight;
	}

	public HcChart setMarginRight(final Integer marginRight) {
		this.marginRight = marginRight;
		return this;
	}

	public Integer getMarginBottom() {
		return marginBottom;
	}

	public HcChart setMarginBottom(final Integer marginBottom) {
		this.marginBottom = marginBottom;
		return this;
	}

	public Integer getMarginLeft() {
		return marginLeft;
	}

	public HcChart setMarginLeft(final Integer marginLeft) {
		this.marginLeft = marginLeft;
		return this;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public HcChart setBackgroundColor(final String backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public EZoomType getZoomType() {
		return zoomType == null ? EZoomType.xy : zoomType;
	}

	public HcChart setZoomType(final EZoomType zoomType) {
		this.zoomType = zoomType;
		return this;
	}

	public Integer getHeight() {
		return height;
	}

	public HcChart setHeight(final Integer height) {
		this.height = height;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap().add("backgroundColor", getBackgroundColor());
		Object val;
		if ((val = getHeight()) != null) {
			kv.add("height", val);
		}
		if ((val = getType()) != null) {
			kv.add("type", val);
		}
		if ((val = getMarginTop()) != null) {
			kv.add("marginTop", val);
		}
		if ((val = getMarginRight()) != null) {
			kv.add("marginRight", val);
		}
		if ((val = getMarginBottom()) != null) {
			kv.add("marginBottom", val);
		}
		if ((val = getMarginLeft()) != null) {
			kv.add("marginLeft", val);
		}
		if ((val = getZoomType()) != null) {
			kv.add("zoomType", val);
		}
		return kv;
	}
}
