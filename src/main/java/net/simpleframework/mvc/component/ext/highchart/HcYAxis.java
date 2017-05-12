package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractAxis;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcYAxis extends AbstractAxis<HcYAxis> {
	private static final long serialVersionUID = -6568437002179883596L;
	private Boolean opposite;// 显示对面维度

	public HcYAxis() {
	}

	public Boolean getOpposite() {
		return opposite;
	}

	public HcYAxis setOpposite(final Boolean opposite) {
		this.opposite = opposite;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getOpposite()) != null) {
			kv.add("opposite", val);
		}
		return kv;
	}

}