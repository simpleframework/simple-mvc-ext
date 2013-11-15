package net.simpleframework.mvc.component.ui.tree.db;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ui.tree.AbstractTreeRegistry;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(DbTreeRegistry.DBTREE)
@ComponentBean(DbTreeBean.class)
public class DbTreeRegistry extends AbstractTreeRegistry {
	public static final String DBTREE = "dbTree";

	@Override
	protected TreeNode createTreeNode(final XmlElement xmlElement, final TreeBean treeBean,
			final TreeNode parent) {
		return null;
	}
}
