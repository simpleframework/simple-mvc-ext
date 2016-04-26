<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.userselect.UserSelectUtils"%>
<%@ page import="net.simpleframework.mvc.component.ext.userselect.UserSelectBean"%>
<%@ page import="net.simpleframework.mvc.component.ui.dictionary.DictionaryRender"%>
<%
	final ComponentParameter cp = UserSelectUtils
			.get(request, response);
	final String componentName = (String) cp.getComponentName();
	final String hashId = cp.hashId();
%>
<div class="user_select">
  <%
  	String vtype = cp.getParameter("vtype");
  	boolean bTree = UserSelectBean.VT_TREE.equals(vtype);
  	if (bTree) {
  %>
  <div class="ttop"><%=UserSelectUtils.toTypeHTML(cp)%></div>
  <%
  	}
  %>
  <div id="users_<%=hashId%>">#(user_select.4)</div>
  <div class="b">
    <input type="button" class="button2" value="#(Button.Ok)" onclick="$Actions['<%=componentName%>'].doDblclick();" /> <input type="button"
      value="#(Button.Cancel)" onclick="$Actions['<%=componentName%>'].close();" />
  </div>
</div>
<script type="text/javascript">
  $ready(function() {
    var us = $Actions['<%=componentName%>'];
    var w = us.window;
    w.content.setStyle("overflow:hidden;");
    
    var tAct, tp;
    <%if (bTree) {%>
    var tree = $("users_<%=hashId%>");
    var s = function() {
      tree.setStyle("height:" + (w.content.getHeight() - 68) + "px;");
    };
    
    tAct = $Actions["<%=componentName + "_tree"%>"];
    us.doDblclick = function(branch) {
      var selects = $tree_getSelects(tAct.tree, branch);
      <%=DictionaryRender.genSelectCallback(cp, "selects")%>
    };
    <%} else {%>
   	tp = $Actions["<%=componentName + "_tablePager"%>"];
    var s = function() {
      tp.setHeight(w.content.getHeight() - 93);
    };
    us.doDblclick = function(d) {
      var selects = new Array();
      var arr = tp.checkArr();
      if (arr && arr.length > 0) {
        arr.each(function(d2) {
          selects.push({
            id : tp.rowId(d2),
            text : d2.readAttribute("userText"),
            row: d2
          });
        });
      } else {
        d = d || tp.pager.down('.titem.titem_selected');
        if (d) {
          selects.push({
            id : tp.rowId(d),
            text : d.readAttribute("userText"),
            row: d
          });
        }
      }
      if (selects.length == 0) {
        alert("#(user_select.0)");
      }
      <%=DictionaryRender.genSelectCallback(cp, "selects")%>
    };
    <%}%>
    s();
    w.observe("resize:ended", s);
    
    var _evn = function(ev) {
      if (tp) {
        tp.checkAll(this);
      } else if (tAct) {
        tAct.checkAll(this.checked);
      }
    };
    var _click = function() {
      var checkAll = $("<%=componentName%>_check_all");
      if (checkAll)
        checkAll.observe("click", _evn);
    };
    _click();
    if (tp) {
      tp.jsLoadedCallback = _click;
    }
  });
</script>