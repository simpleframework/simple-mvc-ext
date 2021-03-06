package net.simpleframework.mvc.component.ext.ckeditor;

import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HtmlEditorBean extends AbstractContainerBean {
	private static final long serialVersionUID = 847412101739953657L;

	private boolean toolbarCanCollapse = BeanDefaults.getBool(getClass(), "toolbarCanCollapse",
			false);

	private boolean resizeEnabled = BeanDefaults.getBool(getClass(), "resizeEnabled", false);

	private boolean elementsPath = BeanDefaults.getBool(getClass(), "elementsPath", false);

	private boolean startupFocus = BeanDefaults.getBool(getClass(), "startupFocus", false);

	private EEditorLineMode enterMode, shiftEnterMode;

	private boolean codeEnabled = BeanDefaults.getBool(getClass(), "codeEnabled", false);

	private String htmlContent;

	private boolean contentPageStyle;

	private String textarea;

	private Toolbar toolbar;

	private String attachAction, attachLbl;

	private String jsLoadedCallback;

	public Toolbar getToolbar() {
		return toolbar == null ? Toolbar.BASIC : toolbar;
	}

	public HtmlEditorBean setToolbar(final Toolbar toolbar) {
		this.toolbar = toolbar;
		return this;
	}

	public String getTextarea() {
		return textarea;
	}

	public HtmlEditorBean setTextarea(final String textarea) {
		this.textarea = textarea;
		return this;
	}

	public boolean isToolbarCanCollapse() {
		return toolbarCanCollapse;
	}

	public HtmlEditorBean setToolbarCanCollapse(final boolean toolbarCanCollapse) {
		this.toolbarCanCollapse = toolbarCanCollapse;
		return this;
	}

	public boolean isResizeEnabled() {
		return resizeEnabled;
	}

	public HtmlEditorBean setResizeEnabled(final boolean resizeEnabled) {
		this.resizeEnabled = resizeEnabled;
		return this;
	}

	public boolean isCodeEnabled() {
		return codeEnabled;
	}

	public HtmlEditorBean setCodeEnabled(final boolean codeEnabled) {
		this.codeEnabled = codeEnabled;
		return this;
	}

	public boolean isElementsPath() {
		return elementsPath;
	}

	public HtmlEditorBean setElementsPath(final boolean elementsPath) {
		this.elementsPath = elementsPath;
		return this;
	}

	public boolean isStartupFocus() {
		return startupFocus;
	}

	public HtmlEditorBean setStartupFocus(final boolean startupFocus) {
		this.startupFocus = startupFocus;
		return this;
	}

	public EEditorLineMode getEnterMode() {
		return enterMode == null ? EEditorLineMode.p : enterMode;
	}

	public HtmlEditorBean setEnterMode(final EEditorLineMode enterMode) {
		this.enterMode = enterMode;
		return this;
	}

	public EEditorLineMode getShiftEnterMode() {
		return shiftEnterMode == null ? EEditorLineMode.br : shiftEnterMode;
	}

	public HtmlEditorBean setShiftEnterMode(final EEditorLineMode shiftEnterMode) {
		this.shiftEnterMode = shiftEnterMode;
		return this;
	}

	public String getAttachAction() {
		return attachAction;
	}

	public HtmlEditorBean setAttachAction(final String attachAction) {
		this.attachAction = attachAction;
		return this;
	}

	public String getAttachLbl() {
		return attachLbl;
	}

	public HtmlEditorBean setAttachLbl(final String attachLbl) {
		this.attachLbl = attachLbl;
		return this;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public HtmlEditorBean setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
		return this;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public HtmlEditorBean setHtmlContent(final String htmlContent) {
		this.htmlContent = htmlContent;
		return this;
	}

	public boolean isContentPageStyle() {
		return contentPageStyle;
	}

	public HtmlEditorBean setContentPageStyle(final boolean contentPageStyle) {
		this.contentPageStyle = contentPageStyle;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback", "htmlContent" };
	}
}
