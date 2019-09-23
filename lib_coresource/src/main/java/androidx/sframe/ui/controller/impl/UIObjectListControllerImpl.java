package androidx.sframe.ui.controller.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.widget.RefreshLayout;
import androidx.demon.widget.RefreshMode;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sframe.R;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.DataSourceNotifyController2;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIObjectListController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.utils.Collections;
import androidx.sframe.utils.SystemCompat;
import androidx.sframe.widget.RefreshLoadView;
import androidx.sframe.widget.adapter.RecyclerAdapter;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;
import androidx.sframe.widget.adapter.RecyclerFragment;

/**
 * Author create by ok on 2019/1/5
 * Email : ok@163.com.
 */
public class UIObjectListControllerImpl<Page, DataSource> implements UIObjectListController<Page, DataSource>, RefreshLayout.OnRefreshListener, LifecycleEventObserver {

	private final AppPageController<Page> mPageController;

	private OnFindViewListener mFindViewListener;
	private OnDataSourceListener mDataSourceListener;

	private int page = 1;

	public UIObjectListControllerImpl(@NonNull AppPageController<Page> pageController) {
		this.mPageController = pageController;
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.fragment_list_layout;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getPageController()
				.getLifecycle().addObserver(this);

		final Page pageOwner = this.getPageController().getPageOwner();
		if (pageOwner instanceof RecyclerAdapter.Delegate) {
			this.setDelegate((RecyclerAdapter.Delegate<DataSource>) pageOwner);
		}
		if (pageOwner instanceof RecyclerAdapter.OnItemClickListener) {
			this.setOnItemClickListener((RecyclerAdapter.OnItemClickListener<DataSource>) pageOwner);
		}
		if (pageOwner instanceof OnDataSourceListener) {
			this.setOnDataSourceListener((OnDataSourceListener) pageOwner);
		}

		this.getLayoutController()
				.setShouldAutoLayoutMode(true);
		this.getViewController()
				.addOnFindViewListener((mFindViewListener = new OnFindViewListener()));

		final RefreshLayout preRefreshLayout = this.getRefreshLayout();
		preRefreshLayout.setOnRefreshListener(this);

		final LinearLayoutManager layoutManager = new LinearLayoutManager(preRefreshLayout.getContext());
		DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
		defaultItemAnimator.setSupportsChangeAnimations(false);

		final RecyclerView preRecyclerView = this.getRecyclerView();
//		preRecyclerView.setItemAnimator(defaultItemAnimator);
		preRecyclerView.setLayoutManager(layoutManager);
		preRecyclerView.setHasFixedSize(true);

		this.setLoadingView(R.layout.layout_page_loading_default)
				.setLoadingEnabled(true);
		this.setEmptyView(R.layout.layout_page_empty_default)
				.setEmptyEnabled(true);
	}

