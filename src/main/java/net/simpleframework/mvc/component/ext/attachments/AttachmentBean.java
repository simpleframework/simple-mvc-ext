package net.simpleframework.mvc.component.ext.attachments;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AttachmentBean extends AbstractContainerBean {

	private String insertTextarea;

	private boolean readonly = BeanDefaults.getBool(getClass(), "readonly", false);

	public AttachmentBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public String getInsertTextarea() {
		return insertTextarea;
	}

	public AttachmentBean setInsertTextarea(final String insertTextarea) {
		this.insertTextarea = insertTextarea;
		return this;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public AttachmentBean setReadonly(final boolean readonly) {
		this.readonly = readonly;
		return this;
	}
}
