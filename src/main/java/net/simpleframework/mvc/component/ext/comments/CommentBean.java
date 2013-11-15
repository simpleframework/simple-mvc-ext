package net.simpleframework.mvc.component.ext.comments;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CommentBean extends AbstractContainerBean {

	/* 是否显示表情 */
	private boolean showSmiley = BeanDefaults.getBool(getClass(), "showSmiley", true);

	/* 是否允许评论 */
	private boolean canReply = BeanDefaults.getBool(getClass(), "canReply", true);

	/* 操作角色 */
	private String role;

	public CommentBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public boolean isShowSmiley() {
		return showSmiley;
	}

	public CommentBean setShowSmiley(final boolean showSmiley) {
		this.showSmiley = showSmiley;
		return this;
	}

	public boolean isCanReply() {
		return canReply;
	}

	public CommentBean setCanReply(final boolean canReply) {
		this.canReply = canReply;
		return this;
	}

	public String getRole() {
		return role;
	}

	public CommentBean setRole(final String role) {
		this.role = role;
		return this;
	}
}
