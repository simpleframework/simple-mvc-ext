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
public class DataObj implements IDataVal {
	private String name;

	private Boolean sliced, selected;

	private Number x, y;

	private String color;

	private KVMap attris;

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

	public DataObj addAttribute(final String key, final Object val) {
		if (attris == null)
			attris = new KVMap();
		attris.add(key, val);
		return this;
	}

	@Override
	public Object toVal() {
		final KVMap kv = new KVMap();
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
		if (attris != null && attris.size() > 0) {
			kv.putAll(attris);
		}
		return kv;
	}
}
