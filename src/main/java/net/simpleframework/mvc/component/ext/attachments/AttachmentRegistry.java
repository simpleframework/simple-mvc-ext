package net.simpleframework.mvc.component.ext.attachments;

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
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(AttachmentRegistry.ATTACHMENT)
@ComponentBean(AttachmentBean.class)
@ComponentRender(AttachmentRender.class)
@ComponentResourceProvider(AttachmentResourceProvider.class)
public class AttachmentRegistry extends AbstractComponentRegistry {

	public static final String ATTACHMENT = "attachment";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp, final Object attriData) {
		final AttachmentBean attachmentBean = (AttachmentBean) super.createComponentBean(pp,
				attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, attachmentBean));

		return attachmentBean;
	}
}
