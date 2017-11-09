<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.attachments.AttachmentHistoryLoaded"%>
<%@ page import="net.simpleframework.mvc.component.ext.attachments.AttachmentUtils"%>
<%=AttachmentHistoryLoaded.toHTML(AttachmentUtils.get(request, response))%>