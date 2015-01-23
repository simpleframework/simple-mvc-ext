package net.simpleframework.mvc.component.ui.pager.db;

import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.IGroupTablePagerHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class GroupDbTablePagerHandler extends AbstractDbTablePagerHandler implements
		IGroupTablePagerHandler {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		final Map<String, Object> params = super.getFormParameters(cp);
		final String g = cp.getParameter(G);
		if (StringUtils.hasText(g) && !"none".equals(g)) {
			params.put(G, g);
		}
		return params;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
		if ("groupColumn".equals(beanProperty)) {
			final String g = cp.getParameter(G);
			if (StringUtils.hasText(g) && !"none".equals(g)) {
				return g;
			}
		}
		return super.getBeanProperty(cp, beanProperty);
	}
}
