package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.common.element.ETextAlign;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHcText<T extends AbstractHcText<T>> extends AbstractHcElement<T> {

	private String text;

	private ETextAlign align;

	private String style;

	public AbstractHcText(final XmlElement beanElement) {
		super(beanElement);
	}

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

		public _HcSubtitle(final XmlElement beanElement) {
			super(beanElement);
		}

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
