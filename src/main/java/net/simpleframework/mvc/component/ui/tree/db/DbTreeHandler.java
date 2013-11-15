package net.simpleframework.mvc.component.ui.tree.db;

import java.util.Map;

import net.simpleframework.ado.db.IDbEntityManager;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeHandler;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class DbTreeHandler extends AbstractTreeHandler {

	@Override
	public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode treeNode) {
		final TreeNodes nodes = TreeNodes.of();
		final DbTreeBean dbTree = (DbTreeBean) cp.componentBean;
		final DbTreeNode dbNode = (DbTreeNode) treeNode;
		final IDataQuery<Map<String, Object>> set = getDataQuery(cp, dbNode);
		Map<String, Object> rowData;
		while ((rowData = set.next()) != null) {
			nodes.add(new DbTreeNode(dbTree, dbNode, rowData));
		}
		return nodes;
	}

	protected IDataQuery<Map<String, Object>> getDataQuery(final ComponentParameter cp,
			final DbTreeNode dbNode) {
		final DbTreeBean dbTree = (DbTreeBean) cp.componentBean;
		final IDbEntityManager<?> tem = getEntityManager(dbTree);
		final String parentId = dbTree.getParentIdName();
		if (dbNode == null) {
			return tem.queryMapSet(new ExpressionValue(getRootExpressionValue(parentId)));
		} else {
			return tem.queryMapSet(new ExpressionValue(parentId + "=?", dbNode.getRowData().get(
					dbTree.getIdName())));
		}
	}

	protected abstract IDbEntityManager<?> getEntityManager(DbTreeBean dbTree);

	protected String getRootExpressionValue(final String parentId) {
		return parentId + "=0";
	}
}
