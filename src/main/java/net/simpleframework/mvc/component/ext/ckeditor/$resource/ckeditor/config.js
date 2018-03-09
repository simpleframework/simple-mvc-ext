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

	config.allowedContent = true;

	config.removePlugins = 'elementspath';

	// 扩展插件
	config.extraPlugins = '_syntaxhighlight,_attach';
};
