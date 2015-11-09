package net.simpleframework.mvc.component.ext.attachments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AttachmentUtils {

	public static final String BEAN_ID = "attachment_@bid";

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static void doSave(final ComponentParameter cp, final IAttachmentSaveCallback callback)
			throws Exception {
		((IAttachmentHandler) cp.getComponentHandler()).doSave(cp, callback);
	}

	public static String toAttachmentHTML(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final ComponentParameter cp = AttachmentUtils.get(request, response);
		final String beanId = cp.hashId();
		final IAttachmentHandler aHandle = (IAttachmentHandler) cp.getComponentHandler();
		final StringBuilder sb = new StringBuilder();
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		sb.append("<div class='Comp_Attachment");
		if (readonly) {
			sb.append(" readonly");
		}
		sb.append("'>");
		if (readonly) {
			sb.append(ComponentRenderUtils.genParameters(cp));
		} else {
			sb.append("<div id=\"attachment_").append(beanId).append("\"></div>");
		}
		sb.append("<div id='attachment_list_").append(beanId).append("'>");
		sb.append(aHandle.toAttachmentListHTML(cp));
		sb.append("</div>");
		sb.append(aHandle.toBottomHTML(cp));
		sb.append("</div>");
		return sb.toString();
	}
}
