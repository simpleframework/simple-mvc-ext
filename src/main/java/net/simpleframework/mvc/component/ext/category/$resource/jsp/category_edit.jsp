<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.category.CategoryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = CategoryUtils.get(request, response);
	final String beanId = nCP.hashId();
	final String categoryName = (String) nCP.getComponentName();
%>
<div class="CategoryEdit simple_window_tcb">
  <div class="c">
    <form id="idCategoryEdit_<%=beanId%>"></form>
  </div>
  <div class="b">
    <input type="button" class="button2" onclick="$Actions['<%=categoryName%>_save']()"
      value="#(Button.Ok)" /> <input type="button" value="#(Button.Cancel)"
      onclick="$win(this).close();" />
  </div>
</div>
<style type="text/css">
.CategoryEdit .formeditor {
	border: 0;
	border-bottom: 1px solid #bbb;
	-moz-border-radius: 0;
	-webkit-border-radius: 0;
	border-radius: 0;
}

.CategoryEdit .c {
	padding: 0;
}
</style>