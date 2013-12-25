package net.simpleframework.mvc.component.ext.category;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CategoryBean extends AbstractContainerBean {

	private boolean dynamicTree = BeanDefaults.getBool(getClass(), "dynamicTree", false);

	private boolean draggable = BeanDefaults.getBool(getClass(), "draggable", true);

	private boolean showContextMenu = BeanDefaults.getBool(getClass(), "showContextMenu", true);

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

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}
}
