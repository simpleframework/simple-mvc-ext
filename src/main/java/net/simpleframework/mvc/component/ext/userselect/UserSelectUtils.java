package net.simpleframework.mvc.component.ext.userselect;

import static net.simpleframework.common.I18n.$m;
import static net.simpleframework.mvc.component.ext.userselect.UserSelectBean.VT_GROUP;
import static net.simpleframework.mvc.component.ext.userselect.UserSelectBean.VT_LIST;
import static net.simpleframework.mvc.component.ext.userselect.UserSelectBean.VT_TREE;

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
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class UserSelectUtils {

	public static final String BEAN_ID = "userselect_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String toTypeHTML(final ComponentParameter cp) {
		final AjaxRequestBean ajaxRequest = (AjaxRequestBean) cp.componentBean
				.getAttr(DictionaryRegistry.ATTRI_AJAXREQUEST);
		final InputElement input = InputElement
				.select()
				.setClassName("vtype")
				.setOnchange(
						"$Actions['" + ajaxRequest.getName() + "']('"
								+ HttpUtils.toQueryString(ComponentUtils.toFormParameters(get(cp)))
								+ "&vtype=' + $F(this));");
		String vtype = cp.getParameter("vtype");
		if (vtype == null) {
			vtype = (String) cp.getBeanProperty("vtype");
		}

		if ((Boolean) cp.getBeanProperty("showGroupOpt")) {
			input.addElements(new Option(VT_GROUP, "#(user_select.1)").setSelected(VT_GROUP
					.equals(vtype)));
		}
		input.addElements(new Option(VT_LIST, "#(user_select.2)").setSelected(VT_LIST.equals(vtype)));
		if ((Boolean) cp.getBeanProperty("showTreeOpt")) {
			input.addElements(new Option(VT_TREE, "#(user_select.3)").setSelected(VT_TREE
					.equals(vtype)));
		}

		final StringBuilder sb = new StringBuilder();
		sb.append(input);
		if ((Boolean) cp.getBeanProperty("multiple")) {
			final boolean vtTree = VT_TREE.equals(vtype);
			sb.append(new BlockElement().setClassName(vtTree ? "check_tree_all" : "check_all")
					.addElements(
							new Checkbox(cp.getComponentName() + "_check_all",
									vtTree ? $m("UserSelectUtils.0") : null)
									.addStyle("vertical-align: middle;")));
		}
		return sb.toString();
	}
}
