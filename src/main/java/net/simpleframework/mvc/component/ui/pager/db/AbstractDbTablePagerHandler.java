package net.simpleframework.mvc.component.ui.pager.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.ColumnData;
import net.simpleframework.ado.EFilterRelation;
import net.simpleframework.ado.FilterItem;
import net.simpleframework.ado.bean.IIdBeanAware;
import net.simpleframework.ado.bean.ITextBeanAware;
import net.simpleframework.ado.bean.ITreeBeanAware;
import net.simpleframework.ado.db.DbDataQuery;
import net.simpleframework.ado.db.DbDataQuery.ResultSetMetaDataCallback;
import net.simpleframework.ado.db.DbTableColumn;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.ado.db.common.SqlUtils;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.query.IteratorDataQuery;
import net.simpleframework.common.Convert;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LabelElement;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerHandler;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumns;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbTablePagerHandler extends AbstractTablePagerHandler implements
		IDbTablePagerHandler {

	@Override
	public AbstractTablePagerSchema createTablePagerSchema() {
		return new DefaultDbTablePagerSchema();
	}

	protected class DefaultDbTablePagerSchema extends DefaultTablePagerSchema {

		@Override
		public TablePagerColumns getTablePagerColumns(final ComponentParameter cp) {
			final TablePagerColumns columns = super.getTablePagerColumns(cp);
			IDataQuery<?> dQuery;
			if (columns.size() == 0
					&& (dQuery = getRawDataQuery(getDataObjectQuery(cp))) instanceof DbDataQuery) {
				((DbDataQuery<?>) dQuery).doResultSetMetaData(new ResultSetMetaDataCallback() {

					@Override
					public Object doResultSetMetaData(final ResultSetMetaData metaData)
							throws SQLException {
						for (int i = 1; i <= metaData.getColumnCount(); i++) {
							final String name = metaData.getColumnName(i);
							final String label = metaData.getColumnLabel(i);
							final TablePagerColumn col = new TablePagerColumn(name, label);
							col.setWidth(Math.max(label.length() * 12, 60));
							col.setTextAlign(ETextAlign.center);
							col.setFilter(true);
							columns.add(col);
						}
						return null;
					}
				});
			}
			return columns;
		}
	}

	@Override
	protected ColumnData createColumnData(final TablePagerColumn oCol) {
		return new DbTableColumn(oCol.getColumnName()).setAlias(oCol.getColumnAlias());
	}

	protected void doSortSQL(final ComponentParameter cp, final DbDataQuery<?> qs) {
		final DbTableColumn dbColumn = (DbTableColumn) getSortColumn(cp);
		if (dbColumn != null) {
			qs.addOrderBy(dbColumn);
		}
	}

	protected void doFilterSQL(final ComponentParameter cp, final DbDataQuery<?> qs) {
		final Map<String, ColumnData> filterColumns = getFilterColumns(cp);
		if (filterColumns != null && filterColumns.size() > 0) {
			final TablePagerColumns columns = getTablePagerSchema(cp).getTablePagerColumns(cp);
			for (final Map.Entry<String, ColumnData> entry : filterColumns.entrySet()) {
				final TablePagerColumn oCol = columns.get(entry.getKey());
				if (oCol == null) {
					continue;
				}
				final Iterator<FilterItem> it = entry.getValue().getFilterItems().iterator();
				if (!it.hasNext()) {
					continue;
				}
				final ExpressionValue ev = createFilterExpressionValue(qs, oCol, it);
				if (ev != null) {
					qs.addCondition(ev);
				}
			}
		}
	}

	protected ExpressionValue createFilterExpressionValue(final DbDataQuery<?> qs,
			final TablePagerColumn oCol, final Iterator<FilterItem> it) {
		final ArrayList<Object> params = new ArrayList<Object>();
		final StringBuilder sb = new StringBuilder();
		final String columnAlias = oCol.getColumnAlias();
		final FilterItem item = it.next();
		sb.append(columnAlias).append(filterItemExpr(item, params));
		if (it.hasNext()) {
			sb.append(" ").append(item.getOpe()).append(" ");
			sb.append(columnAlias).append(filterItemExpr(it.next(), params));
			sb.insert(0, "(");
			sb.append(")");
		}
		return new ExpressionValue(sb.toString(), params.toArray());
	}

	protected String filterItemExpr(final FilterItem item, final Collection<Object> params) {
		final StringBuilder sb = new StringBuilder();
		final EFilterRelation relation = item.getRelation();
		sb.append(" ").append(relation);
		if (relation == EFilterRelation.like) {
			sb.append(" '%").append(SqlUtils.sqlEscape(Convert.toString(item.getOriginalValue())))
					.append("%'");
		} else {
			sb.append(" ?");
			params.add(item.getValue());
		}
		return sb.toString();
	}

	protected IDataQuery<?> getRawDataQuery(final IDataQuery<?> dataQuery) {
		if (dataQuery instanceof IteratorDataQuery) {
			return ((IteratorDataQuery<?>) dataQuery).getRawDataQuery();
		}
		return dataQuery;
	}

	@Override
	protected void doCount(final ComponentParameter cp, IDataQuery<?> dataQuery) {
		dataQuery = getRawDataQuery(dataQuery);
		if (dataQuery instanceof DbDataQuery) {
			doFilterSQL(cp, (DbDataQuery<?>) dataQuery);
		}
		super.doCount(cp, dataQuery);
	}

	@Override
	protected List<?> getData(final ComponentParameter cp, final int start) {
		final IDataQuery<?> dataQuery = getRawDataQuery((IDataQuery<?>) cp.getRequestAttr(DATA_QUERY));
		if (dataQuery instanceof DbDataQuery) {
			doSortSQL(cp, (DbDataQuery<?>) dataQuery);
		}
		return super.getData(cp, start);
	}

	protected <T extends ITreeBeanAware> ElementList doNavigationTitle(final ComponentParameter cp,
			final T category, final NavigationTitleCallback<T> callback) {
		final ElementList eles = new ElementList();
		if (category != null) {
			final String componentTable = cp.getComponentName();
			eles.add(new LinkElement(callback.rootText()).setOnclick(callback.onclick(componentTable,
					"")));
			T category2 = category;
			final ArrayList<T> al = new ArrayList<T>();
			while (category2 != null) {
				al.add(0, category2);
				category2 = callback.get(category2.getParentId());
			}
			int size;
			for (int i = 0; i < (size = al.size()); i++) {
				category2 = al.get(i);
				if (callback.isLink(category2) && i < size - 1) {
					eles.add(new LinkElement(category2).setOnclick(callback.onclick(componentTable,
							((IIdBeanAware) category2).getId())));
				} else {
					eles.add(new LabelElement(callback.getText(category2)));
				}
			}
		} else {
			eles.add(new LabelElement(callback.rootText()));
		}
		return eles;
	}

	protected static abstract class NavigationTitleCallback<T> {

		protected abstract String rootText();

		protected abstract T get(Object id);

		protected String categoryIdKey() {
			return "categoryId";
		}

		protected String onclick(final String componentTable, final Object categoryId) {
			final StringBuilder sb = new StringBuilder();
			sb.append("$Actions['").append(componentTable).append("']('").append(categoryIdKey())
					.append("=").append(categoryId).append("');");
			return sb.toString();
		}

		protected boolean isLink(final T t) {
			return true;
		}

		protected String getText(final T t) {
			return t instanceof ITextBeanAware ? ((ITextBeanAware) t).getText() : t.toString();
		}
	}
}
