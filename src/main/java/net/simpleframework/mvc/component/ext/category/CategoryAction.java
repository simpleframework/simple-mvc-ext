package net.simpleframework.mvc.component.ext.category;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.Convert;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
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
		final ComponentParameter tComponentParameter = ComponentParameter.get(cp, treeBean);
		final TreeNode node1 = TreeUtils.getTreenodeById(tComponentParameter,
				tComponentParameter.getParameter("b1"));
		final TreeNode node2 = TreeUtils.getTreenodeById(tComponentParameter,
				tComponentParameter.getParameter("b2"));
		if (node1 == null || node2 == null) {
			return new JavascriptForward("alert('").append($m("CategoryAction.0")).append("');");
		}

		return ((ICategoryHandler) nCP.getComponentHandler()).onCategoryMove(nCP, treeBean,
				node1.getDataObject(), node2.getDataObject(), Convert.toBool(nCP.getParameter("up")));
	}
}
