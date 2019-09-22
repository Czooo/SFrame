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
import androidx.recyclerview.widget.RecyclerView;
import androidx.sframe.widget.adapter.RecyclerAdapter;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;
import androidx.sframe.widget.adapter.RecyclerFragment;

/**
 * Author create by ok on 2019/1/5
 * Email : ok@163.com.
 */
public interface UIObjectListController<Page, DataSource> {

	int LIST_LIMIT = 20;

	int onPageLayoutId(@Nullable Bundle savedInstanceState);

	void onPageViewCreated(@Nullable Bundle savedInstanceState);

	void onPageDataSourceChanged(@Nullable Object params);

	RecyclerView getRecyclerView();

	RefreshLayout getRefreshLayout();

	RecyclerAdapter<DataSource> getRecyclerAdapter();

	DataSourceNotifyController<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController();

	DataSourceNotifyController2<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController2();

	<HDataSource> UIObjectListController<Page, DataSource> setHeaderAdapter(@NonNull RecyclerChildAdapter<HDataSource, DataSource> adapter);

	<FDataSource> UIObjectListController<Page, DataSource> setFooterAdapter(@NonNull RecyclerChildAdapter<FDataSource, DataSource> adapter);

	<HDataSource> RecyclerChildAdapter<HDataSource, DataSource> setHeaderDelegate(@NonNull RecyclerChildAdapter.Delegate<HDataSource, DataSource> delegate);

	<FDataSource> RecyclerChildAdapter<FDataSource, DataSource> setFooterDelegate(@NonNull RecyclerChildAdapter.Delegate<FDataSource, DataSource> delegate);

	<HDataSource> RecyclerChildAdapter<HDataSource, DataSource> getHeaderRecyclerAdapter();

	<FDataSource> RecyclerChildAdapter<FDataSource, DataSource> getFooterRecyclerAdapter();

	UIObjectListController<Page, DataSource> addHeaderView(@LayoutRes int layoutId);

	UIObjectListController<Page, DataSource> addHeaderView(@NonNull View view);

	UIObjectListController<Page, DataSource> addFooterView(@LayoutRes int layoutId);

	UIObjectListController<Page, DataSource> addFooterView(@NonNull View view);

	UIObjectListController<Page, DataSource> addHeaderView(@NonNull RecyclerFragment recyclerFragment);

	UIObjectListController<Page, DataSource> addFooterView(@NonNull RecyclerFragment recyclerFragment);

	UIObjectListController<Page, DataSource> removeHeaderViewAt(int position);

	UIObjectListController<Page, DataSource> removeFooterViewAt(int position);

	UIObjectListController<Page, DataSource> setDraggingToStart(boolean dragEnabled);

	UIObjectListController<Page, DataSource> setDraggingToEnd(boolean dragEnabled);

	UIObjectListController<Page, DataSource> setHeaderEnabled(boolean enabled);

	UIObjectListController<Page, DataSource> setFooterEnabled(boolean enabled);

	UIObjectListController<Page, DataSource> setLoadingEnabled(boolean enabled);

	UIObjectListController<Page, DataSource> setEmptyEnabled(boolean enabled);

	UIObjectListController<Page, DataSource> setLoadingView(@LayoutRes int layoutId);

	UIObjectListController<Page, DataSource> setLoadingView(@NonNull View view);

	UIObjectListController<Page, DataSource> setEmptyView(@LayoutRes int layoutId);

	UIObjectListController<Page, DataSource> setEmptyView(@NonNull View view);

	UIObjectListController<Page, DataSource> setHeaderStickyView(@LayoutRes int layoutId);

	UIObjectListController<Page, DataSource> setHeaderStickyView(@NonNull View view);

	UIObjectListController<Page, DataSource> setFooterStickyView(@LayoutRes int layoutId);

	UIObjectListController<Page, DataSource> setFooterStickyView(@NonNull View view);

	UIObjectListController<Page, DataSource> setRefreshMode(@NonNull RefreshMode mode);

	UIObjectListController<Page, DataSource> setAdapter(@NonNull RecyclerAdapter<DataSource> adapter);

	UIObjectListController<Page, DataSource> setDelegate(@NonNull RecyclerAdapter.Delegate<DataSource> delegate);

	UIObjectListController<Page, DataSource> setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager);

	UIObjectListController<Page, DataSource> setItemAnimator(@NonNull RecyclerView.ItemAnimator animator);

	UIObjectListController<Page, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration);

	UIObjectListController<Page, DataSource> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration, int index);

	UIObjectListController<Page, DataSource> removeItemDecorationAt(int index);

	UIObjectListController<Page, DataSource> removeItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration);

	UIObjectListController<Page, DataSource> setOnDataSourceListener(@NonNull OnDataSourceListener listener);

	UIObjectListController<Page, DataSource> setOnItemClickListener(@NonNull RecyclerAdapter.OnItemClickListener<DataSource> listener);

	UIObjectListController<Page, DataSource> setOnItemLongClickListener(@NonNull RecyclerAdapter.OnItemLongClickListener<DataSource> listener);

	UIObjectListController<Page, DataSource> addOnScrollListener(@NonNull RecyclerView.OnScrollListener listener);

	UIObjectListController<Page, DataSource> removeOnScrollListener(@NonNull RecyclerView.OnScrollListener listener);

	UIObjectListController<Page, DataSource> test(@NonNull List<DataSource> dataSourceList);

	UIObjectListController<Page, DataSource> removeAll();

	UIObjectListController<Page, DataSource> setDataSource(@NonNull DataSource dataSource);

	UIObjectListController<Page, DataSource> setDataSource(@NonNull Collection<? extends DataSource> dataSources);

	UIObjectListController<Page, DataSource> addDataSource(@NonNull DataSource dataSource);

	UIObjectListController<Page, DataSource> addDataSource(@NonNull DataSource dataSource, int index);

	UIObjectListController<Page, DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources);

	UIObjectListController<Page, DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources, int index);

	UIObjectListController<Page, DataSource> notifyDataSetChanged();

	DataSource findDataSourceByPosition(int position);

	interface OnDataSourceListener {

		void onPageDataSourceChanged(@Nullable Object params, int page, int limit);
	}
}
