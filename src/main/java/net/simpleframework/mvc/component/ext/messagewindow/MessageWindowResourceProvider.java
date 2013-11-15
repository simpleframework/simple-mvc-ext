package net.simpleframework.mvc.component.ext.messagewindow;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.component.ui.window.WindowRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MessageWindowResourceProvider extends AbstractComponentResourceProvider {

	public MessageWindowResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getDependentComponents(final PageParameter pp) {
		return new String[] { WindowRegistry.WINDOW };
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/message_window.css" };
	}

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath() + "/js/message_window.js" };
	}
}
