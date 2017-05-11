package net.simpleframework.mvc.component.ext.highchart;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractHcElement;
import net.simpleframework.mvc.component.ext.highchart.EChart.EHcType;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcSeries extends AbstractHcElement<HcSeries> {
	private static final long serialVersionUID = -5479901271303357562L;

	private String name;

	private Boolean colorByPoint;// 是否每个点颜色不同

	private EHcType type;

	private String color;

	private String[] colors;

	private List<Object> data;

	public HcSeries() {
	}

	public String getName() {
		return name;
	}

	public HcSeries setName(final String name) {
		this.name = name;
		return this;
	}

	public Boolean getColorByPoint() {
		return colorByPoint;
	}

	public HcSeries setColorByPoint(Boolean colorByPoint) {
		this.colorByPoint = colorByPoint;
		return this;
	}

	public EHcType getType() {
		return type;
	}

	public HcSeries setType(final EHcType type) {
		this.type = type;
		return this;
	}

	public String getColor() {
		return color;
	}

	public HcSeries setColor(final String color) {
		this.color = color;
		return this;
	}

	public String[] getColors() {
		return colors;
	}

	public HcSeries setColors(final String[] colors) {
		this.colors = colors;
		return this;
	}

	public List<Object> getData() {
		if (data == null) {
			data = new ArrayList<Object>();
		}
		return data;
	}

	public HcSeries addData(final Object... data) {
		final List<Object> list = getData();
		if (data != null) {
			for (final Object o : data) {
				if (o instanceof IDataVal) {
					list.add(((IDataVal) o).toVal());
				} else {
					list.add(o);
				}
			}
		}
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap().add("data", getData());
		Object val;
		if ((val = getName()) != null) {
			kv.add("name", val);
		}
		if ((val = getColorByPoint()) != null) {
			kv.add("colorByPoint", val);
		}
		if ((val = getType()) != null) {
			kv.add("type", val);
		}
		if ((val = getColor()) != null) {
			kv.add("color", val);
		}
		if ((val = getColors()) != null) {
			kv.add("colors", val);
		}
		return kv;
	}
}
