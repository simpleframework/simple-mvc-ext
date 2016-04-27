package net.simpleframework.mvc.component.ext.comments;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.List;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.MVCContext;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.PhotoImage;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractCommentHandler extends ComponentHandlerEx implements ICommentHandler {

	protected static String PARAM_COMMENT = "ccomment";
	protected static String PARAM_PARENTID = "parentId";

	@Override
	public JavascriptForward addComment(final ComponentParameter cp) {
		final JavascriptForward js = new JavascriptForward("$COMMENT.doCallback(").append(
				comments(cp).getCount()).append(");");
		return js;
	}

	@Override
	public JavascriptForward deleteComment(final ComponentParameter cp, final Object id) {
		final JavascriptForward js = new JavascriptForward("$COMMENT.doCallback(").append(
				comments(cp).getCount()).append(");");
		return js;
	}

	@Override
	public Object getProperty(final ComponentParameter cp, final Object o, final String name) {
		return BeanUtils.getProperty(o, name);
	}

	@Override
	public String toEditorHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final String commentName = cp.getComponentName();
		sb.append("<div class='t1_head'>");
		sb.append(" <div class='l1 clearfix'>");
		sb.append("  <div class='left'>");
		sb.append("   <span class='icon'></span>");
		sb.append("   <span class='reply'></span>");
		sb.append("  </div>");
		sb.append("  <div class='right'>").append($m("AbstractCommentHandler.0"));
		sb.append("   <span class='num'>").append(comments(cp).getCount()).append("</span>")
				.append($m("AbstractCommentHandler.1"));
		sb.append("  </div>");
		sb.append(" </div>");
		sb.append(" <div class='l2'>").append(createTextarea(cp));
		sb.append("  <input type='hidden' name='parentId' />");
		sb.append(" </div>");
		sb.append(" <div class='l3 clearfix'>");
		sb.append("  <div class='left'>");
		if ((Boolean) cp.getBeanProperty("showSmiley")) {
			sb.append(createSmiley(cp));
		}
		sb.append("	  <span class='ltxt'>&nbsp;</span>");
		sb.append("  </div>");
		sb.append("  <div class='right'>").append(createSubmit(cp)).append("</div>");
		sb.append(" </div>");
		sb.append("</div>");
		sb.append("<div class='t1_comments'>");
		sb.append(" <div id='id").append(commentName).append("_pager'></div>");
		sb.append("</div>");
		return sb.toString();
	}

	protected AbstractElement<?> createSmiley(final ComponentParameter cp) {
		return LinkElement.style2($m("AbstractCommentHandler.3")).setOnclick(
				"$Actions['" + cp.getComponentName() + "_smiley']();");
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
				.setOnclick("$Actions['" + commentName + "_submit']($Form(this.up('.t1_head')));");
	}

	@Override
	public String toListHTML(final ComponentParameter cp, final List<?> data) {
		final boolean mgr = cp.isLmember(cp.getBeanProperty("role"));

		final StringBuilder sb = new StringBuilder();
		for (final Object o : data) {
			final Object id = getProperty(cp, o, ATTRI_ID);
			final String content = Convert.toString(getProperty(cp, o, ATTRI_COMMENT));
			final Date createDate = (Date) getProperty(cp, o, ATTRI_CREATEDATE);
			final Object userId = getProperty(cp, o, ATTRI_USERID);
			sb.append("<div class='oitem'><table><tr>");
			sb.append("<td class='icon'>");
			final IPagePermissionHandler permission = MVCContext.get().getPermission();
			sb.append(new PhotoImage(permission.getPhotoUrl(cp, userId)));
			final Object oUser = permission.getUser(userId);
			sb.append("<div class='icon_d'>").append(oUser).append("</div>");
			sb.append("</td><td>");
			final Object p = getCommentById(cp, getProperty(cp, o, ATTRI_PARENTID));
			if (p != null) {
				final String reply = (String) getProperty(cp, p, ATTRI_COMMENT);
				sb.append("<div class='rc'>");
				final Object userId2 = getProperty(cp, p, ATTRI_USERID);
				final Date createDate2 = (Date) getProperty(cp, p, ATTRI_CREATEDATE);
				sb.append("<div class='r_desc'>");
				sb.append(Convert.toDateTimeString(createDate2)).append(SpanElement.SEP())
						.append(permission.getUser(userId2));
				sb.append("</div>");
				sb.append(CommentUtils.replace(reply, true));
				sb.append("</div>");
			}
			sb.append("<div class='mc'>").append(CommentUtils.replace(content, true)).append("</div>");
			sb.append("<div class='desc'>");
			sb.append(Convert.toDateTimeString(createDate));

			if (mgr) {
				sb.append(SpanElement.SEP()).append("<a onclick=\"$Actions['")
						.append(cp.getComponentName()).append("_delete']('id=").append(id)
						.append("');\">").append($m("Delete")).append("</a>");
			}
			if ((Boolean) cp.getBeanProperty("canReply")) {
				sb.append(SpanElement.SEP());
				sb.append("<a onclick=\"$COMMENT.reply('").append(id).append("', '").append(oUser)
						.append("');\">").append($m("CommentList.0")).append("</a>");
			}
			sb.append("</div>");
			sb.append("</td>");
			sb.append("</tr></table></div>");
		}
		return sb.toString();
	}
}
