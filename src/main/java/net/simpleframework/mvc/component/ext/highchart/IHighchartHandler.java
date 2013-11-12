package net.simpleframework.mvc.component.ext.highchart;

import java.util.List;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IHighchartHandler extends IComponentHandler {

	/**
	 * 获取图表数据
	 * 
	 * @param cp
	 * @return
	 */
	List<HcSeries> getSeries(ComponentParameter cp);
}
