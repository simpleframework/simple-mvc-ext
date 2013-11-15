package net.simpleframework.mvc.component.ext.comments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.web.html.HtmlEncoder;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.ctx.script.MVEL2Template;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.SmileyUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CommentUtils {
	public static final String BEAN_ID = "comment_@bid";

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static String replace(String content, final boolean autoLink) {
		content = HtmlEncoder.text(content);
		content = HtmlUtils.stripScripts(content);
		content = SmileyUtils.replaceSmiley(content);
		content = HtmlUtils.convertHtmlLines(content);
		if (autoLink) {
			content = HtmlUtils.autoLink(content);
		}
		return content;
	}

	public static String toCommentHTML(final ComponentParameter cp) {
		final ICommentHandler hdl = (ICommentHandler) cp.getComponentHandler();
		final KVMap variables = new KVMap().add("name", cp.getComponentName())
				.add("hashId", cp.hashId()).add("showSmiley", cp.getBeanProperty("showSmiley"))
				.add("count", hdl.comments(cp).getCount());
		return MVEL2Template.replace(variables, ICommentHandler.class, "Comment.html");
	}
}
