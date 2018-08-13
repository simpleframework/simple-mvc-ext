package net.simpleframework.mvc.component.ext.attachments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
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
		sb.append("<div class='attach-list' id='attachment_list_").append(beanId).append("'>");
		sb.append(aHandle.toAttachmentListHTML(cp));
		sb.append("</div>");
		sb.append(aHandle.toBottomHTML(cp));
		sb.append("</div>");
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	public static String toAttachFormHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<table class='form_tbl'><tr>");
		sb.append(" <td class='l'>#(attachment_edit.0)</td>");
		sb.append(" <td class='v'>").append(InputElement.hidden("attach_id"))
				.append(new InputElement("attach_topic")).append("</td>");
		sb.append("</tr></table>");

		final IAttachmentHandler aHandle = (IAttachmentHandler) cp.getComponentHandler();
		final Enum[] arr = aHandle.getAttachTypes();
		int size;
		if (arr != null && (size = arr.length) > 0) {
			final InputElement select = InputElement.select("attach_type");
			for (int i = 0; i < size; i++) {
				final Enum<?> e = arr[i];
				if (e != null) {
					select.addElements(new Option(e.ordinal(), e.toString()));
				}
			}
			sb.append("<table class='form_tbl'><tr>");
			sb.append(" <td class='l'>#(attachment_edit.2)</td>");
			sb.append(" <td class='v'>").append(select).append("</td>");
			sb.append("</tr></table>");
		}

		sb.append("<table class='form_tbl'><tr>");
		sb.append(" <td class='l'>#(attachment_edit.1)</td>");
		sb.append(" <td class='v'>").append(InputElement.textarea("attach_desc").setRows(4))
				.append("</td>");
		sb.append("</tr></table>");
		return sb.toString();
	}
}
