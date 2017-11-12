package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.Convert;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.ImageUtils;
import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.MimeTypes;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.common.web.html.HtmlEncoder;
import net.simpleframework.ctx.common.bean.AttachmentFile;
import net.simpleframework.lib.it.sauronsoftware.jave.Encoder;
import net.simpleframework.lib.it.sauronsoftware.jave.MultimediaInfo;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.SessionCache;
import net.simpleframework.mvc.common.DownloadUtils;
import net.simpleframework.mvc.common.ImageCache;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.ImageElement;
import net.simpleframework.mvc.common.element.InputElement;
import net.simpleframework.mvc.common.element.JS;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;
import net.simpleframework.mvc.impl.DefaultPageResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractAttachmentHandler extends ComponentHandlerEx
		implements IAttachmentHandler {
	@Override
	public void setSwfUploadBean(final ComponentParameter cp, final SwfUploadBean swfUpload) {
		swfUpload.setMultiFileSelected(true);
		if ((Boolean) cp.getBeanProperty("imagesMode")) {
			// swfUpload
			swfUpload.setFileTypes(SwfUploadBean.IMAGES_FILETYPES)
					.setFileTypesDesc($m("AbstractAttachmentHandler.6"))
					.setUploadText($m("AbstractAttachmentHandler.7"));
		}
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		return ((KVMap) super.getFormParameters(cp)).add(AttachmentUtils.BEAN_ID, cp.hashId());
	}

	@Override
	public Map<String, AttachmentFile> attachments(final ComponentParameter cp) throws IOException {
		return getUploadCache(cp);
	}

	@Override
	public AttachmentFile getAttachmentById(final ComponentParameter cp, final String id)
			throws IOException {
		return attachments(cp).get(id);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Enum[] getAttachTypes() {
		return null;
	}

	@Override
	public JavascriptForward doSave(final ComponentParameter cp,
			final IAttachmentSaveCallback callback) throws Exception {
		final Map<String, AttachmentFile> uploads = new LinkedHashMap<>(getUploadCache(cp));
		final Set<String> deletes = new LinkedHashSet<>(getDeleteCache(cp));
		if (callback != null) {
			try {
				doImagesCropper(cp, uploads);
				doThumbnail(cp, uploads);
				callback.save(uploads, deletes);
			} catch (final Exception e) {
				clearCache(cp, uploads.keySet(), deletes);
				throw e;
			}
		}
		final int attachmentsLimit = (Integer) cp.getBeanProperty("attachmentsLimit");
		if (attachmentsLimit > 0 && attachments(cp).size() > attachmentsLimit) {
			throwAttachmentsLimit(attachmentsLimit);
		}
		// 清除
		clearCache(cp, uploads.keySet(), deletes);
		return null;
	}

	protected void doThumbnail(final ComponentParameter cp,
			final Map<String, AttachmentFile> uploads) throws IOException {
	}

	protected void doImagesCropper(final ComponentParameter cp,
			final Map<String, AttachmentFile> uploads) throws IOException {
		for (final Map.Entry<String, AttachmentFile> e : uploads.entrySet()) {
			final String cropper = cp.getParameter("cropper_" + e.getKey());
			if (!StringUtils.hasText(cropper)) {
				continue;
			}

			final Map<String, Object> data = JsonUtils.toMap(cropper);
			final AttachmentFile af = e.getValue();
			final File oFile = af.getAttachment();
			final InputStream istream = new FileInputStream(oFile);
			try {
				final int width = Convert.toInt(data.get("width"));
				final int height = Convert.toInt(data.get("height"));
				final int srcX = Convert.toInt(data.get("x"));
				final int srcY = Convert.toInt(data.get("y"));
				final BufferedImage bi = ImageUtils.clip(istream, width, height, srcX, srcY);

				String path = oFile.getAbsolutePath();
				final int index = path.lastIndexOf('.');
				if (-1 != index) {
					path = path.substring(0, index);
				}
				final File nFile = new File(path + "_.png");
				final FileOutputStream ostream = new FileOutputStream(nFile);
				try {
					ImageIO.write(bi, "png", ostream);
					e.setValue(af.setAttachment(nFile));
				} finally {
					ostream.close();
				}
			} finally {
				istream.close();
				oFile.delete();
			}
		}
	}

	protected void throwAttachmentsLimit(final int attachmentsLimit) {
		throw ComponentHandlerException.of($m("AbstractAttachmentHandler.4", attachmentsLimit));
	}

	@Override
	public void doSave(final ComponentParameter cp, final String id, final String topic,
			final int attachtype, final String description) throws Exception {
		final AttachmentFile af = getAttachmentById(cp, id);
		if (af != null) {
			af.setTopic(topic);
			if (attachtype > 0) {
				af.setType(attachtype);
			}
			af.setDescription(description);
		}
	}

	protected void clearCache(final ComponentParameter cp, final Set<String> add,
			final Set<String> delete) {
		final Map<String, AttachmentFile> addQueue = getUploadCache(cp);
		if (add == null) {
			addQueue.clear();
		} else {
			addQueue.keySet().removeAll(add);
		}
		final Set<String> deleteQueue = getDeleteCache(cp);
		if (deleteQueue != null) {
			if (delete != null) {
				deleteQueue.clear();
			} else {
				deleteQueue.removeAll(delete);
			}
		}
	}

	@Override
	public void doDelete(final ComponentParameter cp, final String id) {
		final Set<String> deleteQueue = getDeleteCache(cp);
		if (deleteQueue != null && deleteQueue.contains(id)) {
			deleteQueue.remove(id);
		} else if (getUploadCache(cp).remove(id) == null && deleteQueue != null) {
			deleteQueue.add(id);
		}
	}

	@Override
	public void doExchange(final ComponentParameter cp, final String... ids) {
		throw NotImplementedException.of(getClass(), "doExchange");
	}

	@Override
	public AttachmentFile upload(final ComponentParameter cp, final IMultipartFile multipartFile,
			final Map<String, Object> variables) throws IOException {
		final int attachmentsQueueLimit = (Integer) cp.getBeanProperty("attachmentsQueueLimit");
		final Map<String, AttachmentFile> attach = attachments(cp);
		if (attachmentsQueueLimit > 1 && attach.size() >= attachmentsQueueLimit) {
			throwAttachmentsQueueLimit(attachmentsQueueLimit);
		}
		final AttachmentFile af = new AttachmentFile(multipartFile.getFile());
		final int attachtype = getAttachtype(cp);
		if (attachtype > -1) {
			af.setType(attachtype);
		}
		final Map<String, AttachmentFile> cache = getUploadCache(cp);
		if (attachmentsQueueLimit == 1) {
			getDeleteCache(cp).addAll(attach.keySet());
			cache.clear();
		}
		cache.put(af.getId(), af);
		return af;
	}

	protected int getAttachtype(final ComponentParameter cp) {
		return -1;
	}

	protected void throwAttachmentsQueueLimit(final int attachmentsQueueLimit) {
		throw ComponentHandlerException.of($m("AbstractAttachmentHandler.5", attachmentsQueueLimit));
	}

	@SuppressWarnings("unchecked")
	protected Map<String, AttachmentFile> getUploadCache(final ComponentParameter cp) {
		final String key = "Upload_Cache_" + getCachekey(cp);
		Map<String, AttachmentFile> cache = (Map<String, AttachmentFile>) SessionCache.lget(key);
		if (cache == null) {
			SessionCache.lput(key, cache = new LinkedHashMap<>());
		}
		// 检测文件是否存在
		for (final String k : new HashSet<>(cache.keySet())) {
			final AttachmentFile aFile = cache.get(k);
			try {
				if (!aFile.getAttachment().exists()) {
					cache.remove(k);
				}
			} catch (final IOException e) {
				log.error(e);
			}
		}
		return cache;
	}

	@SuppressWarnings("unchecked")
	protected Set<String> getDeleteCache(final ComponentParameter cp) {
		final String key = "Delete_Cache_" + getCachekey(cp);
		Set<String> cache = (Set<String>) SessionCache.lget(key);
		if (cache == null) {
			SessionCache.lput(key, cache = new LinkedHashSet<>());
		}
		return cache;
	}

	protected String getCachekey(final ComponentParameter cp) {
		return cp.getComponentName();
	}

	@Override
	public String toAttachmentListHTML(final ComponentParameter cp) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final boolean imageList = (Boolean) cp.getBeanProperty("imagesMode");
		if (imageList) {
			if ((Boolean) cp.getBeanProperty("cropper")) {
				cp.addImportCSS(DefaultPageResourceProvider.class, "/cropper.css");
				cp.addImportJavascript(DefaultPageResourceProvider.class, "/js/cropper.js");
				cp.setRequestAttr("_cropper", true);
			}
			sb.append("<div class='imgc clearfix'>");
		}
		int i = 0;
		for (final Map.Entry<String, AttachmentFile> entry : attachments(cp).entrySet()) {
			final AttachmentFile attach = entry.getValue();
			final String id = entry.getKey();
			if (imageList) {
				sb.append(toAttachmentItemImagesHTML(cp, id, attach, i++));
			} else {
				sb.append(toAttachmentItemHTML(cp, id, attach, i++));
			}
		}
		if (imageList) {
			sb.append("</div>");
		}
		final StringBuilder js = new StringBuilder();
		js.append("AttachmentUtils.doLoad('attachment_list_").append(cp.hashId()).append("');");
		sb.append(JavascriptUtils.wrapScriptTag(js.toString(), true));
		return sb.toString();
	}

	protected String toAttachmentItemImagesHTML(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final int index) throws IOException {
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='iitem'>");
		sb.append(" <div id='attach_").append(id).append("' class='i_attach'>");
		sb.append(createAttachmentItem_Image(cp, id, attachment));
		sb.append(" </div>");
		sb.append(createAttachmentItem_Image_Btns(cp, id, attachment, readonly));
		sb.append("</div>");
		if (Convert.toBool(cp.getRequestAttr("_cropper"))) {
			sb.append(InputElement.hidden("cropper_" + id));
			if (getUploadCache(cp).containsKey(id)) {
				final StringBuilder js = new StringBuilder();
				js.append("var data = $('cropper_").append(id).append("');");
				js.append("var img = $('#attach_").append(id).append(" img');");
				js.append("var cropper = new Cropper(img, {");
				js.append(" dragMode : 'move',");
				js.append(" movable : false,");
				js.append(" viewMode : 3,");
				js.append(" zoomable : false,");
				js.append(" aspectRatio : ").append(cp.getBeanProperty("cropperRatio")).append(",");
				js.append(" crop: function(e) {");
				js.append("  data.value = JSON.stringify(cropper.getData());");
				js.append(" }");
				js.append("});");
				sb.append(JavascriptUtils.wrapScriptTag(js.toString(), true));
			}
		}
		return sb.toString();
	}

	protected ImageElement createAttachmentItem_Image(final PageParameter pp, final String id,
			final AttachmentFile attachmentFile) {
		final ImageElement image = new ImageElement().setClassName("image");
		try {
			final File iFile = attachmentFile.getAttachment();
			if (ImageUtils.isImage(iFile)) {
				return image.setSrc(new ImageCache().getPath(pp, attachmentFile));
			}
		} catch (final IOException e) {
			getLog().warn(e);
		}
		return image;
	}

	protected String createAttachmentItem_Image_Btns(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final boolean readonly) throws IOException {
		final StringBuilder sb = new StringBuilder();
		if (!readonly) {
			final AbstractElement<?> img = createAttachmentItem_StatusBtn(cp, id, attachment);
			if (img != null) {
				sb.append(img);
			}
			sb.append(createAttachmentItem_DelBtn(cp, id, attachment));
		}
		return sb.toString();
	}

	private AbstractElement<?> createAttachmentItem_StatusBtn(final ComponentParameter cp,
			final String id, final AttachmentFile attachment) {
		final String ipath = cp.getCssResourceHomePath(AbstractAttachmentHandler.class) + "/images/";
		if (getUploadCache(cp).containsKey(id)) {
			return new ImageElement(ipath + "add.png").setClassName("status")
					.setTitle($m("DefaultAttachmentHandle.0"));
		} else {
			final Set<String> deleteQueue = getDeleteCache(cp);
			if (deleteQueue != null && deleteQueue.contains(id)) {
				return new ImageElement(ipath + "delete.png").setClassName("status")
						.setTitle($m("DefaultAttachmentHandle.1"));
			}
		}
		return null;
	}

	protected String toAttachmentItemHTML(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final int index) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		final boolean insertTextarea = StringUtils
				.hasText((String) cp.getBeanProperty("insertTextarea"));

		sb.append("<div class='fitem clearfix'");
		if (readonly && index == 0) {
			sb.append(" style='border-top: 0;'");
		}
		sb.append(" rowid='").append(id).append("'>");
		final String fileInfo = toAttachmentItem_fileInfoHTML(cp, attachment, readonly);
		sb.append("<div class='l_attach left'>");
		if ((Boolean) cp.getBeanProperty("showLineNo") && !insertTextarea) {
			sb.append(" <span class='ord'>").append(index + 1).append(".</span>");
		}
		// topic
		sb.append(" <span class='tt'>");
		if (!readonly) {
			final AbstractElement<?> img = createAttachmentItem_StatusBtn(cp, id, attachment);
			if (img != null) {
				sb.append(img);
			}
		}

		if (insertTextarea) {
			sb.append(new Checkbox(id,
					createAttachmentItem_Topic(cp, id, attachment, readonly, false, index)));
		} else {
			// params for tooltip
			sb.append(createAttachmentItem_Topic(cp, id, attachment, readonly, true, index));
		}
		// fileInfo
		sb.append(fileInfo);
		sb.append(" </span>");

		sb.append("</div>");
		// btns
		final String btns = createAttachmentItem_Btns(cp, id, attachment, readonly, index);
		if (StringUtils.hasText(btns)) {
			sb.append("<div class='btns right'>").append(btns).append("</div>");
		}
		final Boolean audio = (Boolean) cp.getAttr("_audio_" + id);
		if (audio != null) {
			sb.append("<div class='audio-player' style='display: none;'>");
			sb.append(" <div class='dot'>00:00</div>");
			sb.append("</div>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	protected String createAttachmentItem_Topic(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final boolean readonly, final boolean showlink,
			final int index) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final Boolean audio = (Boolean) cp.getAttr("_audio_" + id);
		if (showlink && audio == null) {
			sb.append(createAttachmentItem_topicLinkElement(cp, id, attachment, readonly, index));
		} else {
			sb.append(new SpanElement(HtmlEncoder.text(attachment.getTopic())).setClassName("tip")
					.addAttribute("params", "id=" + id));
		}
		return sb.toString();
	}

	protected String createAttachmentItem_Btns(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final boolean readonly, final int index)
			throws IOException {
		final StringBuilder sb = new StringBuilder();
		if (!readonly) {
			if ((Boolean) cp.getBeanProperty("showMenu")) {
				sb.append(createAttachmentItem_menu(cp, id, attachment, index));
			}
			sb.append(createAttachmentItem_DelBtn(cp, id, attachment));
			if ((Boolean) cp.getBeanProperty("showEdit")) {
				sb.append(createAttachmentItem_EditBtn(cp, id, attachment));
			}
		}
		return sb.toString();
	}

	protected AbstractElement<?> createAttachmentItem_menu(final ComponentParameter cp,
			final String id, final AttachmentFile attachment, final int index) {
		String style = "float: right; margin-top: 3px;";
		if (getUploadCache(cp).containsKey(attachment.getId())) {
			style += "display: none;";
		}
		return new SpanElement()
				.setClassName("down_menu_image " + cp.getComponentName() + "_attach_menu")
				.addStyle(style);
	}

	protected LinkElement createAttachmentItem_topicLinkElement(final ComponentParameter cp,
			final String id, final AttachmentFile attachment, final boolean readonly,
			final int index) {
		return new LinkElement(HtmlEncoder.text(attachment.getTopic()))
				.setOnclick("$Actions['" + cp.getComponentName() + "_download']('id=" + id + "');")
				.setClassName("tip").addAttribute("params", "id=" + id);
	}

	protected String toAttachmentItem_fileInfoHTML(final ComponentParameter cp,
			final AttachmentFile attachment, final boolean readonly) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append(new SpanElement("(" + FileUtils.toFileSize(attachment.getSize()) + ")", "size"));
		// 是否可播放
		boolean canplay = true;
		final String ext = attachment.getExt();
		if (StringUtils.hasText(ext)) {
			final String mimeType = MimeTypes.getMimeType(ext.toLowerCase());
			canplay = mimeType.startsWith("audio/") || mimeType.startsWith("video/");
		}

		if (canplay) {
			final Encoder encoder = new Encoder();
			try {
				final MultimediaInfo info = encoder.getInfo(attachment.getAttachment());
				ImageElement img = null;
				final String src = cp.getCssResourceHomePath(AbstractAttachmentHandler.class)
						+ "/images/play.png";
				if (info.getVideo() != null) {
				} else if (info.getAudio() != null) {
					img = new ImageElement(src).addClassName("play audio");
					cp.setAttr("_audio_" + attachment.getId(), Boolean.TRUE);
				}
				if (img != null) {
					final String durl = getDownloadUrl(cp, attachment);
					if (StringUtils.hasText(durl)) {
						cp.addImportJavascript(DefaultPageResourceProvider.class, "/js/howler.js");

						img.addAttribute("_durl", durl);
						sb.append(img);
					}
				}
			} catch (final Exception e) {
				log.warn(e);
			}
		}
		return sb.toString();
	}

	protected String getDownloadUrl(final ComponentParameter cp, final AttachmentFile attachment) {
		return (String) HttpUtils.toQueryParams(DownloadUtils.getDownloadHref(attachment, getClass()))
				.get("durl");
	}

	protected LinkElement createAttachmentItem_Btn(final String text) {
		return LinkElement.style2(text);
	}

	protected AbstractElement<?> createAttachmentItem_DelBtn(final ComponentParameter cp,
			final String id, final AttachmentFile attachment) {
		final Set<String> deleteQueue = getDeleteCache(cp);
		LinkElement del;
		if (deleteQueue != null && deleteQueue.contains(id)) {
			del = createAttachmentItem_Btn($m("Button.Cancel"));
		} else {
			del = createAttachmentItem_Btn($m("Delete"));
		}
		return del.setOnclick("$Actions['" + cp.getComponentName() + "_delete']('id=" + id + "');");
	}

	protected AbstractElement<?> createAttachmentItem_EditBtn(final ComponentParameter cp,
			final String id, final AttachmentFile attachment) {
		return createAttachmentItem_Btn($m("Edit"))
				.setOnclick("$Actions['" + cp.getComponentName() + "_editWin']('id=" + id + "');");
	}

	protected boolean showCheckbox() {
		return false;
	}

	@Override
	public String toBottomHTML(final ComponentParameter cp) throws IOException {
		final boolean insertTextarea = StringUtils
				.hasText((String) cp.getBeanProperty("insertTextarea"));
		final boolean showSubmit = (Boolean) cp.getBeanProperty("showSubmit");
		final boolean showZipDownload = (Boolean) cp.getBeanProperty("showZipDownload")
				&& attachments(cp).size() > 1;
		if (!insertTextarea && !showSubmit && !showZipDownload) {
			return "";
		}

		final StringBuilder sb = new StringBuilder();
		final String hashId = cp.hashId();
		sb.append("<div class='b_attach' id='cc_").append(hashId).append("'>");
		if (insertTextarea) {
			sb.append(new Checkbox("checkAll_" + hashId, $m("AbstractAttachmentHandler.1")));
		}
		sb.append(" <span class='rbtn'>");
		if (insertTextarea) {
			sb.append(LinkButton.corner($m("AbstractAttachmentHandler.0")).addClassName("obtn"));
		} else if (showSubmit) {
			sb.append(LinkButton.corner($m("AbstractAttachmentHandler.3")).addClassName("obtn"));
		}
		if (showZipDownload) {
			sb.append(SpanElement.SPACE);
			sb.append(LinkButton.corner($m("AbstractAttachmentHandler.8"))
					.setOnclick("$Actions['" + cp.getComponentName() + "_zipDownload']();"));
		}
		sb.append(" </span>");
		sb.append("</div>");

		final StringBuilder js = new StringBuilder();
		js.append(" var cc = $('cc_").append(hashId).append("');");
		js.append(" var msg = {");
		js.append("  m1: '").append($m("AbstractAttachmentHandler.2")).append("'");
		js.append(" };");
		js.append(" AttachmentUtils.doOk(cc, ").append(insertTextarea).append(", '")
				.append(cp.getComponentName()).append("', msg);");
		sb.append(JavascriptUtils.wrapScriptTag(js.toString(), true));
		return sb.toString();
	}

	@Override
	public String getTooltipPath(final ComponentParameter cp) {
		return null;
	}

	@Override
	public AbstractElement<?> getDownloadLinkElement(final ComponentParameter cp,
			final AttachmentFile attachmentFile, final String id) throws IOException {
		return null;
	}

	@Override
	public ID getOwnerId(final ComponentParameter cp) {
		return null;
	}

	@Override
	public JavascriptForward doDownload(final ComponentParameter cp, final AttachmentFile af) {
		boolean target = false;
		String ext = af.getExt();
		if (StringUtils.hasText(ext)) {
			ext = ext.toLowerCase();
			if ("pdf".equals(ext)) {
				target = true;
			} else {
				final String mimeType = MimeTypes.getMimeType(ext);
				target = mimeType.startsWith("image/") || mimeType.startsWith("audio/")
						|| mimeType.startsWith("video/");
			}
		}
		return new JavascriptForward(
				JS.loc(DownloadUtils.getDownloadHref(af, getClass()), target && !cp.isMobile()));
	}

	@Override
	public JavascriptForward doZipDownload(final ComponentParameter cp) throws IOException {
		final File target = getModuleContext().getApplicationContext().getContextSettings()
				.getAttachDir(ID.uid() + ".zip");
		if (target.exists()) {
			target.delete();
		}

		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(target);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
			int i = 0;
			final Set<String> names = new HashSet<>();
			for (final AttachmentFile aFile : attachments(cp).values()) {
				final String t = aFile.getTopic();
				final String ext = aFile.getExt();
				String topic = t + "." + ext;
				if (names.contains(topic)) {
					topic = t + "_" + i++ + "." + ext;
				}
				aFile.addZipEntry("/", topic, zos);
				names.add(topic);
			}
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (final IOException e) {
			}
		}

		return new JavascriptForward(
				JS.loc(DownloadUtils.getDownloadHref(new AttachmentFile(target), getClass())));
	}

	// AttachmentHistory
	@Override
	public IDataQuery<?> queryAttachmentHistory(final ComponentParameter cp) {
		return null;
	}

	@Override
	public Map<String, Object> getAttachmentHistoryRowData(final ComponentParameter cp,
			final Object dataObject) {
		return null;
	}

	@Override
	public void doAttachmentHistorySelected(final ComponentParameter cp) throws IOException {
	}

	// IDownloadHandler
	@Override
	public void onDownloaded(final Object beanId, final long length, final String filetype,
			final String topic) {
	}
}