	@Override
	public void onRefreshing(@NonNull RefreshLayout refreshLayout, @NonNull RefreshMode mode) {
		if (RefreshMode.REFRESH_MODE_START == mode) {
			page = 1;
		} else if (RefreshMode.REFRESH_MODE_END == mode) {
			++page;
		}
		if (RefreshMode.REFRESH_MODE_NONE != mode) {
			this.getLayoutController().refreshed();
		}
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params) {
		if (this.mDataSourceListener != null) {
			this.mDataSourceListener.onPageDataSourceChanged(params, page, LIST_LIMIT);
		}
	}

	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		if (Lifecycle.Event.ON_DESTROY == event) {
			this.getViewController().removeOnFindViewListener(mFindViewListener);
		}
	}

	public UIViewController getViewController() {
		return this.getPageController().getViewController();
	}

	public UILayoutController getLayoutController() {
		return this.getPageController().getLayoutController();
	}

	public UIToolbarController getToolbarController() {
		return this.getLayoutController().getToolbarController();
	}

	public AppPageController<Page> getPageController() {
		return this.mPageController;
	}

	@Override
	public RecyclerView getRecyclerView() {
		return this.getViewController().findViewById(R.id.app_refresh_view_id);
	}

	@Override
	public RefreshLayout getRefreshLayout() {
		return this.getViewController().findViewById(R.id.listContainer);
	}

	@NonNull
	@Override
	public RecyclerAdapter<DataSource> getRecyclerAdapter() {
		RecyclerView.Adapter adapter = this.getRecyclerView().getAdapter();
		if (adapter instanceof RecyclerAdapter) {
			return (RecyclerAdapter<DataSource>) adapter;
		}
		throw new IllegalStateException("UIObjectListController " + this + " not set RecyclerAdapter");
	}

	@Override
	public DataSourceNotifyController<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController() {
		return this.getRecyclerAdapter().getDataSourceController();
	}

	@Override
	public DataSourceNotifyController2<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController2() {
		DataSourceNotifyController<? extends RecyclerAdapter<DataSource>, DataSource> dataSourceController = this.getDataSourceController();
		if (dataSourceController instanceof DataSourceNotifyController2) {
			return (DataSourceNotifyController2<? extends RecyclerAdapter<DataSource>, DataSource>) dataSourceController;
		}
		return null;
	}

	@Override
	public <HDataSource> UIObjectListController<Page, DataSource> setHeaderAdapter(@NonNull RecyclerChildAdapter<HDataSource, DataSource> adapter) {
		this.getRecyclerAdapter().setHeaderRecyclerAdapter(adapter);
		return this;
	}

	@Override
	public <FDataSource> UIObjectListController<Page, DataSource> setFooterAdapter(@NonNull RecyclerChildAdapter<FDataSource, DataSource> adapter) {
		this.getRecyclerAdapter().setFooterRecyclerAdapter(adapter);
		return this;
	}

	@Override
	public <HDataSource> RecyclerChildAdapter<HDataSource, DataSource> setHeaderDelegate(@NonNull RecyclerChildAdapter.Delegate<HDataSource, DataSource> delegate) {
		RecyclerChildAdapter<HDataSource, DataSource> adapter = RecyclerChildAdapter.create(delegate);
		this.getRecyclerAdapter().setHeaderRecyclerAdapter(adapter);
		return adapter;
	}

	@Override
	public <FDataSource> RecyclerChildAdapter<FDataSource, DataSource> setFooterDelegate(@NonNull RecyclerChildAdapter.Delegate<FDataSource, DataSource> delegate) {
		RecyclerChildAdapter<FDataSource, DataSource> adapter = RecyclerChildAdapter.create(delegate);
		this.getRecyclerAdapter().setFooterRecyclerAdapter(adapter);
		return adapter;
	}

	@Override
	public <HDataSource> RecyclerChildAdapter<HDataSource, DataSource> getHeaderRecyclerAdapter() {
		return this.getRecyclerAdapter().getHeaderRecyclerAdapter();
	}

	@Override
	public <FDataSource> RecyclerChildAdapter<FDataSource, DataSource> getFooterRecyclerAdapter() {
		return this.getRecyclerAdapter().getFooterRecyclerAdapter();
	}

	@Override
	public UIObjectListController<Page, DataSource> addHeaderView(@LayoutRes int layoutId) {
		return this.addHeaderView(this.getViewController().findAtParent().inflate(layoutId, this.getRecyclerView(), false));
	}

	@Override
	public UIObjectListController<Page, DataSource> addHeaderView(@NonNull final View view) {
		this.addHeaderView(new SimpleRecyclerFragment(view));
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> addFooterView(@LayoutRes int layoutId) {
		return this.addFooterView(this.getViewController().findAtParent().inflate(layoutId, this.getRecyclerView(), false));
	}

	@Override
	public UIObjectListController<Page, DataSource> addFooterView(@NonNull final View view) {
		this.addFooterView(new SimpleRecyclerFragment(view));
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> addHeaderView(@NonNull RecyclerFragment recyclerFragment) {
		this.getRecyclerAdapter().addHeaderView(recyclerFragment);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> addFooterView(@NonNull RecyclerFragment recyclerFragment) {
		this.getRecyclerAdapter().addFooterView(recyclerFragment);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> removeHeaderViewAt(int position) {
		this.getHeaderRecyclerAdapter().getDataSourceController().removeDataSource(position);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> removeFooterViewAt(int position) {
		this.getFooterRecyclerAdapter().getDataSourceController().removeDataSource(position);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setDraggingToStart(boolean dragEnabled) {
		this.getRefreshLayout().setDraggingToStart(dragEnabled);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setDraggingToEnd(boolean dragEnabled) {
		this.getRefreshLayout().setDraggingToEnd(dragEnabled);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setHeaderEnabled(boolean enabled) {
		this.getRecyclerAdapter().setShouldHeaderEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setFooterEnabled(boolean enabled) {
		this.getRecyclerAdapter().setShouldFooterEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setLoadingEnabled(boolean enabled) {
		this.getRecyclerAdapter().setShouldLoadingEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setEmptyEnabled(boolean enabled) {
		this.getRecyclerAdapter().setShouldEmptyEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setLoadingView(@LayoutRes int layoutId) {
		return this.setLoadingView(this.getViewController().findAtParent().inflate(layoutId, this.getRecyclerView(), false));
	}

	@Override
	public UIObjectListController<Page, DataSource> setLoadingView(@NonNull View view) {
		this.getRecyclerAdapter().setLoadingView(view);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setEmptyView(@LayoutRes int layoutId) {
		return this.setEmptyView(this.getViewController().findAtParent().inflate(layoutId, this.getRecyclerView(), false));
	}

	@Override
	public UIObjectListController<Page, DataSource> setEmptyView(@NonNull View view) {
		this.getRecyclerAdapter().setEmptyView(view);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setHeaderStickyView(@LayoutRes int layoutId) {
		return this.setHeaderStickyView(this.getViewController().findAtParent().inflate(layoutId, this.getRefreshLayout(), false));
	}

	private View mHeaderStickyView;
	private View mFooterStickyView;

	@Override
	public UIObjectListController<Page, DataSource> setHeaderStickyView(@NonNull View view) {
		final RecyclerView recyclerView = this.getRecyclerView();
		int paddingLeft = recyclerView.getPaddingLeft();
		int paddingTop = recyclerView.getPaddingTop();
		int paddingRight = recyclerView.getPaddingRight();
		int paddingBottom = recyclerView.getPaddingBottom();

		final RefreshLayout refreshLayout = this.getRefreshLayout();
		final int orientation = refreshLayout.getOrientation();
		if (this.mHeaderStickyView != null && this.mHeaderStickyView.getParent() == refreshLayout) {
			if (RefreshLayout.HORIZONTAL == orientation) {
				paddingLeft -= this.mHeaderStickyView.getMeasuredWidth();
			} else if (RefreshLayout.VERTICAL == orientation) {
				paddingTop -= this.mHeaderStickyView.getMeasuredHeight();
			}
			refreshLayout.removeView(this.mHeaderStickyView);
		}

		final RefreshLayout.LayoutParams preLayoutParams;
		final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (layoutParams == null) {
			preLayoutParams = new RefreshLayout.LayoutParams(RefreshLayout.LayoutParams.WRAP_CONTENT, RefreshLayout.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(preLayoutParams);
		} else if (!(layoutParams instanceof RefreshLayout.LayoutParams)) {
			preLayoutParams = new RefreshLayout.LayoutParams(layoutParams);
			view.setLayoutParams(preLayoutParams);
		} else {
			preLayoutParams = (RefreshLayout.LayoutParams) layoutParams;
		}
		preLayoutParams.mScrollFlag = RefreshLayout.LayoutParams.SCROLL_FLAG_START;

		if (RefreshLayout.HORIZONTAL == orientation) {
			preLayoutParams.addRule(RefreshLayout.ALIGN_PARENT_LEFT);
			preLayoutParams.addRule(RefreshLayout.CENTER_VERTICAL);
		} else if (RefreshLayout.VERTICAL == orientation) {
			preLayoutParams.addRule(RefreshLayout.ALIGN_PARENT_TOP);
			preLayoutParams.addRule(RefreshLayout.CENTER_HORIZONTAL);
		}
		refreshLayout.addView(view, preLayoutParams);
		this.mHeaderStickyView = view;
		this.mHeaderStickyView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

		if (RefreshLayout.HORIZONTAL == orientation) {
			paddingLeft += this.mHeaderStickyView.getMeasuredWidth();
		} else if (RefreshLayout.VERTICAL == orientation) {
			paddingTop += this.mHeaderStickyView.getMeasuredHeight();
		}
		recyclerView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setFooterStickyView(@LayoutRes int layoutId) {
		return this.setFooterStickyView(this.getViewController().findAtParent().inflate(layoutId, this.getRefreshLayout(), false));
	}

	@Override
	public UIObjectListController<Page, DataSource> setFooterStickyView(@NonNull View view) {
		final RecyclerView recyclerView = this.getRecyclerView();
		int paddingLeft = recyclerView.getPaddingLeft();
		int paddingTop = recyclerView.getPaddingTop();
		int paddingRight = recyclerView.getPaddingRight();
		int paddingBottom = recyclerView.getPaddingBottom();

		final RefreshLayout refreshLayout = this.getRefreshLayout();
		final int orientation = refreshLayout.getOrientation();
		if (this.mFooterStickyView != null && this.mFooterStickyView.getParent() == refreshLayout) {
			if (RefreshLayout.HORIZONTAL == orientation) {
				paddingRight -= this.mFooterStickyView.getMeasuredWidth();
			} else if (RefreshLayout.VERTICAL == orientation) {
				paddingBottom -= this.mFooterStickyView.getMeasuredHeight();
			}
			refreshLayout.removeView(this.mFooterStickyView);
		}

		final RefreshLayout.LayoutParams preLayoutParams;
		final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (layoutParams == null) {
			preLayoutParams = new RefreshLayout.LayoutParams(RefreshLayout.LayoutParams.WRAP_CONTENT, RefreshLayout.LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(preLayoutParams);
		} else if (!(layoutParams instanceof RefreshLayout.LayoutParams)) {
			preLayoutParams = new RefreshLayout.LayoutParams(layoutParams);
			view.setLayoutParams(preLayoutParams);
		} else {
			preLayoutParams = (RefreshLayout.LayoutParams) layoutParams;
		}
		preLayoutParams.mScrollFlag = RefreshLayout.LayoutParams.SCROLL_FLAG_END;

		if (RefreshLayout.HORIZONTAL == orientation) {
			preLayoutParams.addRule(RefreshLayout.ALIGN_PARENT_RIGHT);
			preLayoutParams.addRule(RefreshLayout.CENTER_VERTICAL);
		} else if (RefreshLayout.VERTICAL == orientation) {
			preLayoutParams.addRule(RefreshLayout.ALIGN_PARENT_BOTTOM);
			preLayoutParams.addRule(RefreshLayout.CENTER_HORIZONTAL);
		}
		refreshLayout.addView(view, preLayoutParams);
		this.mFooterStickyView = view;
		this.mFooterStickyView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

		if (RefreshLayout.HORIZONTAL == orientation) {
			paddingRight += this.mFooterStickyView.getMeasuredWidth();
		} else if (RefreshLayout.VERTICAL == orientation) {
			paddingBottom += this.mFooterStickyView.getMeasuredHeight();
		}
		recyclerView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setRefreshMode(@NonNull RefreshMode mode) {
		this.getRefreshLayout().setRefreshMode(mode);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setAdapter(@NonNull RecyclerAdapter<DataSource> adapter) {
		this.getRecyclerView().setAdapter(adapter);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setDelegate(@NonNull RecyclerAdapter.Delegate<DataSource> delegate) {
		return this.setAdapter(new RecyclerAdapter<>(delegate));
	}

	@Override
	public UIObjectListController<Page, DataSource> setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
		this.getRecyclerView().setLayoutManager(layoutManager);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setOnDataSourceListener(@NonNull OnDataSourceListener listener) {
		this.mDataSourceListener = listener;
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setOnItemClickListener(@NonNull RecyclerAdapter.OnItemClickListener<DataSource> listener) {
		this.getRecyclerAdapter().setOnItemClickListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setOnItemLongClickListener(@NonNull RecyclerAdapter.OnItemLongClickListener<DataSource> listener) {
		this.getRecyclerAdapter().setOnItemLongClickListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setItemAnimator(@NonNull RecyclerView.ItemAnimator animator) {
		this.getRecyclerView().setItemAnimator(animator);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
		return this.addItemDecoration(itemDecoration, -1);
	}

	@Override
	public UIObjectListController<Page, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration, int index) {
		this.getRecyclerView().addItemDecoration(itemDecoration, index);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> removeItemDecorationAt(int index) {
		this.getRecyclerView().removeItemDecorationAt(index);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> removeItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
		this.getRecyclerView().removeItemDecoration(itemDecoration);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> addOnScrollListener(@NonNull RecyclerView.OnScrollListener listener) {
		this.getRecyclerView().addOnScrollListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> removeOnScrollListener(@NonNull RecyclerView.OnScrollListener listener) {
		this.getRecyclerView().removeOnScrollListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> test(@NonNull final List<DataSource> dataSourceList) {
		this.getViewController().postDelayed(new Runnable() {
			@Override
			public void run() {
				setDataSource(dataSourceList);
			}
		}, 2000);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> removeAll() {
		this.getDataSourceController().removeAll();
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> setDataSource(@NonNull DataSource dataSource) {
		return this.removeAll().addDataSource(dataSource);
	}

	@Override
	public UIObjectListController<Page, DataSource> setDataSource(@NonNull Collection<? extends DataSource> dataSources) {
		return this.removeAll().addDataSource(dataSources);
	}

	@Override
	public UIObjectListController<Page, DataSource> addDataSource(@NonNull DataSource dataSource) {
		return this.addDataSource(dataSource, this.getDataSourceController().getDataSourceCount());
	}

	@Override
	public UIObjectListController<Page, DataSource> addDataSource(@NonNull DataSource dataSource, int index) {
		return this.addDataSource(Collections.toList(dataSource), index);
	}

	@Override
	public UIObjectListController<Page, DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources) {
		return this.addDataSource(dataSources, this.getDataSourceController().getDataSourceCount());
	}

	@Override
	public UIObjectListController<Page, DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources, int index) {
		UpdateDataSourceTask mUpdateDataSourceTask = new UpdateDataSourceTask(dataSources, index);
		if (SystemCompat.isOnMainThread()) {
			mUpdateDataSourceTask.run();
			return this;
		}
		this.getViewController().post(mUpdateDataSourceTask);
		return this;
	}

	@Override
	public UIObjectListController<Page, DataSource> notifyDataSetChanged() {
		this.getRecyclerAdapter().notifyDataSetChanged();
		return this;
	}

	@Override
	public DataSource findDataSourceByPosition(int position) {
		return this.getDataSourceController().findDataSourceByPosition(position);
	}

	/* package */ final class UpdateDataSourceTask implements Runnable {

		private final Collection<? extends DataSource> mDataSources;
		private final int index;

		UpdateDataSourceTask(@Nullable Collection<? extends DataSource> dataSources, int index) {
			this.mDataSources = dataSources;
			this.index = index;
		}

		@Override
		public void run() {
			if (Lifecycle.State.DESTROYED == getPageController().getLifecycle().getCurrentState()) {
				return;
			}
			setLoadingEnabled(false);

			if (this.mDataSources == null || this.mDataSources.isEmpty()) {
				page--;
				page = page <= 0 ? 1 : page;
				// update list
				getDataSourceController().notifyDataSetChanged();
			} else {
				if (page == 1) {
					getDataSourceController().setDataSource(this.mDataSources);
				} else {
					getDataSourceController().addDataSource(this.mDataSources, this.index);
				}
			}

			int mLayoutState = getLayoutController().getCurLayoutKey();
			getLayoutController().layoutOfContent(400);

			if (UILayoutController.LayoutType.Content.key == mLayoutState) {
				getRefreshLayout().setRefreshing(false, 700);
			} else {
				getRefreshLayout().setRefreshing(false, 1100);
			}

			final RefreshLayout.LoadView mLoadView = getRefreshLayout().getFooterLoadView();
			if (mLoadView instanceof RefreshLoadView) {
				if (this.mDataSources == null
						|| this.mDataSources.isEmpty()
						|| this.mDataSources.size() < LIST_LIMIT) {
					((RefreshLoadView) mLoadView).setRefreshTips(true);
				} else {
					((RefreshLoadView) mLoadView).setRefreshTips(false);
				}
			}
		}
	}

	/* package */ final class OnFindViewListener implements UIViewController.OnFindViewListener {

		public <V extends View> V findViewById(@IdRes int id) {
			View children;
			if (getRecyclerAdapter().getLoadingView() != null) {
				children = getRecyclerAdapter().getLoadingView().findViewById(id);
				if (children != null) {
					return (V) children;
				}
			}
			if (getRecyclerAdapter().getEmptyView() != null) {
				children = getRecyclerAdapter().getEmptyView().findViewById(id);
				if (children != null) {
					return (V) children;
				}
			}
			return null;
		}
	}

	/* package */ final class SimpleRecyclerFragment extends RecyclerFragment {

		private final View view;

		SimpleRecyclerFragment(@NonNull View view) {
			this.view = view;
		}

		@NonNull
		@Override
		public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
			return this.view;
		}
	}
}
