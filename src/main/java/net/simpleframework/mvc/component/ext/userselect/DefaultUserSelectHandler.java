package net.simpleframework.mvc.component.ext.userselect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.query.IteratorDataQuery;
import net.simpleframework.common.ID;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultUserSelectHandler extends AbstractDictionaryHandler implements
		IUserSelectHandler {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		final KVMap kv = new KVMap();
		final PermissionDept org = AbstractMVCPage.getPermissionOrg(cp);
		if (org.exists()) {
			kv.put("orgId", org.getId());
		}
		return kv;
	}

	@Override
	public IDataQuery<PermissionUser> getUsers(final ComponentParameter cp) {
		if (cp.isLmanager()) {
			return new IteratorDataQuery<PermissionUser>(cp.getPermission().allUsers());
		} else {
			final PermissionDept org = AbstractMVCPage.getPermissionOrg(cp);
			if (org != null) {
				return new IteratorDataQuery<PermissionUser>(org.orgUsers());
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> getUserAttributes(final ComponentParameter cp,
			final PermissionUser user) {
		return new KVMap().add("userText", user.getText());
	}

	@Override
	public List<Object> doSort(final ComponentParameter cp, final Set<Object> groups) {
		final ArrayList<Object> _groups = new ArrayList<Object>(groups);
		Collections.sort(_groups, new Comparator<Object>() {
			@Override
			public int compare(final Object o1, final Object o2) {
				if (!(o1 instanceof PermissionDept)) {
					return 1;
				}
				if (!(o2 instanceof PermissionDept)) {
					return -1;
				}
				final PermissionDept d1 = (PermissionDept) o1;
				final PermissionDept d2 = (PermissionDept) o2;
				final int l1 = d1.getLevel();
				final int l2 = d2.getLevel();
				if (l1 == l2) {
					return d1.getOorder() - d2.getOorder();
				} else {
					return l1 - l2;
				}
			}
		});
		return _groups;
	}

	private Map<ID, List<PermissionDept>> dTreemap;

	private void loadDeptTreemap(final PermissionDept parent) {
		final List<PermissionDept> list = parent.getAllChildren();
		dTreemap.put(parent.getId(), new ArrayList<PermissionDept>(list));
		for (final PermissionDept dept : list) {
			loadDeptTreemap(dept);
		}
	}

	@Override
	public List<DeptMemory> getDepartmentList(final ComponentParameter cp) {
		if (dTreemap == null) {
			dTreemap = new HashMap<ID, List<PermissionDept>>();
			final List<PermissionDept> list = cp.getPermission().getRootChildren();
			dTreemap.put(ID.NULL_ID, new ArrayList<PermissionDept>(list));
			for (final PermissionDept dept : list) {
				loadDeptTreemap(dept);
			}
		}

		final Map<ID, List<PermissionUser>> users = new HashMap<ID, List<PermissionUser>>();
		final IDataQuery<PermissionUser> dq = getUsers(cp);
		if (dq != null) {
			dq.setFetchSize(0);
			PermissionUser user;
			while ((user = dq.next()) != null) {
				final PermissionDept dept = user.getDept();
				final ID deptId = dept.exists() ? dept.getId() : ID.NULL_ID;
				List<PermissionUser> l = users.get(deptId);
				if (l == null) {
					users.put(deptId, l = new ArrayList<PermissionUser>());
				}
				l.add(user);
			}
		}
		return createDeptMemory(users, dTreemap.get(ID.NULL_ID));
	}

	private List<DeptMemory> createDeptMemory(final Map<ID, List<PermissionUser>> users,
			final List<PermissionDept> children) {
		final List<DeptMemory> wrappers = new ArrayList<DeptMemory>();
		if (children != null) {
			for (final PermissionDept dept : children) {
				final DeptMemory wrapper = new DeptMemory(dept);
				final ID deptId = dept.getId();
				final List<PermissionDept> v1 = dTreemap.get(deptId);
				final List<PermissionUser> v2 = users.get(deptId);
				if (v1 != null) {
					wrapper.getChildren().addAll(createDeptMemory(users, v1));
				}
				if (v2 != null) {
					wrapper.getUsers().addAll(v2);
				}
				wrappers.add(wrapper);
			}
		}
		return wrappers;
	}
}