package net.simpleframework.mvc.component.ext.messagewindow;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.window.WindowRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(MessageWindowRegistry.MESSAGEWINDOW)
@ComponentBean(MessageWindowBean.class)
@ComponentRender(MessageWindowRender.class)
@ComponentResourceProvider(MessageWindowResourceProvider.class)
public class MessageWindowRegistry extends WindowRegistry {
	public static final String MESSAGEWINDOW = "messageWindow";

	@Override
	public MessageWindowBean createComponentBean(final PageParameter pp, final Object attriData) {
		final MessageWindowBean messageWindowBean = (MessageWindowBean) super.createComponentBean(pp,
				attriData);

		final String beanId = messageWindowBean.hashId();
		if (!StringUtils.hasText(messageWindowBean.getContent())
				&& !StringUtils.hasText(messageWindowBean.getContentRef())) {
			final String ajaxRequestName = "ajaxRequest2_" + beanId;
			messageWindowBean.setContent(ComponentUtils.getLoadingContent());
			messageWindowBean.setContentRef(ajaxRequestName);
			pp.addComponentBean(ajaxRequestName, AjaxRequestBean.class)
					.setShowLoading(false)
					.setUrlForward(
							getComponentResourceProvider().getResourceHomePath()
									+ "/jsp/message_window.jsp?" + MessageWindowUtils.BEAN_ID + "=" + beanId);
		}

		pp.addComponentBean("ajaxRequest_" + beanId, AjaxRequestBean.class)
				.setShowLoading(false)
				.setJsCompleteCallback(
						"$Actions['" + messageWindowBean.getName() + "'].ajaxRequestCallback(json);")
				.setParameters(MessageWindowUtils.BEAN_ID + "=" + beanId)
				.setHandlerClass(MessageAction.class);
		return messageWindowBean;
	}
}
