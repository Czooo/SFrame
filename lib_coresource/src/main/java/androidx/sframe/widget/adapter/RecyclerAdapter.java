package androidx.sframe.widget.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.sframe.adapter.RecyclerFragmentDelegate;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.DataSourceNotifyController2;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl2;
import androidx.sframe.ui.controller.impl.UIViewControllerImpl;

/**
 * @Author create by Zoran on 2019-09-12
 * @Email : 171905184@qq.com
 * @Description :
 */
public class RecyclerAdapter<DataSource> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<DataSource>> {

	public static <DataSource> RecyclerAdapter<DataSource> create(@NonNull RecyclerAdapter.Delegate<DataSource> delegate) {
		return new RecyclerAdapter<>(delegate);
	}

	public static <DataSource> ViewHolder<DataSource> createViewHolder(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
		return RecyclerAdapter.createViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
	}

	public static <DataSource> ViewHolder<DataSource> createViewHolder(@NonNull View itemView) {
		return new ViewHolder<>(itemView);
	}

	private static final int DEFAULT_SPACE_SIZE = 1024;

	public static final int HOST_ITEM_TYPE_NONE = -1;
	public static final int HOST_ITEM_TYPE_HEADER = -2;
	public static final int HOST_ITEM_TYPE_FOOTER = -3;
	public static final int HOST_ITEM_TYPE_LOADING = -4;
	public static final int HOST_ITEM_TYPE_EMPTY = -5;

	private final SparseArray<View> mSingleLayoutArray = new SparseArray<>();

	private RecyclerView mRecyclerView;
	private Delegate<DataSource> mDelegate;
	private RecyclerChildAdapter<RecyclerFragment, DataSource> mHeaderViewRecyclerAdapter;
	private RecyclerChildAdapter<RecyclerFragment, DataSource> mFooterViewRecyclerAdapter;
	private RecyclerChildAdapter<?, DataSource> mHeaderRecyclerAdapter;
	private RecyclerChildAdapter<?, DataSource> mFooterRecyclerAdapter;
	private HeaderAdapterDataObserver mHeaderAdapterDataObserver;
	private FooterAdapterDataObserver mFooterAdapterDataObserver;

	private DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> mDataSourceController;
	private OnItemClickListener<DataSource> mOnItemClickListener;
	private OnItemLongClickListener<DataSource> mOnItemLongClickListener;

	private int mOriginSpaceSize = DEFAULT_SPACE_SIZE;
	private boolean mIsShouldHeaderEnabled = true;
	private boolean mIsShouldFooterEnabled = true;
	private boolean mIsShouldLoadingEnabled = false;
	private boolean mIsShouldEmptyEnabled = false;

	public RecyclerAdapter() {
		this(null);
	}

	public RecyclerAdapter(@Nullable Delegate<DataSource> delegate) {
		this.mDelegate = delegate;
	}

