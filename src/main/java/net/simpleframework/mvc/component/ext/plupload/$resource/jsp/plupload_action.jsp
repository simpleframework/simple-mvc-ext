<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.plupload.PluploadUtils"%>
<%
	out.write(PluploadUtils.upload(request, response)); 
%>
