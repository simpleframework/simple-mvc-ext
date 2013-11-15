package net.simpleframework.mvc.component.ext.category;

import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CategoryRender extends ComponentHtmlRenderEx {

	public CategoryRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/category.jsp?").append(CategoryUtils.BEAN_ID);
		sb.append("=").append(cp.hashId());
		return sb.toString();
	}
}
