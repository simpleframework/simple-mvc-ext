package net.simpleframework.mvc.component.ext.category;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.Convert;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.window.AbstractWindowHandler;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentBean(CategoryBean.class)
@ComponentName(CategoryRegistry.CATEGORY)
@ComponentRender(CategoryRender.class)
@ComponentResourceProvider(CategoryResourceProvider.class)
public class CategoryRegistry extends AbstractComponentRegistry {

	public static final String CATEGORY = "category";

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pp, final Object attriData) {
		final CategoryBean categoryBean = (CategoryBean) super.createComponentBean(pp, attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, categoryBean));

		final ComponentParameter nCP = ComponentParameter.get(pp, categoryBean);
		final String categoryName = nCP.getComponentName();

		// 编辑
		pp.addComponentBean(categoryName + "_edit_page", AjaxRequestBean.class)
				.setHandlerMethod("editUrl").setHandlerClass(CategoryAction.class);
		final WindowBean window = (WindowBean) pp
				.addComponentBean(categoryName + "_edit", WindowBean.class)
				.setContentRef(categoryName + "_edit_page").setHandlerClass(CategoryEditWindow.class);
		window.setAttr("@category", categoryBean);

		// 删除
		pp.addComponentBean(categoryName + "_delete", AjaxRequestBean.class)
				.setConfirmMessage($m("Confirm.Delete")).setHandlerMethod("doDelete")
				.setHandlerClass(CategoryAction.class);

		// 移动
		pp.addComponentBean(categoryName + "_move", AjaxRequestBean.class).setHandlerMethod("doMove")
				.setHandlerClass(CategoryAction.class);
		return categoryBean;
	}

	public static class CategoryEditWindow extends AbstractWindowHandler {
		@Override
		public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
			if (beanProperty.equals("title") || beanProperty.equals("resizable")
					|| beanProperty.equals("height") || beanProperty.equals("width")) {
				final ComponentParameter nCP = ComponentParameter.get(cp,
						(AbstractComponentBean) cp.componentBean.getAttr("@category"));
				final Map<String, Object> attri = ((ICategoryHandler) nCP.getComponentHandler())
						.categoryEdit_attri(nCP);
				if (beanProperty.equals("title")) {
					return Convert.toString(attri.get(ICategoryHandler.window_title),
							$m("CategoryRegistry.0"));
				} else if (beanProperty.equals("height")) {
					return Convert.toInt(attri.get(ICategoryHandler.window_height), 280);
				} else if (beanProperty.equals("width")) {
					return Convert.toInt(attri.get(ICategoryHandler.window_width), 340);
				} else {
					return Convert.toBool(attri.get(ICategoryHandler.window_resizable), false);
				}
			}
			return super.getBeanProperty(cp, beanProperty);
		}
	}
}
