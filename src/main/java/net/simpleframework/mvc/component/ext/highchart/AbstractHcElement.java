package net.simpleframework.mvc.component.ext.highchart;

import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.lib.net.minidev.json.JSONStyle;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * www.highcharts.com/license
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHcElement<T extends AbstractHcElement<T>> extends AbstractElementBean {

	protected AbstractElementBean parent;

	public AbstractHcElement(final XmlElement beanElement) {
		super(beanElement);
	}

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
