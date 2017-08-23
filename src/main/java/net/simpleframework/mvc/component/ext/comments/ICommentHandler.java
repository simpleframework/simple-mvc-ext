package net.simpleframework.mvc.component.ext.comments;

import java.util.List;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.ID;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface ICommentHandler extends IComponentHandler {

	/**
	 * 获取评论的拥有者id
	 * 
	 * @param cParameter
	 * @return
	 */
	ID getOwnerId(ComponentParameter cp);

	/**
	 * 获取评论的内容列表
	 * 
	 * @param cParameter
	 * @return
	 */
	IDataQuery<?> comments(ComponentParameter cp);

	IDataQuery<?> children(ComponentParameter cp, Object id);

	/**
	 * 获取评论对象
	 * 
	 * @param cParameter
	 * @param id
	 * @return
	 */
	Object getCommentById(ComponentParameter cp, Object id);

	/**
	 * 发表评论
	 * 
	 * @param cParameter
	 * @return
	 */
	JavascriptForward addComment(ComponentParameter cp);

	/**
	 * 删除评论
	 * 
	 * @param cParameter
	 * @param id
	 * @return
	 */
	JavascriptForward deleteComment(ComponentParameter cp, Object id);

	JavascriptForward likeComment(ComponentParameter cp, Object id);

	static final String ATTRI_COMMENT = "ccomment";

	static final String ATTRI_USERID = "userId";

	static final String ATTRI_CREATEDATE = "createDate";

	static final String ATTRI_ID = "id";

	static final String ATTRI_PARENTID = "parentId";

	static final String ATTRI_LIKES = "likes";

	/**
	 * 获取评论对象的相关属性
	 * 
	 * @param cParameter
	 * @param o
	 * @param name
	 * @return
	 */
	Object getProperty(ComponentParameter cp, Object o, String name);

	/**
	 * 获取评论的列表内容显示
	 * 
	 * @param cp
	 * @param data
	 * @return
	 */
	String toListHTML(ComponentParameter cp, List<?> data);

	/**
	 * 
	 */
	String toEditorHTML(ComponentParameter cp);
}
