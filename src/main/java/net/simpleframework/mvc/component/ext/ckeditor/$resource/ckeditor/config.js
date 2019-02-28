/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights
 *          reserved. For licensing, see LICENSE.html or
 *          http://ckeditor.com/license
 */
CKEDITOR.editorConfig = function(config) {
  config.skin = 'moono-lisa';

  config.font_names = "宋体/SimSun;新宋体/NSimSun;仿宋_GB2312/FangSong_GB2312;\
                                                 楷体_GB2312/KaiTi_GB2312;黑体/SimHei;微软雅黑/Microsoft YaHei;\
                                                 幼圆/YouYuan;华文彩云/STCaiyun;华文行楷/STXingkai;\
                                                 方正舒体/FZShuTi;方正姚体/FZYaoti;"
      + config.font_names;

  config.smiley_descriptions = [];
  config.smiley_columns = 15;
  config.smiley_images = [];
  for (var i = 0; i <= 89; i++) {
    config.smiley_images.push(i + '.gif');
  }

  // 移除word拷贝样式
  config.pasteFromWordRemoveFontStyles = true;

  config.allowedContent = {
    $1 : {
      elements : CKEDITOR.dtd,
      attributes : true,
      styles : true,
      classes : true
    }
  };
  config.disallowedContent = 'script; *[on*]; p{margin*}';

  // 编辑器的z-index值
  config.baseFloatZIndex = 10000;

  // 拖拽以改变尺寸
  config.resize_enabled = true;
  // 是否开启 图片和表格 的改变大小的功能
  config.disableObjectResizing = false;
  
  // 行高
  config.line_height="1;1.1;1.2;1.3;1.4;1.5;1.6;1.8;2.0;2.1;2.4;2.8;3.0";

  config.removePlugins = 'elementspath';
  // 扩展插件
  config.extraPlugins = '_syntaxhighlight,_attach,lineheight,balloontoolbar,balloonpanel,image2,tableresize,html5audio,html5video';

  CKEDITOR.on('dialogDefinition', function(ev) {
    var dialogName = ev.data.name;
    var dialogDefinition = ev.data.definition;

    if (dialogName == 'table') {
      var info = dialogDefinition.getContents('info');

      info.get('txtWidth')['default'] = '100%';
      // info.get('txtBorder')['default'] = '0';
    }
  });

  CKEDITOR.on("instanceReady", function(ev) {
    var editor = ev.editor;
    editor.balloonToolbars.create({
      buttons : 'Link,Unlink,Image',
      widgets : 'image'
    });
    editor.balloonToolbars.create({
      buttons : 'Bold,Italic,Link,Unlink',
      cssSelector : 'a'
//      priority : CKEDITOR.plugins.balloontoolbar.PRIORITY.HIGH
    });
  });
  
  // Mobile
  if (isMobile.any()) {
    config.enableContextMenu = false;
  }
};
