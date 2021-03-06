package net.simpleframework.mvc.component.ext.chosen;

import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
@ComponentName(ChosenRegistry.CHOSEN)
@ComponentBean(ChosenBean.class)
@ComponentRender(ChosenRender.class)
@ComponentResourceProvider(ChosenResourceProvider.class)
public class ChosenRegistry extends AbstractComponentRegistry {
	public static final String CHOSEN = "chosen";

}
