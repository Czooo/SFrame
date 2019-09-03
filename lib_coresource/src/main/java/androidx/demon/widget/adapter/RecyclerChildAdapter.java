package androidx.demon.widget.adapter;

import android.database.Observable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.ui.controller.DataSourceNotifyController;
import androidx.demon.ui.controller.DataSourceNotifyController2;
import androidx.demon.ui.controller.RecyclerAdapterController;
import androidx.demon.ui.controller.impl.DataSourceNotifyControllerImpl2;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author create by ok on 2019/3/27
 * Email : ok@163.com.
 */
public class RecyclerChildAdapter<DataSource, ParentDataSource> {

	public static <DataSource, ParentDataSource> RecyclerChildAdapter<DataSource, ParentDataSource> create(RecyclerAdapterController.ChildDelegate<DataSource, ParentDataSource> dataSourceDelegate) {
		return new RecyclerChildAdapter<>(dataSourceDelegate);
	}

	private RecyclerAdapterController.OnChildItemClickListener<DataSource, ParentDataSource> mItemClickListener;
	private RecyclerAdapterController.OnChildItemTouchListener<DataSource, ParentDataSource> mItemTouchListener;
	private RecyclerAdapterController.OnChildItemLongClickListener<DataSource, ParentDataSource> mItemLongClickListener;

	private final RecyclerAdapterController.ChildDelegate<DataSource, ParentDataSource> mDataSourceDelegate;

	private final AdapterDataObservable mAdapterDataObservable = new AdapterDataObservable();

	private final DataSourceNotifyController2<RecyclerChildAdapter<DataSource, ParentDataSource>, DataSource> mDataSourceNotifyController;

	public RecyclerChildAdapter() {
		this(null);
	}

	public RecyclerChildAdapter(RecyclerAdapterController.ChildDelegate<DataSource, ParentDataSource> dataSourceDelegate) {
		this.mDataSourceDelegate = dataSourceDelegate;
		this.mDataSourceNotifyController = new DataSourceNotifyControllerImpl2<>(this);
	}

