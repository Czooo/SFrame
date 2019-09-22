package androidx.sframe.widget.adapter;

import android.database.Observable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sframe.ui.controller.DataSourceNotifyController2;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl3;

/**
 * @Author create by Zoran on 2019-09-12
 * @Email : 171905184@qq.com
 * @Description :
 */
public class RecyclerChildAdapter<DataSource, ParentDataSource> {

	public static <DataSource, ParentDataSource> RecyclerChildAdapter<DataSource, ParentDataSource> create(@NonNull Delegate<DataSource, ParentDataSource> delegate) {
		return new RecyclerChildAdapter<>(delegate);
	}

	public static <DataSource, ParentDataSource> ViewHolder<DataSource, ParentDataSource> createViewHolder(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
		return RecyclerChildAdapter.createViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
	}

	public static <DataSource, ParentDataSource> ViewHolder<DataSource, ParentDataSource> createViewHolder(@NonNull View itemView) {
		return new ViewHolder<>(itemView);
	}

	private final AdapterDataObservable mAdapterDataObservable = new AdapterDataObservable();

	private RecyclerAdapter<ParentDataSource> mRecyclerAdapter;
	private Delegate<DataSource, ParentDataSource> mDelegate;

	private DataSourceNotifyController2<RecyclerChildAdapter<DataSource, ParentDataSource>, DataSource> mDataSourceController;
	private OnItemClickListener<DataSource, ParentDataSource> mOnItemClickListener;
	private OnItemLongClickListener<DataSource, ParentDataSource> mOnItemLongClickListener;

	public RecyclerChildAdapter() {
		this(null);
	}

	public RecyclerChildAdapter(@Nullable Delegate<DataSource, ParentDataSource> delegate) {
		this.mDelegate = delegate;
	}

