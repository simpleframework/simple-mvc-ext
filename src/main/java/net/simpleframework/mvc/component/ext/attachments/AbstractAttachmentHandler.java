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
import net.simpleframework.mvc.common.ImageCache;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.ImageElement;
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
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractAttachmentHandler extends ComponentHandlerEx implements
		IAttachmentHandler {
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
			final String description) throws Exception {
		final AttachmentFile af = getAttachmentById(cp, id);
		if (af != null) {
			af.setTopic(topic);
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
		getUploadCache(cp).put(af.getId(), af);
	}

	protected void throwAttachmentsQueueLimit(final int attachmentsQueueLimit) {
		throw ComponentHandlerException.of($m("AbstractAttachmentHandler.5", attachmentsQueueLimit));
	}

	@SuppressWarnings("unchecked")
	protected Map<String, AttachmentFile> getUploadCache(final ComponentParameter cp) {
		final String key = "Cache_" + cp.getComponentName();
		Map<String, AttachmentFile> cache = (Map<String, AttachmentFile>) cp.getSessionAttr(key);
		if (cache == null) {
			cp.setSessionAttr(key, cache = new LinkedHashMap<String, AttachmentFile>());
		}
		return cache;
	}

	@SuppressWarnings("unchecked")
	protected Set<String> getDeleteCache(final ComponentParameter cp) {
		final String key = "Delete_Cache_" + cp.getComponentName();
		Set<String> cache = (Set<String>) cp.getSessionAttr(key);
		if (cache == null) {
			cp.setSessionAttr(key, cache = new LinkedHashSet<String>());
		}
		return cache;
	}

	@Override
	public String toAttachmentListHTML(final ComponentParameter cp) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final boolean imageList = (Boolean) cp.getBeanProperty("imagesMode");
		if (imageList) {
			sb.append("<div class='imgc clearfix'>");
		}
		for (final Map.Entry<String, AttachmentFile> entry : attachments(cp).entrySet()) {
			if (imageList) {
				sb.append(toAttachmentItemImagesHTML(cp, entry.getKey(), entry.getValue()));
			} else {
				sb.append(toAttachmentItemHTML(cp, entry.getKey(), entry.getValue()));
			}
		}
		if (imageList) {
			sb.append("</div>");
		}
		return sb.toString();
	}

	protected String toAttachmentItemImagesHTML(final ComponentParameter cp, final String id,
			final AttachmentFile attachment) throws IOException {
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
			final AttachmentFile attachment) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='fitem' rowid='").append(id).append("'>");
		sb.append("<div class='l_attach");
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		if (!readonly) {
			appendStatusClass(cp, id, sb);
		}
		sb.append("'>");
		// btns
		sb.append(createAttachmentItem_Btns(cp, id, attachment, readonly));
		// topic
		final boolean insertTextarea = StringUtils.hasText((String) cp
				.getBeanProperty("insertTextarea"));
		if (insertTextarea) {
			sb.append(new Checkbox(id, createAttachmentItem_Topic(cp, id, attachment, false)));
		} else {
			// params for tooltip
			sb.append(createAttachmentItem_Topic(cp, id, attachment, true));
		}
		sb.append("</div></div>");
		return sb.toString();
	}

	protected String createAttachmentItem_Topic(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final boolean showlink) throws IOException {
		final StringBuilder sb = new StringBuilder();
		if (showlink) {
			sb.append(createAttachmentItem_topicLinkElement(cp, id, attachment));
		} else {
			sb.append(attachment.getTopic());
		}
		sb.append(createAttachmentItem_fileSizeElement(attachment));
		return sb.toString();
	}

	protected String createAttachmentItem_Btns(final ComponentParameter cp, final String id,
			final AttachmentFile attachment, final boolean readonly) throws IOException {
		final StringBuilder sb = new StringBuilder();
		if (!readonly) {
			if ((Boolean) cp.getBeanProperty("showMenu")) {
				sb.append(createAttachmentItem_menu(cp, id, attachment));
			}
			sb.append(createAttachmentItem_DelBtn(cp, id, attachment));
			if ((Boolean) cp.getBeanProperty("showEdit")) {
				sb.append(createAttachmentItem_EditBtn(cp, id, attachment));
			}
		}
		return sb.toString();
	}

	protected AbstractElement<?> createAttachmentItem_menu(final ComponentParameter cp,
			final String id, final AttachmentFile attachment) {
		return new SpanElement().setClassName("down_menu_image attach_menu").addStyle(
				"float: right; margin-top: 3px;");
	}

	protected LinkElement createAttachmentItem_topicLinkElement(final ComponentParameter cp,
			final String id, final AttachmentFile attachment) {
		return new LinkElement(attachment.getTopic()).setOnclick(
				"$Actions['" + cp.getComponentName() + "_download']('id=" + id + "');").addAttribute(
				"params", "id=" + id);
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
		return createAttachmentItem_Btn($m("Edit")).setOnclick(
				"$Actions['" + cp.getComponentName() + "_editWin']('id=" + id + "');");
	}

	protected boolean showCheckbox() {
		return false;
	}

	@Override
	public String toBottomHTML(final ComponentParameter cp) {
		final boolean insertTextarea = StringUtils.hasText((String) cp
				.getBeanProperty("insertTextarea"));
		final boolean showSubmit = (Boolean) cp.getBeanProperty("showSubmit");
		if (!insertTextarea && !showSubmit) {
			return "";
		}

		final StringBuilder sb = new StringBuilder();
		final String hashId = cp.hashId();
		sb.append("<div class='b_attach' id='cc_").append(hashId).append("'>");
		sb.append(" <span class='rbtn'>");
		sb.append(
				LinkButton.corner(insertTextarea ? $m("AbstractAttachmentHandler.0")
						: $m("AbstractAttachmentHandler.3"))).append("</span>");
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
			sb.append("   attach.select('input[type=checkbox]').each(function(box) { box.checked = _box.checked; });");
			sb.append(" });");
			sb.append(" cc.down('.simple_btn').observe('click', function(evn) {");
			sb.append("   var idArr = attach.select('input[type=checkbox]').inject([], function(r, box) {");
			sb.append("     if (box.checked) r.push(box.id);");
			sb.append("     return r;");
			sb.append("   });");
			sb.append("   if (idArr.length == 0) { alert('#(AbstractAttachmentHandler.2)'); return; }");
			sb.append("   $Actions['").append(cp.getComponentName())
					.append("_selected']('ids=' + idArr.join(';'));");
			sb.append(" });");
		} else {
			sb.append(" cc.down('.simple_btn').observe('click', function(evn) {");
			sb.append("  if (attach.select('.fitem').length == 0) { alert('#(AbstractAttachmentHandler.2)'); return; }");
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

	// IDownloadHandler
	@Override
	public void onDownloaded(final Object beanId, final String topic, final File oFile) {
	}
}
