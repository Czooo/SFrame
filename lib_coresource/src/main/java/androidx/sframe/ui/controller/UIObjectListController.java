package androidx.sframe.ui.controller;

import android.os.Bundle;
import android.view.View;

import java.util.Collection;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.widget.RefreshLayout;
import androidx.demon.widget.RefreshMode;
import androidx.sframe.widget.adapter.RecyclerAdapter;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author create by ok on 2019/1/5
 * Email : ok@163.com.
 */
public interface UIObjectListController<PageType, DataSource> {

	int LIST_LIMIT = 20;

	int onPageLayoutId(@Nullable Bundle savedInstanceState);

	void onPageViewCreated(@Nullable Bundle savedInstanceState);

	void onPageDataSourceChanged(@Nullable Object params);

	UIViewController getViewController();

	UILayoutController getLayoutController();

	UIToolbarController getToolbarController();

	AppPageController<PageType> getPageController();

	RefreshLayout getRefreshLayout();

	RecyclerView getRecyclerView();

	DataSource findDataSourceByPosition(int position);

	DataSourceController<DataSource> getDataSourceController();

	RecyclerAdapterController<DataSource> getRecyclerAdapterController();

	DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> getDataSourceNotifyController();

	<HDataSource> UIObjectListController<PageType, DataSource> setHeaderAdapter(@NonNull RecyclerChildAdapter<HDataSource, DataSource> adapter);

	<HDataSource> RecyclerChildAdapter<HDataSource, DataSource> setHeaderDelegate(@NonNull RecyclerAdapterController.ChildDelegate<HDataSource, DataSource> delegate);

	<FDataSource> UIObjectListController<PageType, DataSource> setFooterAdapter(@NonNull RecyclerChildAdapter<FDataSource, DataSource> adapter);

	<FDataSource> RecyclerChildAdapter<FDataSource, DataSource> setFooterDelegate(@NonNull RecyclerAdapterController.ChildDelegate<FDataSource, DataSource> delegate);

	<HDataSource> RecyclerChildAdapter<HDataSource, DataSource> getHeaderAdapter();

	<FDataSource> RecyclerChildAdapter<FDataSource, DataSource> getFooterAdapter();

	UIObjectListController<PageType, DataSource> addHeaderView(@NonNull View children);

	UIObjectListController<PageType, DataSource> addHeaderLayout(@LayoutRes int layoutId);

	UIObjectListController<PageType, DataSource> addFooterView(@NonNull View children);

	UIObjectListController<PageType, DataSource> addFooterLayout(@LayoutRes int layoutId);

	UIObjectListController<PageType, DataSource> removeHeaderViewAt(int position);

	UIObjectListController<PageType, DataSource> removeFooterViewAt(int position);

	UIObjectListController<PageType, DataSource> setDraggingToStart(boolean dragEnabled);

	UIObjectListController<PageType, DataSource> setDraggingToEnd(boolean dragEnabled);

	UIObjectListController<PageType, DataSource> setHeaderEnabled(boolean enabled);

	UIObjectListController<PageType, DataSource> setFooterEnabled(boolean enabled);

	UIObjectListController<PageType, DataSource> setLoadingEnabled(boolean enabled);

	UIObjectListController<PageType, DataSource> setEmptyEnabled(boolean enabled);

	UIObjectListController<PageType, DataSource> setLoadingView(@NonNull View view);

	UIObjectListController<PageType, DataSource> setEmptyView(@NonNull View view);

	UIObjectListController<PageType, DataSource> setStartStickLayout(@LayoutRes int layoutId);

	UIObjectListController<PageType, DataSource> setStartStickView(@NonNull View view);

	UIObjectListController<PageType, DataSource> setEndStickLayout(@LayoutRes int layoutId);

	UIObjectListController<PageType, DataSource> setEndStickView(@NonNull View view);

	UIObjectListController<PageType, DataSource> setRefreshMode(@NonNull RefreshMode mode);

	UIObjectListController<PageType, DataSource> setDelegate(@NonNull RecyclerAdapterController.Delegate<DataSource> delegate);

	UIObjectListController<PageType, DataSource> setHeaderOrFooterSpanSizeLookup(RecyclerAdapterController.SpanSizeLookup spanSizeLookup);

	UIObjectListController<PageType, DataSource> setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager);

	UIObjectListController<PageType, DataSource> setItemAnimator(@NonNull RecyclerView.ItemAnimator animator);

	UIObjectListController<PageType, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration);

	UIObjectListController<PageType, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration, int index);

	UIObjectListController<PageType, DataSource> removeItemDecorationAt(int index);

	UIObjectListController<PageType, DataSource> removeItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration);

	UIObjectListController<PageType, DataSource> setOnDataSourceListener(@NonNull OnDataSourceListener listener);

	UIObjectListController<PageType, DataSource> setOnItemClickListener(@NonNull RecyclerAdapterController.OnItemClickListener<DataSource> listener);

	UIObjectListController<PageType, DataSource> setOnItemTouchListener(@NonNull RecyclerAdapterController.OnItemTouchListener<DataSource> listener);

	UIObjectListController<PageType, DataSource> setOnItemLongClickListener(@NonNull RecyclerAdapterController.OnItemLongClickListener<DataSource> listener);

	UIObjectListController<PageType, DataSource> addOnScrollListener(RecyclerView.OnScrollListener listener);

	UIObjectListController<PageType, DataSource> removeOnScrollListener(RecyclerView.OnScrollListener listener);

	UIObjectListController<PageType, DataSource> test(List<DataSource> dataSourceList);

	UIObjectListController<PageType, DataSource> removeAll();

	UIObjectListController<PageType, DataSource> setDataSource(DataSource dataSource);

	UIObjectListController<PageType, DataSource> setDataSource(DataSource dataSource, int index);

	UIObjectListController<PageType, DataSource> setDataSourceList(Collection<? extends DataSource> dataSourceList);

	UIObjectListController<PageType, DataSource> setDataSourceList(Collection<? extends DataSource> dataSourceList, int index);

	UIObjectListController<PageType, DataSource> addDataSource(DataSource dataSource);

	UIObjectListController<PageType, DataSource> addDataSource(DataSource dataSource, int index);

	UIObjectListController<PageType, DataSource> addDataSourceList(Collection<? extends DataSource> dataSourceList);

	UIObjectListController<PageType, DataSource> addDataSourceList(Collection<? extends DataSource> dataSourceList, int index);

	UIObjectListController<PageType, DataSource> notifyDataSetChanged();

	UIObjectListController<PageType, DataSource> notifyHeaderDataSetChanged();

	UIObjectListController<PageType, DataSource> notifyFooterDataSetChanged();

	interface OnDataSourceListener {

		void onPageDataSourceChanged(@Nullable Object params, int page, int limit);
	}
}
