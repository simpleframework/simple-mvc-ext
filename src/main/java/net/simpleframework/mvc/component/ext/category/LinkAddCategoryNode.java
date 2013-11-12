package net.simpleframework.mvc.component.ext.category;

import net.simpleframework.mvc.component.ui.tree.LinkAddTreeNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LinkAddCategoryNode extends LinkAddTreeNode {

	public LinkAddCategoryNode() {
		super("$category_action(this).add();");
	}
}
