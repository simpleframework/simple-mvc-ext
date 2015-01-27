package net.simpleframework.mvc.component.ext.comments;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.List;

import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.Convert;
import net.simpleframework.mvc.JavascriptForward;
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
	public String toListHTML(final ComponentParameter cp, final List<?> data) {
		final boolean mgr = cp.getLogin().isMember(cp.getBeanProperty("role"));

		final StringBuilder sb = new StringBuilder();
		for (final Object o : data) {
			final Object id = getProperty(cp, o, ATTRI_ID);
			final String content = Convert.toString(getProperty(cp, o, ATTRI_COMMENT));
			final Date createDate = (Date) getProperty(cp, o, ATTRI_CREATEDATE);
			final Object userId = getProperty(cp, o, ATTRI_USERID);
			sb.append("<div class='oitem'><table><tr>");
			sb.append("<td class='icon'>");
			final IPagePermissionHandler permission = mvcContext.getPermission();
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
				sb.append(Convert.toDateString(createDate2)).append(SpanElement.SEP)
						.append(permission.getUser(userId2));
				sb.append("</div>");
				sb.append(CommentUtils.replace(reply, true));
				sb.append("</div>");
			}
			sb.append("<div class='mc'>").append(CommentUtils.replace(content, true)).append("</div>");
			sb.append("<div class='desc'>");
			sb.append(Convert.toDateString(createDate));

			if (mgr) {
				sb.append(SpanElement.SEP).append("<a onclick=\"$Actions['")
						.append(cp.getComponentName()).append("_delete']('id=").append(id)
						.append("');\">").append($m("Delete")).append("</a>");
			}
			sb.append(SpanElement.SEP);
			sb.append("<a onclick=\"$COMMENT.reply('").append(id).append("', '").append(oUser)
					.append("');\">").append($m("CommentList.0")).append("</a>");
			sb.append("</div>");
			sb.append("</td>");
			sb.append("</tr></table></div>");
		}
		return sb.toString();
	}
}
