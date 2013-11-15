package net.simpleframework.mvc.component.ext.messagewindow;

import java.util.Collection;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.window.IWindowHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IMessageWindowHandle extends IWindowHandler {

	Collection<MessageNotification> getMessageNotifications(ComponentParameter cp);
}
