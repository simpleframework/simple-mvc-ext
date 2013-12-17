package net.simpleframework.mvc.component.ext.userselect;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class UserSelectResourceProvider extends DictionaryResourceProvider {

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/user_select.css" };
	}
}
