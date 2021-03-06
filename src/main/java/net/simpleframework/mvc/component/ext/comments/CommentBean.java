package net.simpleframework.mvc.component.ext.comments;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CommentBean extends AbstractContainerBean {
	private static final long serialVersionUID = -5677942277141539026L;

	/* 是否显示表情 */
	private boolean showSmiley = BeanDefaults.getBool(getClass(), "showSmiley", true);
	/* 是否允许评论 */
	private boolean canReply = BeanDefaults.getBool(getClass(), "canReply", true);

	/* 是否显示点赞按钮 */
	private boolean showLike = BeanDefaults.getBool(getClass(), "canReply", false);

	/* 提交按钮文本 */
	private String submitText;

	/* 允许填写意见的最大字符数 */
	private int maxlength = 400;

	/* 是否只读 */
	private boolean readonly;

	/* 操作角色 */
	private String role;

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

	public int getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(final int maxlength) {
		this.maxlength = maxlength;
	}

	public String getSubmitText() {
		return submitText;
	}

	public CommentBean setSubmitText(final String submitText) {
		this.submitText = submitText;
		return this;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public CommentBean setReadonly(final boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	public boolean isShowLike() {
		return showLike;
	}

	public CommentBean setShowLike(final boolean showLike) {
		this.showLike = showLike;
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
