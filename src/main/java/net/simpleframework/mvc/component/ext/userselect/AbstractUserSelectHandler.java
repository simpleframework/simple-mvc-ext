package net.simpleframework.mvc.component.ext.userselect;

import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.PermissionDept;
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
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		final KVMap kv = new KVMap();
		final PermissionDept org = AbstractMVCPage.getPermissionOrg(cp);
		if (org.exists()) {
			kv.put("orgId", org.getId());
		}
		return kv;
	}
}