	/**
	 * Called by RecyclerView when it starts observing this Adapter.
	 * <p>
	 * Keep in mind that same adapter may be observed by multiple RecyclerViews.
	 *
	 * @param recyclerView The RecyclerView instance which started observing this adapter.
	 * @see #onDetachedFromRecyclerView(RecyclerView)
	 */
	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		this.mRecyclerView = recyclerView;
	}

	/**
	 * Called by RecyclerView when it stops observing this Adapter.
	 *
	 * @param recyclerView The RecyclerView instance which stopped observing this adapter.
	 * @see #onAttachedToRecyclerView(RecyclerView)
	 */
	@Override
	public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		this.mRecyclerView = null;
	}

	/**
	 * Called when a view created by this adapter has been attached to a window.
	 *
	 * <p>This can be used as a reasonable signal that the view is about to be seen
	 * by the user. If the adapter previously freed any resources in
	 * {@link #onViewDetachedFromWindow(RecyclerView.ViewHolder) onViewDetachedFromWindow}
	 * those resources should be restored here.</p>
	 *
	 * @param holder Holder of the view being attached
	 */
	@Override
	public void onViewAttachedToWindow(@NonNull ViewHolder<DataSource> holder) {
		super.onViewAttachedToWindow(holder);
		ViewGroup.LayoutParams layoutParams = holder.getItemView().getLayoutParams();
		if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
			if (holder.getItemViewType() < 0) {
				((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
			}
		}
	}

	/**
	 * Called when a view created by this adapter has been detached from its window.
	 *
	 * <p>Becoming detached from the window is not necessarily a permanent condition;
	 * the consumer of an Adapter's views may choose to cache views offscreen while they
	 * are not visible, attaching and detaching them as appropriate.</p>
	 *
	 * @param holder Holder of the view being detached
	 */
	@Override
	public void onViewDetachedFromWindow(@NonNull ViewHolder<DataSource> holder) {
		super.onViewDetachedFromWindow(holder);
	}

	/**
	 * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
	 * an item.
	 * <p>
	 * This new ViewHolder should be constructed with a new View that can represent the items
	 * of the given type. You can either create a new View manually or inflate it from an XML
	 * layout file.
	 * <p>
	 * The new ViewHolder will be used to display items of the adapter using
	 * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
	 * different items in the data set, it is a good idea to cache references to sub views of
	 * the View to avoid unnecessary {@link View#findViewById(int)} calls.
	 *
	 * @param parent       The ViewGroup into which the new View will be added after it is bound to
	 *                     an adapter position.
	 * @param itemViewType The view type of the new View.
	 * @return A new ViewHolder that holds a View of the given view type.
	 * @see #getItemViewType(int)
	 * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
	 */
	@NonNull
	@Override
	public ViewHolder<DataSource> onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
		final int hostItemViewType = this.getHostItemViewType(itemViewType);
		if (this.hasSingleLayoutByType(hostItemViewType)) {
			return new ViewHolder<>(this.mSingleLayoutArray.get(hostItemViewType));
		} else if (HOST_ITEM_TYPE_HEADER == hostItemViewType) {
			return this.getHeaderRecyclerAdapter().onCreateViewHolder(parent, this.getChildItemViewType(itemViewType));
		} else if (HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
			return this.getFooterRecyclerAdapter().onCreateViewHolder(parent, this.getChildItemViewType(itemViewType));
		} else if (HOST_ITEM_TYPE_NONE == hostItemViewType) {
			if (this.mDelegate != null) {
				return this.mDelegate.onCreateViewHolder(this, parent, itemViewType);
			}
		}
		return null;
	}

	/**
	 * Called by RecyclerView to display the data at the specified position. This method
	 * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
	 * the given position.
	 * <p>
	 * Note that unlike {@link ListView}, RecyclerView will not call this method
	 * again if the position of the item changes in the data set unless the item itself is
	 * invalidated or the new position cannot be determined. For this reason, you should only
	 * use the <code>position</code> parameter while acquiring the related data item inside
	 * this method and should not keep a copy of it. If you need the position of an item later
	 * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
	 * have the updated adapter position.
	 * <p>
	 * Partial bind vs full bind:
	 * <p>
	 * The payloads parameter is a merge list from {@link #notifyItemChanged(int, Object)} or
	 * {@link #notifyItemRangeChanged(int, int, Object)}.  If the payloads list is not empty,
	 * the ViewHolder is currently bound to old data and Adapter may run an efficient partial
	 * update using the payload info.  If the payload is empty,  Adapter must run a full bind.
	 * Adapter should not assume that the payload passed in notify methods will be received by
	 * onBindViewHolder().  For example when the view is not attached to the screen, the
	 * payload in notifyItemChange() will be simply dropped.
	 *
	 * @param holder   The ViewHolder which should be updated to represent the contents of the
	 *                 item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 * @param payloads A non-null list of merged payloads. Can be empty list if requires full
	 */
	@CallSuper
	@Override
	public void onBindViewHolder(@NonNull ViewHolder<DataSource> holder, int position, @NonNull List<Object> payloads) {
		this.tryBindViewHolder(holder, position, payloads);
	}

	/**
	 * Called by RecyclerView to display the data at the specified position. This method should
	 * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
	 * position.
	 * <p>
	 * Note that unlike {@link ListView}, RecyclerView will not call this method
	 * again if the position of the item changes in the data set unless the item itself is
	 * invalidated or the new position cannot be determined. For this reason, you should only
	 * use the <code>position</code> parameter while acquiring the related data item inside
	 * this method and should not keep a copy of it. If you need the position of an item later
	 * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
	 * have the updated adapter position.
	 * <p>
	 * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} instead if Adapter can
	 * handle efficient partial bind.
	 *
	 * @param holder   The ViewHolder which should be updated to represent the contents of the
	 *                 item at the given position in the data set.
	 * @param position The position of the item within the adapter's data set.
	 */
	@CallSuper
	@Override
	public void onBindViewHolder(@NonNull ViewHolder<DataSource> holder, int position) {

	}

	private <ChildDataSource> void tryBindViewHolder(@NonNull final ViewHolder<DataSource> holder, final int position, @NonNull List<Object> payloads) {
		try {
			final int realPosition = this.getRealPosition(position);
			final int itemViewType = this.getItemViewType(position);
			final int hostItemViewType = this.getHostItemViewType(itemViewType);
			this.bindAdapterByViewHolder(holder);

			if (this.hasSingleLayoutByType(hostItemViewType)) {
				return;
			}

			if (HOST_ITEM_TYPE_HEADER == hostItemViewType) {
				if (holder instanceof RecyclerChildAdapter.ViewHolder) {
					RecyclerChildAdapter.ViewHolder<ChildDataSource, DataSource> headerViewHolder = (RecyclerChildAdapter.ViewHolder<ChildDataSource, DataSource>) holder;
					RecyclerChildAdapter<ChildDataSource, DataSource> headerRecyclerAdapter = this.getHeaderRecyclerAdapter();
					headerRecyclerAdapter.tryBindViewHolder(headerViewHolder, realPosition, payloads);
				}
			} else if (HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
				if (holder instanceof RecyclerChildAdapter.ViewHolder) {
					RecyclerChildAdapter.ViewHolder<ChildDataSource, DataSource> footerViewHolder = (RecyclerChildAdapter.ViewHolder<ChildDataSource, DataSource>) holder;
					RecyclerChildAdapter<ChildDataSource, DataSource> footerRecyclerAdapter = this.getFooterRecyclerAdapter();
					footerRecyclerAdapter.tryBindViewHolder(footerViewHolder, realPosition, payloads);
				}
			} else if (HOST_ITEM_TYPE_NONE == hostItemViewType) {
				if (this.mOnItemClickListener != null) {
					holder.getItemView().setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (holder.getAdapterPosition() == -1) {
								return;
							}
							RecyclerAdapter.this.mOnItemClickListener.onItemClick(holder, holder.getRealLayoutPosition());
						}
					});
				}
				if (this.mOnItemLongClickListener != null) {
					holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(View view) {
							if (holder.getAdapterPosition() == -1) {
								return false;
							}
							return RecyclerAdapter.this.mOnItemLongClickListener.onItemLongClick(holder, holder.getRealLayoutPosition());
						}
					});
				}
				if (this.mDelegate != null) {
					this.mDelegate.onBindViewHolder(holder, realPosition, payloads);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.onBindViewHolder(holder, position);
		}
	}

	/**
	 * Return the view type of the item at <code>position</code> for the purposes
	 * of view recycling.
	 *
	 * <p>The default implementation of this method returns 0, making the assumption of
	 * a single view type for the adapter. Unlike ListView adapters, types need not
	 * be contiguous. Consider using id resources to uniquely identify item view types.
	 *
	 * @param position position to query
	 * @return integer value identifying the type of the view needed to represent the item at
	 * <code>position</code>. Type codes need not be contiguous.
	 */
	@Override
	public int getItemViewType(int position) {
		int headerItemCount = 0;
		if (this.isShouldHeaderEnabled()) {
			headerItemCount = this.getHeaderRecyclerAdapter().getItemCount();
			if (position < headerItemCount) {
				return this.getHeaderItemViewType(position);
			}
		}
		int dataSourceCount = this.getDataSourceCount();
		if (this.isShouldFooterEnabled()) {
			int tempItemCount = headerItemCount + dataSourceCount;
			if (dataSourceCount <= 0) {
				if (this.isShouldLoadingEnabled() || this.isShouldEmptyEnabled()) {
					tempItemCount += 1;
				}
			}
			if (position >= tempItemCount) {
				return this.getFooterItemViewType(position - tempItemCount);
			}
		}
		if (dataSourceCount <= 0) {
			if (this.isShouldLoadingEnabled()) {
				return HOST_ITEM_TYPE_LOADING;
			}
			if (this.isShouldEmptyEnabled()) {
				return HOST_ITEM_TYPE_EMPTY;
			}
		}
		if (this.mDelegate != null) {
			final int itemViewType = this.mDelegate.getItemViewType(this, position - headerItemCount);
			if (itemViewType < 0) {
				throw new RuntimeException("The itemViewType value must be greater than or equal to 0");
			}
			return itemViewType;
		}
		return super.getItemViewType(position - headerItemCount);
	}

	/**
	 * Returns the total number of items in the data set held by the adapter.
	 *
	 * @return The total number of items in this adapter.
	 */
	@Override
	public int getItemCount() {
		int itemCount = this.getDataSourceCount();
		if (itemCount <= 0) {
			if (this.isShouldLoadingEnabled() || this.isShouldEmptyEnabled()) {
				itemCount = 1;
			}
		}
		if (this.isShouldHeaderEnabled()) {
			itemCount += this.getHeaderRecyclerAdapter().getItemCount();
		}
		if (this.isShouldFooterEnabled()) {
			itemCount += this.getFooterRecyclerAdapter().getItemCount();
		}
		return itemCount;
	}

	/**
	 * Return the stable ID for the item at <code>position</code>. If {@link #hasStableIds()}
	 * would return false this method should return {@link RecyclerView#NO_ID}. The default implementation
	 * of this method returns {@link RecyclerView#NO_ID}.
	 *
	 * @param position Adapter position to query
	 * @return the stable ID of the item at position
	 */
	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	public int getHostItemViewType(int itemViewType) {
		if (itemViewType >= 0) {
			return HOST_ITEM_TYPE_NONE;
		}
		// ? >= 2048
		if (Math.abs(itemViewType) >= (this.mOriginSpaceSize << 1)) {
			if (Math.abs(itemViewType) < (this.mOriginSpaceSize << 2)) {
				// Bound : [2048 , 4096)
				return HOST_ITEM_TYPE_HEADER;
			} else if (Math.abs(itemViewType) < (this.mOriginSpaceSize << 3)) {
				// Bound : [4096 , 8192)
				return HOST_ITEM_TYPE_FOOTER;
			}
		}
		return itemViewType;
	}

	private int getChildItemViewType(int itemViewType) {
		int hostItemViewType = this.getHostItemViewType(itemViewType);
		if (HOST_ITEM_TYPE_HEADER == hostItemViewType) {
			return (~itemViewType) - (this.mOriginSpaceSize << 1);
		} else if (HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
			return (~itemViewType) - (this.mOriginSpaceSize << 2);
		}
		return itemViewType;
	}

	private int getHeaderItemViewType(int position) {
		final int itemViewType = this.getHeaderRecyclerAdapter().getItemViewType(position);
		if (itemViewType < 0) {
			throw new RuntimeException("The itemViewType value must be greater than or equal to 0");
		}
		int tempItemViewType = ~(itemViewType + (this.mOriginSpaceSize << 1));
		if (HOST_ITEM_TYPE_HEADER == this.getHostItemViewType(tempItemViewType)) {
			return tempItemViewType;
		}
		throw new RuntimeException("Header space crossing : " + tempItemViewType);
	}

	private int getFooterItemViewType(int position) {
		final int itemViewType = this.getFooterRecyclerAdapter().getItemViewType(position);
		if (itemViewType < 0) {
			throw new RuntimeException("The itemViewType value must be greater than or equal to 0");
		}
		int tempItemViewType = ~(itemViewType + (this.mOriginSpaceSize << 2));
		if (HOST_ITEM_TYPE_FOOTER == this.getHostItemViewType(tempItemViewType)) {
			return tempItemViewType;
		}
		throw new RuntimeException("Footer space crossing : " + tempItemViewType);
	}

	public int getDataSourceCount() {
		return this.getDataSourceController().getDataSourceCount();
	}

	public int getRealPosition(int adapterPosition) {
		if (adapterPosition < 0 || adapterPosition >= this.getItemCount()) {
			throw new IndexOutOfBoundsException("Position : " + adapterPosition + " , Size : " + getItemCount());
		}
		int headerItemCount = 0;
		if (this.isShouldHeaderEnabled()) {
			headerItemCount = this.getHeaderRecyclerAdapter().getItemCount();
			if (adapterPosition < headerItemCount) {
				return adapterPosition;
			}
		}
		final int dataSourceCount = this.getDataSourceCount();
		if (this.isShouldFooterEnabled()) {
			int tempItemCount = headerItemCount + dataSourceCount;
			if (dataSourceCount <= 0) {
				if (this.isShouldLoadingEnabled() || this.isShouldEmptyEnabled()) {
					tempItemCount += 1;
				}
			}
			if (adapterPosition >= tempItemCount) {
				return adapterPosition - tempItemCount;
			}
		}
		return adapterPosition - headerItemCount;
	}

	public boolean hasSingleLayoutByType(int hostItemViewType) {
		return this.mSingleLayoutArray.indexOfKey(hostItemViewType) >= 0;
	}

	public void setOnItemClickListener(@Nullable OnItemClickListener<DataSource> listener) {
		this.mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(@Nullable OnItemLongClickListener<DataSource> listener) {
		this.mOnItemLongClickListener = listener;
	}

	public void setLoadingView(@Nullable View view) {
		if (this.mSingleLayoutArray.get(HOST_ITEM_TYPE_LOADING) == view) {
			return;
		}
		this.mSingleLayoutArray.put(HOST_ITEM_TYPE_LOADING, view);
		this.notifyDataSetChanged();
	}

	public void setEmptyView(@Nullable View view) {
		if (this.mSingleLayoutArray.get(HOST_ITEM_TYPE_EMPTY) == view) {
			return;
		}
		this.mSingleLayoutArray.put(HOST_ITEM_TYPE_EMPTY, view);
		this.notifyDataSetChanged();
	}

	public void addHeaderView(@NonNull RecyclerFragment recyclerFragment) {
		try {
			if (this.mHeaderViewRecyclerAdapter == null) {
				this.mHeaderViewRecyclerAdapter = RecyclerChildAdapter.create(new RecyclerFragmentDelegate<DataSource>());
			}
			this.setHeaderRecyclerAdapter(this.mHeaderViewRecyclerAdapter);
			recyclerFragment.setTempItemViewType(this.mHeaderViewRecyclerAdapter.getItemCount());
			this.mHeaderViewRecyclerAdapter.getDataSourceController().addDataSource(recyclerFragment);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public void addFooterView(@NonNull RecyclerFragment recyclerFragment) {
		try {
			if (this.mFooterViewRecyclerAdapter == null) {
				this.mFooterViewRecyclerAdapter = RecyclerChildAdapter.create(new RecyclerFragmentDelegate<DataSource>());
			}
			this.setFooterRecyclerAdapter(this.mFooterViewRecyclerAdapter);
			recyclerFragment.setTempItemViewType(this.mFooterViewRecyclerAdapter.getItemCount());
			this.mFooterViewRecyclerAdapter.getDataSourceController().addDataSource(recyclerFragment);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public <ChildDataSource> void setHeaderRecyclerAdapter(@Nullable RecyclerChildAdapter<ChildDataSource, DataSource> adapter) {
		if (this.mHeaderRecyclerAdapter == adapter) {
			return;
		}
		if (this.mHeaderRecyclerAdapter != null) {
			this.mHeaderRecyclerAdapter.unregisterAdapterDataObserver(this.mHeaderAdapterDataObserver);
			this.mHeaderRecyclerAdapter.onDetachedToRecyclerAdapter(this);
			// update adapter
			if (this.mRecyclerView != null) {
				this.mRecyclerView.setAdapter(this);
			}
		}
		this.mHeaderRecyclerAdapter = adapter;
		if (this.mHeaderRecyclerAdapter != null) {
			if (this.mHeaderAdapterDataObserver == null) {
				this.mHeaderAdapterDataObserver = new HeaderAdapterDataObserver();
			}
			this.mHeaderRecyclerAdapter.registerAdapterDataObserver(this.mHeaderAdapterDataObserver);
			this.mHeaderRecyclerAdapter.onAttachedToRecyclerAdapter(this);
			this.mHeaderRecyclerAdapter.notifyDataSetChanged();
		}
	}

	public <ChildDataSource> void setFooterRecyclerAdapter(@Nullable RecyclerChildAdapter<ChildDataSource, DataSource> adapter) {
		if (this.mFooterRecyclerAdapter == adapter) {
			return;
		}
		if (this.mFooterRecyclerAdapter != null) {
			this.mFooterRecyclerAdapter.unregisterAdapterDataObserver(this.mFooterAdapterDataObserver);
			this.mFooterRecyclerAdapter.onDetachedToRecyclerAdapter(this);
			// update adapter
			if (this.mRecyclerView != null) {
				this.mRecyclerView.setAdapter(this);
			}
		}
		this.mFooterRecyclerAdapter = adapter;
		if (this.mFooterRecyclerAdapter != null) {
			if (this.mFooterAdapterDataObserver == null) {
				this.mFooterAdapterDataObserver = new FooterAdapterDataObserver();
			}
			this.mFooterRecyclerAdapter.registerAdapterDataObserver(this.mFooterAdapterDataObserver);
			this.mFooterRecyclerAdapter.onAttachedToRecyclerAdapter(this);
			this.mFooterRecyclerAdapter.notifyDataSetChanged();
		}
	}

	public void setOriginSpaceSize(int originSpaceSize) {
		this.mOriginSpaceSize = Math.max(originSpaceSize, DEFAULT_SPACE_SIZE);
	}

	public void setShouldHeaderEnabled(boolean shouldHeaderEnabled) {
		this.mIsShouldHeaderEnabled = shouldHeaderEnabled;
		this.notifyDataSetChanged();
	}

	public void setShouldFooterEnabled(boolean shouldFooterEnabled) {
		this.mIsShouldFooterEnabled = shouldFooterEnabled;
		this.notifyDataSetChanged();
	}

	public void setShouldLoadingEnabled(boolean shouldLoadingEnabled) {
		this.mIsShouldLoadingEnabled = shouldLoadingEnabled;
		this.notifyDataSetChanged();
	}

	public void setShouldEmptyEnabled(boolean shouldEmptyEnabled) {
		this.mIsShouldEmptyEnabled = shouldEmptyEnabled;
		this.notifyDataSetChanged();
	}

	public void bindAdapterByViewHolder(@NonNull ViewHolder<DataSource> holder) {
		holder.mRecyclerAdapter = RecyclerAdapter.this;
	}

	@NonNull
	public DataSource findDataSourceByPosition(int position) {
		return this.getDataSourceController().findDataSourceByPosition(position);
	}

	@Nullable
	public Delegate<DataSource> getDelegate() {
		return this.mDelegate;
	}

	@NonNull
	public RecyclerView getRecyclerView() {
		if (this.mRecyclerView == null) {
			throw new IllegalStateException("RecyclerAdapter " + this + " not attached to RecyclerView");
		}
		return this.mRecyclerView;
	}

	@Nullable
	public View getLoadingView() {
		return this.mSingleLayoutArray.get(HOST_ITEM_TYPE_LOADING);
	}

	@Nullable
	public View getEmptyView() {
		return this.mSingleLayoutArray.get(HOST_ITEM_TYPE_EMPTY);
	}

	@NonNull
	public DataSourceNotifyController<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController() {
		if (this.mDataSourceController == null) {
			this.mDataSourceController = new DataSourceNotifyControllerImpl2<>(this);
		}
		return this.mDataSourceController;
	}

	@NonNull
	public <ChildDataSource> RecyclerChildAdapter<ChildDataSource, DataSource> getHeaderRecyclerAdapter() {
		if (this.mHeaderRecyclerAdapter == null) {
			throw new IllegalStateException("not set header adapter");
		}
		return (RecyclerChildAdapter<ChildDataSource, DataSource>) this.mHeaderRecyclerAdapter;
	}

	@NonNull
	public <ChildDataSource> RecyclerChildAdapter<ChildDataSource, DataSource> getFooterRecyclerAdapter() {
		if (this.mFooterRecyclerAdapter == null) {
			throw new IllegalStateException("not set footer adapter");
		}
		return (RecyclerChildAdapter<ChildDataSource, DataSource>) this.mFooterRecyclerAdapter;
	}

	@Nullable
	public OnItemClickListener<DataSource> getOnItemClickListener() {
		return this.mOnItemClickListener;
	}

	@Nullable
	public OnItemLongClickListener<DataSource> getOnItemLongClickListener() {
		return this.mOnItemLongClickListener;
	}

	public boolean isShouldHeaderEnabled() {
		return this.mIsShouldHeaderEnabled && this.mHeaderRecyclerAdapter != null && this.mHeaderRecyclerAdapter.getItemCount() > 0;
	}

	public boolean isShouldFooterEnabled() {
		return this.mIsShouldFooterEnabled && this.mFooterRecyclerAdapter != null && this.mFooterRecyclerAdapter.getItemCount() > 0;
	}

	public boolean isShouldLoadingEnabled() {
		return this.mIsShouldLoadingEnabled && this.hasSingleLayoutByType(HOST_ITEM_TYPE_LOADING) && this.getDataSourceCount() <= 0;
	}

	public boolean isShouldEmptyEnabled() {
		return this.mIsShouldEmptyEnabled && this.hasSingleLayoutByType(HOST_ITEM_TYPE_EMPTY) && this.getDataSourceCount() <= 0;
	}

	public static class ViewHolder<DataSource> extends RecyclerView.ViewHolder {

		private RecyclerAdapter<DataSource> mRecyclerAdapter;

		private final UIViewController mViewController;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			this.mViewController = new UIViewControllerImpl(itemView);
		}

		public int getRealAdapterPosition() {
			return this.getRecyclerAdapter().getRealPosition(this.getAdapterPosition());
		}

		public int getRealLayoutPosition() {
			return this.getRecyclerAdapter().getRealPosition(this.getLayoutPosition());
		}

		public int getRealItemViewType() {
			return this.getRecyclerAdapter().getChildItemViewType(this.getItemViewType());
		}

		public int getHostItemViewType() {
			return this.getRecyclerAdapter().getHostItemViewType(this.getItemViewType());
		}

		@NonNull
		public View getItemView() {
			return this.getViewController().getView();
		}

		@NonNull
		public UIViewController getViewController() {
			return this.mViewController;
		}

		@NonNull
		public DataSource findDataSourceByPosition(int position) {
			return this.getRecyclerAdapter().findDataSourceByPosition(position);
		}

		@NonNull
		public DataSourceNotifyController<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController() {
			return this.getRecyclerAdapter().getDataSourceController();
		}

		@NonNull
		public RecyclerAdapter<DataSource> getRecyclerAdapter() {
			return this.mRecyclerAdapter;
		}
	}

	public interface OnItemClickListener<DataSource> {

		void onItemClick(@NonNull ViewHolder<DataSource> holder, int position);
	}

	public interface OnItemLongClickListener<DataSource> {

		boolean onItemLongClick(@NonNull ViewHolder<DataSource> holder, int position);
	}

	public interface Delegate<DataSource> {

		@NonNull
		ViewHolder<DataSource> onCreateViewHolder(@NonNull RecyclerAdapter<DataSource> adapter, @NonNull ViewGroup parent, int itemViewType);

		void onBindViewHolder(@NonNull ViewHolder<DataSource> holder, int position, @Nullable List<Object> payloads);

		int getItemViewType(@NonNull RecyclerAdapter<DataSource> adapter, int position);
	}

	/* package */ final class HeaderAdapterDataObserver extends RecyclerView.AdapterDataObserver {

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

	/* package */ final class FooterAdapterDataObserver extends RecyclerView.AdapterDataObserver {

		@Override
		public void onChanged() {
			notifyDataSetChanged();
		}

		@Override
		public void onItemRangeChanged(int positionStart, int itemCount) {
			notifyItemRangeChanged(this.toRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			notifyItemRangeInserted(this.toRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			notifyItemRangeRemoved(this.toRealPosition(positionStart), itemCount);
		}

		@Override
		public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
			notifyItemMoved(this.toRealPosition(fromPosition), this.toRealPosition(toPosition));
		}

		private int toRealPosition(int position) {
			int realPosition = position;
			if (isShouldHeaderEnabled()) {
				realPosition += getHeaderRecyclerAdapter().getItemCount();
			}
			int dataSourceCount = getDataSourceCount();
			if (dataSourceCount <= 0) {
				if (isShouldLoadingEnabled() || isShouldEmptyEnabled()) {
					dataSourceCount ++;
				}
			}
			realPosition += dataSourceCount;
			return realPosition;
		}
	}
}
