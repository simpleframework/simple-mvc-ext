CKEDITOR.plugins.add('_attach', {
  lang : [ 'zh-cn', 'en' ],
  
  init : function(editor) {
    editor.addCommand('Attach', {
      exec : function(editor) {
        $call(editor._action);
      }
    });
    editor.ui.addButton('Attach', {
      command : 'Attach',
      label : editor._label || editor.lang._attach.addAttach,
      icon : this.path + 'icons/attach.png'
    });
  }
});