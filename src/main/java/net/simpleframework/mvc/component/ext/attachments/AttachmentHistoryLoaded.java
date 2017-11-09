package net.simpleframework.mvc.component.ext.attachments;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
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
				.addComponentBean(componentName + "_tbl", TablePagerBean.class).setShowHead(false)
				.setPagerBarLayout(EPagerBarLayout.bottom).setExportAction("false")
				.setContainerId("id" + componentName + "_tbl")
				.setHandlerClass(AttachmentHistoryTbl.class).setAttr("$attachment", attachmentBean);
		tablePager.addColumn(new TablePagerColumn("topic"));
	}

	public static String toHTML(final ComponentParameter cp) throws Exception {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class='AttachmentHistoryTbl'>");
		sb.append(" <div class='tt'></div>");
		sb.append(" <div id='id").append(cp.getComponentName()).append("_tbl'></div>");
		sb.append(" <div class='bb'></div>");
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
		protected Map<String, Object> getRowData(final ComponentParameter cp,
				final Object dataObject) {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			return ((IAttachmentHandler) nCP.getComponentHandler()).getAttachmentHistoryRowData(nCP,
					dataObject);
		}
	}
}