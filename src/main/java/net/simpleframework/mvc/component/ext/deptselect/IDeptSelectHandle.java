package net.simpleframework.mvc.component.ext.deptselect;

import java.util.List;

import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;
import net.simpleframework.mvc.component.ui.tree.TreeBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IDeptSelectHandle extends IDictionaryHandle {

	/**
	 * 获取机构树数据
	 * 
	 * @param cp
	 * @param treeBean
	 * @param parent
	 * @return
	 */
	List<PermissionDept> getDepartments(ComponentParameter cp, TreeBean treeBean,
			PermissionDept parent);
}
