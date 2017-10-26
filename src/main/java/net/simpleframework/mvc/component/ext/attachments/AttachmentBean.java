package net.simpleframework.mvc.component.ext.attachments;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AttachmentBean extends AbstractContainerBean {
	private static final long serialVersionUID = 1267234794383598769L;

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

	/* 是否显示zip下载 */
	private boolean showZipDownload = BeanDefaults.getBool(getClass(), "showZipDownload", false);

	/* 显示编辑按钮 */
	private boolean showEdit = BeanDefaults.getBool(getClass(), "showEdit", true);

	/* 显示操作菜单 */
	private boolean showMenu = BeanDefaults.getBool(getClass(), "showMenu", true);

	private boolean readonly = BeanDefaults.getBool(getClass(), "readonly", false);

	private boolean showLineNo = BeanDefaults.getBool(getClass(), "showLineNo", false);

	private boolean plupload = BeanDefaults.getBool(getClass(), "plupload", false);

	private boolean cropper = BeanDefaults.getBool(getClass(), "cropper", false);

	private String cropperRatio = "16 / 10";

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

	public boolean isShowZipDownload() {
		return showZipDownload;
	}

	public AttachmentBean setShowZipDownload(final boolean showZipDownload) {
		this.showZipDownload = showZipDownload;
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

	public boolean isShowMenu() {
		return showMenu;
	}

	public AttachmentBean setShowMenu(final boolean showMenu) {
		this.showMenu = showMenu;
		return this;
	}

	public boolean isShowLineNo() {
		return showLineNo;
	}

	public AttachmentBean setShowLineNo(final boolean showLineNo) {
		this.showLineNo = showLineNo;
		return this;
	}

	public boolean isPlupload() {
		return plupload;
	}

	public AttachmentBean setPlupload(final boolean plupload) {
		this.plupload = plupload;
		return this;
	}

	public boolean isCropper() {
		return cropper;
	}

	public AttachmentBean setCropper(final boolean cropper) {
		this.cropper = cropper;
		return this;
	}

	public String getCropperRatio() {
		return cropperRatio;
	}

	public AttachmentBean setCropperRatio(final String cropperRatio) {
		this.cropperRatio = cropperRatio;
		return this;
	}
}
