package androidx.demon.ui.controller.impl;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.ui.controller.DataSourceController;
import androidx.demon.ui.controller.RecyclerAdapterController;
import androidx.demon.widget.adapter.RecyclerAdapter;
import androidx.demon.widget.adapter.RecyclerChildAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Author create by ok on 2019/1/24
 * Email : ok@163.com.
 */
public abstract class RecyclerAdapterControllerImpl<DataSource> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<?>> implements RecyclerAdapterController<DataSource> {

	private static final int ORIGIN_SPACE_SIZE = 1024;

	private Delegate<DataSource> mDelegate;
	private RecyclerChildAdapter<?, DataSource> mHeaderAdapter;
	private RecyclerChildAdapter<?, DataSource> mFooterAdapter;
	private RecyclerView mRecyclerView;
	private View mLoadingView;
	private View mEmptyView;

	private RecyclerView.AdapterDataObserver mHeaderAdapterDataObserver;
	private RecyclerView.AdapterDataObserver mFooterAdapterDataObserver;

	private OnItemClickListener<DataSource> mItemClickListener;
	private OnItemTouchListener<DataSource> mItemTouchListener;
	private OnItemLongClickListener<DataSource> mItemLongClickListener;

	private SpanSizeLookup mSpanSizeLookup;

	private boolean isHeaderAdapterEnabled = true;
	private boolean isFooterAdapterEnabled = true;
	private boolean isLoadingEnabled = false;
	private boolean isEmptyEnabled = false;

