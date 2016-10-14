package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractHcElement;
import net.simpleframework.mvc.component.ext.highchart.EChart.EHcDashStyle;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcPlotOptions extends AbstractHcElement<HcPlotOptions> {
	private static final long serialVersionUID = -2235545615844195615L;

	private HcOptLine line;

	private HcOptPie pie;

	private HcOptColumn column;

	private HcOptSeries series;

	public HcPlotOptions() {
	}

	public HcOptPie getPie() {
		return pie;
	}

	public HcPlotOptions setPie(final HcOptPie pie) {
		this.pie = pie;
		return this;
	}

	public HcOptLine getLine() {
		return line;
	}

	public HcPlotOptions setLine(final HcOptLine line) {
		this.line = line;
		return this;
	}

	public HcOptColumn getColumn() {
		return column;
	}

	public HcPlotOptions setColumn(final HcOptColumn column) {
		this.column = column;
		return this;
	}

	public HcOptSeries getSeries() {
		return series;
	}

	public HcPlotOptions setSeries(final HcOptSeries series) {
		this.series = series;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		if (line != null) {
			kv.add("line", line.toMap());
		}
		if (pie != null) {
			kv.add("pie", pie.toMap());
		}
		if (column != null) {
			kv.add("column", column.toMap());
		}
		if (series != null) {
			kv.add("series", series.toMap());
		}
		return kv;
	}

	public static class HcOptLine extends HcPlotOption<HcOptLine> {
		private static final long serialVersionUID = 5445877752992253556L;

		private HcOptMarker marker;

		public HcOptMarker getMarker() {
			return marker;
		}

		public HcOptLine setMarker(final HcOptMarker marker) {
			this.marker = marker;
			return this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getMarker()) != null) {
				kv.put("marker", ((HcOptMarker) val).toMap());
			}
			return kv;
		}
	}

	public static class HcOptPie extends HcPlotOption<HcOptPie> {
		private static final long serialVersionUID = -6860850197830390650L;

		public HcOptPie() {
		}
	}

	public static class HcOptColumn extends HcPlotOption<HcOptColumn> {
		private static final long serialVersionUID = -8750639664832275156L;

		private Integer borderRadius, borderWidth;

		public Integer getBorderRadius() {
			return borderRadius;
		}

		public HcOptColumn setBorderRadius(final Integer borderRadius) {
			this.borderRadius = borderRadius;
			return this;
		}

		public Integer getBorderWidth() {
			return borderWidth;
		}

		public HcOptColumn setBorderWidth(final Integer borderWidth) {
			this.borderWidth = borderWidth;
			return this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getBorderWidth()) != null) {
				kv.put("borderWidth", val);
			}
			if ((val = getBorderRadius()) != null) {
				kv.put("borderRadius", val);
			}
			return kv;
		}
	}

	public static class HcOptSeries extends HcPlotOption<HcOptSeries> {
		private static final long serialVersionUID = 685114138807890706L;

		private HcOptMarker marker;

		public HcOptSeries() {
		}

		public HcOptMarker getMarker() {
			return marker;
		}

		public HcOptSeries setMarker(final HcOptMarker marker) {
			this.marker = marker;
			return this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getMarker()) != null) {
				kv.put("marker", ((HcOptMarker) val).toMap());
			}
			return kv;
		}
	}

	@SuppressWarnings("unchecked")
	static abstract class HcPlotOption<T extends HcPlotOption<T>> extends AbstractHcElement<T> {
		private static final long serialVersionUID = -6272891071516460912L;

		private Boolean allowPointSelect;

		private String color;

		private EHcDashStyle dashStyle;

		private String cursor;

		private String point_onClick;

		private HcDataLabels dataLabels;

		public Boolean getAllowPointSelect() {
			return allowPointSelect;
		}

		public T setAllowPointSelect(final Boolean allowPointSelect) {
			this.allowPointSelect = allowPointSelect;
			return (T) this;
		}

		public String getCursor() {
			return cursor;
		}

		public T setCursor(final String cursor) {
			this.cursor = cursor;
			return (T) this;
		}

		public String getColor() {
			return color;
		}

		public T setColor(final String color) {
			this.color = color;
			return (T) this;
		}

		public EHcDashStyle getDashStyle() {
			return dashStyle;
		}

		public T setDashStyle(final EHcDashStyle dashStyle) {
			this.dashStyle = dashStyle;
			return (T) this;
		}

		public HcDataLabels getDataLabels() {
			return dataLabels;
		}

		public T setDataLabels(final HcDataLabels dataLabels) {
			this.dataLabels = dataLabels;
			return (T) this;
		}

		public String getPoint_onClick() {
			return point_onClick;
		}

		public T setPoint_onClick(final String point_onClick) {
			this.point_onClick = point_onClick;
			return (T) this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getAllowPointSelect()) != null) {
				kv.put("allowPointSelect", val);
			}
			if ((val = getCursor()) != null) {
				kv.put("cursor", val);
			}
			if ((val = getColor()) != null) {
				kv.put("color", val);
			}
			if ((val = getDashStyle()) != null) {
				kv.put("dashStyle", val);
			}
			if ((val = getDataLabels()) != null) {
				kv.put("dataLabels", ((HcDataLabels) val).toMap());
			}

			final KVMap events = new KVMap();
			if ((val = getPoint_onClick()) != null) {
				events.add("click", val);
			}
			if (events.size() > 0) {
				kv.put("point", new KVMap().add("events", events));
			}
			return kv;
		}
	}

	public static class HcOptMarker extends _HcOptMarkerBase<HcOptMarker> {
		private static final long serialVersionUID = 3204432952681405498L;

		// "circle", "square", "diamond", "triangle", "triangle-down"
		private String symbol;

		private HcOptMarkerState hover;

		private HcOptMarkerState select;

		public HcOptMarker() {
		}

		public String getSymbol() {
			return symbol;
		}

		public HcOptMarker setSymbol(final String symbol) {
			this.symbol = symbol;
			return this;
		}

		public HcOptMarkerState getHover() {
			return hover;
		}

		public HcOptMarker setHover(final HcOptMarkerState hover) {
			this.hover = hover;
			return this;
		}

		public HcOptMarkerState getSelect() {
			return select;
		}

		public HcOptMarker setSelect(final HcOptMarkerState select) {
			this.select = select;
			return this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getSymbol()) != null) {
				kv.put("symbol", val);
			}
			final KVMap states = new KVMap();
			if ((val = getHover()) != null) {
				states.put("hover", ((HcOptMarkerState) val).toMap());
			}
			if ((val = getSelect()) != null) {
				states.put("select", ((HcOptMarkerState) val).toMap());
			}
			if (states.size() > 0) {
				kv.put("states", states);
			}
			return kv;
		}
	}

	public static class HcOptMarkerState extends _HcOptMarkerBase<HcOptMarkerState> {
		private static final long serialVersionUID = 2898669993822026853L;

		public HcOptMarkerState() {
		}
	}

	@SuppressWarnings("unchecked")
	private abstract static class _HcOptMarkerBase<T extends _HcOptMarkerBase<T>>
			extends AbstractHcElement<T> {
		private static final long serialVersionUID = 1255714806998095354L;

		private Boolean enabled;

		private Integer radius;

		private String fillColor;

		private String lineColor;

		private Integer lineWidth;

		public Boolean getEnabled() {
			return enabled;
		}

		public T setEnabled(final Boolean enabled) {
			this.enabled = enabled;
			return (T) this;
		}

		public Integer getRadius() {
			return radius;
		}

		public T setRadius(final Integer radius) {
			this.radius = radius;
			return (T) this;
		}

		public String getFillColor() {
			return fillColor;
		}

		public T setFillColor(final String fillColor) {
			this.fillColor = fillColor;
			return (T) this;
		}

		public String getLineColor() {
			return lineColor;
		}

		public T setLineColor(final String lineColor) {
			this.lineColor = lineColor;
			return (T) this;
		}

		public Integer getLineWidth() {
			return lineWidth;
		}

		public T setLineWidth(final Integer lineWidth) {
			this.lineWidth = lineWidth;
			return (T) this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getEnabled()) != null) {
				kv.put("enabled", val);
			}
			if ((val = getRadius()) != null) {
				kv.put("radius", val);
			}
			if ((val = getFillColor()) != null) {
				kv.put("fillColor", val);
			}
			if ((val = getLineColor()) != null) {
				kv.put("lineColor", val);
			}
			if ((val = getLineWidth()) != null) {
				kv.put("lineWidth", val);
			}
			return kv;
		}
	}
}
