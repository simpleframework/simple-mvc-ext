package net.simpleframework.mvc.component.ext.login;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.ctx.common.bean.BeanDefaults;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.component.AbstractContainerBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LoginBean extends AbstractContainerBean {

	private boolean showAccountType = BeanDefaults.getBool(getClass(), "showAccountType", true);
	private boolean showAutoLogin = BeanDefaults.getBool(getClass(), "showAutoLogin", true);
	private boolean showResetAction = BeanDefaults.getBool(getClass(), "showResetAction", true);
	private boolean showValidateCode = BeanDefaults.getBool(getClass(), "showValidateCode", false);

	private ETextAlign actionAlign = (ETextAlign) BeanDefaults.get(getClass(), "actionAlign",
			ETextAlign.right);

	private String loginForward = BeanDefaults.getString(getClass(), "loginForward", "/");

	private String passwordGetUrl;

	private String jsLoginCallback;

	public boolean isShowAccountType() {
		return showAccountType;
	}

	public LoginBean setShowAccountType(final boolean showAccountType) {
		this.showAccountType = showAccountType;
		return this;
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

	public boolean isShowValidateCode() {
		return showValidateCode;
	}

	public void setShowValidateCode(final boolean showValidateCode) {
		this.showValidateCode = showValidateCode;
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

	{
		setSelector("#_loginForm");
		try {
			setHandlerClass(ClassUtils.forName(
					"net.simpleframework.organization.web.component.login.DefaultLoginHandler"));
		} catch (final ClassNotFoundException e) {
		}
	}
}
