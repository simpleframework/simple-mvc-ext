package net.simpleframework.mvc.component.ext.ckeditor;

import java.util.Locale;

import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.ArrayUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HtmlEditorRender extends ComponentJavascriptRender {

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		final String componentName = cp.getComponentName();
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		final String textarea = (String) cp.getBeanProperty("textarea");
		final boolean hasTextarea = StringUtils.hasText(textarea);
		sb.append("if (CKEDITOR['_loading_").append(componentName)
				.append("']) return; CKEDITOR['_loading_").append(componentName).append("'] = true;");
		if (hasTextarea) {
			sb.append(actionFunc).append(".editor = CKEDITOR.replace(\"");
			sb.append(textarea).append("\", {");
		} else {
			sb.append(ComponentRenderUtils.initContainerVar(cp));
			sb.append(actionFunc).append(".editor = CKEDITOR.appendTo(")
					.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		}

		final String hpath = cp.getCssResourceHomePath();
		sb.append("contentsCss: [\"").append(cp.getPageResourceProvider().getCssResourceHomePath(cp))
				.append("/default-base.css\", \"").append(hpath).append("/contents.css\"");
		if ((Boolean) cp.getBeanProperty("contentPageStyle")) {
			if (cp.isMobile(true)) {
				sb.append(", \"").append(hpath).append("/contents-page-m.css\"");
			} else {
				sb.append(", \"").append(hpath).append("/contents-page.css\"");
			}
		}
		sb.append("],");
		sb.append("smiley_path: \"").append(ComponentUtils.getResourceHomePath(DictionaryBean.class))
				.append("/smiley/\",");

		sb.append("enterMode: ")
				.append(getLineMode((EEditorLineMode) cp.getBeanProperty("enterMode"))).append(",");
		sb.append("shiftEnterMode: ")
				.append(getLineMode((EEditorLineMode) cp.getBeanProperty("shiftEnterMode")))
				.append(",");

		sb.append("language: \"").append(getLanguage()).append("\",");
		sb.append("autoUpdateElement: false,");
		String[] removePlugins = new String[] {};
		if (!(Boolean) cp.getBeanProperty("elementsPath")) {
			removePlugins = ArrayUtils.add(removePlugins, "elementspath");
		}
		if (removePlugins.length > 0) {
			sb.append("removePlugins: '").append(StringUtils.join(removePlugins, ",")).append("',");
		}
		sb.append("startupFocus: ").append(cp.getBeanProperty("startupFocus")).append(",");
		sb.append("toolbarCanCollapse: ").append(cp.getBeanProperty("toolbarCanCollapse"))
				.append(",");
		sb.append("resize_enabled: ").append(cp.getBeanProperty("resizeEnabled")).append(",");
		final String height = (String) cp.getBeanProperty("height");
		if (StringUtils.hasText(height)) {
			sb.append("height: \"").append(height).append("\",");
		}

		final Toolbar toolbar = (Toolbar) cp.getBeanProperty("toolbar");
		int size;
		if (toolbar != null && (size = toolbar.size()) > 0) {
			sb.append("toolbar: [");
			for (int i = 0; i < size; i++) {
				if (i > 0) {
					sb.append(",");
				}
				final String[] sArr = toolbar.get(i);
				if (sArr.length == 0) {
					sb.append("'/'");
				} else {
					sb.append(JsonUtils.toJSON(ArrayUtils.asList(sArr)));
				}
			}
			sb.append("],");
		}

		sb.append("on: {");
		sb.append(" instanceReady: function(ev) { ");
		// sb.append(" var editor = ev.editor;");
		sb.append("  CKEDITOR['_loading_").append(componentName).append("'] = false; ");
		final String jsLoadedCallback = (String) cp.getBeanProperty("jsLoadedCallback");
		if (StringUtils.hasText(jsLoadedCallback)) {
			sb.append(jsLoadedCallback);
		}
		sb.append(" }");
		sb.append("}");
		sb.append("});");

		if (hasTextarea) {
			sb.append("$(\"").append(textarea).append("\").htmlEditor = ");
			sb.append(actionFunc).append(".editor;");
		}

		final String htmlContent = (String) cp.getBeanProperty("htmlContent");
		if (StringUtils.hasText(htmlContent)) {
			sb.append(actionFunc).append(".editor.setData(\"");
			sb.append(JavascriptUtils.escape(htmlContent)).append("\");");
		}

		// for Attach
		final String attachAction = (String) cp.getBeanProperty("attachAction");
		if (StringUtils.hasText(attachAction)) {
			sb.append(actionFunc).append(".editor._action = \"")
					.append(JavascriptUtils.escape(attachAction)).append("\";");
		}
		final String attachLbl = (String) cp.getBeanProperty("attachLbl");
		if (StringUtils.hasText(attachLbl)) {
			sb.append(actionFunc).append(".editor._label = \"")
					.append(JavascriptUtils.escape(attachLbl)).append("\";");
		}

		// for SyntaxHighlighter
		sb.append(actionFunc).append(".editor.syntaxhighlighter = 'sh_").append(componentName)
				.append("';");

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("var act = $Actions[\"").append(componentName).append("\"];");
		sb2.append("if (act && act.editor) { CKEDITOR.remove(act.editor); }");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString(), sb2.toString());
	}

	private String getLanguage() {
		final Locale l = mvcSettings.getLocale();
		if (l.equals(Locale.SIMPLIFIED_CHINESE)) {
			return "zh-cn";
		} else {
			return "en";
		}
	}

	private String getLineMode(final EEditorLineMode lineMode) {
		if (lineMode == EEditorLineMode.br) {
			return "CKEDITOR.ENTER_BR";
		} else if (lineMode == EEditorLineMode.div) {
			return "CKEDITOR.ENTER_DIV";
		} else {
			return "CKEDITOR.ENTER_P";
		}
	}
}
