package net.simpleframework.mvc.component.ext.login;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LoginAction extends DefaultAjaxRequestHandler {

	public IForward login(final ComponentParameter cp) {
		final ComponentParameter nCP = LoginUtils.get(cp);
		return ((ILoginHandler) nCP.getComponentHandler()).login(nCP);
	}
}
