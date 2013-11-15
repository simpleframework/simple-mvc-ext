package net.simpleframework.mvc.component.ext.attachments;

import java.util.Map;
import java.util.Set;

import net.simpleframework.ctx.common.bean.AttachmentFile;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IAttachmentSaveCallback {

	/**
	 * 
	 * @param addQueue
	 * @param deleteQueue
	 */
	void save(Map<String, AttachmentFile> addQueue, Set<String> deleteQueue);
}
