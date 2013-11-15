package net.simpleframework.mvc.component.ext.messagewindow;

import java.io.Serializable;
import java.util.Date;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class MessageNotification implements Serializable {
	private String subject, textBody;

	private Date sentDate;

	public MessageNotification(final String subject, final String textBody) {
		setSubject(subject).setTextBody(textBody).setSentDate(new Date());
	}

	public String getSubject() {
		return subject;
	}

	public MessageNotification setSubject(final String subject) {
		this.subject = subject;
		return this;
	}

	public String getTextBody() {
		return textBody;
	}

	public MessageNotification setTextBody(final String textBody) {
		this.textBody = textBody;
		return this;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public MessageNotification setSentDate(final Date sentDate) {
		this.sentDate = sentDate;
		return this;
	}

	private static final long serialVersionUID = -7879721950189354452L;
}
