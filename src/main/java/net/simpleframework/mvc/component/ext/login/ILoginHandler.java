package net.simpleframework.mvc.component.ext.login;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ILoginHandler extends IComponentHandler {

	/**
	 * 登录验证
	 * 
	 * @param compParameter
	 * @return
	 */
	IForward login(ComponentParameter cp);

	/**
	 * 
	 * @param cp
	 * @return
	 */
	String getToolbarHTML(ComponentParameter cp);
}
