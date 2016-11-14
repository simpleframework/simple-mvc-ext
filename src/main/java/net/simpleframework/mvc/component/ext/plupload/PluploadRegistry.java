package net.simpleframework.mvc.component.ext.plupload;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(PluploadRegistry.PLUPLOAD)
@ComponentBean(PluploadBean.class)
@ComponentRender(PluploadRender.class)
@ComponentResourceProvider(PluploadResourceProvider.class)
public class PluploadRegistry extends AbstractComponentRegistry {
	public static final String PLUPLOAD = "plupload";

	@Override
	public SwfUploadBean createComponentBean(final PageParameter pp, final Object attriData) {
		final PluploadBean plupload = (PluploadBean) super.createComponentBean(pp, attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, plupload));
		return plupload;
	}
}
