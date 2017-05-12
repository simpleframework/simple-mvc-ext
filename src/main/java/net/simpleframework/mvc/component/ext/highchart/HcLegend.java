package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.common.element.ELayout;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.EVerticalAlign;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractHcElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcLegend extends AbstractHcElement<HcLegend> {
	private static final long serialVersionUID = -4337307449483525641L;

	private ELayout layout;

	private ETextAlign align;

	private EVerticalAlign verticalAlign;

	private Integer borderWidth;

	private Integer x, y;

	private Boolean enabled;

	private Boolean floating;

	public HcLegend() {
	}

	public ELayout getLayout() {
		return layout;
	}

	public HcLegend setLayout(final ELayout layout) {
		this.layout = layout;
		return this;
	}

	public ETextAlign getAlign() {
		return align;
	}

	public HcLegend setAlign(final ETextAlign align) {
		this.align = align;
		return this;
	}

	public EVerticalAlign getVerticalAlign() {
		return verticalAlign;
	}

	public HcLegend setVerticalAlign(final EVerticalAlign verticalAlign) {
		this.verticalAlign = verticalAlign;
		return this;
	}

	public Integer getBorderWidth() {
		return borderWidth;
	}

	public HcLegend setBorderWidth(final Integer borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}

	public Integer getX() {
		return x;
	}

	public HcLegend setX(final Integer x) {
		this.x = x;
		return this;
	}

	public Integer getY() {
		return y;
	}

	public HcLegend setY(final Integer y) {
		this.y = y;
		return this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public HcLegend setEnabled(final Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public Boolean getFloating() {
		return floating;
	}

	public HcLegend setFloating(final Boolean floating) {
		this.floating = floating;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getLayout()) != null) {
			kv.add("layout", val);
		}
		if ((val = getAlign()) != null) {
			kv.add("align", val);
		}
		if ((val = getVerticalAlign()) != null) {
			kv.add("verticalAlign", val);
		}
		if ((val = getBorderWidth()) != null) {
			kv.add("borderWidth", val);
		}
		if ((val = getX()) != null) {
			kv.add("x", val);
		}
		if ((val = getY()) != null) {
			kv.add("y", val);
		}
		if ((val = getEnabled()) != null) {
			kv.add("enabled", val);
		}
		if ((val = getFloating()) != null) {
			kv.add("floating", val);
		}
		return kv;
	}
}
