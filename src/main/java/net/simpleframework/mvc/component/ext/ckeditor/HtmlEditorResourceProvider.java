package net.simpleframework.mvc.component.ext.ckeditor;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HtmlEditorResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath() + "/ckeditor/ckeditor.js?v=4.9.2" };
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/ckeditor.css" };
	}
}
