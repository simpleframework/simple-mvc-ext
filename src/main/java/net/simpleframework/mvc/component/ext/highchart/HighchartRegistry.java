package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(HighchartRegistry.HIGHCHART)
@ComponentBean(HighchartBean.class)
@ComponentRender(HighchartRender.class)
@ComponentResourceProvider(HighchartResourceProvider.class)
public class HighchartRegistry extends AbstractComponentRegistry {

	public static final String HIGHCHART = "highchart";
}
