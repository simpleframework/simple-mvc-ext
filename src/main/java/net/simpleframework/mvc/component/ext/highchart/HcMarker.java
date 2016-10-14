package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractMarker;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcMarker extends AbstractMarker<HcMarker> {
	private static final long serialVersionUID = 1685005277540929902L;

	private Integer height, width;

	private HcMarkerState hover;
	private HcMarkerState select;

	public Integer getHeight() {
		return height;
	}

	public HcMarker setHeight(final Integer height) {
		this.height = height;
		return this;
	}

	public Integer getWidth() {
		return width;
	}

	public HcMarker setWidth(final Integer width) {
		this.width = width;
		return this;
	}

	public HcMarkerState getHover() {
		return hover;
	}

	public HcMarker setHover(final HcMarkerState hover) {
		this.hover = hover;
		return this;
	}

	public HcMarkerState getSelect() {
		return select;
	}

	public HcMarker setSelect(final HcMarkerState select) {
		this.select = select;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getHeight()) != null) {
			kv.add("height", val);
		}
		if ((val = getWidth()) != null) {
			kv.add("width", val);
		}
		final HcMarkerState hover = getHover();
		final HcMarkerState select = getSelect();
		if (hover != null || select != null) {
			final KVMap states = new KVMap();
			kv.add("states", states);
			if (hover != null) {
				states.add("hover", hover);
			}
			if (select != null) {
				states.add("select", select);
			}
		}
		return kv;
	}

	public static class HcMarkerState extends AbstractMarker<HcMarker> {
		private static final long serialVersionUID = 5261945005207435164L;

		private Integer lineWidthPlus;
		private Integer radiusPlus;

		public Integer getLineWidthPlus() {
			return lineWidthPlus;
		}

		public HcMarkerState setLineWidthPlus(final Integer lineWidthPlus) {
			this.lineWidthPlus = lineWidthPlus;
			return this;
		}

		public Integer getRadiusPlus() {
			return radiusPlus;
		}

		public HcMarkerState setRadiusPlus(final Integer radiusPlus) {
			this.radiusPlus = radiusPlus;
			return this;
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getLineWidthPlus()) != null) {
				kv.add("lineWidthPlus", val);
			}
			if ((val = getRadiusPlus()) != null) {
				kv.add("radiusPlus", val);
			}
			return kv;
		}
	}
}
