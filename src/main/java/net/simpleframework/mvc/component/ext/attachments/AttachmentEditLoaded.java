package net.simpleframework.mvc.component.ext.attachments;

import java.util.Map;

import net.simpleframework.ctx.common.bean.AttachmentFile;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AttachmentEditLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);
		final ComponentParameter cp = AttachmentUtils.get(pp);
		final String beanId = cp.hashId();
		final String attachmentName = cp.getComponentName();

		pp.addComponentBean(attachmentName + "_edit_Validation", ValidationBean.class)
				.setWarnType(EWarnType.insertAfter).setTriggerSelector("#btn_" + beanId)
				.addValidators(new Validator(EValidatorMethod.required, "#attach_topic"));

		pp.addComponentBean(attachmentName + "_edit_Save", AjaxRequestBean.class)
				.setSelector("#af_" + beanId).setHandlerClass(SaveAction.class);
	}

	@Override
	public void onPageLoad(final PageParameter pp, final Map<String, Object> dataBinding,
			final PageSelector selector) throws Exception {
		super.onPageLoad(pp, dataBinding, selector);
		final ComponentParameter cp = AttachmentUtils.get(pp);
		final IAttachmentHandler aHdl = (IAttachmentHandler) cp.getComponentHandler();
		final AttachmentFile af = aHdl.getAttachmentById(cp, pp.getParameter("id"));
		if (af != null) {
			dataBinding.put("attach_id", af.getId());
			dataBinding.put("attach_topic", af.getTopic());
			dataBinding.put("attach_type", af.getType());
			dataBinding.put("attach_desc", af.getDescription());
		}
	}

	public static class SaveAction extends DefaultAjaxRequestHandler {
		@Override
		public IForward ajaxProcess(final ComponentParameter cp) throws Exception {
			final ComponentParameter nCP = AttachmentUtils.get(cp);
			final String attachmentName = nCP.getComponentName();
			((IAttachmentHandler) nCP.getComponentHandler()).doSave(nCP, cp.getParameter("attach_id"),
					cp.getParameter("attach_topic"), cp.getIntParameter("attach_type"),
					cp.getParameter("attach_desc"));
			return new JavascriptForward("$Actions['").append(attachmentName)
					.append("_editWin'].close();$Actions['").append(attachmentName).append("_list']();");
		}
	}
}
