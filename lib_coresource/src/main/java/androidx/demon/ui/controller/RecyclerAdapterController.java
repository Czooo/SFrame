package androidx.demon.ui.controller;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.widget.adapter.RecyclerAdapter;
import androidx.demon.widget.adapter.RecyclerChildAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author create by ok on 2019/1/24
 * Email : ok@163.com.
 */
public interface RecyclerAdapterController<DataSource> {

	public static final int HOST_ITEM_TYPE_HEADER = -1;
	public static final int HOST_ITEM_TYPE_FOOTER = -2;
	public static final int HOST_ITEM_TYPE_EMPTY = -3;
	public static final int HOST_ITEM_TYPE_LOAD = -4;

	RecyclerAdapter<DataSource> getAdapter();

	<HDataSource> RecyclerChildAdapter<HDataSource, DataSource> getHeaderAdapter();

	<FDataSource> RecyclerChildAdapter<FDataSource, DataSource> getFooterAdapter();

	<HDataSource> RecyclerAdapterController<DataSource> setHeaderAdapter(RecyclerChildAdapter<HDataSource, DataSource> adapter);

	<FDataSource> RecyclerAdapterController<DataSource> setFooterAdapter(RecyclerChildAdapter<FDataSource, DataSource> adapter);

	RecyclerAdapterController<DataSource> setHeaderEnabled(boolean enabled);

	RecyclerAdapterController<DataSource> setFooterEnabled(boolean enabled);

	RecyclerAdapterController<DataSource> setLoadingEnabled(boolean enabled);

	RecyclerAdapterController<DataSource> setEmptyEnabled(boolean enabled);

	RecyclerAdapterController<DataSource> setLoadingView(@NonNull View view);

	RecyclerAdapterController<DataSource> setEmptyView(@NonNull View view);

	RecyclerAdapterController<DataSource> setDelegate(Delegate<DataSource> delegate);

	RecyclerAdapterController<DataSource> setHeaderOrFooterSpanSizeLookup(SpanSizeLookup spanSizeLookup);

	RecyclerAdapterController<DataSource> setOnItemClickListener(OnItemClickListener<DataSource> listener);

	RecyclerAdapterController<DataSource> setOnItemTouchListener(OnItemTouchListener<DataSource> listener);

	RecyclerAdapterController<DataSource> setOnItemLongClickListener(OnItemLongClickListener<DataSource> listener);

	RecyclerAdapterController<DataSource> notifyHeaderDataSetChanged();

	RecyclerAdapterController<DataSource> notifyFooterDataSetChanged();

	DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> getDataSourceNotifyController();

	DataSourceController<DataSource> getDataSourceController();

	DataSource findDataSourceByPosition(int position);

	Delegate<DataSource> getDelegate();

	RecyclerView getRecyclerView();

	int getRealPosition(int adapterPosition);

	int getRealItemViewType(int adapterPosition);

	boolean hasHeaderAdapter();

	boolean hasFooterAdapter();

	boolean hasLoadingEnabled();

	boolean hasEmptyEnabled();

	interface Delegate<DataSource> {

		int getItemViewType(RecyclerAdapterController<DataSource> adapterController, int position);

		View onCreateItemView(RecyclerAdapterController<DataSource> adapterController, LayoutInflater inflater, ViewGroup parent, int itemViewType);

		void onBindItemView(RecyclerAdapter.ViewHolder<DataSource> holder, int position, @Nullable List<Object> payloads);
	}

	interface ChildDelegate<DataSource, ParentDataSource> {

		int getItemViewType(RecyclerAdapterController<ParentDataSource> adapterController, RecyclerChildAdapter<DataSource, ParentDataSource> recyclerChildAdapter, int position);

		View onCreateItemView(RecyclerAdapterController<ParentDataSource> adapterController, RecyclerChildAdapter<DataSource, ParentDataSource> recyclerChildAdapter, LayoutInflater inflater, ViewGroup parent, int itemViewType);

		void onBindItemView(RecyclerChildAdapter.ViewHolder<DataSource, ParentDataSource> holder, int position, @Nullable List<Object> payloads);
	}

	interface SpanSizeLookup {

		int getHeaderSpanSize(GridLayoutManager layoutManager, int position);

		int getFooterSpanSize(GridLayoutManager layoutManager, int position);
	}

	interface OnItemClickListener<DataSource> {
		void onItemClick(View childView, RecyclerAdapter.ViewHolder<DataSource> holder, int position);
	}

	interface OnItemLongClickListener<DataSource> {
		boolean onItemLongClick(View childView, RecyclerAdapter.ViewHolder<DataSource> holder, int position);
	}

	interface OnItemTouchListener<DataSource> {
		boolean onItemTouch(View childView, RecyclerAdapter.ViewHolder<DataSource> holder, int position, MotionEvent event);
	}

	interface OnChildItemClickListener<DataSource, ParentDataSource> {
		void onItemClick(View childView, RecyclerChildAdapter.ViewHolder<DataSource, ParentDataSource> holder, int position);
	}

	interface OnChildItemLongClickListener<DataSource, ParentDataSource> {
		boolean onItemLongClick(View childView, RecyclerChildAdapter.ViewHolder<DataSource, ParentDataSource> holder, int position);
	}

	interface OnChildItemTouchListener<DataSource, ParentDataSource> {
		boolean onItemTouch(View childView, RecyclerChildAdapter.ViewHolder<DataSource, ParentDataSource> holder, int position, MotionEvent event);
	}
}
