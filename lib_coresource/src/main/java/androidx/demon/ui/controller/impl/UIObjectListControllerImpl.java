package androidx.demon.ui.controller.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.parent.refreshview.widget.RefreshMode;
import org.parent.refreshview.widget.RefreshView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.R;
import androidx.demon.ui.abs.AbsChildDelegate;
import androidx.demon.compat.CoreCompat;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.ui.controller.DataSourceController;
import androidx.demon.ui.controller.DataSourceNotifyController2;
import androidx.demon.ui.controller.RecyclerAdapterController;
import androidx.demon.ui.controller.UILayoutController;
import androidx.demon.ui.controller.UIObjectListController;
import androidx.demon.ui.controller.UIToolbarController;
import androidx.demon.ui.controller.UIViewController;
import androidx.demon.widget.FooterLoadCallback;
import androidx.demon.widget.HeaderLoadCallback;
import androidx.demon.widget.adapter.RecyclerAdapter;
import androidx.demon.widget.adapter.RecyclerChildAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Author create by ok on 2019/1/5
 * Email : ok@163.com.
 */
public class UIObjectListControllerImpl<PageType, DataSource> implements UIObjectListController<PageType, DataSource>, RefreshView.OnRefreshListener, RefreshView.OnScrollListener, LifecycleEventObserver {

	private final AppPageController<PageType> mPageController;
	private RecyclerAdapter<DataSource> mRecyclerAdapter;

	private OnFindViewListener mFindViewListener;
	private OnDataSourceListener mDataSourceListener;

	private int page = 1;

