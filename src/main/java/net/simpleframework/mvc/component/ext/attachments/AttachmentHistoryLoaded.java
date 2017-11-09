package net.simpleframework.mvc.component.ext.attachments;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.Radio;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class AttachmentHistoryLoaded extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		final ComponentParameter cp = AttachmentUtils.get(pp);
		final AttachmentBean attachmentBean = (AttachmentBean) cp.componentBean;
		final String componentName = cp.getComponentName();

		final TablePagerBean tablePager = (TablePagerBean) pp
				.addComponentBean(componentName + "_tbl", TablePagerBean.class).setShowCheckbox(false)
				.setShowHead(false).setScrollHead(false).setShowEditPageItems(false)
				.setPagerBarLayout(EPagerBarLayout.bottom).setExportAction("false").setNoResultDesc("")
				.setContainerId("id" + componentName + "_tbl")
				.setHandlerClass(AttachmentHistoryTbl.class).setAttr("$attachment", attachmentBean);
		tablePager.addColumn(new TablePagerColumn("topic")).addColumn(TablePagerColumn.OPE(50));
	}

	public static String toHTML(final ComponentParameter cp) throws Exception {
		final StringBuilder sb = new StringBuilder();
		final ComponentParameter nCP = AttachmentUtils.get(cp);
		final String componentName = nCP.getComponentName();
		sb.append("<div class='AttachmentHistoryTbl jcc'>");
		sb.append(" <div class='tb'>");
		final String rname = componentName + "_radio";
		final String act = "$Actions['" + componentName + "_tbl']";
		sb.append(new Radio("id" + componentName + "_radio0", "全部").setOnclick(act + "('types=');")
				.setChecked(true).setName(rname));
		sb.append(new Radio("id" + componentName + "_radio1", "仅文档")
				.setOnclick(act + "('types=doc;docx;pdf');").setVal("doc").setName(rname));
		sb.append(new Radio("id" + componentName + "_radio2", "仅图片")
				.setOnclick(act + "('types=png;jpg;jpeg;bmp');").setVal("pic").setName(rname));
		sb.append(new Radio("id" + componentName + "_radio3", "仅音频")
				.setOnclick(act + "('types=mp3;wav');").setVal("audio").setName(rname));
		sb.append(new Radio("id" + componentName + "_radio4", "仅视频")
				.setOnclick(act + "('types=mp4;webm;ogg;flv');").setVal("video").setName(rname));
		sb.append(" </div>");
		sb.append(" <div id='id").append(cp.getComponentName()).append("_tbl'></div>");
		sb.append("</div>");
		return sb.toString();
	}

	public static class AttachmentHistoryTbl extends AbstractDbTablePagerHandler {
		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cp) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			return ((IAttachmentHandler) nCP.getComponentHandler()).queryAttachmentHistory(nCP);
		}

		@Override
		protected AbstractElement<?> createHeadStatElement(final int count, final String pTitle) {
			return super.createHeadStatElement(count, pTitle).addClassName("br");
		}

		@Override
		protected Map<String, Object> getRowData(final ComponentParameter cp,
				final Object dataObject) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			return ((IAttachmentHandler) nCP.getComponentHandler()).getAttachmentHistoryRowData(nCP,
					dataObject);
		}
	}
}