CKEDITOR.plugins.add('_attach', {
  lang : [ 'zh-cn', 'en' ],
  
  init : function(editor) {
    editor.addCommand('Attach', {
      exec : function(editor) {
        $call(editor._action);
        if (UI.defaultWM) {
          var win = UI.defaultWM.stack.windows.last();
          win.element.setStyle("display: table;");
          win.setZIndex(10000 + win.zIndex);          
        }
      }
    });
    editor.ui.addButton('Attach', {
      command : 'Attach',
      label : editor._label || editor.lang._attach.addAttach,
      icon : this.path + 'icons/attach.png'
    });
  }
});