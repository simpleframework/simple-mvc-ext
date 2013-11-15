package net.simpleframework.mvc.component.ext.highchart;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DataKV implements IDataVal {
	private final Object name;

	private final Number val;

	public DataKV(final Object name, final Number val) {
		this.name = name;
		this.val = val;
	}

	@Override
	public Object toVal() {
		return new Object[] { name, val };
	}
}
