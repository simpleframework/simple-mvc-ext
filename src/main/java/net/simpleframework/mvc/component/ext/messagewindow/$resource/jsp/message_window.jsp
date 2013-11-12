<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%@ page import="net.simpleframework.common.Convert"%>
<%@ page import="net.simpleframework.mvc.component.ext.messagewindow.MessageNotification"%>
<%@ page import="net.simpleframework.mvc.component.ext.messagewindow.IMessageWindowHandle"%>
<%@ page import="net.simpleframework.mvc.component.ext.messagewindow.MessageWindowUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = MessageWindowUtils.get(request,
			response);
	final IMessageWindowHandle mh = (IMessageWindowHandle) nCP
			.getComponentHandler();
	if (mh == null) {
		return;
	}

	final Collection<MessageNotification> l = mh
			.getMessageNotifications(nCP);
	if (l == null || l.size() == 0) {
%>
<div class="messageWindow_no_result">#(message_window.0)</div>
<%
	return;
	}
	final int i = Convert.toInt(request.getParameter("mIndex"));
	final String next = i < l.size() - 1 ? "next" : "next2";
	final String pre = i > 0 ? "pre" : "pre2";
	final MessageNotification mn = new ArrayList<MessageNotification>(l)
	.get(i);
	final String componentName = (String) nCP
	.getComponentName();
%>
<div class="messageWindow">
  <div class="tb">
    <table style="width: 100%;" cellpadding="0" cellspacing="0">
      <tr>
        <td width="40px;">
          <div class="icon"></div>
        </td>
        <td class="subject"><%=StringUtils.blank(mn.getSubject())%></td>
        <td width="50px;">
          <div class="<%=next%>" title="#(message_window.1)"
            onclick="__message_view_click(this, <%=i + 1%>);"></div>
          <div class="<%=pre%>" title="#(message_window.2)"
            onclick="__message_view_click(this, <%=i - 1%>);"></div>
        </td>
      </tr>
    </table>
  </div>
  <div class="c"><%=StringUtils.blank(mn.getTextBody())%></div>
</div>
<script type="text/javascript">
  function __message_view_click(obj, i) {
    if (obj.hasClassName("next") || obj.hasClassName("pre")) {
      $Actions["<%=componentName%>"].refreshContentRef("mIndex=" + i);
    }
  }
</script>
