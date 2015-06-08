package net.simpleframework.mvc.component.ext.userselect;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.ClassUtils;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class UserSelectBean extends DictionaryBean {
	public static final String VT_GROUP = "group";
	public static final String VT_LIST = "list";
	public static final String VT_TREE = "tree";

	/* 每页的数据数量 */
	private int pageItems;

	/* 是否显示分组模式 */
	private boolean showGroupOpt = true;
	/* 是否显示树模式 */
	private boolean showTreeOpt = true;

	/* 缺省模式 */
	private String vtype = VT_LIST;

	public UserSelectBean() {
		setShowHelpTooltip(false);
		setTitle($m("UserSelectBean.0"));
		setMinWidth(360);
		setWidth(360);
		setHeight(445);
		setPageItems(100);
		try {
			setHandlerClass(ClassUtils
					.forName("net.simpleframework.organization.web.component.userselect.DefaultUserSelectHandler"));
		} catch (final ClassNotFoundException e) {
		}
	}

	public int getPageItems() {
		return pageItems;
	}

	public UserSelectBean setPageItems(final int pageItems) {
		this.pageItems = pageItems;
		return this;
	}

	public String getVtype() {
		return (!showGroupOpt && !showTreeOpt) ? VT_LIST : vtype;
	}

	public UserSelectBean setVtype(final String vtype) {
		this.vtype = vtype;
		return this;
	}

	public boolean isShowGroupOpt() {
		return showGroupOpt;
	}

	public UserSelectBean setShowGroupOpt(final boolean showGroupOpt) {
		this.showGroupOpt = showGroupOpt;
		return this;
	}

	public boolean isShowTreeOpt() {
		return showTreeOpt;
	}

	public UserSelectBean setShowTreeOpt(final boolean showTreeOpt) {
		this.showTreeOpt = showTreeOpt;
		return this;
	}
}
