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