package net.simpleframework.mvc.component.ext.plupload;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PluploadResourceProvider extends SwfUploadResourceProvider {

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		return new String[] { getResourceHomePath() + "/js/plupload.full.min.js" };
	}
}
