<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.login.LoginUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentUtils"%>
<%@ page import="net.simpleframework.mvc.component.ext.login.ILoginHandler"%>
<%
	final ComponentParameter nCP = LoginUtils.get(request, response);
	final ILoginHandler lHdl = (ILoginHandler) nCP
			.getComponentHandler();
	final String beanId = nCP.hashId();
%>
<form id="_loginForm">
  <input type="hidden" id="<%=LoginUtils.BEAN_ID%>" name="<%=LoginUtils.BEAN_ID%>"
    value="<%=beanId%>" /> <input type="hidden" id="_accountType" name="_accountType" />
  <div class="lm">
    <a id="_accountMenu"></a><span class="right_down_menu"></span>
  </div>
  <div>
    <input id="_accountName" name="_accountName" type="text" class="ifocus" />
  </div>
  <div>
    <label>#(login.0)</label>
  </div>
  <div>
    <input id="_passwordName" name="_passwordName" type="password" class="ifocus" />
  </div>
  <div><%=lHdl.getToolbarHTML(nCP)%></div>
  <div style="text-align: <%=nCP.getBeanProperty("actionAlign")%>">
    <input id="_loginBtn" class="button2" type="submit" value="#(login.3)"
      onclick="$Actions['arLogin']();" />
    <%
    	if ((Boolean) nCP.getBeanProperty("showResetAction")) {
    		out.append("<input type='reset' onclick=\"this.up('form').reset();\" />");
    	}
    %>
  </div>
</form>
<script type="text/javascript">
  var _AccountTypeMSG = {
    "normal" : "#(login.4)",
    "email" : "#(login.5)",
    "mobile" : "#(login.6)"
  };

  function _changeAccountType(type) {
    if (!type) {
      type = "normal";
    }
    document.setCookie("_account_type", type, 24 * 365);

    $("_accountType").value = type;
    var m = $("_accountMenu").update(_AccountTypeMSG[type]);
    m.className = "login_icon_" + type;
  }

  function _save_cookie() {
    document.setCookie("_account_name", encodeURIComponent($F("_accountName")),
        24 * 365);
    var _autoLogin = $("_autoLogin");
    if (_autoLogin && $F(_autoLogin) == "true") {
      document.setCookie("_account_pwd", $F("_passwordName"), 24 * 14);
    }
  }

  $ready(function() {
    _changeAccountType(document.getCookie("_account_type"));
    var name = document.getCookie("_account_name");
    if (name) {
      $("_accountName").value = decodeURIComponent(name);
    }
  });
</script>