	public UIObjectListControllerImpl(@NonNull AppPageController<PageType> pageController) {
		this.mPageController = pageController;
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.layout_list_default;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getPageController()
				.getLifecycle().addObserver(this);

		this.getLayoutController()
				.setShouldManualLayoutMode(true)
				.getViewController()
				.addOnFindViewListener((mFindViewListener = new OnFindViewListener()));

		final RecyclerView preRecyclerView = this.getRecyclerView();
		preRecyclerView.setHasFixedSize(true);
		preRecyclerView.setAdapter(this.getRecyclerAdapter());

		DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
		defaultItemAnimator.setSupportsChangeAnimations(false);
		preRecyclerView.setItemAnimator(defaultItemAnimator);

		final RefreshView preRefreshView = this.getRefreshView();
		preRefreshView.addOnScrollListener(this);
		preRefreshView.setOnRefreshListener(this);
		preRefreshView.setHeaderLoadCallback(new HeaderLoadCallback());
		preRefreshView.setFooterLoadCallback(new FooterLoadCallback());

		View mLoadingView = View.inflate(preRecyclerView.getContext(), R.layout.layout_loading_default, null);
		RecyclerView.LayoutParams mLayoutParams = new RecyclerView.LayoutParams(-1, -1);
		mLoadingView.setLayoutParams(mLayoutParams);
		this.setLoadingView(mLoadingView).setLoadingEnabled(true);

		View mEmptyView = View.inflate(preRecyclerView.getContext(), R.layout.layout_empty_default, null);
		mLayoutParams = new RecyclerView.LayoutParams(-1, -1);
		mEmptyView.setLayoutParams(mLayoutParams);
		this.setEmptyView(mEmptyView).setEmptyEnabled(true);

		final PageType prePageType = this.getPageController().getPageOwner();

		if (prePageType instanceof RecyclerAdapterController.Delegate) {
			this.setDelegate((RecyclerAdapterController.Delegate<DataSource>) prePageType);
		}

		if (prePageType instanceof RecyclerAdapterController.OnItemClickListener) {
			this.setOnItemClickListener((RecyclerAdapterController.OnItemClickListener<DataSource>) prePageType);
		}

		if (prePageType instanceof OnDataSourceListener) {
			this.setOnDataSourceListener((OnDataSourceListener) prePageType);
		}

		this.setLayoutManager(new LinearLayoutManager(preRecyclerView.getContext()));
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params) {
		if (this.mDataSourceListener != null) {
			this.mDataSourceListener.onPageDataSourceChanged(params, page, LIST_LIMIT);
		}
	}

	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		if(Lifecycle.Event.ON_DESTROY == event) {
			this.getViewController()
					.removeOnFindViewListener(mFindViewListener);
		}
	}

	@Override
	public UIViewController getViewController() {
		return this.getPageController().getViewController();
	}

	@Override
	public UILayoutController getLayoutController() {
		return this.getPageController().getLayoutController();
	}

	@Override
	public UIToolbarController getToolbarController() {
		return this.getLayoutController().getToolbarController();
	}

	@Override
	public AppPageController<PageType> getPageController() {
		return mPageController;
	}

	@Override
	public RefreshView getRefreshView() {
		return getViewController().findViewById(R.id.listContainer);
	}

	@Override
	public RecyclerView getRecyclerView() {
		return getViewController().findViewById(R.id.refreshTargetId);
	}

	@Override
	public DataSource findDataSourceByPosition(int position) {
		return getDataSourceController().findDataSourceByPosition(position);
	}

	@Override
	public DataSourceController<DataSource> getDataSourceController() {
		return getRecyclerAdapterController().getDataSourceController();
	}

	@Override
	public RecyclerAdapterController<DataSource> getRecyclerAdapterController() {
		return getRecyclerAdapter().getAdapter();
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> getDataSourceNotifyController() {
		return getRecyclerAdapterController().getDataSourceNotifyController();
	}

	@Override
	public <HDataSource> UIObjectListController<PageType, DataSource> setHeaderAdapter(@NonNull RecyclerChildAdapter<HDataSource, DataSource> adapter) {
		getRecyclerAdapter().setHeaderAdapter(adapter);
		return this;
	}

	@Override
	public <HDataSource> RecyclerChildAdapter<HDataSource, DataSource> setHeaderDelegate(@NonNull RecyclerAdapterController.ChildDelegate<HDataSource, DataSource> delegate) {
		final RecyclerChildAdapter<HDataSource, DataSource> preChildAdapter = RecyclerChildAdapter.create(delegate);
		getRecyclerAdapter().setHeaderAdapter(preChildAdapter);
		return preChildAdapter;
	}

	@Override
	public <FDataSource> UIObjectListController<PageType, DataSource> setFooterAdapter(@NonNull RecyclerChildAdapter<FDataSource, DataSource> adapter) {
		getRecyclerAdapter().setFooterAdapter(adapter);
		return this;
	}

	@Override
	public <FDataSource> RecyclerChildAdapter<FDataSource, DataSource> setFooterDelegate(@NonNull RecyclerAdapterController.ChildDelegate<FDataSource, DataSource> delegate) {
		final RecyclerChildAdapter<FDataSource, DataSource> preChildAdapter = RecyclerChildAdapter.create(delegate);
		getRecyclerAdapter().setFooterAdapter(preChildAdapter);
		return preChildAdapter;
	}

	@Override
	public <HDataSource> RecyclerChildAdapter<HDataSource, DataSource> getHeaderAdapter() {
		return getRecyclerAdapter().getHeaderAdapter();
	}

	@Override
	public <FDataSource> RecyclerChildAdapter<FDataSource, DataSource> getFooterAdapter() {
		return getRecyclerAdapter().getFooterAdapter();
	}

	private RecyclerChildAdapter<ViewModel, DataSource> mHeaderAdapter;
	private RecyclerChildAdapter<ViewModel, DataSource> mFooterAdapter;

	@Override
	public UIObjectListController<PageType, DataSource> addHeaderView(@NonNull View children) {
		if (mHeaderAdapter == null) {
			mHeaderAdapter = RecyclerChildAdapter.create(new ViewDelegate());
		}
		ViewModel mViewModel = new ViewModel(mHeaderAdapter.getDataSourceController().size(), children);
		mHeaderAdapter.getDataSourceController().addDataSource(mViewModel);
		return setHeaderAdapter(mHeaderAdapter);
	}

	@Override
	public UIObjectListController<PageType, DataSource> addHeaderLayout(@LayoutRes int layoutId) {
		return addHeaderView(getViewController().findAtParent().inflate(layoutId, getRecyclerView(), false));
	}

	@Override
	public UIObjectListController<PageType, DataSource> addFooterView(@NonNull View children) {
		if (mFooterAdapter == null) {
			mFooterAdapter = RecyclerChildAdapter.create(new ViewDelegate());
		}
		ViewModel mViewModel = new ViewModel(mFooterAdapter.getDataSourceController().size(), children);
		mFooterAdapter.getDataSourceController().addDataSource(mViewModel);
		return setFooterAdapter(mFooterAdapter);
	}

	@Override
	public UIObjectListController<PageType, DataSource> addFooterLayout(@LayoutRes int layoutId) {
		return addFooterView(getViewController().findAtParent().inflate(layoutId, getRecyclerView(), false));
	}

	@Override
	public UIObjectListController<PageType, DataSource> removeHeaderViewAt(int position) {
		RecyclerChildAdapter<Object, DataSource> headerAdapter = getHeaderAdapter();
		if (headerAdapter != null && position >= 0 && position < headerAdapter.getDataSourceController().size()) {
			headerAdapter.getDataSourceController().removeDataSource(position);
		}
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> removeFooterViewAt(int position) {
		RecyclerChildAdapter<Object, DataSource> footerAdapter = getFooterAdapter();
		if (footerAdapter != null && position >= 0 && position < footerAdapter.getDataSourceController().size()) {
			footerAdapter.getDataSourceController().removeDataSource(position);
		}
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setDragStartEnabled(boolean dragEnabled) {
		getRefreshView().setDragStartEnabled(dragEnabled);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setDragEndEnabled(boolean dragEnabled) {
		getRefreshView().setDragEndEnabled(dragEnabled);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setHeaderEnabled(boolean enabled) {
		getRecyclerAdapter().setHeaderEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setFooterEnabled(boolean enabled) {
		getRecyclerAdapter().setFooterEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setLoadingEnabled(boolean enabled) {
		getRecyclerAdapter().setLoadingEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setEmptyEnabled(boolean enabled) {
		getRecyclerAdapter().setEmptyEnabled(enabled);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setLoadingView(@NonNull View view) {
		getRecyclerAdapter().setLoadingView(view);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setEmptyView(@NonNull View view) {
		getRecyclerAdapter().setEmptyView(view);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setStartStickLayout(int layoutId) {
		return setStartStickView(getViewController().findAtParent().inflate(layoutId, getRefreshView(), false));
	}

	private WeakReference<View> mStartStickViewReference;
	private WeakReference<View> mEndStickViewReference;

	@Override
	public UIObjectListController<PageType, DataSource> setStartStickView(@NonNull View view) {
		if (mStartStickViewReference != null) {
			CoreCompat.removeInParentView(mStartStickViewReference.get());
			mStartStickViewReference.clear();
			mStartStickViewReference = null;
		}
		getRefreshView().addView(view);
		// save
		mStartStickViewReference = new WeakReference<>(view);
		return setOrientation(getRefreshView().getOrientation());
	}

	@Override
	public UIObjectListController<PageType, DataSource> setEndStickLayout(int layoutId) {
		return setEndStickView(getViewController().findAtParent().inflate(layoutId, getRefreshView(), false));
	}

	@Override
	public UIObjectListController<PageType, DataSource> setEndStickView(@NonNull View view) {
		if (mEndStickViewReference != null) {
			CoreCompat.removeInParentView(mEndStickViewReference.get());
			mEndStickViewReference.clear();
			mEndStickViewReference = null;
		}
		getRefreshView().addView(view);
		// save
		mEndStickViewReference = new WeakReference<>(view);
		return setOrientation(getRefreshView().getOrientation());
	}

	@Override
	public UIObjectListController<PageType, DataSource> setRefreshMode(@NonNull RefreshMode mode) {
		getRefreshView().setRefreshMode(mode);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setDelegate(@NonNull RecyclerAdapterController.Delegate<DataSource> delegate) {
		getRecyclerAdapter().setDelegate(delegate);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setHeaderOrFooterSpanSizeLookup(@NonNull RecyclerAdapterController.SpanSizeLookup spanSizeLookup) {
		getRecyclerAdapter().setHeaderOrFooterSpanSizeLookup(spanSizeLookup);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
		getRecyclerView().setLayoutManager(layoutManager);
		getRecyclerAdapter().onAttachedToRecyclerView(getRecyclerView());

		final int orientation;
		// 滚动方向
		if (layoutManager instanceof LinearLayoutManager) {
			orientation = ((LinearLayoutManager) layoutManager).getOrientation();
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
		} else {
			orientation = LinearLayoutManager.VERTICAL;
		}
		return setOrientation(orientation);
	}

	private UIObjectListController<PageType, DataSource> setOrientation(int orientation) {
		// 暂只考虑RefreshView的Orientation，不考虑LinearLayout
		getRefreshView().setOrientation(orientation);
		// TODO 其他布局的方向改变：StartStickView／EndStickView

		if (mStartStickViewReference != null || mEndStickViewReference != null) {
			int paddingLeft = getRecyclerView().getPaddingLeft();
			int paddingTop = getRecyclerView().getPaddingTop();
			int paddingRight = getRecyclerView().getPaddingRight();
			int paddingBottom = getRecyclerView().getPaddingBottom();

			View children;
			RelativeLayout.LayoutParams mLayoutParams;

			if (mStartStickViewReference != null) {
				children = mStartStickViewReference.get();

				if (RefreshView.VERTICAL == getRefreshView().getOrientation()) {
					mLayoutParams = new RelativeLayout.LayoutParams(-1, -2);
					mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				} else {
					mLayoutParams = new RelativeLayout.LayoutParams(-2, -1);
					mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				}
				children.setLayoutParams(mLayoutParams);
				children.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

				if (RefreshView.VERTICAL == getRefreshView().getOrientation()) {
					paddingTop = children.getMeasuredHeight();
				} else {
					paddingLeft = children.getMeasuredWidth();
				}
			}

			if (mEndStickViewReference != null) {
				children = mEndStickViewReference.get();

				if (RefreshView.VERTICAL == getRefreshView().getOrientation()) {
					mLayoutParams = new RelativeLayout.LayoutParams(-1, -2);
					mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				} else {
					mLayoutParams = new RelativeLayout.LayoutParams(-2, -1);
					mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				}
				children.setLayoutParams(mLayoutParams);
				children.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

				if (RefreshView.VERTICAL == getRefreshView().getOrientation()) {
					paddingBottom = children.getMeasuredHeight();
				} else {
					paddingRight = children.getMeasuredWidth();
				}
			}
			getRecyclerView().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		}
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setOnDataSourceListener(@NonNull OnDataSourceListener listener) {
		this.mDataSourceListener = listener;
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setOnItemClickListener(@NonNull RecyclerAdapterController.OnItemClickListener<DataSource> listener) {
		getRecyclerAdapter().setOnItemClickListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setOnItemTouchListener(@NonNull RecyclerAdapterController.OnItemTouchListener<DataSource> listener) {
		getRecyclerAdapter().setOnItemTouchListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setOnItemLongClickListener(@NonNull RecyclerAdapterController.OnItemLongClickListener<DataSource> listener) {
		getRecyclerAdapter().setOnItemLongClickListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setItemAnimator(@NonNull RecyclerView.ItemAnimator animator) {
		getRecyclerView().setItemAnimator(animator);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
		return addItemDecoration(itemDecoration, -1);
	}

	@Override
	public UIObjectListController<PageType, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration, int index) {
		getRecyclerView().addItemDecoration(itemDecoration, index);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> removeItemDecorationAt(int index) {
		getRecyclerView().removeItemDecorationAt(index);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> removeItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
		getRecyclerView().removeItemDecoration(itemDecoration);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> addOnScrollListener(RecyclerView.OnScrollListener listener) {
		getRecyclerView().addOnScrollListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> removeOnScrollListener(RecyclerView.OnScrollListener listener) {
		getRecyclerView().removeOnScrollListener(listener);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> test(final List<DataSource> dataSourceList) {
		getViewController().postDelayed(new Runnable() {

			@Override
			public void run() {
				setDataSourceList(dataSourceList);
			}
		}, 2000);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> removeAll() {
		getDataSourceController().removeAll();
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> setDataSource(DataSource dataSource) {
		return setDataSource(dataSource, getDataSourceController().size());
	}

	@Override
	public UIObjectListController<PageType, DataSource> setDataSource(DataSource dataSource, int index) {
		List<DataSource> mDataSourceList = new ArrayList<>();
		mDataSourceList.add(dataSource);
		return setDataSourceList(mDataSourceList, index);
	}

	@Override
	public UIObjectListController<PageType, DataSource> setDataSourceList(Collection<? extends DataSource> dataSourceList) {
		return setDataSourceList(dataSourceList, getDataSourceController().size());
	}

	@Override
	public UIObjectListController<PageType, DataSource> setDataSourceList(Collection<? extends DataSource> dataSourceList, int index) {
		return removeAll().addDataSourceList(dataSourceList, index);
	}

	@Override
	public UIObjectListController<PageType, DataSource> addDataSource(DataSource dataSource) {
		return addDataSource(dataSource, getDataSourceController().size());
	}

	@Override
	public UIObjectListController<PageType, DataSource> addDataSource(DataSource dataSource, int index) {
		List<DataSource> mDataSourceList = new ArrayList<>();
		mDataSourceList.add(dataSource);
		return addDataSourceList(mDataSourceList, index);
	}

	@Override
	public UIObjectListController<PageType, DataSource> addDataSourceList(Collection<? extends DataSource> dataSourceList) {
		return addDataSourceList(dataSourceList, getDataSourceController().size());
	}

	@Override
	public UIObjectListController<PageType, DataSource> addDataSourceList(Collection<? extends DataSource> dataSourceList, int index) {
		AddDataSourceRunnable mDataSourceRunnable = new AddDataSourceRunnable(dataSourceList, index);
		if (CoreCompat.isOnMainThread()) {
			mDataSourceRunnable.run();
			return this;
		}
		getViewController().post(mDataSourceRunnable);
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> notifyDataSetChanged() {
		getRecyclerAdapter().notifyDataSetChanged();
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> notifyHeaderDataSetChanged() {
		getRecyclerAdapter().notifyHeaderDataSetChanged();
		return this;
	}

	@Override
	public UIObjectListController<PageType, DataSource> notifyFooterDataSetChanged() {
		getRecyclerAdapter().notifyFooterDataSetChanged();
		return this;
	}

	private RecyclerAdapter<DataSource> getRecyclerAdapter() {
		if (mRecyclerAdapter == null) {
			mRecyclerAdapter = new RecyclerAdapter<>();
		}
		return mRecyclerAdapter;
	}

	@Override
	public void onRefresh(@NonNull RefreshView refreshView, @NonNull RefreshMode mode) {
		if (RefreshMode.REFRESH_MODE_START == mode) {
			page = 1;
		} else if (RefreshMode.REFRESH_MODE_END == mode) {
			++page;
		}

		if (RefreshMode.REFRESH_MODE_NONE != mode) {
			getLayoutController()
					.refreshed();
		}
	}

	@Override
	public void onScrolled(@NonNull RefreshView refreshView, RefreshMode refreshMode, int newState, float scrollOffset) {
		ensureStickViewOffsetChange(refreshMode, scrollOffset);
	}

	private void ensureStickViewOffsetChange(RefreshMode mode, float scrollOffset) {
		View children = null;

		if (RefreshMode.REFRESH_MODE_START == mode) {

			if (mStartStickViewReference != null) {
				children = mStartStickViewReference.get();
			}
		} else if (RefreshMode.REFRESH_MODE_END == mode) {

			if (mEndStickViewReference != null) {
				children = mEndStickViewReference.get();
			}
		}

		if (children != null && children.getVisibility() == View.VISIBLE) {
			if (RefreshView.VERTICAL == getRefreshView().getOrientation()) {
				children.setTranslationY(scrollOffset);
			} else {
				children.setTranslationX(scrollOffset);
			}
		}
	}

	final class AddDataSourceRunnable implements Runnable {

		final Collection<? extends DataSource> mDataSourceList;

		final int index;

		AddDataSourceRunnable(Collection<? extends DataSource> dataSourceList, int index) {
			this.mDataSourceList = dataSourceList;
			this.index = index;
		}

		@Override
		public void run() {
			if (Lifecycle.State.DESTROYED == getPageController().getLifecycle().getCurrentState()) {
				return;
			}

			setLoadingEnabled(false);

			if (page == 1) {
				getDataSourceController()
						.setDataSourceList(mDataSourceList, index);
			} else {
				getDataSourceController()
						.addDataSourceList(mDataSourceList, index);
			}

			if (mDataSourceList == null || mDataSourceList.isEmpty()) {
				page--;
				page = page <= 0 ? 1 : page;
			}

			int mLayoutState = getLayoutController().getCurLayoutKey();
			getLayoutController().layoutOfContent(400);

			if (UILayoutController.LayoutType.Content.key == mLayoutState) {
				getRefreshView().setRefreshing(false, 700);
			} else {
				getRefreshView().setRefreshing(false, 1100);
			}

			if (mDataSourceList != null && mDataSourceList.size() >= LIST_LIMIT) {
				getRefreshView().getFooterLoadCallback().setRefreshTips(false);
			} else {
				getRefreshView().getFooterLoadCallback().setRefreshTips(true);
			}
		}
	}

	/* package */ final class ViewModel {

		private final View itemView;

		private final int itemViewType;

		ViewModel(int itemViewType, View itemView) {
			this.itemView = itemView;
			this.itemViewType = itemViewType;
		}
	}

	/* package */ final class ViewDelegate extends AbsChildDelegate<ViewModel, DataSource> {

		@Override
		public int getItemViewType(RecyclerAdapterController<DataSource> adapterController, RecyclerChildAdapter<ViewModel, DataSource> recyclerChildAdapter, int position) {
			ViewModel mViewModel = recyclerChildAdapter.findDataSourceByPosition(position);
			return mViewModel.itemViewType;
		}

		@Override
		public View onCreateItemView(RecyclerAdapterController<DataSource> adapterController, RecyclerChildAdapter<ViewModel, DataSource> recyclerChildAdapter, LayoutInflater inflater, ViewGroup parent, int itemViewType) {
			for (ViewModel mViewModel : recyclerChildAdapter.getDataSourceController().getAllDataSource()) {
				if (mViewModel.itemViewType == itemViewType) {
					return mViewModel.itemView;
				}
			}
			return null;
		}

		@Override
		public void onBindItemView(RecyclerChildAdapter.ViewHolder<ViewModel, DataSource> holder, int position, @Nullable List<Object> payloads) {
			// not-op
		}
	}

	/* package */ final class OnFindViewListener implements UIViewController.OnFindViewListener {

		public <V extends View> V findViewById(@IdRes int id) {
			View children;

			if (mHeaderAdapter != null) {

				for (ViewModel mViewModel : mHeaderAdapter.getDataSourceController().getAllDataSource()) {
					children = mViewModel.itemView.findViewById(id);

					if (children != null) {
						return (V) children;
					}
				}
			}

			if (mFooterAdapter != null) {

				for (ViewModel mViewModel : mFooterAdapter.getDataSourceController().getAllDataSource()) {
					children = mViewModel.itemView.findViewById(id);

					if (children != null) {
						return (V) children;
					}
				}
			}

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
}
