package net.simpleframework.mvc.component.ext.userselect;

import static net.simpleframework.common.I18n.$m;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.ado.FilterItem;
import net.simpleframework.ado.db.DbDataQuery;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.PermissionDept;
import net.simpleframework.ctx.permission.PermissionUser;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.SupElement;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ext.userselect.IUserSelectHandler.DeptMemory;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryTreeHandler;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.GroupWrapper;
import net.simpleframework.mvc.component.ui.pager.IGroupTablePagerHandler;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class UserSelectLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		final ComponentParameter cp = UserSelectUtils.get(pp);
		final UserSelectBean userSelect = (UserSelectBean) cp.componentBean;

		final String userSelectName = cp.getComponentName();

		String vtype = pp.getParameter("vtype");
		if (vtype == null) {
			vtype = (String) cp.getBeanProperty("vtype");
		}

		final String containerId = "users_" + userSelect.hashId();
		if (UserSelectBean.VT_TREE.equals(vtype)) {
			pp.addComponentBean(userSelectName + "_tree", TreeBean.class).setCookies(false)
					.setContainerId(containerId).setHandlerClass(UserTree.class)
					.setAttr("userSelect", userSelect);
		} else {
			final TablePagerBean tablePager = (TablePagerBean) pp
					.addComponentBean(userSelectName + "_tablePager", TablePagerBean.class)
					.setJsRowDblclick("$Actions['" + userSelectName + "'].doDblclick(item);")
					.setShowHead(false).setPagerBarLayout(EPagerBarLayout.top)
					.setShowEditPageItems(false).setExportAction("false").setIndexPages(4)
					.setContainerId(containerId).setHandlerClass(UserList.class)
					.setAttr("userSelect", userSelect);
			final boolean multiple = (Boolean) cp.getBeanProperty("multiple");
			if (!multiple) {
				tablePager.setJsRowDblclick("$Actions['" + userSelectName + "'].doDblclick(item);");
				tablePager.setJsRowClick(
						"item.up('.tablepager').select('.titem.titem_selected').invoke('removeClassName', 'titem_selected');"
								+ "item.addClassName('titem_selected');");
			} else {
				tablePager.setJsRowClick("item.down('input[type=checkbox]').simulate('click');");
			}

			final TablePagerColumn txtColumn = new TablePagerColumn("text", $m("UserSelectLoaded.0"));
			if (UserSelectBean.VT_GROUP.equals(vtype)) {
				tablePager.setGroupColumn("departmentId");
				tablePager.addColumn(txtColumn);
			} else {
				tablePager.addColumn(txtColumn.setWidth(110));
				tablePager.addColumn(new TablePagerColumn("departmentText", $m("UserSelectLoaded.1"))
						.setFilterSort(false));
			}
		}
	}

	public static class UserList extends AbstractDbTablePagerHandler
			implements IGroupTablePagerHandler {

		@Override
		protected ExpressionValue createFilterExpressionValue(final DbDataQuery<?> qs,
				final TablePagerColumn oCol, final Collection<FilterItem> coll) {
			final String col = oCol.getColumnName();
			if ("text".equals(col)) {
				final ExpressionValue ev = super.createFilterExpressionValue(qs, oCol, coll);
				final ExpressionValue ev2 = super.createFilterExpressionValue(qs,
						new TablePagerColumn("py"), coll);
				ev.setExpression("((" + ev.getExpression() + ") or (" + ev2.getExpression() + "))");
				ev.addValues(ev2.getValues());
				return ev;
			}
			return super.createFilterExpressionValue(qs, oCol, coll);
		}

		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("pageItems".equals(beanProperty)) {
				return UserSelectUtils.get(cp).getBeanProperty(beanProperty);
			} else if ("showCheckbox".equals(beanProperty)) {
				return UserSelectUtils.get(cp).getBeanProperty("multiple");
			} else if ("title".equals(beanProperty)) {
				return UserSelectUtils.toTypeHTML(UserSelectUtils.get(cp));
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cp) {
			final KVMap kv = (KVMap) super.getFormParameters(cp);
			kv.putAll(ComponentUtils.toFormParameters(UserSelectUtils.get(cp)));
			return kv.add(UserSelectUtils.BEAN_ID, cp.getParameter(UserSelectUtils.BEAN_ID))
					.add("vtype", cp.getParameter("vtype"));
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final ComponentParameter nCP = UserSelectUtils.get(cp);
			return ((IUserSelectHandler) nCP.getComponentHandler()).getUsers(nCP);
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {

			return new DefaultDbTablePagerSchema() {
				@Override
				public Map<String, Object> getRowData(final ComponentParameter cp,
						final Object dataObject) {
					final PermissionUser user = (PermissionUser) dataObject;
					final KVMap kv = new KVMap();
					kv.put("text", new SpanElement(dataObject)
							.setTitle((String) BeanUtils.getProperty(dataObject, "email")));
					final PermissionDept dept = user.getDept();
					if (dept.exists()) {
						kv.put("departmentText", dept);
					}
					kv.put("checkVal", user.getAttr("checkVal"));
					return kv;
				}

				@Override
				public Map<String, Object> getRowAttributes(final ComponentParameter cp,
						final Object dataObject) {
					final Map<String, Object> attributes = super.getRowAttributes(cp, dataObject);
					final ComponentParameter nCP = ComponentParameter.get(cp,
							(AbstractComponentBean) cp.componentBean.getAttr("userSelect"));
					final Map<String, Object> attri = ((IUserSelectHandler) nCP.getComponentHandler())
							.getUserAttributes(nCP, (PermissionUser) dataObject);
					if (attri != null) {
						attributes.putAll(attri);
					}
					return attributes;
				}
			};
		}

		@Override
		protected AbstractElement<?> createHeadStatElement(final int count, final String pTitle) {
			return new SpanElement("(" + count + ")");
		}

		@Override
		public Collection<Object> doGroups(final ComponentParameter cp,
				final Map<Object, GroupWrapper> groups) {
			final ComponentParameter nCP = UserSelectUtils.get(cp);
			return ((IUserSelectHandler) nCP.getComponentHandler()).doSort(nCP, groups.keySet());
		}

		@Override
		public Object getGroupValue(final ComponentParameter cp, final Object bean,
				final String groupColumn) {
			final PermissionDept dept = ((PermissionUser) bean).getDept();
			return dept.exists() ? dept : null;
		}
	}

	private static final String REQUEST_DEPARTMENTS = "UserSelectLoaded_departments";

	public static class UserTree extends DictionaryTreeHandler {

		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("checkboxes".equals(beanProperty)) {
				return UserSelectUtils.get(cp).getBeanProperty("multiple");
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode parent) {
			final ComponentParameter nCP = UserSelectUtils.get(cp);
			final IUserSelectHandler uHandler = (IUserSelectHandler) nCP.getComponentHandler();
			final Collection<DeptMemory> wrappers = nCP.getRequestCache(REQUEST_DEPARTMENTS,
					new CacheV<Collection<DeptMemory>>() {
						@Override
						public Collection<DeptMemory> get() {
							return uHandler.getDepartmentList(nCP);
						}
					});

			final String userSelectName = nCP.getComponentName();
			final TreeBean treeBean = (TreeBean) cp.componentBean;
			final TreeNodes nodes = TreeNodes.of();
			if (parent == null) {
				for (final DeptMemory wrapper : wrappers) {
					if (!wrapper.hasUser()) {
						continue;
					}
					final TreeNode tn = new TreeNode(treeBean, wrapper);
					tn.setPostfixText(new SupElement(wrapper.getUsers().size()).toString());
					tn.setOpened(true);
					tn.setCheckbox(false);
					nodes.add(tn);
				}
			} else {
				final Object data = parent.getDataObject();
				final String imgPath = ComponentUtils.getCssResourceHomePath(cp, UserSelectBean.class)
						+ "/images/";
				if (data instanceof DeptMemory) {
					final DeptMemory wrapper = (DeptMemory) data;
					for (final PermissionUser user : wrapper.getUsers()) {
						final TreeNode tn = new TreeNode(treeBean, user);
						tn.setImage(imgPath + "users.png");
						tn.setJsDblclickCallback(
								"$Actions['" + userSelectName + "'].doDblclick(branch);");
						nodes.add(tn);
					}
					for (final DeptMemory w2 : wrapper.getChildren()) {
						if (!w2.hasUser()) {
							continue;
						}
						final TreeNode tn = new TreeNode(treeBean, w2);
						tn.setPostfixText(new SupElement(w2.getUsers().size()).toString());
						tn.setCheckbox(false);
						nodes.add(tn);
					}
				}
			}
			return nodes;
		}

		@Override
		public Map<String, Object> getTreenodeAttributes(final ComponentParameter cp,
				final TreeNode treeNode, final TreeNodes children) {
			final Map<String, Object> attributes = super.getTreenodeAttributes(cp, treeNode, children);
			Object dataObject;
			if (treeNode != null
					&& (dataObject = treeNode.getDataObject()) instanceof PermissionUser) {
				final ComponentParameter nCP = ComponentParameter.get(cp,
						(AbstractComponentBean) cp.componentBean.getAttr("userSelect"));
				final Map<String, Object> attri = ((IUserSelectHandler) nCP.getComponentHandler())
						.getUserAttributes(nCP, (PermissionUser) dataObject);
				if (attri != null) {
					attributes.putAll(attri);
				}
			}
			return attributes;
		}
	}
}
