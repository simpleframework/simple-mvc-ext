<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.attachments.AttachmentUtils"%>
<%
	final ComponentParameter cp = AttachmentUtils
			.get(request, response);
	final String beanId = cp.hashId();
	final String attachmentName = (String) cp.getComponentName();
%>
<div class="simple_window_tcb">
  <div class="c" id="af_<%=beanId%>">
    <table class="form_tbl">
      <tr>
        <td class="l">#(attachment_edit.0)</td>
        <td class="v"><input type="hidden" id="attach_id" name="attach_id" /><input
          type="text" id="attach_topic" name="attach_topic" /></td>
      </tr>
    </table>
    <table class="form_tbl" style="margin-top: -1px;">
      <tr>
        <td class="l">#(attachment_edit.1)</td>
        <td class="v"><textarea rows="4" id="attach_desc" name="attach_desc"></textarea></td>
      </tr>
    </table>
  </div>
  <div class="b">
    <div style="text-align: right; margin-top: 6px;">
      <input type="button" id="btn_<%=beanId%>" value="#(Button.Ok)" class="button2"
        onclick="$Actions['<%=attachmentName%>_edit_Save']();" /> <input type="button"
        value="#(Button.Cancel)" onclick="$win(this).close();" />
    </div>
  </div>
</div>