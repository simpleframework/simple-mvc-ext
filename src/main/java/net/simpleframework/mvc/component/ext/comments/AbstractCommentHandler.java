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
import net.simpleframework.mvc.ctx.permission.IPagePermissionHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractCommentHandler extends ComponentHandlerEx implements ICommentHandler {

	protected static String PARAM_COMMENT = "ccomment";
	protected static String PARAM_PARENTID = "parentId";

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

	protected String toEditorHTML_tright(final ComponentParameter cp, final int count) {
		final StringBuilder sb = new StringBuilder();
		sb.append($m("AbstractCommentHandler.0")).append("<span class='num'>").append(count)
				.append("</span>").append($m("AbstractCommentHandler.1"));
		return sb.toString();
	}

	@Override
	public String toEditorHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		if (!readonly) {
			sb.append("<div class='t1_head'>");
			sb.append(" <div class='l1 clearfix'>");
			sb.append("  <div class='left'>");
			sb.append("   <span class='icon'></span>");
			sb.append("   <span class='reply'></span>");
			sb.append("  </div>");
			sb.append("  <div class='right'>").append(toEditorHTML_tright(cp, comments(cp).getCount()))
					.append("</div>");
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

	protected String toEditorHTML_tlogin(final ComponentParameter cp) {
		return null;
	}

	protected AbstractElement<?> createSmiley(final ComponentParameter cp) {
		return LinkElement.style2($m("AbstractCommentHandler.3"))
				.setOnclick("$Actions['" + cp.getComponentName() + "_smiley']();");
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
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		final StringBuilder sb = new StringBuilder();
		for (final Object o : data) {
			sb.append("<div class='oitem'><table><tr>");
			sb.append("<td class='icon'>");
			sb.append(toIconTDHTML(cp, o));
			sb.append("</td><td>");
			sb.append(toCommenTDHTML(cp, o, mgr, readonly));
			sb.append("</td>");
			sb.append("</tr></table></div>");
		}
		return sb.toString();
	}

	protected String toCommenTDHTML(final ComponentParameter cp, final Object o, final boolean mgr,
			final boolean readonly) {
		final StringBuilder sb = new StringBuilder();
		final String content = Convert.toString(getProperty(cp, o, ATTRI_COMMENT));
		sb.append("<div class='mc'>").append(CommentUtils.replace(content, true)).append("</div>");
		sb.append(toCommenTDHTML_desc(cp, o, mgr, readonly));
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
			sb.append(CommentUtils.replace(reply, true));
			sb.append("</div>");
		}
		// sb.append(toCommenTDHTML_children(cp, o, mgr, readonly));
		return sb.toString();
	}

	protected LinkElement createViewReplies(final ComponentParameter cp, final Object parent,
			final int count) {
		return new LinkElement("查看全部" + count + "条回复 &raquo;");
	}

	protected String toCommenTDHTML_children(final ComponentParameter cp, final Object o,
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

	protected String toCommenTDHTML_desc(final ComponentParameter cp, final Object o,
			final boolean mgr, final boolean readonly) {
		final Object id = getProperty(cp, o, ATTRI_ID);
		final Date createDate = (Date) getProperty(cp, o, ATTRI_CREATEDATE);
		final Object userId = getProperty(cp, o, ATTRI_USERID);
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='desc'>");
		sb.append(Convert.toDateTimeString(createDate));
		if (!readonly && (Boolean) cp.getBeanProperty("canReply")) {
			sb.append(SpanElement.SPACE);
			sb.append(LinkElement.style2($m("CommentList.0"))
					.setOnclick("$COMMENT.reply('" + id + "', '" + permission.getUser(userId) + "');"));
		}
		if (mgr || ObjectUtils.objectEquals(cp.getLoginId(), userId)) {
			sb.append(SpanElement.SPACE);
			sb.append(LinkElement.style2($m("Delete"))
					.setOnclick("$Actions['" + cp.getComponentName() + "_delete']('id=" + id + "');"));
		}
		if ((Boolean) cp.getBeanProperty("showLike")) {
			sb.append("<div class='right'>");
			final int likes = ((Number) getProperty(cp, o, ATTRI_LIKES)).intValue();
			final SpanElement le = new SpanElement();
			if (likes > 0) {
				le.setText(likes);
			}
			sb.append(le);
			final String ipath = cp.getCssResourceHomePath(CommentBean.class) + "/images/";
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
			final String image) {
		final JavascriptForward js = new JavascriptForward(
				"var img = $Actions['" + cp.getComponentName() + "_like'].trigger;");
		js.append("var l = img.previous();");
		if (likes > 0) {
			js.append("l.update('").append(likes).append("');");
		} else {
			js.append("l.update('');");
		}
		js.append("img.src = '").append(cp.getCssResourceHomePath(CommentBean.class))
				.append("/images/").append(image).append("';");
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

	public static class ViewRepliesPage extends AbstractBasePage {

		@Override
		protected void onForward(final PageParameter pp) throws Exception {
			super.onForward(pp);

			pp.addImportCSS(AbstractCommentHandler.class, "/comment.css");
		}

		@Override
		protected String toHtml(final PageParameter pp, final Map<String, Object> variables,
				final String currentVariable) throws IOException {
			final ComponentParameter cp = CommentUtils.get(pp);
			final AbstractCommentHandler hdl = (AbstractCommentHandler) cp.getComponentHandler();
			final StringBuilder sb = new StringBuilder();
			if (hdl != null) {
				final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
				final boolean mgr = cp.isLmember(cp.getBeanProperty("role"));
				final IDataQuery<?> dq = hdl.children(cp, cp.getParameter("id"));
				if (dq != null) {
					sb.append("<div class='Comp_Comment'><div class='t1_comments'>");
					Object o;
					while ((o = dq.next()) != null) {
						sb.append("<div class='oitem'>");
						sb.append(hdl.toCommenTDHTML(cp, hdl.getCommentById(cp, o), mgr, readonly));
						sb.append("</div>");
					}
					sb.append("</div></div>");
				}
			}
			return sb.toString();
		}
	}
}
