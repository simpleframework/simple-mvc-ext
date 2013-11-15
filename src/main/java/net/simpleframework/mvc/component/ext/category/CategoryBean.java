package net.simpleframework.mvc.component.ext.category;

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
public class CategoryBean extends AbstractContainerBean {
	private boolean cookies = BeanDefaults.getBool(getClass(), "cookies", true);

	private String imgHome;

	private boolean draggable = BeanDefaults.getBool(getClass(), "draggable", true);

	private boolean dynamicTree = BeanDefaults.getBool(getClass(), "dynamicTree", false);

	private boolean showContextMenu = BeanDefaults.getBool(getClass(), "showContextMenu", true);

	private String jsLoadedCallback;

	public CategoryBean(final PageDocument pageDocument, final XmlElement xmlElement) {
		super(pageDocument, xmlElement);
	}

	public boolean isCookies() {
		return cookies;
	}

	public CategoryBean setCookies(final boolean cookies) {
		this.cookies = cookies;
		return this;
	}

	public String getImgHome() {
		return imgHome;
	}

	public CategoryBean setImgHome(final String imgHome) {
		this.imgHome = imgHome;
		return this;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public CategoryBean setDraggable(final boolean draggable) {
		this.draggable = draggable;
		return this;
	}

	public boolean isShowContextMenu() {
		return showContextMenu;
	}

	public CategoryBean setShowContextMenu(final boolean showContextMenu) {
		this.showContextMenu = showContextMenu;
		return this;
	}

	public boolean isDynamicTree() {
		return dynamicTree;
	}

	public CategoryBean setDynamicTree(final boolean dynamicTree) {
		this.dynamicTree = dynamicTree;
		return this;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public CategoryBean setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}
}
