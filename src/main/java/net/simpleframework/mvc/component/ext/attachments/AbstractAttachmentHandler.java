package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.FileUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.common.bean.AttachmentFile;
import net.simpleframework.ctx.script.MVEL2Template;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.Checkbox;
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
		swfUpload.setFileSizeLimit("10MB").setMultiFileSelected(true);
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
	public void doSave(final ComponentParameter cp, final IAttachmentSaveCallback callback) {
		callback.save(getUploadCache(cp), getDeleteCache(cp));
		// 清除
		clearCache(cp);
	}

	@Override
	public void doSave(final ComponentParameter cp, final String id, final String topic,
			final String description) {
		try {
			final AttachmentFile af = getAttachmentById(cp, id);
			if (af != null) {
				af.setTopic(topic);
				af.setDescription(description);
			}
		} catch (final IOException e) {
			throw ComponentHandlerException.of(e);
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
	public void upload(final ComponentParameter cp, final IMultipartFile multipartFile,
			final Map<String, Object> variables) throws IOException {
		final AttachmentFile af = new AttachmentFile(multipartFile.getFile());
		getUploadCache(cp).put(af.getId(), af);
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
		final String name = cp.getComponentName();
		final boolean showInsert = StringUtils.hasText((String) cp.getBeanProperty("insertTextarea"));
		final boolean readonly = (Boolean) cp.getBeanProperty("readonly");
		final Set<String> deleteQueue = getDeleteCache(cp);
		final StringBuilder sb = new StringBuilder();
		for (final Map.Entry<String, AttachmentFile> entry : attachments(cp).entrySet()) {
			final String id = entry.getKey();
			final AttachmentFile attachment = entry.getValue();
			sb.append("<div class='fitem'>");
			sb.append("<div class='l_attach");
			if (!readonly) {
				if (getUploadCache(cp).containsKey(id)) {
					sb.append(" l_add' title='").append($m("DefaultAttachmentHandle.0"));
				} else {
					if (deleteQueue != null && deleteQueue.contains(id)) {
						sb.append(" l_delete' title='").append($m("DefaultAttachmentHandle.1"));
					}
				}
			}
			sb.append("'>");
			final SpanElement fileSize = new SpanElement("("
					+ FileUtils.toFileSize(attachment.getSize()) + ")").setClassName("size");
			if (!readonly) {
				final ButtonElement del = new ButtonElement().setStyle(
						"float: right; margin-left: 3px;").setOnclick(
						"$Actions['" + name + "_delete']('id=" + id + "');");
				if (deleteQueue != null && deleteQueue.contains(id)) {
					del.setText($m("Button.Cancel"));
				} else {
					del.setText($m("Delete"));
				}
				sb.append(del);
				sb.append(ButtonElement.editBtn().setStyle("float: right;")
						.setOnclick("$Actions['" + name + "_editWin']('id=" + id + "');"));
			}
			if (showInsert) {
				sb.append(new Checkbox(id, attachment.getTopic() + fileSize));
			} else {
				// params for tooltip
				sb.append(
						new LinkElement(attachment.getTopic()).addAttribute("params", "id=" + id)
								.setOnclick("$Actions['" + name + "_download']('id=" + id + "');")).append(
						fileSize);
			}
			sb.append("</div></div>");
		}
		return sb.toString();
	}

	@Override
	public String toInsertHTML(final ComponentParameter cp) {
		final boolean showInsert = StringUtils.hasText((String) cp.getBeanProperty("insertTextarea"));
		if (!showInsert) {
			return "";
		}
		final KVMap variables = new KVMap().add("name", cp.getComponentName()).add("beanId",
				cp.hashId());
		return MVEL2Template.replace(variables, IAttachmentHandler.class, "AttachmentInsert.html");
	}

	@Override
	public String getTooltipPath(final ComponentParameter cp) {
		return null;
	}

	@Override
	public void onDownloaded(final Object beanId, final String topic, final File oFile) {
	}
}
