package androidx.sframe.widget.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sframe.ui.controller.DataSourceNotifyController3;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl4;

/**
 * @Author create by Zoran on 2019-09-14
 * @Email : 171905184@qq.com
 * @Description :
 */
public abstract class ExpandableRecyclerAdapter<DataSource> extends RecyclerAdapter<DataSource> {

	public static final int HOST_ITEM_TYPE_GROUP = -6;

	private final ExpandableItemDecoration mExpandableItemDecoration = new ExpandableItemDecoration();
	private DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> mDataSourceController;

	private OnItemClickListener<DataSource> mOnItemClickListener;
	private OnItemLongClickListener<DataSource> mOnItemLongClickListener;
	private OnGroupClickListener<DataSource> mOnGroupClickListener;
	private OnGroupLongClickListener<DataSource> mOnGroupLongClickListener;

	public ExpandableRecyclerAdapter() {
		super();
	}

	private ExpandableRecyclerAdapter(@Nullable Delegate<DataSource> delegate) {
		super(delegate);
	}

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		recyclerView.addItemDecoration(this.mExpandableItemDecoration);
	}

	@Override
	public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		recyclerView.removeItemDecoration(this.mExpandableItemDecoration);
	}

	@NonNull
	@Override
	public RecyclerAdapter.ViewHolder<DataSource> onCreateViewHolder(@NonNull ViewGroup parent, int itemViewType) {
		final int hostItemViewType = this.getHostItemViewType(itemViewType);
		if (HOST_ITEM_TYPE_GROUP == hostItemViewType) {
			return this.onCreateGroupViewHolder(parent, itemViewType);
		} else if (HOST_ITEM_TYPE_NONE == hostItemViewType) {
			return this.onCreateChildrenViewHolder(parent, itemViewType);
		}
		return super.onCreateViewHolder(parent, itemViewType);
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerAdapter.ViewHolder<DataSource> holder, int position, @NonNull List<Object> payloads) {
		super.onBindViewHolder(holder, position, payloads);
		final int itemViewType = this.getItemViewType(position);
		final int hostItemViewType = this.getHostItemViewType(itemViewType);

		if (holder instanceof ViewHolder) {
			final ViewHolder<DataSource> tempViewHolder = (ViewHolder<DataSource>) holder;
			if (HOST_ITEM_TYPE_GROUP == hostItemViewType) {
				if (this.mOnGroupClickListener != null) {
					holder.getItemView().setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (tempViewHolder.getGroupAdapterPosition() == -1) {
								return;
							}
							ExpandableRecyclerAdapter.this.mOnGroupClickListener.onGroupClick(tempViewHolder, tempViewHolder.getGroupLayoutPosition());
						}
					});
				}
				if (this.mOnGroupLongClickListener != null) {
					holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(View view) {
							if (tempViewHolder.getGroupAdapterPosition() == -1) {
								return false;
							}
							return ExpandableRecyclerAdapter.this.mOnGroupLongClickListener.onGroupLongClick(tempViewHolder, tempViewHolder.getGroupLayoutPosition());
						}
					});
				}
				this.onBindGroupViewHolder(tempViewHolder, this.getRealGroupPosition(position), payloads);
			} else if (HOST_ITEM_TYPE_NONE == hostItemViewType) {
				if (this.mOnItemClickListener != null) {
					holder.getItemView().setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (tempViewHolder.getChildrenAdapterPosition() == -1) {
								return;
							}
							ExpandableRecyclerAdapter.this.mOnItemClickListener.onItemClick(tempViewHolder, tempViewHolder.getGroupLayoutPosition(), tempViewHolder.getChildrenLayoutPosition());
						}
					});
				}
				if (this.mOnItemLongClickListener != null) {
					holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(View view) {
							if (tempViewHolder.getChildrenAdapterPosition() == -1) {
								return false;
							}
							return ExpandableRecyclerAdapter.this.mOnItemLongClickListener.onItemClick(tempViewHolder, tempViewHolder.getGroupLayoutPosition(), tempViewHolder.getChildrenLayoutPosition());
						}
					});
				}
				this.onBindChildrenViewHolder(tempViewHolder, this.getRealGroupPosition(position), this.getRealChildrenPosition(position), payloads);
			}
		}
	}

	@Override
	public int getItemViewType(int position) {
		int dataSourceCount = this.getDataSourceCount();
		if (dataSourceCount > 0) {
			if (this.isShouldHeaderEnabled()) {
				if (position < this.getHeaderRecyclerAdapter().getItemCount()) {
					return super.getItemViewType(position);
				}
			}
			if (this.hasGroupIndexByPosition(position)) {
				return HOST_ITEM_TYPE_GROUP;
			}
			int itemViewType = super.getItemViewType(position);
			int hostItemViewType = this.getHostItemViewType(itemViewType);
			if (HOST_ITEM_TYPE_NONE == hostItemViewType) {
				int realGroupPosition = this.getRealGroupPosition(position);
				int realChildrenPosition = this.getRealChildrenPosition(position);
				return this.getChildrenItemViewType(realGroupPosition, realChildrenPosition);
			}
		}
		return super.getItemViewType(position);
	}

	@Override
	public int getDataSourceCount() {
		int tempItemCount = 0;
		int groupCount = this.getGroupCount();
		if (groupCount > 0) {
			tempItemCount += groupCount;
			for (int groupPosition = 0; groupPosition < groupCount; groupPosition++) {
				tempItemCount += this.getChildrenCount(groupPosition);
				if (!this.hasGroupEnabled(groupPosition)) {
					tempItemCount--;
				}
			}
		}
		return tempItemCount;
	}

	@NonNull
	@Override
	public DataSourceNotifyController3<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController() {
		if (this.mDataSourceController == null) {
			this.mDataSourceController = new DataSourceNotifyControllerImpl4<>(this);
		}
		return this.mDataSourceController;
	}

	public abstract ViewHolder<DataSource> onCreateGroupViewHolder(@NonNull ViewGroup parent, int itemViewType);

	public abstract ViewHolder<DataSource> onCreateChildrenViewHolder(@NonNull ViewGroup parent, int itemViewType);

	public abstract void onBindGroupViewHolder(@NonNull ViewHolder<DataSource> holder, int groupPosition, @NonNull List<Object> payloads);

	public abstract void onBindChildrenViewHolder(@NonNull ViewHolder<DataSource> holder, int groupPosition, int position, @NonNull List<Object> payloads);

	public abstract int getGroupCount();

	public abstract int getChildrenCount(int groupPosition);

	public final int getGroupItemViewType(int groupPosition) {
		// TODO: 2019-09-20
		return 0;
	}

	public int getChildrenItemViewType(int groupPosition, int position) {
		if (groupPosition < 0 || groupPosition >= this.getGroupCount()) {
			throw new IndexOutOfBoundsException("GroupPosition : " + groupPosition + " , Size : " + this.getGroupCount());
		}
		if (position < 0 || position >= this.getChildrenCount(groupPosition)) {
			throw new IndexOutOfBoundsException("ChildrenPosition : " + position + " , Size : " + this.getChildrenCount(groupPosition));
		}
		return 0;
	}

	public final int getRealGroupPosition(int adapterPosition) {
		int realAdapterPosition = adapterPosition;
		if (this.isShouldHeaderEnabled()) {
			realAdapterPosition -= this.getHeaderRecyclerAdapter().getItemCount();
		}
		for (int position = 0, groupPosition = 0; position < this.getDataSourceCount() && realAdapterPosition >= 0; position++) {
			position += this.getChildrenCount(groupPosition);
			if (this.hasGroupEnabled(groupPosition)) {
				position++;
			}
			if (position > realAdapterPosition) {
				return groupPosition;
			}
			position--;
			groupPosition++;
		}
		return -1;
	}

	public final int getRealChildrenPosition(int adapterPosition) {
		int realAdapterPosition = adapterPosition;
		if (this.isShouldHeaderEnabled()) {
			realAdapterPosition -= this.getHeaderRecyclerAdapter().getItemCount();
		}
		if (realAdapterPosition >= 0 && this.getGroupCount() > 0) {
			int groupPosition = this.getRealGroupPosition(adapterPosition);
			int childrenPosition = realAdapterPosition;
			if (this.hasGroupEnabled(groupPosition)) {
				childrenPosition--;
			}
			while (groupPosition > 0) {
				childrenPosition -= this.getChildrenCount(--groupPosition);
				if (this.hasGroupEnabled(groupPosition)) {
					childrenPosition--;
				}
			}
			return childrenPosition;
		}
		return -1;
	}

	public final int toLayoutPositionByGroupPosition(int groupPosition) {
		if (groupPosition >= 0) {
			int layoutPosition = 0;
			if (this.isShouldHeaderEnabled()) {
				layoutPosition += this.getHeaderRecyclerAdapter().getItemCount();
			}
			while (groupPosition > 0 && this.getGroupCount() > 0) {
				layoutPosition += this.getChildrenCount(--groupPosition);
				if (this.hasGroupEnabled(groupPosition)) {
					layoutPosition++;
				}
			}
			return layoutPosition;
		}
		return -1;
	}

	public final int toLayoutPositionByChildrenPosition(int groupPosition, int childrenPosition) {
		int layoutPosition = this.toLayoutPositionByGroupPosition(groupPosition);
		if (layoutPosition >= 0) {
			layoutPosition += childrenPosition;
			if (this.hasGroupEnabled(groupPosition)) {
				layoutPosition++;
			}
			return layoutPosition;
		}
		return -1;
	}

	public boolean hasGroupEnabled(int groupPosition) {
		if (groupPosition < 0 || groupPosition >= this.getGroupCount()) {
			return false;
		}
		return true;
	}

	private boolean hasGroupIndexByPosition(int adapterPosition) {
		int dataSourceCount = this.getDataSourceCount();
		if (dataSourceCount > 0) {
			int realAdapterPosition = adapterPosition;
			if (this.isShouldHeaderEnabled()) {
				realAdapterPosition -= this.getHeaderRecyclerAdapter().getItemCount();
			}
			for (int position = 0, groupPosition = 0; position < dataSourceCount && realAdapterPosition >= 0; position++) {
				if (this.hasGroupEnabled(groupPosition)) {
					if (position == realAdapterPosition) {
						return true;
					}
				} else {
					position--;
				}
				position += this.getChildrenCount(groupPosition++);
			}
		}
		return false;
	}

	public void setExpandableStickyEnabled(boolean enabled) {
		this.mExpandableItemDecoration.setShouldEnabled(enabled);
	}

	public void setOnItemClickListener(@NonNull OnItemClickListener<DataSource> listener) {
		this.mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(@Nullable OnItemLongClickListener<DataSource> listener) {
		this.mOnItemLongClickListener = listener;
	}

	public void setOnGroupClickListener(@NonNull OnGroupClickListener<DataSource> listener) {
		this.mOnGroupClickListener = listener;
	}

	public void setOnGroupLongClickListener(@NonNull OnGroupLongClickListener<DataSource> listener) {
		this.mOnGroupLongClickListener = listener;
	}

	public static class ViewHolder<DataSource> extends RecyclerAdapter.ViewHolder<DataSource> {

		private static final int NO_POSITION = RecyclerView.NO_POSITION;

		private int mTempPosition = NO_POSITION;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
		}

		public int getGroupAdapterPosition() {
			try {
				return this.getRecyclerAdapter().getRealGroupPosition(this.mTempPosition == NO_POSITION ? this.getAdapterPosition() : this.mTempPosition);
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}
		}

		public int getGroupLayoutPosition() {
			try {
				return this.getRecyclerAdapter().getRealGroupPosition(this.mTempPosition == NO_POSITION ? this.getLayoutPosition() : this.mTempPosition);
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}
		}

		public int getChildrenAdapterPosition() {
			try {
				return this.getRecyclerAdapter().getRealChildrenPosition(this.mTempPosition == NO_POSITION ? this.getAdapterPosition() : this.mTempPosition);
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}
		}

		public int getChildrenLayoutPosition() {
			try {
				return this.getRecyclerAdapter().getRealChildrenPosition(this.mTempPosition == NO_POSITION ? this.getLayoutPosition() : this.mTempPosition);
			} catch (IndexOutOfBoundsException e) {
				return -1;
			}
		}

		public void setTempPosition(int position) {
			this.mTempPosition = position;
		}

		@NonNull
		@Override
		public DataSourceNotifyController3<? extends RecyclerAdapter<DataSource>, DataSource> getDataSourceController() {
			return this.getRecyclerAdapter().getDataSourceController();
		}

		@NonNull
		@Override
		public ExpandableRecyclerAdapter<DataSource> getRecyclerAdapter() {
			return (ExpandableRecyclerAdapter<DataSource>) super.getRecyclerAdapter();
		}
	}

	public interface OnItemClickListener<DataSource> {

		void onItemClick(@NonNull ViewHolder<DataSource> holder, int groupPosition, int position);
	}

	public interface OnItemLongClickListener<DataSource> {

		boolean onItemClick(@NonNull ViewHolder<DataSource> holder, int groupPosition, int position);
	}

	public interface OnGroupClickListener<DataSource> {

		void onGroupClick(@NonNull ViewHolder<DataSource> holder, int groupPosition);
	}

	public interface OnGroupLongClickListener<DataSource> {

		boolean onGroupLongClick(@NonNull ViewHolder<DataSource> holder, int groupPosition);
	}
}
