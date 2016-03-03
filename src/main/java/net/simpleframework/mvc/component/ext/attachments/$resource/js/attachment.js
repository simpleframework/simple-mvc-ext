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
  }
};