package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractHcElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DataObj extends AbstractHcElement<DataObj> implements IDataVal {
	private String name;

	private Boolean sliced, selected;

	private Number x, y;

	private String color;

	private HcDataLabels dataLabels;
	private HcMarker marker;

	public DataObj(final String name, final Number y) {
		this.name = name;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public DataObj setName(final String name) {
		this.name = name;
		return this;
	}

	public Boolean getSliced() {
		return sliced;
	}

	public DataObj setSliced(final Boolean sliced) {
		this.sliced = sliced;
		return this;
	}

	public Boolean getSelected() {
		return selected;
	}

	public DataObj setSelected(final Boolean selected) {
		this.selected = selected;
		return this;
	}

	public Number getX() {
		return x;
	}

	public DataObj setX(final Number x) {
		this.x = x;
		return this;
	}

	public Number getY() {
		return y;
	}

	public DataObj setY(final Number y) {
		this.y = y;
		return this;
	}

	public String getColor() {
		return color;
	}

	public DataObj setColor(final String color) {
		this.color = color;
		return this;
	}

	public HcDataLabels getDataLabels() {
		return dataLabels;
	}

	public DataObj setDataLabels(final HcDataLabels dataLabels) {
		this.dataLabels = dataLabels;
		return this;
	}

	public HcMarker getMarker() {
		return marker;
	}

	public DataObj setMarker(final HcMarker marker) {
		this.marker = marker;
		return this;
	}

	@Override
	public Object toVal() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getName()) != null) {
			kv.add("name", val);
		}
		if ((val = getSliced()) != null) {
			kv.add("sliced", val);
		}
		if ((val = getSelected()) != null) {
			kv.add("selected", val);
		}
		if ((val = getX()) != null) {
			kv.add("x", val);
		}
		if ((val = getY()) != null) {
			kv.add("y", val);
		}
		if ((val = getColor()) != null) {
			kv.add("color", val);
		}
		if ((val = getDataLabels()) != null) {
			kv.add("dataLabels", val);
		}
		if ((val = getMarker()) != null) {
			kv.add("marker", val);
		}
		return kv;
	}
}