	public final void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
		this.mAdapterDataObservable.registerObserver(observer);
	}

	public final void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
		this.mAdapterDataObservable.unregisterObserver(observer);
	}

	public final void notifyDataSetChanged() {
		this.mAdapterDataObservable.notifyChanged();
	}

	public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
		this.mAdapterDataObservable.notifyItemRangeRemoved(positionStart, itemCount);
	}

	public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
		this.mAdapterDataObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
	}

	public final void notifyItemRangeInserted(int positionStart, int itemCount) {
		this.mAdapterDataObservable.notifyItemRangeInserted(positionStart, itemCount);
	}

	public final void notifyItemMoved(int fromPosition, int toPosition) {
		this.mAdapterDataObservable.notifyItemMoved(fromPosition, toPosition);
	}

	@CallSuper
	public void onAttachedToRecyclerAdapter(@NonNull RecyclerAdapter<ParentDataSource> adapter) {
		this.mRecyclerAdapter = adapter;
	}

	@CallSuper
	public void onDetachedToRecyclerAdapter(@NonNull RecyclerAdapter<ParentDataSource> adapter) {
		this.mRecyclerAdapter = null;
	}

	@CallSuper
	public ViewHolder<DataSource, ParentDataSource> onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
		if (this.mDelegate != null) {
			return this.mDelegate.onCreateViewHolder(this, parent, itemViewType);
		}
		return null;
	}

	@CallSuper
	public void onBindViewHolder(@NonNull ViewHolder<DataSource, ParentDataSource> holder, int position, @Nullable List<Object> payloads) {
		if (this.mDelegate != null) {
			this.mDelegate.onBindViewHolder(holder, position, payloads);
		}
	}

	@CallSuper
	public int getItemViewType(int position) {
		if (this.mDelegate != null) {
			return this.mDelegate.getItemViewType(this, position);
		}
		return 0;
	}

	@CallSuper
	public int getItemCount() {
		return this.getDataSourceController().getDataSourceCount();
	}

	public void setOnItemClickListener(@Nullable OnItemClickListener<DataSource, ParentDataSource> listener) {
		this.mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(@Nullable OnItemLongClickListener<DataSource, ParentDataSource> listener) {
		this.mOnItemLongClickListener = listener;
	}

	@NonNull
	public DataSource findDataSourceByPosition(int position) {
		final DataSource dataSourceByPosition = this.getDataSourceController().findDataSourceByPosition(position);
		if (dataSourceByPosition == null) {
			throw new IllegalStateException("Position : " + position + " not find dataSource");
		}
		return dataSourceByPosition;
	}

	@NonNull
	public RecyclerAdapter<ParentDataSource> getRecyclerAdapter() {
		if (this.mRecyclerAdapter == null) {
			throw new IllegalStateException("RecyclerChildAdapter " + this + " not attached to RecyclerAdapter");
		}
		return this.mRecyclerAdapter;
	}

	@NonNull
	public DataSourceNotifyController2<RecyclerChildAdapter<DataSource, ParentDataSource>, DataSource> getDataSourceController() {
		if (this.mDataSourceController == null) {
			this.mDataSourceController = new DataSourceNotifyControllerImpl3<>(this);
		}
		return this.mDataSourceController;
	}

	void tryBindViewHolder(@NonNull final ViewHolder<DataSource, ParentDataSource> holder, final int position, @Nullable List<Object> payloads) {
		try {
			holder.mRecyclerChildAdapter = RecyclerChildAdapter.this;
			if (this.mOnItemClickListener != null) {
				holder.getItemView().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						RecyclerChildAdapter.this.mOnItemClickListener.onItemClick(holder, position);
					}
				});
			}
			if (this.mOnItemLongClickListener != null) {
				holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View view) {
						return RecyclerChildAdapter.this.mOnItemLongClickListener.onItemLongClick(holder, position);
					}
				});
			}
			this.onBindViewHolder(holder, position, payloads);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class ViewHolder<DataSource, ParentDataSource> extends RecyclerAdapter.ViewHolder<ParentDataSource> {

		private RecyclerChildAdapter<DataSource, ParentDataSource> mRecyclerChildAdapter;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		@NonNull
		public DataSource findChildDataSourceByPosition(int position) {
			return this.getRecyclerChildAdapter().findDataSourceByPosition(position);
		}

		@NonNull
		public DataSourceNotifyController2<RecyclerChildAdapter<DataSource, ParentDataSource>, DataSource> getChildDataSourceController() {
			return this.getRecyclerChildAdapter().getDataSourceController();
		}

		@NonNull
		public RecyclerChildAdapter<DataSource, ParentDataSource> getRecyclerChildAdapter() {
			return this.mRecyclerChildAdapter;
		}
	}

	public interface OnItemClickListener<DataSource, ParentDataSource> {

		void onItemClick(@NonNull ViewHolder<DataSource, ParentDataSource> holder, int position);
	}

	public interface OnItemLongClickListener<DataSource, ParentDataSource> {

		boolean onItemLongClick(@NonNull ViewHolder<DataSource, ParentDataSource> holder, int position);
	}

	public interface Delegate<DataSource, ParentDataSource> {

		@NonNull
		ViewHolder<DataSource, ParentDataSource> onCreateViewHolder(@NonNull RecyclerChildAdapter<DataSource, ParentDataSource> adapter, @NonNull ViewGroup parent, int itemViewType);

		void onBindViewHolder(@NonNull ViewHolder<DataSource, ParentDataSource> holder, int position, @Nullable List<Object> payloads);

		int getItemViewType(@NonNull RecyclerChildAdapter<DataSource, ParentDataSource> adapter, int position);
	}

	/* package */ static final class AdapterDataObservable extends Observable<RecyclerView.AdapterDataObserver> {

		public boolean hasObservers() {
			return !this.mObservers.isEmpty();
		}

		public void notifyChanged() {
			for (int i = this.mObservers.size() - 1; i >= 0; --i) {
				this.mObservers.get(i).onChanged();
			}
		}

		public void notifyItemRangeChanged(int positionStart, int itemCount) {
			this.notifyItemRangeChanged(positionStart, itemCount, null);
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
