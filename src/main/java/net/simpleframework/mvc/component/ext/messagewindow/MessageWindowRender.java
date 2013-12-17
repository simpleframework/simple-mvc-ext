package net.simpleframework.mvc.component.ext.messagewindow;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ui.window.WindowRender;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MessageWindowRender extends WindowRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getJavascriptCode(cp));
		sb.append("__message_window_actions_init(").append(ComponentRenderUtils.actionFunc(cp))
				.append(", '").append(cp.getComponentName()).append("', '").append("ajaxRequest_")
				.append(cp.hashId()).append("', ").append(cp.getBeanProperty("frequency")).append(", ")
				.append(cp.getBeanProperty("closeDelay")).append(");");
		return sb.toString();
	}
}
