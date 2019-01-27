package net.simpleframework.mvc.component.ext.plupload;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class PluploadUtils {
	public static final String BEAN_ID = "plupload_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String genJavascript(final ComponentParameter cp) {
		final PluploadBean plupload = (PluploadBean) cp.componentBean;
		final String beanId = plupload.hashId();
		final StringBuilder sb = new StringBuilder();
		final String hpath = ComponentUtils.getResourceHomePath(PluploadBean.class);
		sb.append("var act = $Actions['" + cp.getComponentName() + "'];");
		sb.append("var uploader = new plupload.Uploader({");
		if (cp.isMobile()) {
			sb.append(" runtimes : 'html5',");
		} else {
			sb.append(" runtimes : 'html5,html4,flash',");
		}
		sb.append(" file_data_name : 'Filedata',");
		sb.append(" multi_selection : ").append(cp.getBeanProperty("multiFileSelected")).append(",");
		sb.append(" flash_swf_url : '").append(hpath).append("/flash/Moxie.swf").append("',");
		sb.append(" browse_button : 'placeholder_").append(beanId).append("',");
		sb.append(" url : '").append(hpath).append("/jsp/plupload_action.jsp;jsessionid=")
				.append(cp.getSessionId()).append("?").append(BEAN_ID).append("=").append(beanId)
				.append("',");

		sb.append(" filters: {");
		String fileTypes = (String) cp.getBeanProperty("fileTypes");
		if (StringUtils.hasText(fileTypes)) {
			fileTypes = StringUtils.replace(fileTypes, "*.", "");
			fileTypes = StringUtils.replace(fileTypes, ";", ",");
			sb.append("mime_types : [");
			sb.append("{ title : '").append(cp.getBeanProperty("fileTypesDesc"))
					.append("', extensions : '").append(fileTypes).append("'}");
			sb.append("],");
		}
		final String fileSizeLimit = (String) cp.getBeanProperty("fileSizeLimit");
		if (StringUtils.hasText(fileSizeLimit)) {
			sb.append("max_file_size: \"").append(fileSizeLimit).append("\",");
		}
		sb.append("prevent_duplicates : false");
		sb.append(" },");

		sb.append(" init: {");
		// FilesAdded
		sb.append("FilesAdded: function(up, files) {");
		sb.append("  var queue = $(\"fileQueue_").append(beanId).append("\");");
		sb.append("  plupload.each(files, function(file) {");
		sb.append("  var osize = file.origSize;");
		sb.append("  var html =\"<div id='item_\" + file.id + \"' class='item'>");
		sb.append("    <table width='100%' cellpadding='0' cellspacing='0'>");
		sb.append("      <tr><td>");
		sb.append("        <table width='100%' cellpadding='0' cellspacing='0'><tr>");
		sb.append("          <td><span class='fn'>\" + file.name + \"<\\/span>");
		sb.append(
				"            <span class='fs'>\" + (osize ? osize.toFileString() : '') + \"<\\/span><\\/td>");
		sb.append("          <td width='30px;' align='right'>");
		sb.append("            <div class='delete_image'><\\/div>");
		sb.append("          <\\/td>");
		sb.append("        <\\/tr><\\/table>");
		sb.append("      <\\/td><\\/tr>");
		sb.append("      <tr><td>");
		sb.append("        <div class='bar'><\\/div>");
		sb.append("      <\\/td><\\/tr>");
		sb.append("      <tr><td>");
		sb.append("        <div class='message'><\\/div>");
		sb.append("      <\\/td><\\/tr>");
		sb.append("    <\\/table><\\/div>\";");
		sb.append("  queue.insert(html);");
		sb.append("  var item = $(\"item_\" + file.id);");
		sb.append("  item.down(\".delete_image\").observe(\"click\", function(e) {");
		sb.append("    var fo = up.getFile(file.id);");
		sb.append("    if (fo.status == plupload.UPLOADING && !confirm(\"")
				.append($m("SwfUploadUtils.5")).append("\")) return;");
		sb.append("    up.removeFile(file.id);");
		sb.append("    $Effect.remove(item);");
		sb.append("  });");
		sb.append("  item.bar = new $UI.ProgressBar(item.down(\".bar\"), {");
		sb.append("    maxProgressValue: file.origSize,");
		sb.append("    startAfterCreate: false,");
		sb.append("    showAbortAction: false,");
		sb.append("    showDetailAction: false");
		sb.append("  });");
		sb.append("});");
		sb.append("act.startUpload();");
		sb.append("},");

		// UploadProgress
		sb.append("UploadProgress: function(up, file) {");
		sb.append("  var item = $(\"item_\" + file.id);");
		sb.append("  if (item.bar) {");
		sb.append("    item.bar.setProgress(file.loaded);");
		sb.append("  }");
		sb.append("  if (file.loaded >= file.origSize) {");
		sb.append("    item.down(\".message\").update(\"").append($m("SwfUploadUtils.1"))
				.append("\");");
		sb.append("  }");
		sb.append("},");

		// FileUploaded
		sb.append("FileUploaded: function(up, file, serverData) {");
		sb.append("  var json = serverData.response.evalJSON();");
		sb.append("  var item = $(\"item_\" + file.id);");
		sb.append("  var message = item.down(\".message\");");
		sb.append("  if (json[\"error\"]) {");
		sb.append("    message.update(json[\"error\"]);");
		sb.append("  } else {");
		sb.append("    message.update(\"").append($m("SwfUploadUtils.6")).append("\");");
		sb.append("    (function() { $Effect.remove(item); }).delay(2);");
		sb.append("  }");
		sb.append("  var hasQueued = message.up(\".queue\").select(\".item\").any(function(item) {");
		sb.append("	   var fo = up.getFile(item.id.substring(5));");
		sb.append("    return fo && fo.status == plupload.QUEUED;");
		sb.append("  });");
		sb.append("  act.jsCompleteCallback.delay(0.1, hasQueued);");
		sb.append("},");

		// Error
		sb.append("Error: function(up, errObject) {");
		sb.append(" var msgc = $(\"message_").append(beanId).append("\");");
		sb.append(" var errorCode = errObject.code;");
		sb.append(" var file = errObject.file;");
		sb.append(" if (errorCode == plupload.FILE_SIZE_ERROR) {");
		sb.append("  msgc.update('\"' + file.name + '\" ")
				.append(SwfUploadUtils.msg("SwfUploadUtils.2", fileSizeLimit)).append("');");
		sb.append(" } else {");
		sb.append("  msgc.update(errObject.message);");
		sb.append(" }");
		sb.append(" $Effect.shake(msgc);");
		sb.append(" (function() { msgc.update(\"\"); }).delay(2);");
		sb.append("},");

		sb.append(" }");
		sb.append("});");
		sb.append("uploader.init();");

		sb.append("act.startUpload = function() {");
		sb.append("var params = \"\".addSelectorParameter(\"");
		sb.append(cp.getBeanProperty("selector")).append("\");");
		sb.append("uploader.setOption('multipart_params', params.toQueryParams());");
		sb.append("uploader.start();");
		sb.append("};");

		sb.append("act.updateUI = function() {");
		sb.append("  $$(\"#fileQueue_").append(beanId).append(" .item\").each(function(item) {");
		sb.append("  	if (item.bar) item.bar.updateUI();");
		sb.append("  });");
		sb.append("};");

		sb.append("act.jsCompleteCallback = function(hasQueued) {");
		sb.append(StringUtils.blank(cp.getBeanProperty("jsCompleteCallback")));
		sb.append("};");
		return JavascriptUtils.wrapWhenReady(sb.toString());
	}

	public static String upload(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		return SwfUploadUtils.upload(get(request, response));
	}

	public static String genBtnsHTML(final ComponentParameter cp) {
		return SwfUploadUtils.genBtnsHTML(cp);
	}
}