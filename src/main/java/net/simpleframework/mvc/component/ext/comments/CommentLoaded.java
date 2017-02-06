package net.simpleframework.mvc.component.ext.comments;

import static net.simpleframework.common.I18n.$m;

import java.util.List;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.permission.PermissionConst;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.pager.AbstractPagerHandler;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.PagerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CommentLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		final ComponentParameter cp = CommentUtils.get(pp);
		final CommentBean commentBean = (CommentBean) cp.componentBean;
		final String commentName = cp.getComponentName();

		// 提交与验证
		final String idTa = "id" + commentName + "_textarea";
		pp.addComponentBean(commentName + "_validation", ValidationBean.class)
				.setTriggerSelector("#id" + commentName + "_submit").setWarnType(EWarnType.insertAfter)
				.addValidators(new Validator(EValidatorMethod.required, "#" + idTa),
						new Validator(EValidatorMethod.min_length, "#" + idTa, 6)
								.setMessage($m("CommentLoaded.0")));
		pp.addComponentBean(commentName + "_submit", AjaxRequestBean.class)
				.setRole(PermissionConst.ROLE_ALL_ACCOUNT).setConfirmMessage($m("Confirm.Post"))
				.setHandlerMethod("addComment").setHandlerClass(CommentAction.class)
				.setAttr("$comment", commentBean);

		if ((Boolean) cp.getBeanProperty("showSmiley")) {
			// 表情
			pp.addComponentBean(commentName + "_smiley", DictionaryBean.class).setBindingId(idTa)
					.addSmiley(pp);
		}

		if ((Boolean) cp.getBeanProperty("showLike")) {
			pp.addComponentBean(commentName + "_like", AjaxRequestBean.class)
					.setHandlerMethod("doLike").setHandlerClass(CommentAction.class)
					.setAttr("$comment", commentBean);
		}

		if (cp.isLmember(cp.getBeanProperty("role"))) {
			pp.addComponentBean(commentName + "_delete", AjaxRequestBean.class)
					.setConfirmMessage($m("Confirm.Delete")).setHandlerMethod("doDelete")
					.setHandlerClass(CommentAction.class).setAttr("$comment", commentBean);
		}

		// pager
		pp.addComponentBean(commentName + "_pager", PagerBean.class)
				.setPagerBarLayout(EPagerBarLayout.bottom).setShowEditPageItems(false)
				.setNoResultDesc($m("CommentLoaded.1")).setContainerId("id" + commentName + "_pager")
				.setHandlerClass(CommentList.class).setAttr("$comment", commentBean);
	}

	public static class CommentAction extends DefaultAjaxRequestHandler {

		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("selector".equals(beanProperty)) {
				final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$comment");
				if (nCP.componentBean != null) {
					return nCP.getBeanProperty("selector");
				}
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		public IForward addComment(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$comment");
			return ((ICommentHandler) nCP.getComponentHandler()).addComment(nCP);
		}

		public IForward doDelete(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$comment");
			return ((ICommentHandler) nCP.getComponentHandler()).deleteComment(nCP,
					nCP.getParameter("id"));
		}

		public IForward doLike(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$comment");
			return ((ICommentHandler) nCP.getComponentHandler()).likeComment(nCP,
					nCP.getParameter("id"));
		}
	}

	public static class CommentList extends AbstractPagerHandler {

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$comment");
			return ComponentUtils.toFormParameters(nCP);
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$comment");
			return ((ICommentHandler) nCP.getComponentHandler()).comments(nCP);
		}

		@Override
		public String toPagerHTML(final ComponentParameter cp, final List<?> data) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$comment");
			return ((ICommentHandler) nCP.getComponentHandler()).toListHTML(nCP, data);
		}
	}
}
