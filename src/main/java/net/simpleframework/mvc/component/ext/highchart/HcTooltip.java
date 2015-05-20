package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractHcElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class HcTooltip extends AbstractHcElement<HcTooltip> {

	private Boolean shared, useHTML;

	private String headerFormat, pointFormat, footerFormat;

	private Integer valueDecimals;

	private String valuePrefix, valueSuffix;

	public HcTooltip() {
	}

	public Boolean getShared() {
		return shared;
	}

	public HcTooltip setShared(final Boolean shared) {
		this.shared = shared;
		return this;
	}

	public Boolean getUseHTML() {
		return useHTML;
	}

	public HcTooltip setUseHTML(final Boolean useHTML) {
		this.useHTML = useHTML;
		return this;
	}

	public String getHeaderFormat() {
		return headerFormat;
	}

	public HcTooltip setHeaderFormat(final String headerFormat) {
		this.headerFormat = headerFormat;
		return this;
	}

	public String getPointFormat() {
		return pointFormat;
	}

	public HcTooltip setPointFormat(final String pointFormat) {
		this.pointFormat = pointFormat;
		return this;
	}

	public String getFooterFormat() {
		return footerFormat;
	}

	public HcTooltip setFooterFormat(final String footerFormat) {
		this.footerFormat = footerFormat;
		return this;
	}

	public Integer getValueDecimals() {
		return valueDecimals;
	}

	public HcTooltip setValueDecimals(final Integer valueDecimals) {
		this.valueDecimals = valueDecimals;
		return this;
	}

	public String getValuePrefix() {
		return valuePrefix;
	}

	public HcTooltip setValuePrefix(final String valuePrefix) {
		this.valuePrefix = valuePrefix;
		return this;
	}

	public String getValueSuffix() {
		return valueSuffix;
	}

	public HcTooltip setValueSuffix(final String valueSuffix) {
		this.valueSuffix = valueSuffix;
		return this;
	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		Object val;
		if ((val = getHeaderFormat()) != null) {
			kv.put("headerFormat", val);
		}
		if ((val = getPointFormat()) != null) {
			kv.put("pointFormat", val);
		}
		if ((val = getFooterFormat()) != null) {
			kv.put("footerFormat", val);
		}
		if ((val = getShared()) != null) {
			kv.put("shared", val);
		}
		if ((val = getUseHTML()) != null) {
			kv.put("useHTML", val);
		}
		if ((val = getValueDecimals()) != null) {
			kv.put("valueDecimals", val);
		}
		if ((val = getValuePrefix()) != null) {
			kv.put("valuePrefix", val);
		}
		if ((val = getValueSuffix()) != null) {
			kv.put("valueSuffix", val);
		}
		return kv;
	}
}
