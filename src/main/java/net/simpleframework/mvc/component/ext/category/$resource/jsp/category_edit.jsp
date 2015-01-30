<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.category.CategoryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%
	final ComponentParameter nCP = CategoryUtils.get(request, response);
	final String beanId = nCP.hashId();
	final String categoryName = (String) nCP.getComponentName();
	final String category_id = nCP.getParameter("category_id");
	final String category_parentId = nCP
			.getParameter("category_parentId");
%>
<div class="CategoryEdit">
  <div class="c" id="idCategoryEdit_<%=beanId%>">
    <%
    	if (category_id != null) {
    		out.write("<input type='hidden' name='category_id' value='"
    				+ category_id + "' />");
    	}
    	if (category_parentId != null) {
    		out.write("<input type='hidden' name='category_parentId' value='"
    				+ category_parentId + "' />");
    	}
    %>
    <form id="idCategoryForm_<%=beanId%>"></form>
  </div>
  <div class="b">
    <input type="button" class="button2" onclick="$Actions['<%=categoryName%>_save']()" value="#(Button.Ok)" /> <input type="button"
      value="#(Button.Cancel)" onclick="$win(this).close();" />
  </div>
</div>