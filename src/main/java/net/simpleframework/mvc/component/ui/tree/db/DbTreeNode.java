package net.simpleframework.mvc.component.ui.tree.db;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DbTreeNode extends TreeNode {

	public DbTreeNode(final TreeBean treeBean, final DbTreeNode parent,
			final Map<String, Object> rowData) {
		super(treeBean, parent, rowData);
	}

	public KVMap getRowData() {
		return (KVMap) getDataObject();
	}

	@Override
	public DbTreeBean getTreeBean() {
		return (DbTreeBean) super.getTreeBean();
	}

	@Override
	public String getId() {
		return Convert.toString(getRowData().get(getTreeBean().getIdName()));
	}

	@Override
	public String getText() {
		return Convert.toString(getRowData().get(getTreeBean().getTextName()));
	}
}
