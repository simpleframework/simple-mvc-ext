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
    var _comment = {
      audioplay : function(obj, src) {
        var chg = function(img, h) {
          if (img) {
            var isrc = img.src;
            img.src = isrc.substring(0, isrc.lastIndexOf('/'))
                + (h ? "/sound2.png" : "/sound.png");
          }
        };

        var audio = $("simple_audio_temp_player");
        if (!audio) {
          audio = new Element("audio", {
            id : "simple_audio_temp_player",
            style : "display: none;"
          });
          document.body.appendChild(audio);

          audio.observe("ended", function(e) {
            audio._playing = false;
            var _obj = audio._obj;
            if (_obj) {
              chg(_obj.down("img"), false);
            }
          });
        }

        audio.src = src;

        var playing;
        if (audio._playing && audio._obj == obj) {
          audio._playing = playing = false;
        } else {
          audio.play();
          audio._playing = playing = true;
        }
        audio._obj = obj;

        var img2;
        if (obj) {
          img2 = obj.down("img");
          obj.up(".pager").select(".audio-attach img").each(function(img) {
            chg(img, false);
          });
        }

        chg(img2, playing);
      }
    };
    window.$COMMENT = _comment;

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
  
    _comment.reply = function(val, txt) {
      parent.value = val;
      reply.innerHTML = 
            "<span class='reply_1'>" +
              txt +
              "<img src='<%=ipath%>/images/del.png' class='del' />" +
            "</span>";
      reply.down(".del").observe("click", function(evn) {
        reply.innerHTML = "";
        parent.value = "";
      });
      ta.focus();
    };
    
    _comment.doCallback = function(n, fixed) {
      ta.clear();
      ta.next().clear();
      
      reply.innerHTML = "";
      num.innerHTML = n;
      
      ta.ltxt.innerHTML = "&nbsp;";
      
      var act = $Actions['<%=componentName%>_pager'];
      act.jsLoadedCallback = function() {
        h.next().scrollTo();
      };
      act();
    };
  });
</script>