<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.attachments.AttachmentUtils"%>
<%
	final ComponentParameter cp = AttachmentUtils
			.get(request, response);
	final String beanId = cp.hashId();
	final String attachmentName = (String) cp.getComponentName();
%>
<div class="attachment_edit simple_window_tcb">
  <div class="c" id="af_<%=beanId%>">
    <%=AttachmentUtils.toAttachFormHTML(cp)%>
  </div>
  <div class="b">
    <input type="button" id="btn_<%=beanId%>" value="#(Button.Ok)" class="button2"
      onclick="$Actions['<%=attachmentName%>_edit_Save']();" /> <input type="button"
      value="#(Button.Cancel)" onclick="$win(this).close();" />
  </div>
</div>