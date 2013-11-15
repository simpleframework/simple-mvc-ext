package net.simpleframework.mvc.component.ext.userselect;

import static net.simpleframework.common.I18n.$m;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class UserSelectUtils {

	public static final String BEAN_ID = "userselect_@bid";

	public static final String VTYPE = "vtype";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static final String VT_GROUP = "group";
	public static final String VT_LIST = "list";
	public static final String VT_TREE = "tree";

	public static String toTypeHTML(final ComponentParameter cp) {
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) cp.componentBean
				.getAttr(DictionaryRegistry.ATTRI_AJAXREQUEST);
		final InputElement input = InputElement
				.select()
				.setClassName(VTYPE)
				.setOnchange(
						"$Actions['" + ajaxRequest.getName() + "']('"
								+ HttpUtils.toQueryString(ComponentUtils.toFormParameters(get(cp))) + "&"
								+ VTYPE + "=' + $F(this));");
		final String vtype = cp.getParameter(VTYPE);
		input.addElements(
				new Option(VT_GROUP, "#(user_select.1)").setSelected(VT_GROUP.equals(vtype)),
				new Option(VT_LIST, "#(user_select.2)").setSelected(VT_LIST.equals(vtype)));
		BlockElement block = null;
		if ((Boolean) cp.getBeanProperty("treeMode")) {
			final boolean selected = VT_TREE.equals(vtype);
			input.addElements(new Option(VT_TREE, "#(user_select.3)").setSelected(selected));
			if (selected) {
				final String name = cp.getComponentName();
				block = new BlockElement().setClassName("check_tree_all").addElements(
						new Checkbox(name + "_check_all", $m("UserSelectUtils.0")));
			}
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(input);
		if (block != null) {
			sb.append(block);
		}
		return sb.toString();
	}
}
