package net.simpleframework.mvc.component.ext.userselect;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserSelectBean extends DictionaryBean {

	private int pageItems;

	private boolean treeMode = true;

	public UserSelectBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
		setShowHelpTooltip(false);
		setTitle($m("UserSelectBean.0"));
		setMinWidth(360);
		setWidth(360);
		setHeight(445);
		setPageItems(100);
		try {
			setHandleClass(ClassUtils
					.forName("net.simpleframework.organization.web.component.userselect.DefaultUserSelectHandler"));
		} catch (final ClassNotFoundException e) {
		}
	}

	public int getPageItems() {
		return pageItems;
	}

	public UserSelectBean setPageItems(final int pageItems) {
		this.pageItems = pageItems;
		return this;
	}

	public boolean isTreeMode() {
		return treeMode;
	}

	public UserSelectBean setTreeMode(final boolean treeMode) {
		this.treeMode = treeMode;
		return this;
	}
}
