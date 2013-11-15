package net.simpleframework.mvc.component.ext.login;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(LoginRegistry.LOGIN)
@ComponentBean(LoginBean.class)
@ComponentRender(LoginRender.class)
@ComponentResourceProvider(LoginResourceProvider.class)
public class LoginRegistry extends AbstractComponentRegistry {
	public static final String LOGIN = "login";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp, final Object attriData) {
		final LoginBean loginBean = (LoginBean) super.createComponentBean(pp, attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, loginBean));
		return loginBean;
	}
}
