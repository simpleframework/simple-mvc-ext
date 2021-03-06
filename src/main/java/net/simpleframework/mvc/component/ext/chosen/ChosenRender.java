package net.simpleframework.mvc.component.ext.chosen;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ChosenRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final String selector = (String) cp.getBeanProperty("selector");
		if (!StringUtils.hasText(selector)) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("var selects = $$('").append(selector).append("');");
		sb.append("for (var i = 0; i < selects.length; i++) {");
		sb.append("var select = selects[i];");
		sb.append("select.chosen = new Chosen(select, {");
		sb.append("disable_search: !").append(cp.getBeanProperty("enableSearch")).append(",");
		sb.append("allow_single_deselect: true,");
		sb.append("no_results_text: 'no_results_text',");
		sb.append("placeholder_text_single: '请选择...'");
		sb.append("});");
		sb.append("}");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
