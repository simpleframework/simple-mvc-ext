<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.ext.syntaxhighlighter.SyntaxHighlighterUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%@ page import="net.simpleframework.common.StringUtils"%>
<%
	final ComponentParameter nCP = SyntaxHighlighterUtils.get(request, response);
	String jsSelectedCallback = (String) nCP.getBeanProperty("jsSelectedCallback");
%>
<%=SyntaxHighlighterUtils.toShWindowHTML(nCP) %>
<script type="text/javascript">
  var _sh_window = $Actions['window_<%=nCP.hashId()%>'];
  
  var sh_comp = {
    insert : function() {
      var script = ('<pre class=\"brush: ' + $F('sh_window_lang').toLowerCase()
          + '; gutter: ' + $('sh_window_opt1').checked + '; html-script: '
          + $('sh_window_opt2').checked + ';\">'
          + $F('sh_window_textarea').escapeHTML() + '</pre>').makeElement();
          
      var act = $Actions['<%=nCP.getComponentName()%>'];    
      if (act.jsSelectedCallback) {
        if (act.jsSelectedCallback(script))
          this.close();
      } else {
        <%if (StringUtils.hasText(jsSelectedCallback)) {%>
          if ((function(script) {
            <%=jsSelectedCallback%>
          })(script))
            this.close();
        <%} else {%>
          alert('#(sh_window.3)');
        <%}%>
      }
    },
    
    close : function() {
      _sh_window.close();
    }
  };
  
  (function() {
    var ta = $("sh_window_textarea");
    $UI.fixTextareaTab(ta);
    
    if (_sh_window._sh_data) {
      var d = _sh_window._sh_data;
      $Actions.setValue(ta, d.ta);
      $Actions.setValue("sh_window_lang", d.brush);
      $Actions.setValue("sh_window_opt1", d.gutter);
      $Actions.setValue("sh_window_opt2", d["html-script"]);
    }
  })();
</script>