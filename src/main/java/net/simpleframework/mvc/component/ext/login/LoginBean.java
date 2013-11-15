package net.simpleframework.mvc.component.ext.login;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.PageDocument;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LoginBean extends AbstractContainerBean {

	private boolean showAutoLogin = BeanDefaults.getBool(getClass(), "showAutoLogin", true);

	private boolean showResetAction = BeanDefaults.getBool(getClass(), "showResetAction", true);

	private ETextAlign actionAlign = (ETextAlign) BeanDefaults.get(getClass(), "actionAlign",
			ETextAlign.right);

	private String loginForward = BeanDefaults.getString(getClass(), "loginForward", "/");

	private String passwordGetUrl;

	private String jsLoginCallback;

	public LoginBean(final PageDocument pageDocument, final XmlElement element) {
		super(pageDocument, element);
		setSelector("#_loginForm");
		try {
			setHandleClass(ClassUtils
					.forName("net.simpleframework.organization.web.component.login.DefaultLoginHandler"));
		} catch (final ClassNotFoundException e) {
		}
	}

	public LoginBean(final PageDocument pageDocument) {
		this(pageDocument, null);
	}

	public boolean isShowAutoLogin() {
		return showAutoLogin;
	}

	public LoginBean setShowAutoLogin(final boolean showAutoLogin) {
		this.showAutoLogin = showAutoLogin;
		return this;
	}

	public boolean isShowResetAction() {
		return showResetAction;
	}

	public LoginBean setShowResetAction(final boolean showResetAction) {
		this.showResetAction = showResetAction;
		return this;
	}

	public String getLoginForward() {
		return loginForward;
	}

	public LoginBean setLoginForward(final String loginForward) {
		this.loginForward = loginForward;
		return this;
	}

	public ETextAlign getActionAlign() {
		return actionAlign;
	}

	public LoginBean setActionAlign(final ETextAlign actionAlign) {
		this.actionAlign = actionAlign;
		return this;
	}

	public String getPasswordGetUrl() {
		return passwordGetUrl;
	}

	public LoginBean setPasswordGetUrl(final String passwordGetUrl) {
		this.passwordGetUrl = passwordGetUrl;
		return this;
	}

	public String getJsLoginCallback() {
		return jsLoginCallback;
	}

	public LoginBean setJsLoginCallback(final String jsLoginCallback) {
		this.jsLoginCallback = jsLoginCallback;
		return this;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "loginForward", "jsLoginCallback" };
	}
}
