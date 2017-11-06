package net.simpleframework.mvc.component.ext.login;

import java.util.ArrayList;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.LastUrlListener;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractLoginHandler extends AbstractComponentHandler
		implements ILoginHandler {

	protected String getLastUrl(final ComponentParameter cp) {
		return LastUrlListener.getInstance().getLastUrl(cp);
	}

	@Override
	public String getToolbarHTML(final ComponentParameter cp) {
		final ArrayList<String> al = new ArrayList<>();
		final StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='0' cellspacing='0' style='width: 100%;'><tr>");
		sb.append("<td>");
		if ((Boolean) cp.getBeanProperty("showAutoLogin")) {
			sb.append(new Checkbox("_autoLogin", "#(login.1)"));
		}
		sb.append("</td>");
		sb.append("<td align='right'>");
		final String passwordGetUrl = (String) cp.getBeanProperty("passwordGetUrl");
		if (StringUtils.hasText(passwordGetUrl)) {
			final StringBuilder sb2 = new StringBuilder();
			sb2.append("<a onclick=\"$Actions['getPasswordWindow']();\">#(login.2)</a>");
			al.add(sb2.toString());
		}
		sb.append(StringUtils.join(al, SpanElement.SEP().toString()));
		sb.append("</td>");
		sb.append("</tr></table>");
		return sb.toString();
	}
}
