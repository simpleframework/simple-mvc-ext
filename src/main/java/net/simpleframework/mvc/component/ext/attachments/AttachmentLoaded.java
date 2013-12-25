package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.ctx.common.bean.AttachmentFile;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.TextForward;
import net.simpleframework.mvc.common.DownloadUtils;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.EElementEvent;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.swfupload.AbstractSwfUploadHandler;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;
import net.simpleframework.mvc.component.ui.tooltip.ETipElement;
import net.simpleframework.mvc.component.ui.tooltip.ETipPosition;
import net.simpleframework.mvc.component.ui.tooltip.TipBean;
import net.simpleframework.mvc.component.ui.tooltip.TipBean.HideOn;
import net.simpleframework.mvc.component.ui.tooltip.TipBean.Hook;
import net.simpleframework.mvc.component.ui.tooltip.TooltipBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AttachmentLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		final ComponentParameter cp = AttachmentUtils.get(pp);
		final AttachmentBean attachmentBean = (AttachmentBean) cp.componentBean;
		final String beanId = attachmentBean.hashId();
		final String attachmentName = cp.getComponentName();

		SwfUploadBean swfUpload = null;
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		if (!readonly) {
			swfUpload = (SwfUploadBean) pp
					.addComponentBean(attachmentName + "_swfUpload", SwfUploadBean.class)
					.setJsCompleteCallback("$Actions['" + attachmentName + "_list']();")
					.setContainerId("attachment_" + attachmentBean.hashId())
					.setHandleClass(SwfUploadAction.class).setAttr("$attachment", attachmentBean);
			((IAttachmentHandler) cp.getComponentHandler()).setSwfUploadBean(cp, swfUpload);
		}

		final String attachmentListId = "attachment_list_" + beanId;
		// 附件列表
		pp.addComponentBean(attachmentName + "_list", AjaxRequestBean.class)
				.setJsCompleteCallback("$Actions['AttachmentLoaded_Tip']();")
				.setUpdateContainerId(attachmentListId).setHandleMethod("doList")
				.setHandleClass(AttachmentAction.class).setAttr("$attachment", attachmentBean)
				.setAttr("$swfupload", swfUpload);

		if (!readonly) {
			// 删除附件条目
			pp.addComponentBean(attachmentName + "_delete", AjaxRequestBean.class)
					.setHandleMethod("doDelete").setHandleClass(AttachmentAction.class)
					.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);

			// 编辑
			final AjaxRequestBean editPage = pp.addComponentBean(attachmentName + "_editPage",
					AjaxRequestBean.class).setUrlForward(
					pp.getResourceHomePath(AttachmentLoaded.class) + "/jsp/attachment_edit.jsp");
			if (swfUpload != null) {
				editPage.setSelector(swfUpload.getSelector());
			}
			pp.addComponentBean(attachmentName + "_editWin", WindowBean.class)
					.setContentRef(attachmentName + "_editPage").setHeight(240).setWidth(420)
					.setTitle($m("AttachmentLoaded.3"));

			// 选取
			final String insertTextarea = (String) cp.getBeanProperty("insertTextarea");
			if (StringUtils.hasText(insertTextarea)) {
				pp.addComponentBean(attachmentName + "_selected", AjaxRequestBean.class)
						.setHandleMethod("doSelect").setHandleClass(AttachmentAction.class)
						.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);
			}
		}

		// 下载
		pp.addComponentBean(attachmentName + "_download", AjaxRequestBean.class)
				.setHandleMethod("doDownload").setHandleClass(AttachmentAction.class)
				.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);

		final IAttachmentHandler aHandler = (IAttachmentHandler) cp.getComponentHandler();
		final String tPath = aHandler.getTooltipPath(cp);
		if (StringUtils.hasText(tPath)) {
			// tip
			final AjaxRequestBean tooltipPage = pp.addComponentBean("AttachmentLoaded_TipPage",
					AjaxRequestBean.class).setUrlForward(tPath);
			if (swfUpload != null) {
				tooltipPage.setSelector(swfUpload.getSelector());
			}
			final TooltipBean tooltip = pp.addComponentBean("AttachmentLoaded_Tip", TooltipBean.class);
			tooltip.addTip(new TipBean(tooltip).setSelector("#" + attachmentListId + " a")
					.setContentRef("AttachmentLoaded_TipPage").setCache(true)
					.setTitle($m("AttachmentLoaded.1")).setStem(ETipPosition.leftTop)
					.setHook(new Hook(ETipPosition.rightTop, ETipPosition.topLeft))
					.setHideOn(new HideOn(ETipElement.closeButton, EElementEvent.click)).setWidth(400));
		}
	}

	public static class SwfUploadAction extends AbstractSwfUploadHandler {

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			return ComponentUtils.toFormParameters(nCP);
		}

		@Override
		public void upload(final ComponentParameter cp, final IMultipartFile multipartFile,
				final Map<String, Object> variables) throws IOException {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			((IAttachmentHandler) nCP.getComponentHandler()).upload(nCP, multipartFile, variables);
		}
	}

	public static class AttachmentAction extends DefaultAjaxRequestHandler {

		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("selector".equals(beanProperty)) {
				ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$swfupload");
				if (nCP.componentBean != null) {
					return nCP.getBeanProperty("selector");
				} else {
					// 只读模式下
					nCP = ComponentParameter.getByAttri(cp, "$attachment");
					if (nCP.componentBean != null) {
						return nCP.getBeanProperty("selector");
					}
				}
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		public IForward doDelete(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final String attachmentName = nCP.getComponentName();

			((IAttachmentHandler) nCP.getComponentHandler()).doDelete(nCP, nCP.getParameter("id"));
			return new JavascriptForward("$Actions['" + attachmentName + "_list']();");
		}

		public IForward doList(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			try {
				return new TextForward(
						((IAttachmentHandler) nCP.getComponentHandler()).toAttachmentListHTML(nCP));
			} catch (final IOException e) {
				throw ComponentHandlerException.of(e);
			}
		}

		public IForward doDownload(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final JavascriptForward js = new JavascriptForward();
			try {
				final IAttachmentHandler handler = (IAttachmentHandler) nCP.getComponentHandler();
				final AttachmentFile af = handler.getAttachmentById(nCP, nCP.getParameter("id"));
				if (af != null) {
					js.append("$Actions.loc('")
							.append(DownloadUtils.getDownloadHref(af, handler.getClass())).append("');");
				} else {
					js.append("alert(\"").append($m("AttachmentLoaded.0")).append("\");");
				}
			} catch (final IOException e) {
				throw ComponentHandlerException.of(e);
			}
			return js;
		}

		public IForward doSelect(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final JavascriptForward js = new JavascriptForward();
			final String insertTextarea = (String) nCP.getBeanProperty("insertTextarea");
			final String[] idArr = StringUtils.split(nCP.getParameter("ids"), ";");
			final StringBuilder sb = new StringBuilder();
			if (idArr != null) {
				try {
					final IAttachmentHandler attachmentHdl = (IAttachmentHandler) nCP
							.getComponentHandler();
					for (final String id : idArr) {
						final AttachmentFile af = attachmentHdl.getAttachmentById(nCP, id);
						final AbstractElement<?> element = attachmentHdl.getDownloadLink(nCP, af, id);
						if (element != null) {
							if (!StringUtils.hasText(element.getText())) {
								element.setText(af.getTopic());
							}
							if (element instanceof LinkElement) {
								((LinkElement) element).addStyle("line-height:21px;");
							}
							sb.append(element).append("<br />");
						}
					}
				} catch (final IOException e) {
					throw ComponentHandlerException.of(e);
				}
			}
			js.append("$win($Actions['").append(cp.getComponentName()).append("'].trigger).close();");
			js.append("$Actions.setValue(\"").append(insertTextarea).append("\", \"")
					.append(JavascriptUtils.escape(sb.toString())).append("\", true);");
			return js;
		}
	}
}
