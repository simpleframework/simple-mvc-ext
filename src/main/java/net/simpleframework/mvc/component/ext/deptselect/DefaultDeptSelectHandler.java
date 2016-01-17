package net.simpleframework.mvc.component.ext.deptselect;

import static net.simpleframework.common.I18n.$m;

import java.util.Arrays;
import java.util.List;

import net.simpleframework.common.Convert;
import net.simpleframework.common.coll.CollectionUtils;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.dictionary.AbstractDictionaryHandler;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean.DictionaryTreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultDeptSelectHandler extends AbstractDictionaryHandler implements
		IDeptSelectHandle {

	@Override
	public List<PermissionDept> getDepartments(final ComponentParameter cp, final TreeBean treeBean,
			final PermissionDept parent) {
		// 仅显示机构
		final boolean borg = (Boolean) cp.getBeanProperty("org");
		if (borg) {
			if (parent == null) {
				return cp.getPermission().getRootChildren();
			} else {
				return parent.getOrgChildren();
			}
		} else {
			if (cp.isLmanager()) {
				if (parent == null) {
					return cp.getPermission().getRootChildren();
				} else {
					return parent.getAllChildren();
				}
			} else {
				final PermissionDept org = AbstractMVCPage.getPermissionOrg(cp);
				if (org.exists()) {
					// 单机构
					if (parent == null) {
						return Arrays.asList(org);
					} else {
						return parent.getDeptChildren();
					}
				}
			}
		}
		return CollectionUtils.EMPTY_LIST();
	}

	@Override
	protected ElementList getRightElements(final ComponentParameter cp) {
		if (Convert.toBool(cp.getBeanProperty("multiple"))) {
			final String componentName = ((DictionaryTreeBean) ((DeptSelectBean) cp.componentBean)
					.getDictionaryTypeBean()).getRef();
			return ElementList.of(new Checkbox("idDefaultDeptSelectHandler_cb",
					$m("DefaultDeptSelectHandler.0")).setOnclick("$Actions['" + componentName
					+ "'].checkAll(this.checked);"));
		}
		return null;
	}
}
