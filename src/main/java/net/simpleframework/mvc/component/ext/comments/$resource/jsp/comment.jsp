<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.comments.CommentUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%
	final ComponentParameter cp = CommentUtils.get(request, response);
%>
<div class="Comp_Comment">
  <%=ComponentRenderUtils.genParameters(cp)%>
  <%=CommentUtils.toCommentHTML(cp)%>
</div>
