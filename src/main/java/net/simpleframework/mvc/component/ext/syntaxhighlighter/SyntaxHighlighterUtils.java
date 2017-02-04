package net.simpleframework.mvc.component.ext.syntaxhighlighter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.Option;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class SyntaxHighlighterUtils {
	public static final String BEAN_ID = "syntaxhighlighter_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String getCssResourceHomePath(final PageParameter pp) {
		return ComponentUtils.getCssResourceHomePath(pp, SyntaxHighlighterBean.class);
	}

	public static String toShWindowHTML(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='sh_window'>");
		sb.append(" <div class='tb clearfix'>");
		sb.append("  <div class='right'>");
		sb.append(new Checkbox("sh_window_opt1", "#(sh_window.0)").setChecked(true));
		sb.append("<br>");
		sb.append(new Checkbox("sh_window_opt2", "#(sh_window.4)"));
		sb.append("  </div>");
		sb.append("  <div class='left'>");
		sb.append("   <label>#(sh_window.1)</label>");
		final InputElement lng = InputElement.select("sh_window_lang");
		for (final String lang : new String[] { "Java", "Javascript", "CSS", "HTML", "XML", "SQL",
				"Groovy", "C++", "C", "C#", "PHP", "Python", "Ruby" }) {
			lng.addElements(new Option(lang.toLowerCase(), lang));
		}
		sb.append(lng);
		sb.append("  </div>");
		sb.append(" </div>");
		sb.append(" <div class='ta'>");
		sb.append(InputElement.textarea("sh_window_textarea").setRows(13));
		sb.append(" </div>");
		sb.append(" <div class='btm'>");
		sb.append(LinkButton.corner("#(sh_window.2)").setOnclick("sh_comp.insert();")
				.addClassName("green"));
		sb.append(SpanElement.SPACE);
		sb.append(LinkButton.corner("#(Button.Cancel)").setOnclick("sh_comp.close();")
				.addClassName("gray"));
		sb.append(" </div>");
		sb.append("</div>");
		return sb.toString();
	}
}
