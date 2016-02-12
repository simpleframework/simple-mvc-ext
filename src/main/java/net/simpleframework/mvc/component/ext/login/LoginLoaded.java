package net.simpleframework.mvc.component.ext.login;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.permission.PermissionConst;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.EWarnType;
import net.simpleframework.mvc.component.base.validation.ValidationBean;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ui.menu.EMenuEvent;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.validatecode.ValidateCodeBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class LoginLoaded extends DefaultPageHandler {

	@Override
	public Object getBeanProperty(final PageParameter pp, final String beanProperty) {
		if ("role".equals(beanProperty)) {
			return PermissionConst.ROLE_ANONYMOUS;
		}
		return super.getBeanProperty(pp, beanProperty);
	}

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		final ComponentParameter nCP = LoginUtils.get(pp);
		// 登录
		pp.addComponentBean("arLogin", AjaxRequestBean.class).setRole(PermissionConst.ROLE_ANONYMOUS)
				.setHandlerMethod("login").setHandlerClass(LoginAction.class)
				.setSelector((String) nCP.getBeanProperty("selector"));
		// 忘记密码
		final String passwordGetUrl = (String) nCP.getBeanProperty("passwordGetUrl");
		if (StringUtils.hasText(passwordGetUrl)) {
			pp.addComponentBean("ajaxGetPassword", AjaxRequestBean.class)
					.setRole(PermissionConst.ROLE_ANONYMOUS).setUrlForward(passwordGetUrl);
			pp.addComponentBean("getPasswordWindow", WindowBean.class)
					.setContentRef("ajaxGetPassword").setTitle($m("login.7"))
					.setWidth(Convert.toInt(pp.getParameter("width"), 420))
					.setHeight(Convert.toInt(pp.getParameter("height"), 320));
		}
		// 验证码
		if ((Boolean) nCP.getBeanProperty("showValidateCode")) {
			pp.addComponentBean("LoginLoaded_vcode", ValidateCodeBean.class).setInputName("vcode")
					.setContainerId("idLoginLoaded_vcode");
		}
		if ((Boolean) nCP.getBeanProperty("showAccountType")) {
			// 菜单
			final MenuBean menuBean = (MenuBean) pp.addComponentBean("accountMenu", MenuBean.class)
					.setMenuEvent(EMenuEvent.click).setWidth("100").setSelector("#_accountMenu");
			menuBean
					.addItem(
							MenuItem.of($m("login.4"), "login_icon_normal").setOnclick(
									"_changeAccountType('normal')"))
					.addItem(MenuItem.sep())
					.addItem(
							MenuItem.of($m("login.5"), "login_icon_email").setOnclick(
									"_changeAccountType('email')"))
					.addItem(MenuItem.sep())
					.addItem(
							MenuItem.of($m("login.6"), "login_icon_mobile").setOnclick(
									"_changeAccountType('mobile')"));
		}
		// 验证
		pp.addComponentBean("loginValidation", ValidationBean.class).setTriggerSelector("#_loginBtn")
				.setWarnType(EWarnType.insertAfter)
				.addValidators(new Validator(EValidatorMethod.required, "#_accountName"));
	}
}
