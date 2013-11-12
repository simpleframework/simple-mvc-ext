package net.simpleframework.mvc.component.ext.messagewindow;

import java.util.Collection;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JsonForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageAction extends DefaultAjaxRequestHandler {

	@Override
	public IForward ajaxProcess(final ComponentParameter cp) {
		final JsonForward json = new JsonForward();
		final ComponentParameter nCP = MessageWindowUtils.get(cp);
		final IMessageWindowHandle messageWindowHandle = (IMessageWindowHandle) nCP
				.getComponentHandler();
		if (DefaultMessageWindowHandler.class.equals(messageWindowHandle.getClass())) {
			json.put("showMessageNotification", true);
		} else {
			final Collection<MessageNotification> coll = messageWindowHandle
					.getMessageNotifications(nCP);
			json.put("showMessageNotification", coll != null && coll.iterator().hasNext());
		}
		return json;
	}
}