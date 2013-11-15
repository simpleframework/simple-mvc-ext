package net.simpleframework.mvc.component.ext.userselect;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(UserSelectRegistry.USERSELECT)
@ComponentBean(UserSelectBean.class)
@ComponentResourceProvider(UserSelectResourceProvider.class)
public class UserSelectRegistry extends DictionaryRegistry {
	public static final String USERSELECT = "userSelect";

	@Override
	public UserSelectBean createComponentBean(final PageParameter pageParameter,
			final Object attriData) {
		final UserSelectBean userSelect = (UserSelectBean) super.createComponentBean(pageParameter,
				attriData);
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) userSelect.getAttr(ATTRI_AJAXREQUEST);
		if (ajaxRequest != null) {
			ajaxRequest.setUrlForward(ComponentUtils.getResourceHomePath(UserSelectBean.class)
					+ "/jsp/user_select.jsp?" + UserSelectUtils.BEAN_ID + "=" + userSelect.hashId());
		}
		return userSelect;
	}
}
