package net.simpleframework.mvc.component.ext.userselect;

import java.util.Collection;
import java.util.Set;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.permission.DepartmentWrapper;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IUserSelectHandler extends IDictionaryHandle {

	/**
	 * 获取用户列表，缺省实现获取所有用户
	 * 
	 * @param cp
	 * @return
	 */
	IDataQuery<?> getUsers(ComponentParameter cp);

	/**
	 * 对分组排序
	 * 
	 * @param cp
	 * @param departments
	 * @return
	 */
	Collection<Object> doSort(ComponentParameter cp, Set<Object> groups);

	/**
	 * 获取部门对象
	 * 
	 * @param key
	 * @return
	 */
	Object getDepartment(Object key);

	/**
	 * 获取部门用户
	 * 
	 * 该方法的设计,是基于内存的,不适合稍大点的数据
	 * 
	 * @param cp
	 * @return
	 */
	Collection<DepartmentWrapper> getDepartmentWrappers(ComponentParameter cp);
}
