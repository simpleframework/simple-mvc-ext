package net.simpleframework.mvc.component.ext.highchart;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class EChart {

	public static enum EHcType {
		/**
		 * 线图
		 */
		line,

		/**
		 * 饼图
		 */
		pie,

		/**
		 * 柱状图
		 */
		column,
		/**
		 * 横条
		 */
		bar,
		/**
		 * 仪表盘
		 */
		solidgauge
	}

	public static enum EHcDashStyle {

		Solid,

		ShortDash,

		ShortDot,

		ShortDashDot,

		ShortDashDotDot,

		Dot,

		Dash,

		LongDash,

		DashDot,

		LongDashDot,

		LongDashDotDot
	}

	public static enum EAxisType {

		linear,

		logarithmic,

		datetime,

		category
	}

	public static enum EZoomType {
		none,

		x,

		y,

		xy
	}
}
