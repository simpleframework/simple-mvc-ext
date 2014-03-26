CKEDITOR.plugins.add('_attach', {

  lang : [ 'zh-cn', 'en' ],

  init : function(editor) {
    editor.addCommand('Attach', {
      exec : function(editor) {
        var act = $Actions[editor.attachAction];
        if (act) {
          act();
        } else {
          $call(editor.attachAction);
        }
      }
    });

    editor.ui.addButton('Attach', {
      label : editor.lang._attach.addAttach,
      command : 'Attach',
      icon : this.path + 'icons/attach.png',
    });
  }
});