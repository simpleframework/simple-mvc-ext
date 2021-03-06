package net.simpleframework.mvc.component.ui.tree.db;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.ui.tree.TreeBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DbTreeBean extends TreeBean {
	private static final long serialVersionUID = -8627003520010996232L;

	private String tableName;

	private String idName, parentIdName;

	private String textName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public String getIdName() {
		return StringUtils.blank(idName).toUpperCase();
	}

	public void setIdName(final String idName) {
		this.idName = idName;
	}

	public String getParentIdName() {
		return StringUtils.blank(parentIdName).toUpperCase();
	}

	public void setParentIdName(final String parentIdName) {
		this.parentIdName = parentIdName;
	}

	public String getTextName() {
		return StringUtils.blank(textName).toUpperCase();
	}

	public void setTextName(final String textName) {
		this.textName = textName;
	}

	{
		setDynamicLoading(true);
		setHandlerClass(DbTreeHandler.class);
	}
}
