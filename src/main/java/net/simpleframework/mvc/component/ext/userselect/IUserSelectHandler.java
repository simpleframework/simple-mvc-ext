package net.simpleframework.mvc.component.ext.userselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.IDictionaryHandle;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IUserSelectHandler extends IDictionaryHandle {

	/**
	 * 获取用户列表，缺省实现获取所有用户
	 * 
	 * @param cp
	 * @return
	 */
	IDataQuery<PermissionUser> getUsers(ComponentParameter cp);

	/**
	 * 对分组排序
	 * 
	 * @param cp
	 * @param departments
	 * @return
	 */
	List<Object> doSort(ComponentParameter cp, Set<Object> groups);

	/**
	 * 获取部门用户
	 * 
	 * 该方法的设计,是基于内存的,不适合稍大点的数据
	 * 
	 * @param cp
	 * @return
	 */
	Collection<DeptMemory> getDepartmentList(ComponentParameter cp);

	Map<String, Object> getUserAttributes(ComponentParameter cp, PermissionUser user);

	public static class DeptMemory {

		private List<PermissionUser> users;

		private List<DeptMemory> children;

		private final PermissionDept dept;

		public DeptMemory(final PermissionDept dept) {
			this.dept = dept;
		}

		public PermissionDept getDept() {
			return dept;
		}

		public List<DeptMemory> getChildren() {
			if (children == null) {
				children = new ArrayList<DeptMemory>();
			}
			return children;
		}

		public List<PermissionUser> getUsers() {
			if (users == null) {
				users = new ArrayList<PermissionUser>();
			}
			return users;
		}

		public boolean hasUser() {
			if (getUsers().size() > 0) {
				return true;
			}
			for (final DeptMemory wrapper : getChildren()) {
				if (wrapper.hasUser()) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return dept.toString();
		}
	}
}
