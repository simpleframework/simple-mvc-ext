package net.simpleframework.mvc.component.ext.category;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.bean.IIdBeanAware;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.common.element.EElementEvent;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.category.CategoryUtils.CategoryDictTree;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.propeditor.EInputCompType;
import net.simpleframework.mvc.component.ui.propeditor.InputComp;
import net.simpleframework.mvc.component.ui.propeditor.PropEditorBean;
import net.simpleframework.mvc.component.ui.propeditor.PropField;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultCategoryHandler extends ComponentHandlerEx implements ICategoryHandler {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		return ((KVMap) super.getFormParameters(cp)).add(CategoryUtils.BEAN_ID, cp.hashId());
	}

	protected IDataQuery<?> categoryBeans(final ComponentParameter cp, final Object categoryId) {
		throw NotImplementedException.of(getClass(), "categoryBeans");
	}

	@Override
	public TreeNodes getCategoryTreenodes(final ComponentParameter cp, final TreeBean treeBean,
			final TreeNode parent) {
		Object dataObject = parent != null ? parent.getDataObject() : null;
		if (dataObject instanceof IIdBeanAware) {
			dataObject = ((IIdBeanAware) dataObject).getId();
		}
		final IDataQuery<?> dq = categoryBeans(cp, dataObject);
		if (dq != null && dq.getCount() > 0) {
			final boolean draggable = (Boolean) cp.getBeanProperty("draggable");
			final TreeNodes nodes = TreeNodes.of();
			Object bean;
			while ((bean = dq.next()) != null) {
				final TreeNode treeNode = new TreeNode(treeBean, parent, bean);
				treeNode.setDraggable(draggable);
				treeNode.setAcceptdrop(draggable);
				nodes.add(treeNode);
			}
			return nodes;
		}
		return null;
	}

	@Override
	public TreeNodes getCategoryDictTreenodes(final ComponentParameter cp, final TreeBean treeBean,
			final TreeNode parent) {
		return getCategoryTreenodes(cp, treeBean, parent);
	}

	protected String[] getContextMenuKeys() {
		return new String[] { "Add", "Edit", "Delete", "-", "Refresh", "-", "Move", "-", "Expand",
				"Collapse" };
	}

	protected KVMap createContextMenuItems() {
		return new KVMap()
				.add("Add", MenuItem.itemAdd().setOnclick("$category_action(item).add();"))
				.add("Edit", MenuItem.itemEdit().setOnclick("$category_action(item).edit();"))
				.add("Delete", MenuItem.itemDelete().setOnclick("$category_action(item).del();"))
				.add("Refresh",
						MenuItem.of($m("Refresh"), MenuItem.ICON_REFRESH,
								"$category_action(item).refresh();"))
				.add("Move",
						MenuItem
								.of($m("Menu.move"))
								.addChild(
										MenuItem.of($m("Menu.up"), MenuItem.ICON_UP,
												"$category_action(item).move(true, false);"))
								.addChild(
										MenuItem.of($m("Menu.up2"), MenuItem.ICON_UP2,
												"$category_action(item).move(true, true);"))
								.addChild(
										MenuItem.of($m("Menu.down"), MenuItem.ICON_DOWN,
												"$category_action(item).move(false, false);"))
								.addChild(
										MenuItem.of($m("Menu.down2"), MenuItem.ICON_DOWN2,
												"$category_action(item).move(false, true);")))
				.add("Expand",
						MenuItem.of($m("Tree.expand"), MenuItem.ICON_EXPAND,
								"$category_action(item).expand();"))
				.add("Collapse",
						MenuItem.of($m("Tree.collapse"), MenuItem.ICON_COLLAPSE,
								"$category_action(item).collapse();"));
	}

	@Override
	public MenuItems getContextMenu(final ComponentParameter cp, final MenuBean menuBean,
			final MenuItem menuItem) {
		if (menuItem == null) {
			final MenuItems items = MenuItems.of();
			final KVMap map = createContextMenuItems();
			for (final String k : getContextMenuKeys()) {
				if ("-".equals(k)) {
					items.add(MenuItem.sep());
				} else {
					items.add((MenuItem) map.get(k));
				}
			}
			return items;
		}
		return null;
	}

	@Override
	public JavascriptForward categoryEdit_onSave(final ComponentParameter cp) {
		final JavascriptForward js = refreshTree(cp);
		// 关闭窗口
		js.append("$Actions['").append(cp.getComponentName()).append("_edit'].close();");
		return js;
	}

	@Override
	public JavascriptForward onCategoryDelete(final ComponentParameter cp, final TreeBean treeBean) {
		return refreshTree(cp);
	}

	@Override
	public boolean onCategoryDragDrop(final ComponentParameter cp, final TreeBean treeBean,
			final Object drag, final Object drop) {
		return false;
	}

	@Override
	public JavascriptForward onCategoryMove(final ComponentParameter cp, final TreeBean treeBean,
			final Object form, final Object to, final boolean up) {
		return refreshTree(cp);
	}

	protected JavascriptForward refreshTree(final ComponentParameter cp) {
		final JavascriptForward js = new JavascriptForward();
		final String categoryName = cp.getComponentName();
		// 刷新tree
		js.append("$Actions['").append(categoryName).append("'].refresh();");
		return js;
	}

	@Override
	public void categoryEdit_onLoaded(final ComponentParameter cp,
			final Map<String, Object> dataBinding, final PageSelector selector) {
	}

	@Override
	public Map<String, Object> categoryEdit_attri(final ComponentParameter cp) {
		return new KVMap().add(edit_url, ComponentUtils.getResourceHomePath(CategoryBean.class)
				+ "/jsp/category_edit.jsp");
	}

	@Override
	public void categoryEdit_doInit(final ComponentParameter cp) {
		// 属性列表
		categoryEdit_createPropEditor(cp);
		// 验证
		categoryEdit_createValidation(cp);

		// 类目字典
		categoryEdit_createDictTree(cp);
		categoryEdit_createDict(cp);
		// 保存
		categoryEdit_createSave(cp);
	}

	protected AbstractComponentBean categoryEdit_createPropEditor(final ComponentParameter cp) {
		final CategoryBean category = (CategoryBean) cp.componentBean;
		final String categoryName = cp.getComponentName();

		final PropEditorBean propEditor = (PropEditorBean) cp.addComponentBean(
				categoryName + "_propEditor", PropEditorBean.class).setContainerId(
				"idCategoryForm_" + category.hashId());
		final PropField f1 = new PropField($m("category_edit.0")).addComponents(new InputComp(
				"category_id").setType(EInputCompType.hidden), new InputComp("category_text"));
		final PropField f2 = new PropField($m("category_edit.1")).addComponents(new InputComp(
				"category_name"));
		final PropField f3 = new PropField($m("category_edit.2")).addComponents(new InputComp(
				"category_parentId").setType(EInputCompType.hidden), new InputComp(
				"category_parentText").setType(EInputCompType.textButton).setAttributes("readonly")
				.addEvent(EElementEvent.click, "$Actions['" + categoryName + "_dict']();"));
		final PropField f4 = new PropField($m("Description")).addComponents(new InputComp(
				"category_description").setType(EInputCompType.textarea).setAttributes("rows:6"));
		propEditor.getFormFields().append(f1, f2, f3, f4);

		return propEditor;
	}

	protected AbstractComponentBean categoryEdit_createValidation(final ComponentParameter cp) {
		final String categoryName = cp.getComponentName();
		return cp
				.addComponentBean(categoryName + "_validation", ValidationBean.class)
				.setTriggerSelector(".CategoryEdit .button2")
				.setWarnType(EWarnType.insertAfter)
				.addValidators(
						new Validator().setSelector("#category_text, #category_name").setMethod(
								EValidatorMethod.required));
	}

	protected AbstractComponentBean categoryEdit_createDictTree(final ComponentParameter cp) {
		final String categoryName = cp.getComponentName();
		final TreeBean dict = (TreeBean) cp
				.addComponentBean(categoryName + "_dict_tree", TreeBean.class)
				.setDynamicLoading((Boolean) cp.getBeanProperty("dynamicTree"))
				.setHandlerClass(CategoryDictTree.class);
		setTreeBean(cp, dict);
		return dict;
	}

	protected AbstractComponentBean categoryEdit_createDict(final ComponentParameter cp) {
		final String categoryName = cp.getComponentName();
		final String selector = (String) cp.getBeanProperty("selector");
		final DictionaryBean dictionary = (DictionaryBean) cp
				.addComponentBean(categoryName + "_dict", DictionaryBean.class)
				.setBindingId("category_parentId").setBindingText("category_parentText").setHeight(330)
				.setWidth(270).setTitle($m("category_edit.2")).setSelector(selector);
		dictionary.addTreeRef(cp, categoryName + "_dict_tree");
		return dictionary;
	}

	protected AbstractComponentBean categoryEdit_createSave(final ComponentParameter cp) {
		final String categoryName = cp.getComponentName();
		final String selector = (String) cp.getBeanProperty("selector");
		return cp.addComponentBean(categoryName + "_save", AjaxRequestBean.class)
				.setHandlerMethod("doSave").setHandlerClass(CategoryAction.class)
				.setSelector(selector + ", #idCategoryEdit_" + cp.hashId());
	}

	protected static String PARAM_CATEGORY_ID = "category_id";

	protected static String PARAM_CATEGORY_NAME = "category_name";

	protected static String PARAM_CATEGORY_TEXT = "category_text";

	protected static String PARAM_CATEGORY_PARENTID = "category_parentId";

	protected static String PARAM_CATEGORY_PARENTTEXT = "category_parentText";

	protected static String PARAM_CATEGORY_DESC = "category_description";

	// utils

	protected String getImgBase(final ComponentParameter cp,
			final Class<? extends AbstractMVCPage> pageClass) {
		return AbstractMVCPage.get(pageClass).getCssResourceHomePath(cp) + "/images/";
	}

	@Override
	public void setTreeBean(final ComponentParameter cp, final TreeBean treeBean) {
	}
}
