<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.plupload.PluploadUtils"%>
<%@ page import="net.simpleframework.mvc.common.element.LinkButton"%>
<%
	final ComponentParameter nCP = PluploadUtils.get(request, response);
	final String beanId = nCP.hashId();
%>
<div class="swfupload">
  <%=ComponentRenderUtils.genParameters(nCP)%>
  <div class="swf_btns">
    <%=PluploadUtils.genBtnsHTML(nCP)%>
  </div>
  <div id="message_<%=beanId%>" class="message"></div>
  <div id="fileQueue_<%=beanId%>" class="queue"></div>
</div>
<script type="text/javascript">
<%=PluploadUtils.genJavascript(nCP)%>
</script>