package net.simpleframework.mvc.component.ui.pager.db;

import java.util.ArrayList;

import net.simpleframework.ado.bean.IIdBeanAware;
import net.simpleframework.ado.bean.ITextBeanAware;
import net.simpleframework.ado.bean.ITreeBeanAware;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.BlockElement;
import net.simpleframework.mvc.common.element.ElementList;
import net.simpleframework.mvc.common.element.LabelElement;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class NavigationTitle {

	public static <T extends ITreeBeanAware> BlockElement toElement(final PageParameter pp,
			final T category, final NavigationTitleCallback<T> callback) {
		final ElementList eles = new ElementList();
		if (category != null) {
			eles.add(new LinkElement(callback.getRootText()).setOnclick(callback.setOnclick()));
			T category2 = category;
			final ArrayList<T> al = new ArrayList<>();
			while (category2 != null) {
				al.add(0, category2);
				category2 = callback.get(category2.getParentId());
			}
			int size;
			for (int i = 0; i < (size = al.size()); i++) {
				eles.add(SpanElement.NAV());
				category2 = al.get(i);
				if (callback.isLink(category2) && i < size - 1) {
					eles.add(new LinkElement(category2)
							.setOnclick(callback.setOnclick(((IIdBeanAware) category2).getId())));
				} else {
					eles.add(new LabelElement(callback.getText(category2)));
				}
			}
		} else {
			eles.add(new LabelElement(callback.getRootText()));
		}
		return BlockElement.nav().addElements(eles);
	}

	public static abstract class NavigationTitleCallback<T> {

		protected abstract T get(Object id);

		private String componentTable;

		private String rootText;

		public NavigationTitleCallback(final String rootText, final String componentTableName) {
			this.rootText = rootText;
			this.componentTable = componentTableName;
		}

		public NavigationTitleCallback() {
		}

		protected String getComponentTable() {
			return componentTable;
		}

		protected String getRootText() {
			return rootText;
		}

		protected String getCategoryIdKey() {
			return "categoryId";
		}

		protected String setOnclick() {
			return setOnclick(null);
		}

		protected String setOnclick(final Object categoryId) {
			final StringBuilder sb = new StringBuilder();
			sb.append("$Actions['").append(getComponentTable()).append("']('")
					.append(getCategoryIdKey()).append("=").append(StringUtils.blank(categoryId))
					.append("');");
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
