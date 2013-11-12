<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.category.CategoryUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.common.Convert"%>
<%
	final ComponentParameter nCP = CategoryUtils.get(
	request, response);
	final String beanId = nCP.hashId();
	final String categoryName = (String) nCP
	.getComponentName();
	final boolean runImmediately = Convert.toBool(nCP
	.getBeanProperty("runImmediately"));
%>
<div class="Category">
  <%=ComponentRenderUtils.genParameters(nCP)%>
  <div id="category_<%=beanId%>"></div>
</div>
<script type="text/javascript">
	$ready(function() {
		var action = $Actions["<%=categoryName%>"];
		<%=ComponentRenderUtils.genJSON(nCP, "action")%>
		action.selector = 
      "<%=nCP.getBeanProperty("selector")%>";
      
   	action.treeAction = $Actions["<%=categoryName%>_tree"];
   	action.treeAction.jsLoadedCallback = action.jsLoadedCallback;
    <% if (runImmediately) { %>action.treeAction();<% } %>
   	
		var ele = $("category_<%=beanId%>").up(".Category");
		ele.action = action;
		
		$category_addMethods(action, "<%=categoryName%>");
	});
</script>