package net.simpleframework.mvc.component.ext.highchart;

import static net.simpleframework.common.I18n.$m;

import java.util.Arrays;
import java.util.List;

import net.simpleframework.common.JsonUtils;
import net.simpleframework.mvc.component.AbstractComponentRender.ComponentJavascriptRender;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.IComponentRegistry;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HighchartRender extends ComponentJavascriptRender {

	public HighchartRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Highcharts.setOptions({");
		sb.append(" lang: {");
		sb.append("  resetZoom: '").append($m("HighchartRender.0")).append("',");
		sb.append("  resetZoomTitle: '").append($m("HighchartRender.1")).append("'");
		sb.append(" }");
		sb.append("});");
		final String actionFunc = ComponentRenderUtils.actionFunc(cp);
		sb.append(ComponentRenderUtils.initContainerVar(cp));
		sb.append("var _chart = {");
		sb.append("  renderTo: ").append(ComponentRenderUtils.VAR_CONTAINER);
		sb.append("};");
		final HcChart chart = (HcChart) cp.getBeanProperty("chart");
		if (chart != null) {
			sb.append("_chart = Object.extend(_chart, ").append(chart).append(");");
		}
		sb.append(actionFunc).append(".chart = ").append("new Highcharts.Chart({");
		sb.append(" chart: _chart,");
		final String[] colors = (String[]) cp.getBeanProperty("colors");
		if (colors != null && colors.length > 0) {
			sb.append("colors: ").append(JsonUtils.toJSON(Arrays.asList(colors))).append(",");
		}
		final HcTitle title = (HcTitle) cp.getBeanProperty("title");
		if (title != null) {
			sb.append("title: ").append(title).append(",");
		}
		final HcSubtitle subtitle = (HcSubtitle) cp.getBeanProperty("subtitle");
		if (subtitle != null) {
			sb.append("subtitle: ").append(subtitle).append(",");
		}
		final HcPlotOptions plotOptions = (HcPlotOptions) cp.getBeanProperty("plotOptions");
		if (plotOptions != null) {
			sb.append("plotOptions: ").append(plotOptions).append(",");
		}
		final HcXAxis xAxis = (HcXAxis) cp.getBeanProperty("xAxis");
		if (xAxis != null) {
			sb.append("xAxis: ").append(xAxis).append(",");
		}
		final HcYAxis yAxis = (HcYAxis) cp.getBeanProperty("yAxis");
		if (yAxis != null) {
			sb.append("yAxis: ").append(yAxis).append(",");
		}
		final HcLegend legend = (HcLegend) cp.getBeanProperty("legend");
		if (legend != null) {
			sb.append("legend: ").append(legend).append(",");
		}
		final HcTooltip tooltip = (HcTooltip) cp.getBeanProperty("tooltip");
		if (tooltip != null) {
			sb.append("tooltip: ").append(tooltip).append(",");
		}

		final IHighchartHandler handler = (IHighchartHandler) cp.getComponentHandler();
		List<HcSeries> series = null;
		if (handler != null) {
			series = handler.getSeries(cp);
		}
		if (series == null) {
			series = (List<HcSeries>) cp.getBeanProperty("series");
		}
		sb.append("series: [");
		final int size = series.size();
		for (int i = 0; i < size; i++) {
			sb.append(series.get(i));
			if (i < size - 1) {
				sb.append(",");
			}
		}
		sb.append("],");
		sb.append("credits: { enabled: false }");
		sb.append("});");
		return ComponentRenderUtils.genActionWrapper(cp, sb.toString());
	}
}
