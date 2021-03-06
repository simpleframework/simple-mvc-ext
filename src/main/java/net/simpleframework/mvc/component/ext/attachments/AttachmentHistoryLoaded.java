package net.simpleframework.mvc.component.ext.attachments;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.element.AbstractElement;
import net.simpleframework.mvc.common.element.ButtonElement;
import net.simpleframework.mvc.common.element.Checkbox;
import net.simpleframework.mvc.common.element.Radio;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.base.ajaxrequest.DefaultAjaxRequestHandler;
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
				.addComponentBean(componentName + "_history_tbl", TablePagerBean.class)
				.setShowCheckbox(true).setShowHead(false).setScrollHead(false)
				.setShowEditPageItems(false).setPagerBarLayout(EPagerBarLayout.bottom)
				.setExportAction("false").setNoResultDesc("")
				.setContainerId("id" + componentName + "_history_tbl")
				.setHandlerClass(AttachmentHistoryTbl.class).setAttr("$attachment", attachmentBean);
		tablePager.addColumn(TablePagerColumn.TOPIC()).addColumn(TablePagerColumn.OPE(55));

		// 选择
		pp.addComponentBean(componentName + "_history_selected", AjaxRequestBean.class)
				.setHandlerClass(AttachmentHistorySelected.class)
				.setSelector(attachmentBean.getSelector()).setAttr("$attachment", attachmentBean);
	}

	public static String toHTML(final ComponentParameter cp) throws Exception {
		final StringBuilder sb = new StringBuilder();
		final String componentName = cp.getComponentName();
		final String act = "$Actions['" + componentName + "_history_tbl']";
		sb.append("<div class='AttachmentHistoryTbl'>");
		sb.append(" <div class='tb2 clearfix'>");
		sb.append("  <div class='left'>")
				.append(
						new Checkbox("id" + componentName + "_checkAll", $m("AttachmentHistoryLoaded.6"))
								.setOnclick(act + ".checkAll(this);"))
				.append("</div>");
		sb.append("  <div class='right'>")
				.append(new ButtonElement($m("AttachmentHistoryLoaded.5"))
						.setOnclick("$Actions['" + cp.getComponentName()
								+ "_history_selected']('attachId=' + " + act + ".checkParams());"))
				.append("</div>");
		sb.append(" </div>");
		final String types = cp.getParameter("types");
		if (!StringUtils.hasText(types)) {
			sb.append(" <div class='tb'>");
			final String rname = componentName + "_radio";
			sb.append(new Radio("id" + componentName + "_radio0", $m("AttachmentHistoryLoaded.0"))
					.setOnclick(act + "('types=');").setChecked(true).setName(rname));
			sb.append(new Radio("id" + componentName + "_radio1", $m("AttachmentHistoryLoaded.1"))
					.setOnclick(act + "('types=doc;docx;pdf');").setVal("doc").setName(rname));
			sb.append(new Radio("id" + componentName + "_radio2", $m("AttachmentHistoryLoaded.2"))
					.setOnclick(act + "('types=png;jpg;jpeg;bmp;webp');").setVal("pic").setName(rname));
			sb.append(new Radio("id" + componentName + "_radio3", $m("AttachmentHistoryLoaded.3"))
					.setOnclick(act + "('types=mp3;m4a;wav');").setVal("audio").setName(rname));
			sb.append(new Radio("id" + componentName + "_radio4", $m("AttachmentHistoryLoaded.4"))
					.setOnclick(act + "('types=mp4;webm;ogg;flv');").setVal("video").setName(rname));
			sb.append(" </div>");
		}
		sb.append(" <div id='id").append(componentName).append("_history_tbl'></div>");
		sb.append("</div>");
		return sb.toString();
	}

	public static class AttachmentHistorySelected extends DefaultAjaxRequestHandler {

		@Override
		public IForward ajaxProcess(final ComponentParameter cp) throws Exception {
			final ComponentParameter nCP = ComponentParameter.getByAttri(cp, "$attachment");
			((IAttachmentHandler) nCP.getComponentHandler()).doAttachmentHistorySelected(nCP);
			final String componentName = nCP.getComponentName();
			return new JavascriptForward().append("$Actions['").append(componentName)
					.append("_historyWin'].close();").append("$Actions['").append(componentName)
					.append("_list']();");
		}
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