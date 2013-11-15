/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */

function $category_action(item) {
  var o = function(ele, c) {
    return ele.hasClassName(c) ? ele : ele.up("." + c);
  };
  if (Object.isElement(item)) {
    item = $Target(item);
    var act = o(item, "Category").action;
    act.currentBranch = o(item, "tafelTreecontent").branch;
  } else {
    var act = o(item.obj, "Category").action;
    act.currentBranch = item;
  }
  return act;
}

function $category_addMethods(pa, categoryName) {

  pa.getId = function() {
    var branch = pa.currentBranch;
    return branch ? branch.getId() : "";
  };
  
  pa.add = function(params) {
    var act = $Actions[categoryName + "_edit"];
    act.selector = pa.selector;
    act(("category_parentId=" + pa.getId()).addParameter(params));
  };

  pa.edit = function(params) {
    var act = $Actions[categoryName + "_edit"];
    act.selector = pa.selector;
    act(("category_id=" + pa.getId()).addParameter(params));
  };

  pa.del = function(params) {
    var act = $Actions[categoryName + "_delete"];
    act.selector = pa.selector;
    act(("category_id=" + pa.getId()).addParameter(params));
  };

  pa.move = function(up, last) {
    var act = $Actions[categoryName + "_move"];
    act.selector = pa.selector;
    var branch = pa.currentBranch;
    act(last ? $tree_move2(branch, up) : $tree_move(branch, up));
  };

  pa.expand = function() { pa.currentBranch.expand(); };

  pa.expandAll = function() { pa.treeAction.expandAll(); };

  pa.collapse = function() { pa.currentBranch.collapse(); };

  pa.collapseAll = function() { pa.treeAction.collapseAll(); };
}
