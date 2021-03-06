package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HighchartResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		// rPath + "/js/prototype-adapter.js?v=3.0.1",
		final String rPath = getResourceHomePath();
		return new String[] { rPath + "/js/highcharts.js?v=9.0.0" };
	}
}
