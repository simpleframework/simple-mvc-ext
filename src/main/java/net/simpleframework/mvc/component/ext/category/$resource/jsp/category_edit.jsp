<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.category.CategoryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = CategoryUtils.get(request, response);
	final String beanId = nCP.hashId();
	final String categoryName = (String) nCP.getComponentName();
%>
<div class="CategoryEdit">
  <div class="c" id="idCategoryEdit_<%=beanId%>">
    <form id="idCategoryForm_<%=beanId%>"></form>
  </div>
  <div class="b">
    <input type="button" class="button2" onclick="$Actions['<%=categoryName%>_save']()" value="#(Button.Ok)" /> <input type="button"
      value="#(Button.Cancel)" onclick="$win(this).close();" />
  </div>
</div>