var AttachmentUtils = {
  doMove : function(item, act, up) {
    item = $Target(item);
    var row = item.up(".fitem");
    var row2 = up ? row.previous() : row.next();
    if (!row2) {
      alert($MessageConst["Error.Move"]);
      return;
    }
    var arr = [ row.getAttribute("rowid"), row2.getAttribute("rowid") ];
    $Actions[act]('rowIds=' + arr.join(";"));
  },

  doMove2 : function(item, act, up) {
    item = $Target(item);
    var row = item.up(".fitem");
    var arr = [];
    while (row) {
      arr.push(row.getAttribute("rowid"));
      row = up ? row.previous() : row.next();
    }
    if (arr.length < 2) {
      alert($MessageConst["Error.Move"]);
      return;
    }
    $Actions[act]('rowIds=' + arr.join(";"));
  },

  show_player : function(au) {
    var fitem = au.up('.fitem');
    var player = fitem.down(".audio-player");
    if (player) {
      player.setStyle("bottom: " + (fitem.measure("bottom") - 1) + "px;");
      player.show();
    }
  },

  init_player : function(au) {
    var fitem = au.up('.fitem');
    var player = fitem.down(".audio-player");
    if (!player || player._onclick) {
      return;
    }
    player._onclick = player.onclick = function(e) {
      var x;
      if (e.touches) {
        var t = e.touches[0];
        x = t.pageX;
      } else {
        var mouse = Event.pointer(e);
        x = mouse.x;
      }
      var pos = player.cumulativeOffset();
      var rx = x - pos.left;
      var sound = au.sound;
      sound.seek((rx / player.getWidth()) * sound.duration());
      if (!sound.playing()) {
        sound.play();
      }
    };
  },

  hide_player : function(au) {
    var player = au.up('.fitem').down(".audio-player");
    if (player) {
      player.hide();
    }
  },

  play_next : function(au, pre) {
    var fitem = au.up('.fitem');
    var ele = pre ? fitem.previous(".fitem") : fitem.next(".fitem");
    if (ele) {
      var play = ele.down(".play");
      if (play) {
        play.simulate("click");
      }
    }
  },
  
  gpath : function(au, path) {
    return au.src.substring(0, au.src.lastIndexOf('/')) + path;
  },

  doLoad : function(cc) {
    var gpath = this.gpath;
    
    var show_player = this.show_player;
    var init_player = this.init_player;
    var hide_player = this.hide_player;
    var play_next = this.play_next;

    setInterval(
        function() {
          var au = window._au;
          if (au) {
            var fitem = au.up('.fitem');
            var player = fitem.down(".audio-player");
            if (player) {
              var dot = player.down('.dot');
              var time = au.sound.seek();
              var w = (player.getWidth() / au.sound.duration()) * time;
              var nw = player.getWidth() - dot.getWidth() + 1;
              dot.setStyle("left: " + Math.min(w, nw) + "px");

              time = parseInt(time);
              var m = parseInt(time / 60);
              var s = parseInt(time % 60) + 1;
              dot.innerHTML = (m < 10 ? "0" + m : m) + ":"
                  + (s < 10 ? "0" + s : s);
            }
          }
        }, 1000);

    $(cc).select(".l_attach .play.audio").each(function(au) {
      var durl = au.getAttribute('_durl');
      var ipath = au.src.substring(0, au.src.lastIndexOf('/'));
      au.observe('click', function(ev) {
        if (!au.sound) {
          au.sound = new Howl({
            preload : true,
            html5 : true,
            src : [ durl ],
            onplay : function() {
              if (window._au && window._au != au) {
                var sound = window._au.sound;
                if (sound.playing()) {
                  sound.stop();
                }
                hide_player(window._au);
                window._au = null;
              }
              window._au = au;
              au.src = gpath(au, "/pause.png");
              show_player(au);
              init_player(au);
            },
            onload : function() {
              au.src = gpath(au, "/play.png");
            },
            onpause : function() {
              au.src = gpath(au, "/play.png");
            },
            onstop : function() {
              au.src = gpath(au, "/play.png");
            },
            onend : function() {
              au.src = gpath(au, "/play.png");
              hide_player(au);
              play_next(au);
            }
          });
          au.src = gpath(au, "/loading.gif");
          au.sound.play();
        } else {
          if (au.sound.playing()) {
            au.sound.pause();
          } else {
            au.sound.play();
          }
        }
      });
    });
  },

  doOk : function(cc, insertTextarea, componentName, msg) {
    var attach = cc.previous();
    if (insertTextarea) {
      cc.down('input[type=checkbox]').observe('click', function(evn) {
        var _box = this;
        attach.select('input[type=checkbox]').each(function(box) {
          box.checked = _box.checked;
        });
      });

      cc.down('.simple_btn.obtn').observe(
          'click',
          function(evn) {
            var idArr = attach.select('input[type=checkbox]').inject([],
                function(r, box) {
                  if (box.checked)
                    r.push(box.id);
                  return r;
                });
            if (idArr.length == 0) {
              alert(msg.m1);
              return;
            }
            $Actions[componentName + '_selected']('ids=' + idArr.join(';'));
          });
    } else {
      cc.down('.simple_btn.obtn').observe('click', function(evn) {
        if (attach.select('.fitem').length == 0) {
          alert(msg.m1);
          return;
        }
        $Actions[componentName + '_submit']();
      });
    }
  }
};