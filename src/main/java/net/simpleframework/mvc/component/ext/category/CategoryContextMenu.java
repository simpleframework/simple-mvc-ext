package net.simpleframework.mvc.component.ext.category;

import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.AbstractMenuHandler;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CategoryContextMenu extends AbstractMenuHandler {

	@Override
	public MenuItems getMenuItems(final ComponentParameter cp, final MenuItem menuItem) {
		final ComponentParameter nCP = CategoryUtils.get(cp);
		return ((ComponentHandlerEx) nCP.getComponentHandler()).getContextMenu(nCP,
				(MenuBean) cp.componentBean, menuItem);
	}
}
