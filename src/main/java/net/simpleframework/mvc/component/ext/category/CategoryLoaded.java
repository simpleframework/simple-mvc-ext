package net.simpleframework.mvc.component.ext.category;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.CategoryUtils.CategoryTree;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.tree.TreeBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CategoryLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		final ComponentParameter cp = CategoryUtils.get(pp);

		final CategoryBean categoryBean = (CategoryBean) cp.componentBean;
		final String categoryName = cp.getComponentName();

		final TreeBean treeBean = pp.addComponentBean(categoryName + "_tree", TreeBean.class);
		if ((Boolean) cp.getBeanProperty("showContextMenu")) {
			treeBean.setContextMenu(categoryName + "_contextMenu");

			pp.addComponentBean(categoryName + "_contextMenu", MenuBean.class).setHandleClass(
					CategoryContextMenu.class);
		}

		treeBean.setDynamicLoading((Boolean) cp.getBeanProperty("dynamicTree"))
				.setCookies((Boolean) cp.getBeanProperty("cookies"))
				.setImgHome((String) cp.getBeanProperty("imgHome"))
				.setJsLoadedCallback((String) cp.getBeanProperty("jsLoadedCallback"))
				.setContainerId("category_" + categoryBean.hashId()).setHandleClass(CategoryTree.class)
				.setRunImmediately(false).setAttr("$category", categoryBean);
		categoryBean.setAttr("$tree", treeBean);
	}
}
