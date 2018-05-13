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

	config.removePlugins = 'elementspath';
	// 扩展插件
	config.extraPlugins = '_syntaxhighlight,_attach,tableresize,image';

	CKEDITOR.on('dialogDefinition', function(ev) {
		var dialogName = ev.data.name;
		var dialogDefinition = ev.data.definition;

		if (dialogName == 'table') {
			var info = dialogDefinition.getContents('info');

			info.get('txtWidth')['default'] = '100%'; 
			//info.get('txtBorder')['default'] = '0';
		}
	});
};
