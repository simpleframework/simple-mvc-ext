package net.simpleframework.mvc.component.ext.category;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryTreeHandler;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class CategoryUtils {
	public static final String BEAN_ID = "category_@bid";

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static class CategoryTree extends AbstractTreeHandler {
		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$category");
			return ComponentUtils.toFormParameters(nCP);
		}

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode treeNode) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$category");
			return ((ICategoryHandler) nCP.getComponentHandler()).getCategoryTreenodes(nCP,
					(TreeBean) cp.componentBean, treeNode);
		}

		@Override
		public boolean doDragDrop(final ComponentParameter cp, final TreeNode drag,
				final TreeNode drop) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$category");
			return ((ICategoryHandler) nCP.getComponentHandler()).onCategoryDragDrop(nCP,
					(TreeBean) cp.componentBean, drag.getDataObject(), drop.getDataObject());
		}
	}

	public static class CategoryDictTree extends DictionaryTreeHandler {
		@Override
		public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode treeNode) {
			final ComponentParameter nCP = CategoryUtils.get(cp);
			return ((ICategoryHandler) nCP.getComponentHandler()).getCategoryDictTreenodes(nCP,
					(TreeBean) cp.componentBean, treeNode);
		}
	}
}