	@NonNull
	@Override
	public RecyclerAdapter.ViewHolder<?> onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemViewType) {
		final int hostItemViewType = getHostItemViewType(itemViewType);
		if (HOST_ITEM_TYPE_LOAD == hostItemViewType) {
			return new RecyclerAdapter.ViewHolder<>(this, viewGroup, mLoadingView);
		} else if (HOST_ITEM_TYPE_EMPTY == hostItemViewType) {
			return new RecyclerAdapter.ViewHolder<>(this, viewGroup, mEmptyView);
		} else if (HOST_ITEM_TYPE_HEADER == hostItemViewType) {
			return getHeaderAdapter().onCreateViewHolder(this, viewGroup, getChildItemViewType(itemViewType));
		} else if (HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
			return getFooterAdapter().onCreateViewHolder(this, viewGroup, getChildItemViewType(itemViewType));
		}
		return new RecyclerAdapter.ViewHolder<>(this, viewGroup, getDelegate().onCreateItemView(this, LayoutInflater.from(viewGroup.getContext()), viewGroup, itemViewType));
	}

	@Override
	public final void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder<?> dataSourceViewHolder, int position, @NonNull List<Object> payloads) {
		ensureBindItemView(dataSourceViewHolder, position, payloads);
	}

	@Override
	public final void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder<?> dataSourceViewHolder, int position) {
		ensureBindItemView(dataSourceViewHolder, position, null);
	}

	@Override
	public final int getItemViewType(int position) {
		int headerItemCount = 0;

		if (hasHeaderAdapter()) {
			headerItemCount = getHeaderAdapter().getItemCount();

			if (position < headerItemCount) {
				return assignTypeSpaceSize(getHeaderAdapter(), position, 0);
			}
		}

		if (hasFooterAdapter()) {
			int sumCount = headerItemCount + getDataSourceController().size() + getSingleLayoutCount();

			if (position >= sumCount) {
				return assignTypeSpaceSize(getFooterAdapter(), position - sumCount, 1);
			}
		}

		if (getDataSourceController().isEmpty()) {
			if (hasLoadingEnabled()) {
				return HOST_ITEM_TYPE_LOAD;
			}

			if (hasEmptyEnabled()) {
				return HOST_ITEM_TYPE_EMPTY;
			}
		}

		int itemViewType = getDelegate().getItemViewType(this, position - headerItemCount);

		if (itemViewType < 0) {
			throw new RuntimeException("The itemViewType value must be greater than or equal to 0");
		}
		return itemViewType;
	}

	@Override
	public final int getItemCount() {
		int itemCount = getDataSourceController().size();

		if (itemCount <= 0) {
			if (hasEmptyEnabled() || hasLoadingEnabled()) {
				itemCount = 1;
			}
		}

		if (hasHeaderAdapter()) {
			itemCount += getHeaderAdapter().getItemCount();
		}

		if (hasFooterAdapter()) {
			itemCount += getFooterAdapter().getItemCount();
		}
		return itemCount;
	}

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		this.mRecyclerView = recyclerView;

		RecyclerView.LayoutManager mLayoutManager = recyclerView.getLayoutManager();
		if (GridLayoutManager.class.isInstance(mLayoutManager)) {
			GridLayoutManager mGridLayoutManager = (GridLayoutManager) mLayoutManager;
			if (mGridLayoutManager != null) {
				GridSpanSizeLookup mGridSpanSizeLookup = new GridSpanSizeLookup(mGridLayoutManager);
				mGridLayoutManager.setSpanSizeLookup(mGridSpanSizeLookup);
			}
		}
	}

	@Override
	public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		this.mRecyclerView = null;
	}

	@Override
	public void onViewAttachedToWindow(@NonNull RecyclerAdapter.ViewHolder<?> holder) {
		super.onViewAttachedToWindow(holder);

		ViewGroup.LayoutParams mLayoutParams = holder.getItemView().getLayoutParams();
		if (StaggeredGridLayoutManager.LayoutParams.class.isInstance(mLayoutParams)) {
			StaggeredGridLayoutManager.LayoutParams mSLayoutParams = (StaggeredGridLayoutManager.LayoutParams) mLayoutParams;
			if (holder.getItemViewType() < 0) {
				mSLayoutParams.setFullSpan(true);
			}
		}
	}

	@Override
	public Delegate<DataSource> getDelegate() {
		return this.mDelegate;
	}

	@Override
	public <HDataSource> RecyclerChildAdapter<HDataSource, DataSource> getHeaderAdapter() {
		return (RecyclerChildAdapter<HDataSource, DataSource>) this.mHeaderAdapter;
	}

	@Override
	public <FDataSource> RecyclerChildAdapter<FDataSource, DataSource> getFooterAdapter() {
		return (RecyclerChildAdapter<FDataSource, DataSource>) this.mFooterAdapter;
	}

	@Override
	public <HDataSource> RecyclerAdapterController<DataSource> setHeaderAdapter(RecyclerChildAdapter<HDataSource, DataSource> adapter) {
		if (this.mHeaderAdapter == adapter) {
			return this;
		}

		if (this.mHeaderAdapter != null) {
			this.mHeaderAdapter.unregisterAdapterDataObserver(this.mHeaderAdapterDataObserver);
			this.mHeaderAdapter = null;
			this.notifyDataSetChanged();
		}

		if (adapter != null) {
			if (this.mHeaderAdapterDataObserver == null) {
				this.mHeaderAdapterDataObserver = new HeaderAdapterDataObserver();
			}

			this.mHeaderAdapter = adapter;
			this.mHeaderAdapter.registerAdapterDataObserver(this.mHeaderAdapterDataObserver);
			this.mHeaderAdapter.notifyDataSetChanged();
		}
		return this;
	}

	@Override
	public <FDataSource> RecyclerAdapterController<DataSource> setFooterAdapter(RecyclerChildAdapter<FDataSource, DataSource> adapter) {
		if (this.mFooterAdapter == adapter) {
			return this;
		}

		if (this.mFooterAdapter != null) {
			this.mFooterAdapter.unregisterAdapterDataObserver(this.mFooterAdapterDataObserver);
			this.mFooterAdapter = null;
			this.notifyDataSetChanged();
		}

		if (adapter != null) {
			if (this.mFooterAdapterDataObserver == null) {
				this.mFooterAdapterDataObserver = new FooterAdapterDataObserver();
			}

			this.mFooterAdapter = adapter;
			this.mFooterAdapter.registerAdapterDataObserver(this.mFooterAdapterDataObserver);
			this.mFooterAdapter.notifyDataSetChanged();
		}
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setHeaderEnabled(boolean enabled) {
		this.isHeaderAdapterEnabled = enabled;
		notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setFooterEnabled(boolean enabled) {
		this.isFooterAdapterEnabled = enabled;
		notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setLoadingEnabled(boolean enabled) {
		this.isLoadingEnabled = enabled;
		notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setEmptyEnabled(boolean enabled) {
		this.isEmptyEnabled = enabled;
		notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setLoadingView(@NonNull View view) {
		this.mLoadingView = view;
		notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setEmptyView(@NonNull View view) {
		this.mEmptyView = view;
		notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setDelegate(Delegate<DataSource> delegate) {
		this.mDelegate = delegate;
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setHeaderOrFooterSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
		this.mSpanSizeLookup = spanSizeLookup;
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setOnItemClickListener(OnItemClickListener<DataSource> listener) {
		this.mItemClickListener = listener;
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setOnItemTouchListener(OnItemTouchListener<DataSource> listener) {
		this.mItemTouchListener = listener;
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> setOnItemLongClickListener(OnItemLongClickListener<DataSource> listener) {
		this.mItemLongClickListener = listener;
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> notifyHeaderDataSetChanged() {
		if (hasHeaderAdapter()) {
			getHeaderAdapter().notifyDataSetChanged();
//			notifyItemRangeChanged(0, getHeaderAdapter().getItemCount());
		}
		return this;
	}

	@Override
	public RecyclerAdapterController<DataSource> notifyFooterDataSetChanged() {
		if (hasFooterAdapter()) {
			getFooterAdapter().notifyDataSetChanged();
//			notifyItemRangeChanged(getDataSourceController().size(), getFooterAdapter().getItemCount());
		}
		return this;
	}

	@Override
	public DataSourceController<DataSource> getDataSourceController() {
		return getDataSourceNotifyController();
	}

	@Override
	public DataSource findDataSourceByPosition(int position) {
		return getDataSourceController().findDataSourceByPosition(position);
	}

	@Override
	public RecyclerView getRecyclerView() {
		return this.mRecyclerView;
	}

	@Nullable
	public View getLoadingView() {
		return mLoadingView;
	}

	@Nullable
	public View getEmptyView() {
		return mEmptyView;
	}

	@Override
	public int getRealPosition(int adapterPosition) {
		if (adapterPosition < 0 || adapterPosition >= getItemCount()) {
			throw new IndexOutOfBoundsException("Position : " + adapterPosition + " , Size : " + getItemCount());
		}

		int headerItemCount = 0;

		if (hasHeaderAdapter()) {
			headerItemCount = getHeaderAdapter().getItemCount();

			if (adapterPosition < headerItemCount) {
				return adapterPosition;
			}
		}

		int itemCount = getDataSourceController().size();
		int sumCount = headerItemCount + itemCount;
		int realSumCount = sumCount + getSingleLayoutCount();

		if (hasFooterAdapter()) {
			if (adapterPosition >= realSumCount) {
				return adapterPosition - realSumCount;
			}
		}
		return adapterPosition - headerItemCount;
	}

	@Override
	public int getRealItemViewType(int adapterPosition) {
		return getItemViewType(adapterPosition);
	}

	@Override
	public boolean hasHeaderAdapter() {
		return isHeaderAdapterEnabled && mHeaderAdapter != null && mHeaderAdapter.getItemCount() > 0;
	}

	@Override
	public boolean hasFooterAdapter() {
		return isFooterAdapterEnabled && mFooterAdapter != null && mFooterAdapter.getItemCount() > 0;
	}

	@Override
	public boolean hasLoadingEnabled() {
		return isLoadingEnabled && mLoadingView != null && getDataSourceController().size() <= 0;
	}

	@Override
	public boolean hasEmptyEnabled() {
		return isEmptyEnabled && mEmptyView != null && getDataSourceController().size() <= 0;
	}

	private int getSingleLayoutCount() {
		int itemCount = getDataSourceController().size();

		if (itemCount <= 0) {
			if (hasLoadingEnabled() || hasEmptyEnabled()) {
				return 1;
			}
		}
		return 0;
	}

	private <TargetDataSource> void ensureBindItemView(@NonNull RecyclerAdapter.ViewHolder<TargetDataSource> dataSourceViewHolder, int position, @Nullable List<Object> payloads) {
		final int realPosition = getRealPosition(position);
		final int itemViewType = getItemViewType(position);
		final int hostItemViewType = getHostItemViewType(itemViewType);

		if (HOST_ITEM_TYPE_LOAD == hostItemViewType) {
			return;
		}

		if (HOST_ITEM_TYPE_EMPTY == hostItemViewType) {
			return;
		}

		if (HOST_ITEM_TYPE_HEADER == hostItemViewType) {
			if (RecyclerChildAdapter.ViewHolder.class.isInstance(dataSourceViewHolder)) {
				RecyclerChildAdapter.ViewHolder<TargetDataSource, DataSource> mViewHolder = (RecyclerChildAdapter.ViewHolder<TargetDataSource, DataSource>) dataSourceViewHolder;
				RecyclerChildAdapter<TargetDataSource, DataSource> headerAdapter = getHeaderAdapter();
				headerAdapter.ensureLayoutListener(mViewHolder, realPosition);
				headerAdapter.onBindViewHolder(this, mViewHolder, realPosition/*mViewHolder.getDataSourcePosition()*/, payloads);
			}
			return;
		}

		if (HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
			if (RecyclerChildAdapter.ViewHolder.class.isInstance(dataSourceViewHolder)) {
				RecyclerChildAdapter.ViewHolder<TargetDataSource, DataSource> mViewHolder = (RecyclerChildAdapter.ViewHolder<TargetDataSource, DataSource>) dataSourceViewHolder;
				RecyclerChildAdapter<TargetDataSource, DataSource> footerAdapter = getFooterAdapter();
				footerAdapter.ensureLayoutListener(mViewHolder, realPosition);
				footerAdapter.onBindViewHolder(this, mViewHolder, realPosition/*mViewHolder.getDataSourcePosition()*/, payloads);
			}
			return;
		}

		if (RecyclerAdapter.ViewHolder.class.isInstance(dataSourceViewHolder)) {
			RecyclerAdapter.ViewHolder<DataSource> mViewHolder = (RecyclerAdapter.ViewHolder<DataSource>) dataSourceViewHolder;
			ensureLayoutListener(mViewHolder, realPosition);

			if (realPosition >= 0 && realPosition < mViewHolder.getDataSourceNotifyController().size()) {
				getDelegate().onBindItemView(mViewHolder, realPosition/*mViewHolder.getDataSourcePosition()*/, payloads);
			}
		}
	}

	private void ensureLayoutListener(RecyclerAdapter.ViewHolder<DataSource> dataSourceViewHolder, final int position) {
		final RecyclerAdapter.ViewHolder<DataSource> mViewHolder = dataSourceViewHolder;
		final View itemView = mViewHolder.getItemView();

		if (mItemClickListener != null) {
			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					mItemClickListener.onItemClick(view, mViewHolder, position);
				}
			});
		}

		if (mItemTouchListener != null) {
			itemView.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					return mItemTouchListener.onItemTouch(view, mViewHolder, position, event);
				}
			});
		}

		if (mItemLongClickListener != null) {
			itemView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View view) {
					return mItemLongClickListener.onItemLongClick(view, mViewHolder, position);
				}
			});
		}
	}

	/* 分配类型空间*/
	private int assignTypeSpaceSize(RecyclerChildAdapter<?, DataSource> adapter, int position, int space) {
		final int itemViewType = adapter.getItemViewType(this, position);
		final int originSpaceSize = (ORIGIN_SPACE_SIZE << space);

		if (itemViewType >= 0) {
			return ~(itemViewType + originSpaceSize);
		}
		throw new RuntimeException("The itemViewType value must be greater than or equal to 0. Header, Footer, Loading and Empty are not available at the same time.");
	}

	/* 解析Host类型*/
	public final int getHostItemViewType(int itemViewType) {
		if (itemViewType < 0) {
			if (HOST_ITEM_TYPE_LOAD == itemViewType) {
				return HOST_ITEM_TYPE_LOAD;
			}

			if (HOST_ITEM_TYPE_EMPTY == itemViewType) {
				return HOST_ITEM_TYPE_EMPTY;
			}

			if (itemViewType >= -(ORIGIN_SPACE_SIZE << 1)) {
				return HOST_ITEM_TYPE_HEADER;
			}

			if (itemViewType >= -(ORIGIN_SPACE_SIZE << 2)) {
				return HOST_ITEM_TYPE_FOOTER;
			}
		}
		return itemViewType;
	}

	/* 解析Child类型*/
	public final int getChildItemViewType(int itemViewType) {
		int hostItemViewType = getHostItemViewType(itemViewType);
		int unItemViewType = ~itemViewType;

		if (HOST_ITEM_TYPE_HEADER == hostItemViewType) {
			return unItemViewType - ORIGIN_SPACE_SIZE;
		} else if (HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
			return unItemViewType - (ORIGIN_SPACE_SIZE << 1);
		}
		return itemViewType;
	}

	class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

		final GridLayoutManager mGridLayoutManager;
		final GridLayoutManager.SpanSizeLookup mOldSpanSizeLookup;

		GridSpanSizeLookup(GridLayoutManager layoutManager) {
			this.mOldSpanSizeLookup = layoutManager.getSpanSizeLookup();
			this.mGridLayoutManager = layoutManager;
		}

		@Override
		public int getSpanSize(int position) {
			int itemViewType = getItemViewType(position);

			if (itemViewType < 0) {
				int spanSize = mGridLayoutManager.getSpanCount();

				if (mSpanSizeLookup != null) {
					int hostItemViewType = getHostItemViewType(itemViewType);

					if (HOST_ITEM_TYPE_HEADER == hostItemViewType) {
						spanSize = mSpanSizeLookup.getHeaderSpanSize(mGridLayoutManager, getRealPosition(position));
					} else if (HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
						spanSize = mSpanSizeLookup.getFooterSpanSize(mGridLayoutManager, getRealPosition(position));
					}
				}
				return spanSize > 0 ? spanSize : mGridLayoutManager.getSpanCount();
			}
			if (mOldSpanSizeLookup != null) {
				int headerItemCount = 0;
				if (hasHeaderAdapter()) {
					headerItemCount = getHeaderAdapter().getItemCount();
				}
				return mOldSpanSizeLookup.getSpanSize(position - headerItemCount);
			}
			return 1;
		}
	}

	class HeaderAdapterDataObserver extends RecyclerView.AdapterDataObserver {

		@Override
		public void onChanged() {
			notifyDataSetChanged();
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			notifyItemRangeChanged(positionStart, itemCount);
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			notifyItemRangeInserted(positionStart, itemCount);
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			notifyItemRangeRemoved(positionStart, itemCount);
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			notifyItemMoved(fromPosition, toPosition);
		}
	}

	class FooterAdapterDataObserver extends RecyclerView.AdapterDataObserver {

		@Override
		public void onChanged() {
			notifyDataSetChanged();
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			notifyItemRangeChanged(getRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			notifyItemRangeInserted(getRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			notifyItemRangeRemoved(getRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			notifyItemMoved(getRealPosition(fromPosition), getRealPosition(toPosition));
		}

		int getRealPosition(int position) {
			int realPosition = position;
			int itemCount = getDataSourceController().size();

			if (hasHeaderAdapter()) {
				realPosition += getHeaderAdapter().getItemCount();
			}

			if (itemCount <= 0) {
				itemCount = getSingleLayoutCount();
			}
			realPosition += itemCount;
			return realPosition;
		}
	}
}