	public final void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
		mAdapterDataObservable.registerObserver(observer);
	}

	public final void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
		mAdapterDataObservable.unregisterObserver(observer);
	}

	public final void notifyDataSetChanged() {
		mAdapterDataObservable.notifyChanged();
	}

	public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
		mAdapterDataObservable.notifyItemRangeRemoved(positionStart, itemCount);
	}

	public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
		mAdapterDataObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
	}

	public final void notifyItemRangeInserted(int positionStart, int itemCount) {
		mAdapterDataObservable.notifyItemRangeInserted(positionStart, itemCount);
	}

	public final void notifyItemMoved(int fromPosition, int toPosition) {
		mAdapterDataObservable.notifyItemMoved(fromPosition, toPosition);
	}

	public RecyclerChildAdapter<DataSource, ParentDataSource> setOnItemClickListener(RecyclerAdapterController.OnChildItemClickListener<DataSource, ParentDataSource> listener) {
		this.mItemClickListener = listener;
		return this;
	}

	public RecyclerChildAdapter<DataSource, ParentDataSource> setOnItemLongClickListener(RecyclerAdapterController.OnChildItemLongClickListener<DataSource, ParentDataSource> listener) {
		this.mItemLongClickListener = listener;
		return this;
	}

	public RecyclerChildAdapter<DataSource, ParentDataSource> setOnItemTouchListener(RecyclerAdapterController.OnChildItemTouchListener<DataSource, ParentDataSource> listener) {
		this.mItemTouchListener = listener;
		return this;
	}

	public final DataSourceNotifyController2<RecyclerChildAdapter<DataSource, ParentDataSource>, DataSource> getDataSourceController() {
		return mDataSourceNotifyController;
	}

	public final DataSource findDataSourceByPosition(int position) {
		return getDataSourceController().findDataSourceByPosition(position);
	}

	public final int getItemCount() {
		return getDataSourceController().size();
	}

	public int getItemViewType(RecyclerAdapterController<ParentDataSource> recyclerAdapterController, int position) {
		if (this.mDataSourceDelegate != null) {
			return this.mDataSourceDelegate.getItemViewType(recyclerAdapterController, this, position);
		}
		return 0;
	}

	public ViewHolder<DataSource, ParentDataSource> onCreateViewHolder(RecyclerAdapterController<ParentDataSource> recyclerAdapterController, @NonNull ViewGroup viewGroup, int itemViewType) {
		if (this.mDataSourceDelegate != null) {
			final View itemView = this.mDataSourceDelegate.onCreateItemView(recyclerAdapterController, this, LayoutInflater.from(viewGroup.getContext()), viewGroup, itemViewType);
			return new ViewHolder<>(recyclerAdapterController, this, viewGroup, itemView);
		}
		return null;
	}

	public void onBindViewHolder(RecyclerAdapterController<ParentDataSource> recyclerAdapterController, @NonNull ViewHolder<DataSource, ParentDataSource> dataSourceViewHolder, int position, @Nullable List<Object> payloads) {
		if (this.mDataSourceDelegate != null) {
			this.mDataSourceDelegate.onBindItemView(dataSourceViewHolder, position, payloads);
		}
		this.onBindViewHolder(recyclerAdapterController, dataSourceViewHolder, position);
	}

	public void onBindViewHolder(RecyclerAdapterController<ParentDataSource> recyclerAdapterController, @NonNull ViewHolder<DataSource, ParentDataSource> dataSourceViewHolder, int position) {
		// not-op
	}

	public static final class ViewHolder<ChildDataSource, ParentDataSource> extends RecyclerAdapter.ViewHolder<ParentDataSource> {

		private final RecyclerChildAdapter<ChildDataSource, ParentDataSource> mRecyclerChildAdapter;

		public ViewHolder(@NonNull RecyclerAdapterController<ParentDataSource> recyclerAdapterController, RecyclerChildAdapter<ChildDataSource, ParentDataSource> recyclerChildAdapter, @NonNull ViewGroup parent, @NonNull View itemView) {
			super(recyclerAdapterController, parent, itemView);
			this.mRecyclerChildAdapter = recyclerChildAdapter;
		}

		public final ChildDataSource findChildDataSourceByPosition(int position) {
			return getChildDataSourceNotifyController().findDataSourceByPosition(position);
		}

		public final DataSourceNotifyController<RecyclerChildAdapter<ChildDataSource, ParentDataSource>, ChildDataSource> getChildDataSourceNotifyController() {
			return getRecyclerChildAdapter().getDataSourceController();
		}

		public final RecyclerChildAdapter<ChildDataSource, ParentDataSource> getRecyclerChildAdapter() {
			return mRecyclerChildAdapter;
		}
	}

	public void ensureLayoutListener(RecyclerChildAdapter.ViewHolder<DataSource, ParentDataSource> dataSourceViewHolder, final int position) {
		final RecyclerChildAdapter.ViewHolder<DataSource, ParentDataSource> mViewHolder = dataSourceViewHolder;
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

	static class AdapterDataObservable extends Observable<RecyclerView.AdapterDataObserver> {

		AdapterDataObservable() {

		}

		public boolean hasObservers() {
			return !this.mObservers.isEmpty();
		}

		public void notifyChanged() {
			for (int i = this.mObservers.size() - 1; i >= 0; --i) {
				this.mObservers.get(i).onChanged();
			}
		}

		public void notifyItemRangeChanged(int positionStart, int itemCount) {
			this.notifyItemRangeChanged(positionStart, itemCount, (Object) null);
		}

		public void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
			for (int i = this.mObservers.size() - 1; i >= 0; --i) {
				this.mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
			}
		}

		public void notifyItemRangeInserted(int positionStart, int itemCount) {
			for (int i = this.mObservers.size() - 1; i >= 0; --i) {
				this.mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
			}
		}

		public void notifyItemRangeRemoved(int positionStart, int itemCount) {
			for (int i = this.mObservers.size() - 1; i >= 0; --i) {
				this.mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
			}
		}

		public void notifyItemMoved(int fromPosition, int toPosition) {
			for (int i = this.mObservers.size() - 1; i >= 0; --i) {
				this.mObservers.get(i).onItemRangeMoved(fromPosition, toPosition, 1);
			}
		}
	}
}
