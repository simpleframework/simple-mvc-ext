package net.simpleframework.mvc.component.ext.attachments;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AttachmentBean extends AbstractContainerBean {

	/* 列表大小 */
	private int attachmentsLimit = BeanDefaults.getInt(getClass(), "attachmentsLimit", 0);
	/* 列表队列大小 */
	private int attachmentsQueueLimit = BeanDefaults.getInt(getClass(), "attachmentsQueueLimit", 0);

	/* 是否图片列表显示 */
	private boolean imagesMode = BeanDefaults.getBool(getClass(), "imagesMode", false);

	/* 插入到文本域中，插入模式 */
	private String insertTextarea;
	/* 提交模式 */
	private boolean showSubmit = BeanDefaults.getBool(getClass(), "showSubmit", false);

	/* 显示编辑按钮 */
	private boolean showEdit = BeanDefaults.getBool(getClass(), "showEdit", true);

	private boolean readonly = BeanDefaults.getBool(getClass(), "readonly", false);

	public int getAttachmentsLimit() {
		return attachmentsLimit;
	}

	public AttachmentBean setAttachmentsLimit(final int attachmentsLimit) {
		this.attachmentsLimit = attachmentsLimit;
		return this;
	}

	public int getAttachmentsQueueLimit() {
		return attachmentsQueueLimit;
	}

	public AttachmentBean setAttachmentsQueueLimit(final int attachmentsQueueLimit) {
		this.attachmentsQueueLimit = attachmentsQueueLimit;
		return this;
	}

	public boolean isImagesMode() {
		return imagesMode;
	}

	public AttachmentBean setImagesMode(final boolean imagesMode) {
		this.imagesMode = imagesMode;
		return this;
	}

	public String getInsertTextarea() {
		return insertTextarea;
	}

	public AttachmentBean setInsertTextarea(final String insertTextarea) {
		this.insertTextarea = insertTextarea;
		return this;
	}

	public boolean isShowSubmit() {
		return showSubmit;
	}

	public AttachmentBean setShowSubmit(final boolean showSubmit) {
		this.showSubmit = showSubmit;
		return this;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public AttachmentBean setReadonly(final boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	public boolean isShowEdit() {
		return showEdit;
	}

	public AttachmentBean setShowEdit(final boolean showEdit) {
		this.showEdit = showEdit;
		return this;
	}
}
