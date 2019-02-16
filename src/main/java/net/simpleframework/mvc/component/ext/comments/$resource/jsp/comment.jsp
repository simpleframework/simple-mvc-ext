<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.mvc.component.ext.comments.CommentUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentRenderUtils"%>
<%@ page import="net.simpleframework.mvc.component.ext.comments.CommentBean"%>
<%
	final ComponentParameter cp = CommentUtils.get(request, response);
	final String componentName = cp.getComponentName();
	final int maxlength = (Integer) cp.getBeanProperty("maxlength");
	final String ipath = cp.getCssResourceHomePath(CommentBean.class);
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
    
    var _placeholder = ta.getAttribute("placeholder");
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
        ta.ltxt.innerHTML = "#(comment.jsp.0)<label>" 
        		+ l 
        		+ "</label>#(comment.jsp.1)";
        if (!isMobile.any()) {
          ta.ltxt.innerHTML += "#(comment.jsp.2)";
        }
      }
      
      if (vlen > maxlength) {
        ta.value = ta.value.substring(0, maxlength);
      }  
    };
    
    var ltxt = h.down(".ltxt");
    ta.ltxt = ltxt;
    ta.observe("keyup", comment_ta_valchange);
    
    var submit_btn = ta.up('.l2').next('.l3').down('.simple_btn');
    var inputEvent = function(ev) {
    	if (ta.value.trim() == '') {
    		submit_btn.addClassName("disabled");
    	} else {
    		submit_btn.removeClassName("disabled");
    	}
    };
    ta.observe("input", inputEvent);
    inputEvent();
    
    var hideSmiley = function(ev) {
    	var smiley = h.down('.smiley'); 
    	if (smiley.visible()) 
    		smiley.hide();
    }
    ta.observe("focus", hideSmiley);
  
    var clearReply = function(evn) {
    	ta.setAttribute("placeholder", _placeholder);
    	parent.value = "";
    	reply.innerHTML = "";
    };
    
    window.$COMMENT = {
      insert_smiley : function(img) {
        var s = img.src;
        var j = s.substring(s.lastIndexOf('/') + 1, s.lastIndexOf('.'));
        $Actions.setValue(ta, '[:em' + j + ']', true);
        inputEvent();
        comment_ta_valchange();
      }, 
      
      show_smiley : function(a) {
        var smiley = a.up('.l3').previous('.smiley'); 
        if (smiley) { 
          if (smiley.visible())
            smiley.hide();
          else
            smiley.show();
      	}
      },
    	
      reply : function(val, txt) {
      	if (ta.getAttribute("placeholder") != txt) {
      		parent.value = val;
          reply.innerHTML = 
                "<span class='reply_1'>" + txt +
                "<img src='<%=ipath%>/images/del.png' class='del' />" +
                "</span>";
          reply.down(".del").observe("click", clearReply);
          ta.setAttribute("placeholder", txt);
      	}
      	ta.focus();
      },
      
      submit : function(commentName) {
      	if (submit_btn.hasClassName("disabled")) {
      		return;
      	}
      	$Actions[commentName + '_submit']($Form(h));
      },
      
      doCallback : function(n) {
        ta.clear();
        ta.next().clear();
        hideSmiley();
        inputEvent();
        
        reply.innerHTML = "";
        num.innerHTML = n ? n : parseInt(num.innerHTML) + 1;
        
        ta.ltxt.innerHTML = "&nbsp;";
        
        var act = $Actions['<%=componentName%>_pager'];
        act.jsLoadedCallback = function() {
          h.next().scrollTo();
        };
        act();
      }  
    };
  });
</script>