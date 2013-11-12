package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HcXAxis extends AbstractAxis<HcXAxis> {

	public HcXAxis(final XmlElement beanElement) {
		super(beanElement);
	}

	public HcXAxis() {
		this(null);
	}
}
