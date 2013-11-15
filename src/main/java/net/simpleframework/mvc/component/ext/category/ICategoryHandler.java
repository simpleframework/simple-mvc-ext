package net.simpleframework.mvc.component.ext.category;

import java.util.Map;

import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ICategoryHandler extends IComponentHandler {

	/**
	 * 获取类目的数据
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @param treeNode
	 * @return
	 */
	TreeNodes getCategoryTreenodes(ComponentParameter cp, TreeBean treeBean, TreeNode parent);

	/**
	 * 选取类目（字典）数据
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @param treeNode
	 * @return
	 */
	TreeNodes getCategoryDictTreenodes(ComponentParameter cp, TreeBean treeBean, TreeNode parent);

	/**
	 * 当前类目被删除时触发
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @return
	 */
	JavascriptForward onCategoryDelete(ComponentParameter cp, TreeBean treeBean);

	/**
	 * 在类目的同一层次上下移动
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @param form
	 * @param to
	 * @param up
	 * @return
	 */
	JavascriptForward onCategoryMove(ComponentParameter cp, TreeBean treeBean, Object form,
			Object to, boolean up);

	/**
	 * 树节点的拖放逻辑
	 * 
	 * @param cParameter
	 * @param treeBean
	 * @param drag
	 * @param drop
	 * @return true接受拖放，false拒绝拖放
	 */
	boolean onCategoryDragDrop(ComponentParameter cp, TreeBean treeBean, Object drag, Object drop);

	static final String edit_url = "edit_url";

	static final String window_title = "window_title";

	static final String window_height = "window_height";

	static final String window_width = "window_width";

	static final String window_resizable = "window_resizable";

	/**
	 * 编辑类目时需要的属性
	 * 
	 * edit_url: 编辑类目的url地址
	 * 
	 * window_height, window_width, window_resizable: window的高、宽和是否可以改变window的大小
	 * 
	 * @param cParameter
	 * @return
	 */
	Map<String, Object> categoryEdit_attri(ComponentParameter cp);

	/**
	 * 当类目被保存时触发
	 * 
	 * @param cParameter
	 */
	JavascriptForward categoryEdit_onSave(ComponentParameter cp);

	/**
	 * 当类目数据被装载时
	 * 
	 * @param cParameter
	 * @param dataBinding
	 * @param selector
	 */
	void categoryEdit_onLoaded(ComponentParameter cp, Map<String, Object> dataBinding,
			PageSelector selector);

	/**
	 * 缺省的类目装载机制，如果指定了editUrl，则此实现无效
	 * 
	 * @param cParameter
	 */
	void categoryEdit_doInit(ComponentParameter cp);
}
