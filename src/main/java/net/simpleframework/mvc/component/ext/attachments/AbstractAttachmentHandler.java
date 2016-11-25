package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.ImageUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.th.NotImplementedException;
import net.simpleframework.common.web.html.HtmlConst;
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
import net.simpleframework.mvc.common.element.JS;
import net.simpleframework.mvc.common.element.LinkButton;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.common.element.SpanElement;
import net.simpleframework.mvc.component.ComponentHandlerEx;
import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;

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
			swfUpload.setFileTypes("*.jpg;*.jpeg;*.gif;*.png;*.bmp")
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
			callback.save(getUploadCache(cp), getDeleteCache(cp));
		}
		final int attachmentsLimit = (Integer) cp.getBeanProperty("attachmentsLimit");
		if (attachmentsLimit > 0 && attachments(cp).size() > attachmentsLimit) {
			throwAttachmentsLimit(attachmentsLimit);
		}
		// 清除
		clearCache(cp);
		return null;
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
		if (attachmentsQueueLimit > 0 && attachments(cp).size() >= attachmentsQueueLimit) {
			throwAttachmentsQueueLimit(attachmentsQueueLimit);
		}
		final AttachmentFile af = new AttachmentFile(multipartFile.getFile());
		final int attachtype = getAttachtype(cp);
		if (attachtype > -1) {
			af.setType(attachtype);
		}
		getUploadCache(cp).put(af.getId(), af);
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
			sb.append("<div class='imgc clearfix'>");
		}
		int i = 0;
		for (final Map.Entry<String, AttachmentFile> entry : attachments(cp).entrySet()) {
			final AttachmentFile attach = entry.getValue();
			if (attach.getAttachment() == null) {
				continue;
			}
			if (imageList) {
				sb.append(toAttachmentItemImagesHTML(cp, entry.getKey(), attach, i++));
			} else {
				sb.append(toAttachmentItemHTML(cp, entry.getKey(), attach, i++));
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
		sb.append("<div class='iitem");
		if (!readonly) {
			appendStatusClass(cp, id, sb);
		}
		sb.append("'>");
		sb.append(" <div class='i_attach'>");
		sb.append(createAttachmentItem_Image(cp, id, attachment));
		sb.append(" </div>");
		sb.append(createAttachmentItem_Image_Btns(cp, id, attachment, readonly));
		sb.append("</div>");
		return sb.toString();
	}

	protected ImageElement createAttachmentItem_Image(final PageParameter pp, final String id,
			final AttachmentFile attachmentFile) {
		final ImageElement image = new ImageElement();
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
			sb.append(createAttachmentItem_DelBtn(cp, id, attachment));
		}
		return sb.toString();
	}

	private void appendStatusClass(final ComponentParameter cp, final String id,
			final StringBuilder sb) {
		if (getUploadCache(cp).containsKey(id)) {
			sb.append(" l_add' title='").append($m("DefaultAttachmentHandle.0"));
		} else {
			final Set<String> deleteQueue = getDeleteCache(cp);
			if (deleteQueue != null && deleteQueue.contains(id)) {
				sb.append(" l_delete' title='").append($m("DefaultAttachmentHandle.1"));
			}
		}
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
		sb.append("<div class='l_attach");

		if (!readonly) {
			appendStatusClass(cp, id, sb);
		}
		sb.append("'>");
		// btns
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
			sb.append(attachment.getTopic());
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
		return new SpanElement().setClassName("down_menu_image attach_menu").addStyle(style);
	}

	protected LinkElement createAttachmentItem_topicLinkElement(final ComponentParameter cp,
			final String id, final AttachmentFile attachment, final int index) {
		return new LinkElement(attachment.getTopic())
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
	public String toBottomHTML(final ComponentParameter cp) {
		final boolean insertTextarea = StringUtils
				.hasText((String) cp.getBeanProperty("insertTextarea"));
		final boolean showSubmit = (Boolean) cp.getBeanProperty("showSubmit");
		if (!insertTextarea && !showSubmit) {
			return "";
		}

		final StringBuilder sb = new StringBuilder();
		final String hashId = cp.hashId();
		sb.append("<div class='b_attach' id='cc_").append(hashId).append("'>");
		sb.append(" <span class='rbtn'>");
		sb.append(LinkButton.corner(
				insertTextarea ? $m("AbstractAttachmentHandler.0") : $m("AbstractAttachmentHandler.3")))
				.append("</span>");
		if (insertTextarea) {
			sb.append(new Checkbox("checkAll_" + hashId, $m("AbstractAttachmentHandler.1")));
		}
		sb.append("</div>");

		sb.append(HtmlConst.TAG_SCRIPT_START);
		sb.append("$ready(function() {");
		sb.append(" var cc = $('cc_").append(hashId).append("');");
		sb.append(" var attach = cc.previous();");
		if (insertTextarea) {
			sb.append(" cc.down('input[type=checkbox]').observe('click', function(evn) {");
			sb.append("   var _box = this;");
			sb.append(
					"   attach.select('input[type=checkbox]').each(function(box) { box.checked = _box.checked; });");
			sb.append(" });");
			sb.append(" cc.down('.simple_btn').observe('click', function(evn) {");
			sb.append(
					"   var idArr = attach.select('input[type=checkbox]').inject([], function(r, box) {");
			sb.append("     if (box.checked) r.push(box.id);");
			sb.append("     return r;");
			sb.append("   });");
			sb.append(
					"   if (idArr.length == 0) { alert('#(AbstractAttachmentHandler.2)'); return; }");
			sb.append("   $Actions['").append(cp.getComponentName())
					.append("_selected']('ids=' + idArr.join(';'));");
			sb.append(" });");
		} else {
			sb.append(" cc.down('.simple_btn').observe('click', function(evn) {");
			sb.append(
					"  if (attach.select('.fitem').length == 0) { alert('#(AbstractAttachmentHandler.2)'); return; }");
			sb.append("  $Actions['").append(cp.getComponentName()).append("_submit']();");
			sb.append(" });");
		}
		sb.append("});");
		sb.append(HtmlConst.TAG_SCRIPT_END);
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
	public JavascriptForward doDownloadAction(final ComponentParameter cp, final AttachmentFile af) {
		return new JavascriptForward(JS.loc(DownloadUtils.getDownloadHref(af, getClass())));
	}

	// IDownloadHandler
	@Override
	public void onDownloaded(final Object beanId, final long length, final String filetype,
			final String topic) {
	}
}
