package net.simpleframework.mvc.component.ext.category;

import java.util.Map;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CategoryEditLoaded extends DefaultPageHandler {
	@Override
	public void onPageLoad(final PageParameter pp, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final ComponentParameter cp = CategoryUtils.get(pp);
		((ICategoryHandler) cp.getComponentHandler())
				.categoryEdit_onLoaded(cp, dataBinding, selector);
	}

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		final ComponentParameter cp = CategoryUtils.get(pp);
		((ICategoryHandler) cp.getComponentHandler()).categoryEdit_doInit(cp);
	}
}
