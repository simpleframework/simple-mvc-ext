package net.simpleframework.mvc.component.ext.comments;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.common.MobileUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectUtils;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.mvc.AbstractBasePage;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.MVCContext;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ImageElement;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.PhotoImage;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.pager.PagerBean;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractCommentHandler extends ComponentHandlerEx implements ICommentHandler {

	@Override
	public JavascriptForward addComment(final ComponentParameter cp) {
		final JavascriptForward js = new JavascriptForward("$COMMENT.doCallback(")
				.append(comments(cp).getCount()).append(");");
		return js;
	}

	@Override
	public JavascriptForward deleteComment(final ComponentParameter cp, final Object id) {
		final JavascriptForward js = new JavascriptForward("$COMMENT.doCallback(")
				.append(comments(cp).getCount()).append(");");
		return js;
	}

	@Override
	public Object getProperty(final ComponentParameter cp, final Object o, final String name) {
		return BeanUtils.getProperty(o, name);
	}

	@Override
	public void setPagerBean(final PagerBean pager) {
	}

	@Override
	public void setSubmitBean(final AjaxRequestBean submit) {
	}

	@Override
	public String toEditorHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		if (!readonly) {
			sb.append(toEditor_headHTML(cp));
		} else if (!cp.isLogin()) {
			final String lhtml = toEditorHTML_tlogin(cp);
			if (lhtml != null) {
				sb.append(lhtml);
			}
		}
		sb.append("<div class='t1_comments'>");
		sb.append(" <div id='id").append(cp.getComponentName()).append("_pager'></div>");
		sb.append("</div>");
		return sb.toString();
	}

	protected String toEditor_head_l1HTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='l1 clearfix'>");
		sb.append(" <div class='left'>");
		sb.append("  <span class='icon'></span>");
		sb.append("  <span class='reply'></span>");
		sb.append(" </div>");
		sb.append(" <div class='right'>");
		sb.append(toEditorHTML_tright(cp, comments(cp).getCount()));
		sb.append(" </div>");
		sb.append("</div>");
		return sb.toString();
	}

	protected String toEditorHTML_tright(final ComponentParameter cp, final int count) {
		final StringBuilder sb = new StringBuilder();
		sb.append($m("AbstractCommentHandler.0")).append("<span class='num'>").append(count)
				.append("</span>").append($m("AbstractCommentHandler.1"));
		return sb.toString();
	}

	protected String toEditor_headHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='t1_head'>");
		sb.append(toEditor_head_l1HTML(cp));
		sb.append(" <div class='l2'>").append(createTextarea(cp));
		sb.append("  <input type='hidden' name='parentId' />");
		sb.append(" </div>");
		final boolean showSmiley = (Boolean) cp.getBeanProperty("showSmiley");
		if (showSmiley && cp.isMobile(true)) {
			sb.append("<div class='smiley' style='display: none;'>");
			final String ipath = cp.getResourceHomePath(DictionaryBean.class) + "/smiley/";
			int i = 0;
			for (int j = 1; j <= 3; j++) {
				sb.append(" <div>");
				for (; i < j * 30; i++) {
					sb.append("<div class='iitem'>");
					sb.append(new ImageElement(ipath + i + ".gif")
							.setOnclick("$COMMENT.insert_smiley(this);"));
					sb.append("</div>");
				}
				sb.append(" </div>");
			}
			sb.append("</div>");
		}
		sb.append(" <div class='l3 clearfix'>");
		sb.append("  <div class='left'>");
		if (showSmiley) {
			sb.append(createSmiley(cp).addClassName("smiley-btn"));
		}
		final String audioHtml = toAudioHTML(cp);
		if (audioHtml != null) {
			sb.append(audioHtml);
		}
		sb.append("	  <span class='ltxt'>&nbsp;</span>");
		sb.append("  </div>");
		sb.append("  <div class='right'>").append(createSubmit(cp)).append("</div>");
		sb.append(" </div>");
		sb.append("</div>");
		return sb.toString();
	}

	protected String toAudioHTML(final ComponentParameter cp) {
		return null;
	}

	protected String toEditorHTML_tlogin(final ComponentParameter cp) {
		return null;
	}

	protected AbstractElement<?> createSmiley(final ComponentParameter cp) {
		final ImageElement img = new ImageElement(
				cp.getCssResourceHomePath(AbstractCommentHandler.class) + "/images/icon-smiley.png")
						.setTitle($m("AbstractCommentHandler.3"));
		if (cp.isMobile(true)) {
			img.setOnclick("$COMMENT.show_smiley(this);");
		} else {
			img.setOnclick("$Actions['" + cp.getComponentName() + "_smiley']();");
		}
		return img;
	}

	protected AbstractElement<?> createTextarea(final ComponentParameter cp) {
		return InputElement.textarea("id" + cp.getComponentName() + "_textarea").setName("ccomment")
				.setAutoRows(true).setRows(5)
				.addAttribute("maxlength", cp.getBeanProperty("maxlength"));
	}

	protected AbstractElement<?> createSubmit(final ComponentParameter cp) {
		final String commentName = cp.getComponentName();
		final String submitText = (String) cp.getBeanProperty("submitText");
		return LinkButton.corner(submitText != null ? submitText : $m("AbstractCommentHandler.2"))
				.setId("id" + commentName + "_submit")
				.setOnclick("$COMMENT.submit('" + commentName + "');");
	}

	@Override
	public String toListHTML(final ComponentParameter cp, final List<?> data) {
		final StringBuilder sb = new StringBuilder();
		for (final Object o : data) {
			sb.append(toOitemHTML(cp, o));
		}
		return sb.toString();
	}

	protected String toOitemHTML(final ComponentParameter cp, final Object o) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='oitem'><div class='w'>");
		sb.append(" <div class='icon'>");
		sb.append(toIconTDHTML(cp, o));
		sb.append(" </div><div>");
		final boolean mgr = cp.isLmember(cp.getBeanProperty("role"));
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		sb.append(toCommentTDHTML(cp, o, mgr, readonly));
		sb.append(" </div>");
		sb.append("</div></div>");
		return sb.toString();
	}

	protected boolean isShowRef(final ComponentParameter cp) {
		return true;
	}

	protected String toCommentTDHTML_content(final ComponentParameter cp, final String content) {
		final StringBuilder sb = new StringBuilder();
		sb.append(CommentUtils.replace(content, true));
		return sb.toString();
	}

	protected String toCommentTDHTML(final ComponentParameter cp, final Object o, final boolean mgr,
			final boolean readonly) {
		final StringBuilder sb = new StringBuilder();
		final String content = Convert.toString(getProperty(cp, o, ATTRI_COMMENT));
		sb.append("<div class='mc'>");
		sb.append(toCommentTDHTML_content(cp, StringUtils.blank(content)));
		sb.append("</div>");
		sb.append(toCommentTDHTML_desc(cp, o, mgr, readonly));
		if (isShowRef(cp)) {
			final Object p = getCommentById(cp, getProperty(cp, o, ATTRI_PARENTID));
			if (p != null) {
				final String reply = (String) getProperty(cp, p, ATTRI_COMMENT);
				sb.append("<div class='rc'>");
				final Object userId2 = getProperty(cp, p, ATTRI_USERID);
				final Date createDate2 = (Date) getProperty(cp, p, ATTRI_CREATEDATE);
				sb.append("<div class='r_desc'>");
				sb.append(Convert.toDateTimeString(createDate2)).append(SpanElement.SPACE)
						.append(new SpanElement(permission.getUser(userId2)));
				sb.append("</div>");
				if (reply != null && !reply.startsWith("#blob:")) {
					sb.append(CommentUtils.replace(reply, true));
				}
				sb.append("</div>");
			}
		}
		sb.append(toCommentTDHTML_children(cp, o, mgr, readonly));
		return sb.toString();
	}

	protected LinkElement createViewReplies(final ComponentParameter cp, final Object parent,
			final int count) {
		return new LinkElement($m("AbstractCommentHandler.4", count) + "&raquo;");
	}

	protected String toCommentTDHTML_children(final ComponentParameter cp, final Object o,
			final boolean mgr, final boolean readonly) {
		final StringBuilder sb = new StringBuilder();
		final IDataQuery<?> dq = children(cp, getProperty(cp, o, ATTRI_ID));
		int count;
		if (dq != null && (count = dq.getCount()) > 0) {
			final LinkElement le = createViewReplies(cp, o, count);
			if (le != null) {
				sb.append("<div class='rr'>").append(le).append("</div>");
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	protected String toCommentTDHTML_desc(final ComponentParameter cp, final Object o,
			final boolean mgr, final boolean readonly) {
		final Object id = getProperty(cp, o, ATTRI_ID);
		final Date createDate = (Date) getProperty(cp, o, ATTRI_CREATEDATE);
		final Object userId = getProperty(cp, o, ATTRI_USERID);
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='desc'>");
		if (createDate.getYear() == new Date().getYear()) {
			sb.append(Convert.toDateString(createDate, "MM-dd HH:mm"));
		} else {
			sb.append(Convert.toDateTimeString(createDate));
		}

		final String ipath = cp.getCssResourceHomePath(CommentBean.class) + "/images/";
		if (!readonly && (Boolean) cp.getBeanProperty("canReply")) {
			final String txt = $m("CommentList.0");
			sb.append("&nbsp;&middot;&nbsp;");
			sb.append(new LinkElement(txt).setOnclick(
					"$COMMENT.reply('" + id + "', '" + (txt + permission.getUser(userId)) + "');"));
		}
		if (mgr || ObjectUtils.objectEquals(cp.getLoginId(), userId)) {
			sb.append("&nbsp;&middot;&nbsp;");
			sb.append(new LinkElement($m("Delete"))
					.setOnclick("$Actions['" + cp.getComponentName() + "_delete']('id=" + id + "');"));
		}

		if ((Boolean) cp.getBeanProperty("showLike")) {
			sb.append("<div class='like'>");
			final int likes = ((Number) getProperty(cp, o, ATTRI_LIKES)).intValue();
			final SpanElement le = new SpanElement();
			if (likes > 0) {
				le.setText(likes);
			}
			sb.append(le);
			sb.append(new ImageElement(ipath + (isLike(cp, o) ? "like2.png" : "like.png"))
					.setOnclick("$Actions['" + cp.getComponentName() + "_like']('id=" + id + "');"));
			sb.append("</div>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	protected boolean isLike(final ComponentParameter cp, final Object comment) {
		return false;
	}

	@Override
	public JavascriptForward likeComment(final ComponentParameter cp, final Object id) {
		throw NotImplementedException.of(getClass(), "likeComment");
	}

	protected JavascriptForward toLikeCallback(final ComponentParameter cp, final int likes,
			final boolean like) {
		final JavascriptForward js = new JavascriptForward(
				"var img = $Actions['" + cp.getComponentName() + "_like'].trigger;");
		js.append("var l = img.previous();");
		if (likes > 0) {
			js.append("l.update('").append(likes).append("');");
		} else {
			js.append("l.update('');");
		}
		js.append("img.src = '").append(cp.getCssResourceHomePath(CommentBean.class))
				.append("/images/").append(like ? "like.png" : "like2.png").append("';");
		js.append("var desc = img.up('.desc');");
		js.append("var plus = desc.down('.plus');");
		js.append("if (plus) { plus.remove(); }");
		js.append("desc.insert(plus = new Element('span', {");
		js.append(" 'className' : 'plus'");
		js.append("}));");
		js.append("var like2 = img.src.endsWith('like2.png');");
		js.append("plus.update(like2 ? '+1' : '-1');");
		js.append("plus.setStyle(like2 ? 'color: #8c4' : 'color: #888');");
		js.append("(function(){ plus.addClassName('show'); }).delay(0.8);");
		return js;
	}

	protected final IPagePermissionHandler permission = MVCContext.get().getPermission();

	protected String toIconTDHTML(final ComponentParameter cp, final Object o) {
		final StringBuilder sb = new StringBuilder();
		final Object userId = getProperty(cp, o, ATTRI_USERID);
		sb.append(new PhotoImage(permission.getPhotoUrl(cp, userId)));
		final Object oUser = permission.getUser(userId);
		sb.append("<div class='icon_d'>").append(MobileUtils.replaceAllSMobile(oUser.toString()))
				.append("</div>");
		return sb.toString();
	}

	@Override
	public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
		if ("readonly".equals(beanProperty)) {
			if (!cp.isLogin()) {
				return true;
			}
		}
		return super.getBeanProperty(cp, beanProperty);
	}

	public abstract static class AbstractViewRepliesPage extends AbstractBasePage {

		@Override
		protected void onForward(final PageParameter pp) throws Exception {
			super.onForward(pp);

			addCommentBean(pp);
		}

		protected CommentBean addCommentBean(final PageParameter pp) {
			return (CommentBean) addComponentBean(pp, "ViewRepliesPage_comment", CommentBean.class)
					.setShowLike(true).setReadonly(true).setContainerId("idViewRepliesPage_comment");
		}

		@Override
		protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
				final String currentVariable) throws IOException {
			final StringBuilder sb = new StringBuilder();
			sb.append("<div id='idViewRepliesPage_comment'>");
			sb.append("</div>");
			return sb.toString();
		}
	}
}
