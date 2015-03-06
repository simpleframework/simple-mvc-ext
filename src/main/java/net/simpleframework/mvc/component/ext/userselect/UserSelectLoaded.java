package net.simpleframework.mvc.component.ext.userselect;

import static net.simpleframework.common.I18n.$m;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.BeanUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.common.element.SupElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
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
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class UserSelectLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		final ComponentParameter cp = UserSelectUtils.get(pp);
		final UserSelectBean userSelect = (UserSelectBean) cp.componentBean;

		final String userSelectName = cp.getComponentName();

		String vtype = pp.getParameter(UserSelectUtils.VTYPE);
		if (vtype == null) {
			vtype = UserSelectUtils.VT_GROUP;
		}

		final String containerId = "users_" + userSelect.hashId();
		if (UserSelectUtils.VT_TREE.equals(vtype)) {
			pp.addComponentBean(userSelectName + "_tree", TreeBean.class).setCookies(false)
					.setContainerId(containerId).setHandlerClass(UserTree.class);
		} else {
			final TablePagerBean tablePager = (TablePagerBean) pp
					.addComponentBean(userSelectName + "_tablePager", TablePagerBean.class)
					.setJsRowDblclick("$Actions['" + userSelectName + "'].doDblclick(item);")
					.setShowHead(false).setPagerBarLayout(EPagerBarLayout.top)
					.setShowEditPageItems(false).setExportAction("false").setIndexPages(4)
					.setContainerId(containerId).setHandlerClass(UserList.class);
			final boolean multiple = (Boolean) cp.getBeanProperty("multiple");
			if (!multiple) {
				tablePager.setJsRowDblclick("$Actions['" + userSelectName + "'].doDblclick(item);");
			}
			tablePager.addColumn(new TablePagerColumn("text", $m("UserSelectLoaded.0")).setTextAlign(
					ETextAlign.left).setWidth(135));
			if (UserSelectUtils.VT_GROUP.equals(vtype)) {
				tablePager.setGroupColumn("departmentId");
			} else {
				tablePager.addColumn(new TablePagerColumn("departmentText", $m("UserSelectLoaded.1"))
						.setFilterSort(false).setTextAlign(ETextAlign.left));
			}
		}
	}

	public static class UserList extends AbstractDbTablePagerHandler implements
			IGroupTablePagerHandler {

		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("pageItems".equals(beanProperty)) {
				return UserSelectUtils.get(cp).getBeanProperty(beanProperty);
			} else if ("showCheckbox".equals(beanProperty)) {
				return UserSelectUtils.get(cp).getBeanProperty("multiple");
			} else if ("title".equals(beanProperty)) {
				final String vtype = cp.getParameter(UserSelectUtils.VTYPE);
				final ComponentParameter nCP = UserSelectUtils.get(cp);
				String typeHTML = UserSelectUtils.toTypeHTML(nCP);
				final boolean multiple = (Boolean) UserSelectUtils.get(cp).getBeanProperty("multiple");
				if (multiple) {
					final String name = nCP.getComponentName();
					typeHTML += new BlockElement()
							.setClassName("check_all")
							.addStyle(UserSelectUtils.VT_LIST.equals(vtype) ? "left: 8px;" : "left: 21px;")
							.addElements(InputElement.checkbox(name + "_check_all"));
				}
				return typeHTML;
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cp) {
			final KVMap kv = (KVMap) super.getFormParameters(cp);
			kv.putAll(ComponentUtils.toFormParameters(UserSelectUtils.get(cp)));
			return kv.add(UserSelectUtils.BEAN_ID, cp.getParameter(UserSelectUtils.BEAN_ID)).add(
					UserSelectUtils.VTYPE, cp.getParameter(UserSelectUtils.VTYPE));
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
					final ComponentParameter nCP = UserSelectUtils.get(cp);
					final IUserSelectHandler uHandler = (IUserSelectHandler) nCP.getComponentHandler();
					final KVMap kv = new KVMap();
					kv.put("text", new SpanElement(dataObject).setTitle((String) BeanUtils.getProperty(
							dataObject, "email")));
					final Object dept = uHandler.getDepartment(dataObject);
					if (dept != null) {
						kv.put("departmentText", dept);
					}
					return kv;
				}

				@Override
				public Map<String, Object> getRowAttributes(final ComponentParameter compp,
						final Object dataObject) {
					final Map<String, Object> attributes = super.getRowAttributes(compp, dataObject);
					attributes.put("userText", dataObject.toString());
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
			final Object groupVal = super.getGroupValue(cp, bean, groupColumn);
			final ComponentParameter nCP = UserSelectUtils.get(cp);
			return ((IUserSelectHandler) nCP.getComponentHandler()).getDepartment(groupVal);
		}
	}

	private static final String REQUEST_WRAPPERS = "UserSelectLoaded_wrappers";

	public static class UserTree extends DictionaryTreeHandler {

		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if ("checkboxes".equals(beanProperty)) {
				return UserSelectUtils.get(cp).getBeanProperty("multiple");
			}
			return super.getBeanProperty(cp, beanProperty);
		}

		@SuppressWarnings("unchecked")
		@Override
		public TreeNodes getTreenodes(final ComponentParameter cp, final TreeNode parent) {
			final ComponentParameter nCP = UserSelectUtils.get(cp);
			final IUserSelectHandler uHandler = (IUserSelectHandler) nCP.getComponentHandler();
			Collection<DepartmentW> wrappers = (Collection<DepartmentW>) cp
					.getRequestAttr(REQUEST_WRAPPERS);
			if (wrappers == null) {
				cp.setRequestAttr(REQUEST_WRAPPERS, wrappers = uHandler.getDepartmentWrappers(nCP));
			}

			final String userSelectName = nCP.getComponentName();
			final TreeBean treeBean = (TreeBean) cp.componentBean;
			final TreeNodes nodes = TreeNodes.of();
			if (parent == null) {
				for (final DepartmentW wrapper : wrappers) {
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
				if (data instanceof DepartmentW) {
					final DepartmentW wrapper = (DepartmentW) data;
					for (final Object o : wrapper.getUsers()) {
						final TreeNode tn = new TreeNode(treeBean, o);
						tn.setImage(imgPath + "users.png");
						tn.setJsDblclickCallback("$Actions['" + userSelectName + "'].doDblclick(branch);");
						nodes.add(tn);
					}
					for (final DepartmentW w2 : wrapper.getChildren()) {
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
	}
}
