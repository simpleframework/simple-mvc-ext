package net.simpleframework.mvc.component.ext.deptselect;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class DeptSelectUtils {

	public static String getIconPath(final PageParameter pp, final boolean org) {
		final String imgBase = ComponentUtils.getCssResourceHomePath(pp, DeptSelectBean.class)
				+ "/images/";
		return imgBase + (org ? "org.gif" : "dept.png");
	}
}
