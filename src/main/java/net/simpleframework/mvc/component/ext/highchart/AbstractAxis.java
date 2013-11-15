package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.xml.XmlElement;
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
public abstract class AbstractAxis<T extends AbstractAxis<T>> extends AbstractHcElement<T> {

	private String[] categories;

	private AxisTitle title;

	private Integer max, min;

	private EAxisType type;

	private Integer tickInterval;

	private HcLabels labels;

	public AbstractAxis(final XmlElement beanElement) {
		super(beanElement);
	}

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

	public Integer getMax() {
		return max;
	}

	public T setMax(final Integer max) {
		this.max = max;
		return (T) this;
	}

	public Integer getMin() {
		return min;
	}

	public T setMin(final Integer min) {
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
		public AxisTitle() {
			this(null);
		}

		public AxisTitle(final XmlElement beanElement) {
			super(beanElement);
		}
	}
}
