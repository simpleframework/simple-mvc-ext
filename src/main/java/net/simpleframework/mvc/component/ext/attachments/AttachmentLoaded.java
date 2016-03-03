package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

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
import net.simpleframework.mvc.common.element.JS;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.menu.EMenuEvent;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
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
					.setJsCompleteCallback(
							"if (!hasQueued) { $Actions['" + attachmentName + "_list'](); }")
					.setContainerId("attachment_" + attachmentBean.hashId())
					.setHandlerClass(SwfUploadAction.class).setAttr("$attachment", attachmentBean);
			((IAttachmentHandler) cp.getComponentHandler()).setSwfUploadBean(cp, swfUpload);
		}

		final String attachmentListId = "attachment_list_" + beanId;
		// 附件列表
		pp.addComponentBean(attachmentName + "_list", AjaxRequestBean.class)
				.setJsCompleteCallback("$Actions['AttachmentLoaded_Tip']();")
				.setUpdateContainerId(attachmentListId).setHandlerMethod("doList")
				.setHandlerClass(AttachmentAction.class).setAttr("$attachment", attachmentBean)
				.setAttr("$swfupload", swfUpload);

		if (!readonly) {
			// 删除附件条目
			pp.addComponentBean(attachmentName + "_delete", AjaxRequestBean.class)
					.setHandlerMethod("doDelete").setHandlerClass(AttachmentAction.class)
					.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);

			// 编辑
			if ((Boolean) cp.getBeanProperty("showEdit")) {
				final AjaxRequestBean editPage = pp.addComponentBean(attachmentName + "_editPage",
						AjaxRequestBean.class).setUrlForward(
						pp.getResourceHomePath(AttachmentLoaded.class) + "/jsp/attachment_edit.jsp");
				if (swfUpload != null) {
					editPage.setSelector(swfUpload.getSelector());
				}
				pp.addComponentBean(attachmentName + "_editWin", WindowBean.class)
						.setContentRef(attachmentName + "_editPage").setHeight(240).setWidth(420)
						.setTitle($m("AttachmentLoaded.3"));
			}

			// 菜单
			if ((Boolean) cp.getBeanProperty("showMenu")) {
				final String moveAct = attachmentName + "_move";
				pp.addComponentBean(moveAct, AjaxRequestBean.class).setHandlerMethod("doMove")
						.setHandlerClass(AttachmentAction.class).setAttr("$attachment", attachmentBean);
				final MenuBean menu = (MenuBean) cp
						.addComponentBean(attachmentName + "_menu", MenuBean.class)
						.setMenuEvent(EMenuEvent.click).setSelector(".attach_menu");
				menu.addItem(
						MenuItem.of($m("Menu.up"), MenuItem.ICON_UP, "AttachmentUtils.doMove(item, '"
								+ moveAct + "', true);"))
						.addItem(
								MenuItem.of($m("Menu.up2"), MenuItem.ICON_UP2,
										"AttachmentUtils.doMove2(item, '" + moveAct + "', true);"))
						.addItem(
								MenuItem.of($m("Menu.down"), MenuItem.ICON_DOWN,
										"AttachmentUtils.doMove(item, '" + moveAct + "');"))
						.addItem(
								MenuItem.of($m("Menu.down2"), MenuItem.ICON_DOWN2,
										"AttachmentUtils.doMove2(item, '" + moveAct + "');"));
			}

			// 选取
			final String insertTextarea = (String) cp.getBeanProperty("insertTextarea");
			if (StringUtils.hasText(insertTextarea)) {
				pp.addComponentBean(attachmentName + "_selected", AjaxRequestBean.class)
						.setHandlerMethod("doSelect").setHandlerClass(AttachmentAction.class)
						.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);
			} else {
				// 提交模式
				final boolean showSubmit = (Boolean) cp.getBeanProperty("showSubmit");
				if (showSubmit) {
					pp.addComponentBean(attachmentName + "_submit", AjaxRequestBean.class)
							.setHandlerMethod("doSubmit").setHandlerClass(AttachmentAction.class)
							.setAttr("$attachment", attachmentBean).setAttr("$swfupload", swfUpload);
				}
			}
		}

		// 下载
		pp.addComponentBean(attachmentName + "_download", AjaxRequestBean.class)
				.setHandlerMethod("doDownload").setHandlerClass(AttachmentAction.class)
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
			tooltip.addTip(new TipBean(tooltip).setSelector("#" + attachmentListId + " a[params]")
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
				final Map<String, Object> variables) throws Exception {
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

		public IForward doMove(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final String attachmentName = nCP.getComponentName();

			((IAttachmentHandler) nCP.getComponentHandler()).doExchange(nCP,
					StringUtils.split(cp.getParameter("rowIds"), ";"));
			return new JavascriptForward("$Actions['" + attachmentName + "_list']();");
		}

		public IForward doDelete(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final String attachmentName = nCP.getComponentName();

			((IAttachmentHandler) nCP.getComponentHandler()).doDelete(nCP, nCP.getParameter("id"));
			return new JavascriptForward("$Actions['" + attachmentName + "_list']();");
		}

		public IForward doList(final ComponentParameter cp) throws Exception {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final StringBuilder sb = new StringBuilder();
			sb.append(((IAttachmentHandler) nCP.getComponentHandler()).toAttachmentListHTML(nCP));
			final StringBuilder script = new StringBuilder();
			script.append("var _menu = $Actions['").append(nCP.getComponentName()).append("_menu']; ");
			script.append("if (_menu) { _menu.bindEvent('.attach_menu'); }");
			return new TextForward(sb.append(JavascriptUtils.wrapScriptTag(script.toString(), true))
					.toString());
		}

		public IForward doDownload(final ComponentParameter cp) throws Exception {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final JavascriptForward js = new JavascriptForward();
			final IAttachmentHandler handler = (IAttachmentHandler) nCP.getComponentHandler();
			final AttachmentFile af = handler.getAttachmentById(nCP, nCP.getParameter("id"));
			if (af != null) {
				js.append(JS.loc(DownloadUtils.getDownloadHref(af, handler.getClass()), true));
			} else {
				js.append("alert(\"").append($m("AttachmentLoaded.0")).append("\");");
			}
			return js;
		}

		public IForward doSelect(final ComponentParameter cp) throws Exception {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			final JavascriptForward js = new JavascriptForward();
			final StringBuilder sb = new StringBuilder();

			final IAttachmentHandler attachmentHdl = (IAttachmentHandler) nCP.getComponentHandler();
			for (final String id : StringUtils.split(nCP.getParameter("ids"), ";")) {
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

			js.append("$win($Actions['").append(cp.getComponentName()).append("'].trigger).close();");
			js.append("$Actions.setValue(\"").append(nCP.getBeanProperty("insertTextarea"))
					.append("\", \"").append(JavascriptUtils.escape(sb.toString())).append("\", true);");
			return js;
		}

		public IForward doSubmit(final ComponentParameter cp) throws Exception {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			return ((IAttachmentHandler) nCP.getComponentHandler()).doSave(nCP, null);
		}
	}
}
