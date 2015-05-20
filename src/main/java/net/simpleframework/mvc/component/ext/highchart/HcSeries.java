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

	private String name;

	private EHcType type;

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

	public EHcType getType() {
		return type;
	}

	public HcSeries setType(final EHcType type) {
		this.type = type;
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
		if ((val = getType()) != null) {
			kv.add("type", val);
		}
		return kv;
	}
}
