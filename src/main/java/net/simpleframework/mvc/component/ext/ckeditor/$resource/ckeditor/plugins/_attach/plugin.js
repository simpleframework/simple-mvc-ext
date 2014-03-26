CKEDITOR.plugins.add('_attach', {

  lang : [ 'zh-cn', 'en' ],

  init : function(editor) {
    editor.addCommand('Attach', {

      exec : function(editor) {
      }
    });

    editor.ui.addButton('Attach', {
      label : editor.lang._attach.addAttach,
      command : 'Attach',
      icon : this.path + 'icons/code.png',
    });
  }
});