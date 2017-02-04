package net.simpleframework.mvc.component.ext.syntaxhighlighter;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SHWindowLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		pp.addImportCSS(SHWindowLoaded.class, "/sh_window.css");
	}
}
