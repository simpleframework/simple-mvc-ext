<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.attachments.AttachmentUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.mvc.component.ext.attachments.IAttachmentHandler"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%
	final ComponentParameter cp = AttachmentUtils
			.get(request, response);
	final String beanId = cp.hashId();
	final IAttachmentHandler handle = (IAttachmentHandler) cp
			.getComponentHandler();
	final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
%>
<div class="Comp_Attachment">
  <%=readonly ? ComponentRenderUtils.genParameters(cp)
					: "<div id=\"attachment_" + beanId + "\"></div>"%>
  <div id="attachment_list_<%=beanId%>"><%=handle.toAttachmentListHTML(cp)%></div>
  <%=handle.toInsertHTML(cp)%>
</div>