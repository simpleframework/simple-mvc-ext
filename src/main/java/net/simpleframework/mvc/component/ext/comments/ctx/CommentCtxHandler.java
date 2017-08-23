package net.simpleframework.mvc.component.ext.comments.ctx;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.FilterItems;
import net.simpleframework.ado.bean.IIdBeanAware;
import net.simpleframework.ado.query.DataQueryUtils;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.comments.AbstractCommentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class CommentCtxHandler<T extends IIdBeanAware> extends AbstractCommentHandler {

	protected abstract IDbBeanService<T> getBeanService();

	@Override
	public Object getCommentById(final ComponentParameter cp, final Object id) {
		return getBeanService().getBean(id);
	}

	@Override
	public IDataQuery<?> children(final ComponentParameter cp, final Object id) {
		if (id == null) {
			return DataQueryUtils.nullQuery();
		}
		return getBeanService().queryByParams(FilterItems.of("parentId", id),
				ColumnData.DESC("createdate"));
	}

	@Override
	public JavascriptForward deleteComment(final ComponentParameter cp, final Object id) {
		getBeanService().delete(id);
		return super.deleteComment(cp, id);
	}
}
