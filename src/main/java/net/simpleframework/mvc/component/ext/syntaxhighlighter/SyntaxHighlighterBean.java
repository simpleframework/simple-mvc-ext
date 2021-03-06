package net.simpleframework.mvc.component.ext.syntaxhighlighter;

import net.simpleframework.mvc.component.AbstractComponentBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class SyntaxHighlighterBean extends AbstractComponentBean {
	private static final long serialVersionUID = -7350121242968739128L;

	private ESyntaxHighlighterTheme shTheme;

	private String jsSelectedCallback; // editor

	public ESyntaxHighlighterTheme getShTheme() {
		return shTheme == null ? ESyntaxHighlighterTheme.shThemeDefault : shTheme;
	}

	public void setShTheme(final ESyntaxHighlighterTheme shTheme) {
		this.shTheme = shTheme;
	}

	public String getJsSelectedCallback() {
		return jsSelectedCallback;
	}

	public void setJsSelectedCallback(final String jsSelectedCallback) {
		this.jsSelectedCallback = jsSelectedCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsSelectedCallback" };
	}

	{
		setHandlerClass(DefaultSyntaxHighlighterHandler.class);
	}
}
