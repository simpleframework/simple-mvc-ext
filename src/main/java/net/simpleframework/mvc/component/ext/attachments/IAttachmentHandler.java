package net.simpleframework.mvc.component.ext.attachments;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.ctx.common.bean.AttachmentFile;
import net.simpleframework.mvc.IMultipartFile;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.common.IDownloadHandler;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;
import net.simpleframework.mvc.component.ui.swfupload.SwfUploadBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IAttachmentHandler extends IComponentHandler, IDownloadHandler {

	/**
	 * 获取附件拥有者的id
	 * 
	 * @param cParameter
	 * @return
	 */
	ID getOwnerId(ComponentParameter cp);

	/**
	 * 获取附件列表
	 * 
	 * @param cParameter
	 * @return
	 */
	Map<String, AttachmentFile> attachments(ComponentParameter cp) throws Exception;

	/**
	 * 根据id获取AttachmentFile对象
	 * 
	 * @param cParameter
	 * @param id
	 * @return
	 * @throws Exception
	 */
	AttachmentFile getAttachmentById(ComponentParameter cp, String id) throws Exception;

	@SuppressWarnings("rawtypes")
	Enum[] getAttachTypes();

	/**
	 * 上传执行操作
	 * 
	 * @param cParameter
	 * @param multipartFile
	 * @param variables
	 */
	void upload(ComponentParameter cp, IMultipartFile multipartFile, Map<String, Object> variables)
			throws Exception;

	/**
	 * 插入附件
	 * 
	 * @param cParameter
	 * @param callback
	 */
	JavascriptForward doSave(ComponentParameter cp, IAttachmentSaveCallback callback)
			throws Exception;

	/**
	 * 更改标题
	 * 
	 * @param cp
	 * @param id
	 * @param topic
	 * @param attachtype
	 * @param description
	 * @throws Exception
	 */
	void doSave(ComponentParameter cp, String id, String topic, int attachtype, String description)
			throws Exception;

	/**
	 * 删除上传的附件
	 * 
	 * @param cParameter
	 * @param id
	 */
	void doDelete(ComponentParameter cp, String id);

	void doExchange(ComponentParameter cp, String... ids);

	/**
	 * 获取下载的link元素
	 * 
	 * @param cp
	 * @param attachmentFile
	 * @param id
	 * @return
	 */
	AbstractElement<?> getDownloadLink(ComponentParameter cp, AttachmentFile attachmentFile,
			String id) throws IOException;

	JavascriptForward doDownload(ComponentParameter cp, AttachmentFile af) throws IOException;

	JavascriptForward doZipDownload(ComponentParameter cp) throws IOException;

	/**
	 * 设置上传组件的属性
	 * 
	 * @param cp
	 * @param swfUpload
	 */
	void setSwfUploadBean(ComponentParameter cp, SwfUploadBean swfUpload);

	/**
	 * 附件列表输出HTML
	 * 
	 * @param cParameter
	 * @return
	 */
	String toAttachmentListHTML(ComponentParameter cp) throws Exception;

	/**
	 * 获取插入操作的HTML
	 * 
	 * @param cParameter
	 * @return
	 */
	String toBottomHTML(ComponentParameter cp) throws IOException;

	/**
	 * 获取tooltip的路径
	 * 
	 * @param cp
	 * @return
	 */
	String getTooltipPath(ComponentParameter cp);
}
