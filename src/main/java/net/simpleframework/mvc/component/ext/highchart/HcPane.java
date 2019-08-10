package net.simpleframework.mvc.component.ext.highchart;

import java.util.List;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.mvc.component.ext.highchart.AbstractHcClass.AbstractHcElement;

/**
 * 
 * @开发者 taolm
 * @日期 2019年8月8日 上午10:40:42
 * @功能说明 Applies only to polar charts and angular gauges. This configuration
 *       object holds general options for the combined X and Y axes set. Each
 *       xAxis or yAxis can reference the pane by index.
 */
public class HcPane extends AbstractHcElement<HcPane> {

	private static final long serialVersionUID = 7014444073503932714L;

	private List<PaneBackground> background;
	private String[] center;
	private String size;
	private int startAngle;
	private int endAngle;

	public List<PaneBackground> getBackground() {
		return background;
	}

	public HcPane setBackground(final List<PaneBackground> background) {
		this.background = background;
		return this;
	}

	public String[] getCenter() {
		return center;
	}

	public HcPane setCenter(final String[] center) {
		this.center = center;
		return this;
	}

	public String getSize() {
		return size;
	}

	public HcPane setSize(final String size) {
		this.size = size;
		return this;
	}

	public int getStartAngle() {
		return startAngle;
	}

	public HcPane setStartAngle(final int startAngle) {
		this.startAngle = startAngle;
		return this;
	}

	public int getEndAngle() {
		return endAngle;
	}

	public HcPane setEndAngle(final int endAngle) {
		this.endAngle = endAngle;
		return this;
	}

	public static class PaneBackground extends AbstractHcElement<PaneBackground> {

		private static final long serialVersionUID = 768589443064816040L;

		private String backgroundColor;
		private String borderColor;
		private int borderWidth = 1;
		private String className;
		private String innerRadius;
		private String outerRadius;
		private EShape shape = EShape.solid;

		public String getBackgroundColor() {
			return backgroundColor;
		}

		public PaneBackground setBackgroundColor(final String backgroundColor) {
			this.backgroundColor = backgroundColor;
			return this;
		}

		public String getBorderColor() {
			return borderColor;
		}

		public PaneBackground setBorderColor(final String borderColor) {
			this.borderColor = borderColor;
			return this;
		}

		public int getBorderWidth() {
			return borderWidth;
		}

		public PaneBackground setBorderWidth(final int borderWidth) {
			this.borderWidth = borderWidth;
			return this;
		}

		public String getClassName() {
			return className;
		}

		public PaneBackground setClassName(final String className) {
			this.className = className;
			return this;
		}

		public String getInnerRadius() {
			return innerRadius;
		}

		public PaneBackground setInnerRadius(final String innerRadius) {
			this.innerRadius = innerRadius;
			return this;
		}

		public String getOuterRadius() {
			return outerRadius;
		}

		public PaneBackground setOuterRadius(final String outerRadius) {
			this.outerRadius = outerRadius;
			return this;
		}

		public PaneBackground setShape(final EShape shape) {
			this.shape = shape;
			return this;
		}

		public EShape getShape() {
			return shape;
		}

		public static enum EShape {
			solid, arc
		}

		@Override
		protected KVMap toMap() {
			final KVMap kv = super.toMap();
			Object val;
			if ((val = getBackgroundColor()) != null) {
				kv.put("backgroundColor", val);
			}
			if ((val = getBorderColor()) != null) {
				kv.put("borderColor", val);
			}
			if (getBorderWidth() != 1) {
				kv.put("borderWidth", getBorderWidth());
			}
			if ((val = getClassName()) != null) {
				kv.put("className", val);
			}
			if ((val = getInnerRadius()) != null) {
				kv.put("innerRadius", val);
			}
			if ((val = getOuterRadius()) != null) {
				kv.put("outerRadius", val);
			}
			kv.put("shape", getShape());
			return kv;
		}

	}

	@Override
	protected KVMap toMap() {
		final KVMap kv = super.toMap();
		final List<PaneBackground> backgrounds = getBackground();
		if (backgrounds != null && backgrounds.size() > 0) {
			if (backgrounds.size() == 1) {
				kv.put("background", backgrounds.get(0));
			} else {
				kv.put("background", backgrounds.toArray());
			}
		}
		Object val;
		if ((val = getCenter()) != null) {
			kv.put("center", val);
		}
		if ((val = getSize()) != null) {
			kv.put("size", val);
		}
		if ((val = getStartAngle()) != null) {
			kv.put("startAngle", val);
		}
		if (getEndAngle() != 0) {
			kv.put("endAngle", getEndAngle());
		}
		return kv;
	}

}
