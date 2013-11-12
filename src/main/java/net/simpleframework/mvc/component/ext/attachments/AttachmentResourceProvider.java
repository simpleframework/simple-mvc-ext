package net.simpleframework.mvc.component.ext.attachments;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentRegistry;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttachmentResourceProvider extends AbstractComponentResourceProvider {

	public AttachmentResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/attachment.css" };
	}
}
