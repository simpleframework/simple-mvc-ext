package net.simpleframework.mvc.component.ext.category;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class CategoryAction extends DefaultAjaxRequestHandler {

	public IForward editUrl(final ComponentParameter cp) {
		final ComponentParameter nCP = CategoryUtils.get(cp);
		return new UrlForward((String) ((ICategoryHandler) nCP.getComponentHandler())
				.categoryEdit_attri(nCP).get(ICategoryHandler.edit_url));
	}

	public IForward doSave(final ComponentParameter cp) {
		final ComponentParameter nCP = CategoryUtils.get(cp);
		return ((ICategoryHandler) nCP.getComponentHandler()).categoryEdit_onSave(nCP);
	}

	public IForward doDelete(final ComponentParameter cp) {
		final ComponentParameter nCP = CategoryUtils.get(cp);
		final TreeBean treeBean = (TreeBean) nCP.componentBean.getAttr("$tree");
		return ((ICategoryHandler) nCP.getComponentHandler()).onCategoryDelete(nCP, treeBean);
	}

	public IForward doMove(final ComponentParameter cp) {
		final ComponentParameter nCP = CategoryUtils.get(cp);

		final TreeBean treeBean = (TreeBean) nCP.componentBean.getAttr("$tree");
		final ComponentParameter tnCP = ComponentParameter.get(cp, treeBean);

		final List<Object> list = new ArrayList<>();
		for (final String id : StringUtils.split(tnCP.getParameter("nodeIds"))) {
			final TreeNode node = TreeUtils.getTreenodeById(tnCP, id);
			list.add(node.getDataObject());
		}

		return ((ICategoryHandler) nCP.getComponentHandler()).onCategoryMove(nCP, treeBean,
				list.toArray());
	}
}
