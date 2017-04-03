package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import net.simpleframework.common.Convert;
import net.simpleframework.common.FileUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.ImageUtils;
import net.simpleframework.common.JsonUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.common.web.JavascriptUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.common.web.html.HtmlEncoder;
import net.simpleframework.ctx.common.bean.AttachmentFile;
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
		if (callback != null) {
			try {
				final Map<String, AttachmentFile> uploads = getUploadCache(cp);
				doImagesCropper(cp, uploads);
				doThumbnail(cp, uploads);
				callback.save(uploads, getDeleteCache(cp));
			} catch (final Exception e) {
				clearCache(cp);
				throw e;
			}
		}
		final int attachmentsLimit = (Integer) cp.getBeanProperty("attachmentsLimit");
		if (attachmentsLimit > 0 && attachments(cp).size() > attachmentsLimit) {
			throwAttachmentsLimit(attachmentsLimit);
		}
		// 清除
		clearCache(cp);
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

			final Map<String, ?> data = JsonUtils.toMap(cropper);
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

	protected void clearCache(final ComponentParameter cp) {
		final Map<String, AttachmentFile> addQueue = getUploadCache(cp);
		addQueue.clear();
		final Set<String> deleteQueue = getDeleteCache(cp);
		if (deleteQueue != null) {
			deleteQueue.clear();
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
	public void upload(final ComponentParameter cp, final IMultipartFile multipartFile,
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
			SessionCache.lput(key, cache = new LinkedHashMap<String, AttachmentFile>());
		}
		return cache;
	}

	@SuppressWarnings("unchecked")
	protected Set<String> getDeleteCache(final ComponentParameter cp) {
		final String key = "Delete_Cache_" + getCachekey(cp);
		Set<String> cache = (Set<String>) SessionCache.lget(key);
		if (cache == null) {
			SessionCache.lput(key, cache = new LinkedHashSet<String>());
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
				sb.append(HtmlConst.TAG_SCRIPT_START);
				sb.append("$ready(function() {");
				sb.append(" var data = $('cropper_").append(id).append("');");
				sb.append(" var img = $('#attach_").append(id).append(" img');");
				sb.append(" var cropper = new Cropper(img, {");
				sb.append("  dragMode : 'move',");
				sb.append("  movable : false,");
				sb.append("  viewMode : 3,");
				sb.append("  zoomable : false,");
				sb.append("  aspectRatio : ").append(cp.getBeanProperty("cropperRatio")).append(",");
				sb.append("  crop: function(e) {");
				sb.append("   data.value = JSON.stringify(cropper.getData());");
				sb.append("  }");
				sb.append(" });");
				sb.append("});");
				sb.append(HtmlConst.TAG_SCRIPT_END);
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
		sb.append("<div class='fitem'");
		if (readonly && index == 0) {
			sb.append(" style='border-top: 0;'");
		}
		sb.append(" rowid='").append(id).append("'>");
		sb.append("<div class='l_attach'>");

		// btns
		if (!readonly) {
			final AbstractElement<?> img = createAttachmentItem_StatusBtn(cp, id, attachment);
			if (img != null) {
				sb.append(img);
			}
		}
		sb.append(createAttachmentItem_Btns(cp, id, attachment, readonly, index));

		// topic
		final boolean insertTextarea = StringUtils
				.hasText((String) cp.getBeanProperty("insertTextarea"));
		if (insertTextarea) {
			sb.append(new Checkbox(id, createAttachmentItem_Topic(cp, id, attachment, false, index)));
		} else {
			// params for tooltip
			sb.append(createAttachmentItem_Topic(cp, id, attachment, true, index));
		}
		sb.append("</div></div>");
		return sb.toString();
	}

	protected String createAttachmentItem_Topic(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final boolean showlink, final int index)
			throws IOException {
		final StringBuilder sb = new StringBuilder();
		if ((Boolean) cp.getBeanProperty("showLineNo")) {
			sb.append(index + 1).append(". ");
		}
		if (showlink) {
			sb.append(createAttachmentItem_topicLinkElement(cp, id, attachment, index));
		} else {
			sb.append(HtmlEncoder.text(attachment.getTopic()));
		}
		sb.append(createAttachmentItem_fileSizeElement(attachment));
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
			final String id, final AttachmentFile attachment, final int index) {
		return new LinkElement(HtmlEncoder.text(attachment.getTopic()))
				.setOnclick("$Actions['" + cp.getComponentName() + "_download']('id=" + id + "');")
				.addAttribute("params", "id=" + id);
	}

	protected SpanElement createAttachmentItem_fileSizeElement(final AttachmentFile attachment)
			throws IOException {
		return new SpanElement("(" + FileUtils.toFileSize(attachment.getSize()) + ")")
				.setClassName("size");
	}

	protected LinkElement createAttachmentItem_Btn(final String text) {
		return LinkElement.style2(text).addStyle("float: right;");
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
	public AbstractElement<?> getDownloadLink(final ComponentParameter cp,
			final AttachmentFile attachmentFile, final String id) throws IOException {
		return null;
	}

	@Override
	public ID getOwnerId(final ComponentParameter cp) {
		return null;
	}

	@Override
	public JavascriptForward doDownload(final ComponentParameter cp, final AttachmentFile af) {
		return new JavascriptForward(JS.loc(DownloadUtils.getDownloadHref(af, getClass())));
	}

	@Override
	public JavascriptForward doZipDownload(final ComponentParameter cp) throws IOException {
		final File target = getModuleContext().getApplicationContext().getContextSettings()
				.getAttachDir("attachment.zip");
		if (target.exists()) {
			target.delete();
		}

		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(target);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
			for (final AttachmentFile aFile : attachments(cp).values()) {
				addEntry(aFile, zos);
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

	protected void addEntry(final AttachmentFile source, final ZipOutputStream zos)
			throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			final byte[] buffer = new byte[1024 * 10];
			final File aFile = source.getAttachment();
			fis = new FileInputStream(aFile);
			bis = new BufferedInputStream(fis, buffer.length);
			zos.putNextEntry(new ZipEntry(
					"/" + source.getTopic() + "." + FileUtils.getFilenameExtension(aFile.getName())));
			int read = 0;
			while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
				zos.write(buffer, 0, read);
			}
			zos.closeEntry();
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (bis != null) {
				bis.close();
			}
		}
	}

	// IDownloadHandler
	@Override
	public void onDownloaded(final Object beanId, final long length, final String filetype,
			final String topic) {
	}
}
