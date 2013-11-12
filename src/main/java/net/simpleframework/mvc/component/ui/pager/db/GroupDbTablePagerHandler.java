package net.simpleframework.mvc.component.ui.pager.db;

import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.IGroupTablePagerHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class GroupDbTablePagerHandler extends AbstractDbTablePagerHandler implements
		IGroupTablePagerHandler {

	public static InputElement createGroupElement(final PageParameter pp, final String tblAction,
			final Option... opts) {
		final String g = pp.getParameter(G);
		final InputElement select = InputElement.select("InputElement_group").setOnchange(
				"$Actions['" + tblAction + "']('g=' + $F(this));");
		if (opts != null) {
			for (final Option opt : opts) {
				final String name = opt.getName();
				if (name == null) {
					continue;
				}
				select.addElements(opt.setSelected(name.equals(g)));
			}
		}
		return select;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		final Map<String, Object> params = super.getFormParameters(cp);
		final String g = cp.getParameter(G);
		if (StringUtils.hasText(g)) {
			params.put(G, g);
		}
		return params;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
		if ("groupColumn".equals(beanProperty)) {
			final String g = cp.getParameter(G);
			if (StringUtils.hasText(g)) {
				return g;
			}
		}
		return super.getBeanProperty(cp, beanProperty);
	}
}
