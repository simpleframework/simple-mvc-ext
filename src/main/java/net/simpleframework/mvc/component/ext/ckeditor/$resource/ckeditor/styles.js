/**
 * Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/terms-of-use/#open-source-licences
 */

// This file contains style definitions that can be used by CKEditor plugins.
//
// The most common use for it is the "stylescombo" plugin which shows the Styles drop-down
// list containing all styles in the editor toolbar. Other plugins, like
// the "div" plugin, use a subset of the styles for their features.
//
// If you do not have plugins that depend on this file in your editor build, you can simply
// ignore it. Otherwise it is strongly recommended to customize this file to match your
// website requirements and design properly.
//
// For more information refer to: https://docs.ckeditor.com/ckeditor4/docs/#!/guide/dev_styles-section-style-rules

CKEDITOR.stylesSet.add( 'default', [
	/* Block styles */

	// These styles are already available in the "Format" drop-down list ("format" plugin),
	// so they are not needed here by default. You may enable them to avoid
	// placing the "Format" combo in the toolbar, maintaining the same features.
	/*
	{ name: 'Paragraph',		element: 'p' },
	{ name: 'Heading 1',		element: 'h1' },
	{ name: 'Heading 2',		element: 'h2' },
	{ name: 'Heading 3',		element: 'h3' },
	{ name: 'Heading 4',		element: 'h4' },
	{ name: 'Heading 5',		element: 'h5' },
	{ name: 'Heading 6',		element: 'h6' },
	{ name: 'Preformatted Text',element: 'pre' },
	{ name: 'Address',			element: 'address' },
	*/
  
	{ name: '标题1',   element: 'p', attributes: { 'class' : 'html-editor-topic1' } },
	{ name: '标题2',   element: 'p', attributes: { 'class' : 'html-editor-topic2' } },
	{ name: '标题3',   element: 'p', attributes: { 'class' : 'html-editor-topic3' } },
	
	{ name: '边框1',   element: 'p', attributes: { 'class' : 'html-editor-border1' } },
	{ name: '边框2',   element: 'p', attributes: { 'class' : 'html-editor-border2' } },
	{ name: '边框3',   element: 'p', attributes: { 'class' : 'html-editor-border3' } },
	{ name: '边框4',   element: 'p', attributes: { 'class' : 'html-editor-border4' } },
	
	{ name: '按钮1',   element: 'span', attributes: { 'class' : 'html-editor-button1' } },
	{ name: '按钮2',   element: 'span', attributes: { 'class' : 'html-editor-button2' } },
	
	{ name: '强调1',   element: 'span', attributes: { 'class' : 'html-editor-strong1' } },
	{ name: '强调2',   element: 'span', attributes: { 'class' : 'html-editor-strong2' } },
	{ name: '强调3',   element: 'span', attributes: { 'class' : 'html-editor-strong3' } },
	{ name: '强调4',   element: 'span', attributes: { 'class' : 'html-editor-strong4' } },
  
	{ name: '引用',    element: 'q' },
	{ name: '删除',    element: 'del' },
	{ name: '插入',    element: 'ins' },
  
	{ name: '图片居左',  element: 'img',  attributes: { 'class': 'html-editor-img-left' } },
	{ name: '图片居右',  element: 'img',  attributes: { 'class': 'html-editor-img-right' } },
  
	{
	    name: '带边框表格',
	    element: 'table',
	    attributes: {
	      align: 'center',
	      cellpadding: '5',
	      cellspacing: '0',
	      border: '1',
	      bordercolor: '#ccc'
	    },
	    styles: {
	      'border-collapse': 'collapse'
	    }
	},
	{ 
	    name: '无边框表格',   
	    element: 'table', 
	    styles: { 
	      'border-style': 'hidden', 
	      'background-color': '#E6E6FA' 
	    } 
	},
  
	/*
	{ name: '',   element: 'div', attributes: { 'class' : 'html-editor-line1' } },
	{ name: '',   element: 'div', attributes: { 'class' : 'html-editor-line2' } },
	  */
	/* Inline styles */

	// These are core styles available as toolbar buttons. You may opt enabling
	// some of them in the Styles drop-down list, removing them from the toolbar.
	// (This requires the "stylescombo" plugin.)
	/*
	{ name: 'Strong',			element: 'strong', overrides: 'b' },
	{ name: 'Emphasis',			element: 'em'	, overrides: 'i' },
	{ name: 'Underline',		element: 'u' },
	{ name: 'Strikethrough',	element: 'strike' },
	{ name: 'Subscript',		element: 'sub' },
	{ name: 'Superscript',		element: 'sup' },
	
	{ name: 'Marker',			element: 'span', attributes: { 'class': 'marker' } },
	
	{ name: 'Big',				element: 'big' },
	{ name: 'Small',			element: 'small' },
	{ name: 'Typewriter',		element: 'tt' },

	{ name: 'Computer Code',	element: 'code' },
	{ name: 'Keyboard Phrase',	element: 'kbd' },
	{ name: 'Sample Text',		element: 'samp' },
	{ name: 'Variable',			element: 'var' },

	{ name: 'Deleted Text',		element: 'del' },
	{ name: 'Inserted Text',	element: 'ins' },

	{ name: 'Cited Work',		element: 'cite' },
	{ name: 'Inline Quotation',	element: 'q' },

	{ name: 'Language: RTL',	element: 'span', attributes: { 'dir': 'rtl' } },
	{ name: 'Language: LTR',	element: 'span', attributes: { 'dir': 'ltr' } },
	*/
	
	/* Object styles */
	{ name: 'Square Bulleted List',	element: 'ul',		styles: { 'list-style-type': 'square' } },

	/* Widget styles */
	/*
	{ name: 'Clean Image', type: 'widget', widget: 'image', attributes: { 'class': 'image-clean' } },
	{ name: 'Grayscale Image', type: 'widget', widget: 'image', attributes: { 'class': 'image-grayscale' } },

	{ name: 'Featured Snippet', type: 'widget', widget: 'codeSnippet', attributes: { 'class': 'code-featured' } },

	{ name: 'Featured Formula', type: 'widget', widget: 'mathjax', attributes: { 'class': 'math-featured' } },

	{ name: '240p', type: 'widget', widget: 'embedSemantic', attributes: { 'class': 'embed-240p' }, group: 'size' },
	{ name: '360p', type: 'widget', widget: 'embedSemantic', attributes: { 'class': 'embed-360p' }, group: 'size' },
	{ name: '480p', type: 'widget', widget: 'embedSemantic', attributes: { 'class': 'embed-480p' }, group: 'size' },
	{ name: '720p', type: 'widget', widget: 'embedSemantic', attributes: { 'class': 'embed-720p' }, group: 'size' },
	{ name: '1080p', type: 'widget', widget: 'embedSemantic', attributes: { 'class': 'embed-1080p' }, group: 'size' },

	// Adding space after the style name is an intended workaround. For now, there
	// is no option to create two styles with the same name for different widget types. See https://dev.ckeditor.com/ticket/16664.
	{ name: '240p ', type: 'widget', widget: 'embed', attributes: { 'class': 'embed-240p' }, group: 'size' },
	{ name: '360p ', type: 'widget', widget: 'embed', attributes: { 'class': 'embed-360p' }, group: 'size' },
	{ name: '480p ', type: 'widget', widget: 'embed', attributes: { 'class': 'embed-480p' }, group: 'size' },
	{ name: '720p ', type: 'widget', widget: 'embed', attributes: { 'class': 'embed-720p' }, group: 'size' },
	{ name: '1080p ', type: 'widget', widget: 'embed', attributes: { 'class': 'embed-1080p' }, group: 'size' }
  */
] );

