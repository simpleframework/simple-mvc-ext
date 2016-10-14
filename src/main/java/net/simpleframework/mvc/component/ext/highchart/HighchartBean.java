package net.simpleframework.mvc.component.ext.highchart;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HighchartBean extends AbstractContainerBean {
	private static final long serialVersionUID = -7466729117571092529L;

	private HcChart chart;

	private String[] colors;

	private HcTitle title;

	private HcSubtitle subtitle;

	private HcPlotOptions plotOptions;

	private HcXAxis xAxis;

	private HcYAxis yAxis;

	private HcLegend legend;

	private HcTooltip tooltip;

	private List<HcSeries> series;

	public HcChart getChart() {
		return chart;
	}

	public HighchartBean setChart(final HcChart chart) {
		this.chart = chart;
		return this;
	}

	public String[] getColors() {
		return colors;
	}

	public HighchartBean setColors(final String[] colors) {
		this.colors = colors;
		return this;
	}

	public HcTitle getTitle() {
		return title;
	}

	public HighchartBean setTitle(final HcTitle title) {
		this.title = title;
		return this;
	}

	public HighchartBean setTitle(final String title) {
		return setTitle(new HcTitle().setText(title));
	}

	public HcSubtitle getSubtitle() {
		return subtitle;
	}

	public HighchartBean setSubtitle(final HcSubtitle subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	public HighchartBean setSubtitle(final String subtitle) {
		return setSubtitle(new HcSubtitle().setText(subtitle));
	}

	public HcPlotOptions getPlotOptions() {
		return plotOptions;
	}

	public HighchartBean setPlotOptions(final HcPlotOptions plotOptions) {
		this.plotOptions = plotOptions;
		return this;
	}

	public HcXAxis getxAxis() {
		return xAxis;
	}

	public HighchartBean setxAxis(final HcXAxis xAxis) {
		this.xAxis = xAxis;
		return this;
	}

	public HcYAxis getyAxis() {
		return yAxis;
	}

	public HighchartBean setyAxis(final HcYAxis yAxis) {
		this.yAxis = yAxis;
		return this;
	}

	public HcLegend getLegend() {
		return legend;
	}

	public HighchartBean setLegend(final HcLegend legend) {
		this.legend = legend;
		return this;
	}

	public HcTooltip getTooltip() {
		return tooltip;
	}

	public HighchartBean setTooltip(final HcTooltip tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public List<HcSeries> getSeries() {
		if (series == null) {
			series = new ArrayList<HcSeries>();
		}
		return series;
	}

	public HighchartBean addSeries(final HcSeries series) {
		getSeries().add(series);
		return this;
	}
}
