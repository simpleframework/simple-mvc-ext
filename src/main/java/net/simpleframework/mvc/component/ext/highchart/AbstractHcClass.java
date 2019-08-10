package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.lib.net.minidev.json.JSONStyle;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.component.ext.highchart.EChart.EAxisType;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHcClass {

	@SuppressWarnings("serial")
	public static abstract class AbstractHcElement<T extends AbstractHcElement<T>> extends AbstractElementBean {

		protected AbstractElementBean parent;

		public AbstractElementBean getParent() {
			return parent;
		}

		public T setParent(final AbstractElementBean parent) {
			this.parent = parent;
			return (T) this;
		}

		private final KVMap attris = new KVMap();

		public T addAttribute(final String key, final Object val) {
			attris.add(key, val);
			return (T) this;
		}

		protected KVMap toMap() {
			return attris;
		}

		@Override
		public String toString() {
			return JsonUtils.toJSON(toMap(), new JSONStyle() {
				@Override
				public boolean mustProtectValue(final String s) {
					// 函数
					if (s.startsWith("function")) {
						return false;
					}
					return super.mustProtectValue(s);
				}
			});
		}
	}

	@SuppressWarnings("serial")
	public static abstract class AbstractAxis<T extends AbstractAxis<T>> extends AbstractHcElement<T> {

		private String[] categories;

		private AxisTitle title;

		private Number max, min;

		private EAxisType type;

		private Integer tickInterval;

		private HcLabels labels;

		public String[] getCategories() {
			return categories;
		}

		public T setCategories(final String[] categories) {
			this.categories = categories;
			return (T) this;
		}

		public AxisTitle getTitle() {
			return title;
		}

		public T setTitle(final AxisTitle title) {
			this.title = title;
			return (T) this;
		}

		public T setTitle(final String title) {
			return setTitle(createTitle(title));
		}

		public AxisTitle createTitle(final String text) {
			return new AxisTitle().setText(text);
		}

		public Number getMax() {
			return max;
		}

		public T setMax(final Number max) {
			this.max = max;
			return (T) this;
		}

		public Number getMin() {
			return min;
		}

		public T setMin(final Number min) {
			this.min = min;
			return (T) this;
		}

		public EAxisType getType() {
			return type == null ? EAxisType.linear : type;
		}

		public T setType(final EAxisType type) {
			this.type = type;
			return (T) this;
		}

		public Integer getTickInterval() {
			return tickInterval;
		}

		public T setTickInterval(final Integer tickInterval) {
			this.tickInterval = tickInterval;
			return (T) this;
		}

		public HcLabels getLabels() {
			return labels;
		}

		public T setLabels(final HcLabels labels) {
			this.labels = labels;
			return (T) this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getTitle()) != null) {
				kv.add("title", ((AxisTitle) val).toMap());
			}
			if ((val = getCategories()) != null) {
				kv.add("categories", val);
			}
			if ((val = getMax()) != null) {
				kv.add("max", val);
			}
			if ((val = getMin()) != null) {
				kv.add("min", val);
			}
			if ((val = getType()) != null) {
				kv.add("type", val);
			}
			if ((val = getTickInterval()) != null) {
				kv.add("tickInterval", val);
			}
			if ((val = getLabels()) != null) {
				kv.add("labels", ((HcLabels) val).toMap());
			}
			return kv;
		}

		public static class AxisTitle extends AbstractHcText<AxisTitle> {

			private int x;
			private int y;

			public int getX() {
				return x;
			}

			public AxisTitle setX(final int x) {
				this.x = x;
				return this;
			}

			public int getY() {
				return y;
			}

			public AxisTitle setY(final int y) {
				this.y = y;
				return this;
			}

			public AxisTitle() {
			}

			@Override
			protected KVMap toMap() {
				final KVMap kv = super.toMap();
				int val;
				if ((val = getX()) != 0) {
					kv.put("x", val);
				}
				if ((val = getY()) != 0) {
					kv.put("y", val);
				}
				return kv;
			}
		}
	}

	@SuppressWarnings("serial")
	public static class AbstractHcLabels<T extends AbstractHcLabels<T>> extends AbstractHcElement<T> {
		private Boolean enabled;

		private Integer x, y;

		private String format, formatter;

		private Integer rotation;

		public Boolean getEnabled() {
			return enabled;
		}

		public T setEnabled(final Boolean enabled) {
			this.enabled = enabled;
			return (T) this;
		}

		public Integer getX() {
			return x;
		}

		public T setX(final Integer x) {
			this.x = x;
			return (T) this;
		}

		public Integer getY() {
			return y;
		}

		public T setY(final Integer y) {
			this.y = y;
			return (T) this;
		}

		public Integer getRotation() {
			return rotation;
		}

		public T setRotation(final Integer rotation) {
			this.rotation = rotation;
			return (T) this;
		}

		/**
		 * this.percentage Stacked series and pies only. The point's percentage of the
		 * total.
		 * 
		 * this.point The point object. The point name, if defined, is available through
		 * this.point.name.
		 * 
		 * this.series: The series object. The series name is available through
		 * this.series.name.
		 * 
		 * this.total Stacked series only. The total value at this point's x value.
		 * 
		 * this.x: The x value.
		 * 
		 * this.y: The y value.
		 * 
		 * @return
		 */
		public String getFormat() {
			return format;
		}

		public T setFormat(final String format) {
			this.format = format;
			return (T) this;
		}

		public String getFormatter() {
			return formatter;
		}

		public T setFormatter(final String formatter) {
			this.formatter = formatter;
			return (T) this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getEnabled()) != null) {
				kv.add("enabled", val);
			}
			if ((val = getFormat()) != null) {
				kv.add("format", val);
			}
			if ((val = getFormatter()) != null) {
				kv.add("formatter", val);
			}
			if ((val = getX()) != null) {
				kv.add("x", val);
			}
			if ((val = getY()) != null) {
				kv.add("y", val);
			}
			if ((val = getRotation()) != null) {
				kv.add("rotation", val);
			}
			return kv;
		}
	}

	@SuppressWarnings("serial")
	public static abstract class AbstractHcText<T extends AbstractHcText<T>> extends AbstractHcElement<T> {

		private String text;

		private ETextAlign align;

		private String style;

		public String getText() {
			return text;
		}

		public T setText(final String text) {
			this.text = text;
			return (T) this;
		}

		public ETextAlign getAlign() {
			return align;
		}

		public T setAlign(final ETextAlign align) {
			this.align = align;
			return (T) this;
		}

		public String getStyle() {
			return style;
		}

		public T setStyle(final String style) {
			this.style = style;
			return (T) this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val = getText();
			if (val != null) {
				kv.add("text", val);
			}
			if ((val = getAlign()) != null) {
				kv.add("align", val);
			}
			if ((val = getStyle()) != null) {
				kv.add("style", val);
			}
			return kv;
		}

		abstract static class _HcSubtitle<T extends AbstractHcText<T>> extends AbstractHcText<T> {
			private Number x;

			private Number y;

			private Boolean useHTML;

			public Number getX() {
				return x;
			}

			public T setX(final Number x) {
				this.x = x;
				return (T) this;
			}

			public Number getY() {
				return y;
			}

			public T setY(final Number y) {
				this.y = y;
				return (T) this;
			}

			public Boolean getUseHTML() {
				return useHTML;
			}

			public T setUseHTML(final Boolean useHTML) {
				this.useHTML = useHTML;
				return (T) this;
			}

			@Override
			protected KVMap toMap() {
				final KVMap kv = super.toMap();
				Object val;
				if ((val = getX()) != null) {
					kv.add("x", val);
				}
				if ((val = getY()) != null) {
					kv.add("y", val);
				}
				if ((val = getUseHTML()) != null) {
					kv.add("useHTML", val);
				}
				return kv;
			}
		}
	}

	@SuppressWarnings("serial")
	public static abstract class AbstractMarker<T extends AbstractMarker<T>> extends AbstractHcElement<T> {
		private Boolean enabled;

		private String lineColor;
		private Integer lineWidth;

		private String fillColor;

		private Integer radius;

		public Boolean getEnabled() {
			return enabled;
		}

		public T setEnabled(final Boolean enabled) {
			this.enabled = enabled;
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

		public String getFillColor() {
			return fillColor;
		}

		public T setFillColor(final String fillColor) {
			this.fillColor = fillColor;
			return (T) this;
		}

		public Integer getRadius() {
			return radius;
		}

		public T setRadius(final Integer radius) {
			this.radius = radius;
			return (T) this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getEnabled()) != null) {
				kv.add("enabled", val);
			}
			if ((val = getLineColor()) != null) {
				kv.add("lineColor", val);
			}
			if ((val = getLineWidth()) != null) {
				kv.add("lineWidth", val);
			}
			if ((val = getFillColor()) != null) {
				kv.add("fillColor", val);
			}
			if ((val = getRadius()) != null) {
				kv.add("radius", val);
			}
			return kv;
		}
	}
}
