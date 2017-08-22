<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.comments.CommentUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%
	final ComponentParameter cp = CommentUtils.get(request, response);
  final String componentName = cp.getComponentName();
  final int maxlength = (Integer) cp.getBeanProperty("maxlength");
%>
<div class="Comp_Comment">
  <%=ComponentRenderUtils.genParameters(cp)%>
  <%=CommentUtils.toCommentHTML(cp)%>
</div>
<script type="text/javascript">
	$ready(function() {
    var ta = $("id<%=componentName%>_textarea");
    if (!ta)
      return;
    var h = ta.up(".t1_head");
    var parent = ta.next();
    var reply = h.down(".reply");
    var num = h.down(".num");
    
    var comment_ta_valchange = function(evn) {
      var vlen = ta.value.length;
      var maxlength = <%=maxlength%>;
      var l = Math.max(maxlength - vlen, 0);
      if (vlen == 0) {
        ta.ltxt.innerHTML = "&nbsp;";
      } else {
        ta.ltxt.innerHTML = "#(comment.jsp.0)<label>" + l + "</label>#(comment.jsp.1)";
      }
      
      if (vlen > maxlength) {
        ta.value = ta.value.substring(0, maxlength);
      }  
    };
    
    var ltxt = h.down(".ltxt");
    ta.ltxt = ltxt;
    ta.observe("keyup", comment_ta_valchange);
  
    window.$COMMENT = {
      reply : function(val, txt) {
        parent.value = val;
        reply.innerHTML = 
              "<span class='reply_1'>" +
                txt +
                "<span class='del'></span>" +
              "</span>";
        reply.down(".del").observe("click", function(evn) {
          reply.innerHTML = "";
          parent.value = "";
        });
        reply.scrollTo();
        ta.focus();
      },
  
      doCallback : function(n) {
        ta.clear();
        ta.next().clear();
        
        reply.innerHTML = "";
        num.innerHTML = n;
        
        ta.ltxt.innerHTML = "&nbsp;";
        
        var act = $Actions['<%=componentName%>_pager'];
        act.jsLoadedCallback = function() {
        	$('.Comp_Comment .t1_comments').scrollTo();  
        };
        act();
      }
    };
  });
</script>