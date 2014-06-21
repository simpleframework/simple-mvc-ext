package net.simpleframework.mvc.component.ext.category.ctx;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.ado.ADOException;
import net.simpleframework.ado.bean.IDescriptionBeanAware;
import net.simpleframework.ado.bean.IIdBeanAware;
import net.simpleframework.ado.bean.INameBeanAware;
import net.simpleframework.ado.bean.IOrderBeanAware;
import net.simpleframework.ado.bean.ITextBeanAware;
import net.simpleframework.ado.bean.ITreeBeanAware;
import net.simpleframework.ado.query.IDataQuery;
import net.simpleframework.ado.trans.TransactionVoidCallback;
import net.simpleframework.ctx.service.ado.ITreeBeanServiceAware;
import net.simpleframework.ctx.service.ado.db.IDbBeanService;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.DefaultCategoryHandler;
import net.simpleframework.mvc.component.ext.category.LinkAddCategoryNode;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class CategoryBeanAwareHandler<T extends IIdBeanAware> extends
		DefaultCategoryHandler {

	protected abstract IDbBeanService<T> getBeanService();

	@SuppressWarnings("unchecked")
	@Override
	protected IDataQuery<?> categoryBeans(final ComponentParameter cp, final Object categoryId) {
		final IDbBeanService<T> bService = getBeanService();
		if (bService instanceof ITreeBeanServiceAware) {
			return ((ITreeBeanServiceAware<ITreeBeanAware>) bService)
					.queryChildren((ITreeBeanAware) bService.getBean(categoryId));
		}
		return super.categoryBeans(cp, categoryId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JavascriptForward onCategoryMove(final ComponentParameter cp, final TreeBean treeBean,
			final Object form, final Object to, final boolean up) {
		final JavascriptForward js = super.onCategoryMove(cp, treeBean, form, to, up);
		final IDbBeanService<T> mgr = getBeanService();
		mgr.getEntityManager().doExecuteTransaction(new TransactionVoidCallback() {
			@Override
			protected void doTransactionVoidCallback() throws ADOException {
				mgr.exchange((T) form, (T) to, up);
			}
		});
		return js;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onCategoryDragDrop(final ComponentParameter cp, final TreeBean treeBean,
			final Object drag, final Object drop) {
		final T _drag = (T) drag;
		T _drop = null;
		if (drop != null && drop.getClass().equals(drag.getClass())) {
			_drop = (T) drop;
		}
		if (_drag instanceof ITreeBeanAware) {
			((ITreeBeanAware) _drag).setParentId(_drop == null ? null : _drop.getId());
			getBeanService().update(_drag);
			return true;
		}
		return false;
	}

	@Override
	public void categoryEdit_onLoaded(final ComponentParameter cp,
			final Map<String, Object> dataBinding, final PageSelector selector) {
		super.categoryEdit_onLoaded(cp, dataBinding, selector);
		T parent = null;
		final IDbBeanService<T> mgr = getBeanService();
		final T t = mgr.getBean(cp.getParameter(PARAM_CATEGORY_ID));
		if (t != null) {
			dataBinding.put(PARAM_CATEGORY_ID, t.getId());
			if (t instanceof INameBeanAware) {
				dataBinding.put(PARAM_CATEGORY_NAME, ((INameBeanAware) t).getName());
			}
			if (t instanceof ITextBeanAware) {
				dataBinding.put(PARAM_CATEGORY_TEXT, ((ITextBeanAware) t).getText());
			}
			if (t instanceof IDescriptionBeanAware) {
				dataBinding.put(PARAM_CATEGORY_DESC, ((IDescriptionBeanAware) t).getDescription());
			}
			if (t instanceof ITreeBeanAware) {
				parent = mgr.getBean(((ITreeBeanAware) t).getParentId());
			}
		}
		if (parent == null && mgr instanceof ITreeBeanServiceAware) {
			parent = mgr.getBean(cp.getParameter(PARAM_CATEGORY_PARENTID));
		}
		if (parent != null) {
			dataBinding.put(PARAM_CATEGORY_PARENTID, parent.getId());
			if (parent instanceof ITextBeanAware) {
				dataBinding.put(PARAM_CATEGORY_PARENTTEXT, ((ITextBeanAware) parent).getText());
			}
		}
		onLoaded_dataBinding(cp, dataBinding, selector, t);
	}

	protected void onLoaded_dataBinding(final ComponentParameter cp,
			final Map<String, Object> dataBinding, final PageSelector selector, final T t) {
	}

	protected void onSave_setProperties(final ComponentParameter cp, final T t, final boolean insert) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public JavascriptForward categoryEdit_onSave(final ComponentParameter cp) {
		final JavascriptForward js = super.categoryEdit_onSave(cp);
		final IDbBeanService<T> mgr = getBeanService();
		T t = mgr.getBean(cp.getParameter(PARAM_CATEGORY_ID));
		final boolean insert = t == null;
		if (insert) {
			t = mgr.createBean();
		}
		onSave_setProperties(cp, t, insert);
		if (t instanceof INameBeanAware) {
			((INameBeanAware) t).setName(cp.getParameter(PARAM_CATEGORY_NAME));
		}
		if (t instanceof ITextBeanAware) {
			((ITextBeanAware) t).setText(cp.getParameter(PARAM_CATEGORY_TEXT));
		}
		if (t instanceof IDescriptionBeanAware) {
			((IDescriptionBeanAware) t).setDescription(cp.getParameter(PARAM_CATEGORY_DESC));
		}
		if (t instanceof ITreeBeanAware) {
			final T parent = mgr.getBean(cp.getParameter(PARAM_CATEGORY_PARENTID));
			((ITreeBeanAware) t).setParentId(parent == null ? null : parent.getId());
		}
		if (insert && t instanceof IOrderBeanAware) {
			((IOrderBeanAware) t).setOorder(mgr.max("oorder").intValue() + 1);
		}
		final T t2 = t;
		mgr.getEntityManager().doExecuteTransaction(new TransactionVoidCallback() {
			@Override
			protected void doTransactionVoidCallback() throws ADOException {
				if (insert) {
					mgr.insert(t2);
				} else {
					mgr.update(t2);
				}
			}
		});
		return js;
	}

	@SuppressWarnings("unchecked")
	protected <M extends ITreeBeanAware> void onDelete_assert(final ComponentParameter cp, final T t) {
		final IDbBeanService<T> mgr = getBeanService();
		if (mgr instanceof ITreeBeanServiceAware
				&& ((ITreeBeanServiceAware<M>) mgr).queryChildren((M) t).getCount() > 0) {
			throw ComponentHandlerException.of($m("BeanCategoryHandle.0"));
		}
	}

	@Override
	public JavascriptForward onCategoryDelete(final ComponentParameter cp, final TreeBean treeBean) {
		final JavascriptForward js = super.onCategoryDelete(cp, treeBean);
		final IDbBeanService<T> mgr = getBeanService();
		final T t = mgr.getBean(cp.getParameter(PARAM_CATEGORY_ID));
		if (t != null) {
			onDelete_assert(cp, t);
			mgr.getEntityManager().doExecuteTransaction(new TransactionVoidCallback() {
				@Override
				protected void doTransactionVoidCallback() throws ADOException {
					mgr.delete(t.getId());
				}
			});
		}
		return js;
	}

	protected TreeNode createRoot(final TreeBean treeBean, final String text) {
		return createRoot(treeBean, text, null);
	}

	protected TreeNode createRoot(final TreeBean treeBean, final String text, final String add) {
		final StringBuilder sb = new StringBuilder();
		sb.append(text).append("<br />");
		final LinkAddCategoryNode node = new LinkAddCategoryNode();
		if (add != null) {
			node.setText(add);
		}
		sb.append(node);
		final TreeNode tn = new TreeNode(treeBean, null, sb.toString());
		tn.setOpened(true);
		return tn;
	}
}
