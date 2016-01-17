package net.simpleframework.mvc.component.ext.userselect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.query.IteratorDataQuery;
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
public abstract class AbstractUserSelectHandler extends AbstractDictionaryHandler implements
		IUserSelectHandler {
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
	public List<PermissionDept> getDepartmentChildren(final ComponentParameter cp,
			final PermissionDept dept) {
		if (dept == null) {
			return cp.getPermission().getRootChildren();
		} else {
			return dept.getChildren();
		}
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

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		final KVMap kv = new KVMap();
		final PermissionDept org = AbstractMVCPage.getPermissionOrg(cp);
		if (org.exists()) {
			kv.put("orgId", org.getId());
		}
		return kv;
	}
}