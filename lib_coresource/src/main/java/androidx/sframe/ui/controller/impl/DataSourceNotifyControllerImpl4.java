package androidx.sframe.ui.controller.impl;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.DataSourceNotifyController3;
import androidx.sframe.widget.adapter.ExpandableRecyclerAdapter;

/**
 * @Author create by Zoran on 2019-09-15
 * @Email : 171905184@qq.com
 * @Description :
 */
public class DataSourceNotifyControllerImpl4<DataSource> extends DataSourceControllerImpl<DataSource> implements DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> {

	private final ExpandableRecyclerAdapter<DataSource> mAdapter;

	public DataSourceNotifyControllerImpl4(@NonNull ExpandableRecyclerAdapter<DataSource> adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public DataSourceController<DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources, int index) {
		int positionStart = this.getDataSourceCount();
		if (index >= 0 && index <= positionStart) {
			positionStart = index;
		}
		super.addDataSource(dataSources, index);
		this.notifyGroupItemRangeInserted(positionStart, dataSources.size());
		this.notifyGroupItemRangeChanged(positionStart, this.getDataSourceCount() - positionStart);
		return this;
	}

	@Override
	public DataSourceController<DataSource> removeAll() {
		super.removeAll();
		this.notifyDataSetChanged();
		return this;
	}

	@Override
	public DataSourceController<DataSource> removeDataSource(int position) {
		int positionStart = this.getDataSourceAdapter().toLayoutPositionByGroupPosition(position);
		int removeItemCount = this.getDataSourceAdapter().getChildrenCount(position);
		if (this.getDataSourceAdapter().hasGroupEnabled(position)) {
			removeItemCount++;
		}
		super.removeDataSource(position);
		this.getDataSourceAdapter().notifyItemRangeRemoved(positionStart, removeItemCount);

		int changedItemCount = 0;
		for (int groupPosition = position; groupPosition < this.getDataSourceCount() - position; groupPosition++) {
			changedItemCount += this.getDataSourceAdapter().getChildrenCount(groupPosition);

			if (this.getDataSourceAdapter().hasGroupEnabled(groupPosition)) {
				changedItemCount++;
			}
		}
		this.getDataSourceAdapter().notifyItemRangeChanged(positionStart, changedItemCount);
		return this;
	}

	@Override
	public DataSourceController<DataSource> moveDataSourceOf(int fromPosition, int toPosition) {
		super.moveDataSourceOf(fromPosition, toPosition);
		if (fromPosition != toPosition) {
			this.notifyGroupItemMoved(fromPosition, toPosition);
			this.notifyGroupItemChanged(fromPosition);
			this.notifyGroupItemChanged(toPosition);
		}
		return this;
	}

	@Override
	public boolean switchedSelectStateByAll() {
		boolean state = super.switchedSelectStateByAll();
		this.notifyDataSetChanged();
		return state;
	}

	@Override
	public boolean switchedSelectStateOf(@NonNull DataSource dataSource) {
		boolean state = super.switchedSelectStateOf(dataSource);
		this.notifyGroupItemChanged(this.indexOf(dataSource));
		return state;
	}

	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemRemoved(int groupPosition) {
		return this.notifyGroupItemRangeRemoved(groupPosition, 1);
	}

	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemRangeRemoved(int groupPositionStart, int itemCount) {
		int positionStart = this.getDataSourceAdapter().toLayoutPositionByGroupPosition(groupPositionStart);
		int realItemCount = 0;
		for (int groupPosition = groupPositionStart; groupPosition < groupPositionStart + itemCount; groupPosition++) {
			realItemCount += this.getDataSourceAdapter().getChildrenCount(groupPosition);
			if (this.getDataSourceAdapter().hasGroupEnabled(groupPosition)) {
				realItemCount++;
			}
		}
		this.getDataSourceAdapter().notifyItemRangeRemoved(positionStart, realItemCount);
		return this;
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemChanged(int groupPosition) {
		return this.notifyGroupItemRangeChanged(groupPosition, 1);
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemRangeChanged(int groupPositionStart, int itemCount) {
		return this.notifyGroupItemRangeChanged(groupPositionStart, itemCount, null);
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemRangeChanged(int groupPositionStart, int itemCount, Object payload) {
		int positionStart = this.getDataSourceAdapter().toLayoutPositionByGroupPosition(groupPositionStart);
		int realItemCount = 0;
		for (int groupPosition = groupPositionStart; groupPosition < groupPositionStart + itemCount; groupPosition++) {
			realItemCount += this.getDataSourceAdapter().getChildrenCount(groupPosition);
			if (this.getDataSourceAdapter().hasGroupEnabled(groupPosition)) {
				realItemCount++;
			}
		}
		this.getDataSourceAdapter().notifyItemRangeChanged(positionStart, realItemCount, payload);
		return this;
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemInserted(int groupPosition) {
		return this.notifyGroupItemRangeInserted(groupPosition, 1);
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemRangeInserted(int groupPositionStart, int itemCount) {
		int positionStart = this.getDataSourceAdapter().toLayoutPositionByGroupPosition(groupPositionStart);
		int realItemCount = 0;
		for (int groupPosition = groupPositionStart; groupPosition < groupPositionStart + itemCount; groupPosition++) {
			realItemCount += this.getDataSourceAdapter().getChildrenCount(groupPosition);
			if (this.getDataSourceAdapter().hasGroupEnabled(groupPosition)) {
				realItemCount++;
			}
		}
		this.getDataSourceAdapter().notifyItemRangeInserted(positionStart, realItemCount);
		return this;
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyGroupItemMoved(int fromGroupPosition, int toGroupPosition) {
		int fromGroupPositionStart = this.getDataSourceAdapter().toLayoutPositionByGroupPosition(fromGroupPosition);
		int toGroupPositionStart = this.getDataSourceAdapter().toLayoutPositionByGroupPosition(toGroupPosition);
		this.getDataSourceAdapter().notifyItemMoved(fromGroupPositionStart, toGroupPositionStart);
		return this;
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemRemoved(int groupPosition, int childPosition) {
		return this.notifyChildItemRangeRemoved(groupPosition, childPosition, 1);
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemRangeRemoved(int groupPosition, int childPositionStart, int itemCount) {
		int positionStart = this.getDataSourceAdapter().toLayoutPositionByChildrenPosition(groupPosition, childPositionStart);
		this.getDataSourceAdapter().notifyItemRangeRemoved(positionStart, itemCount);
		return this;
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemChanged(int groupPosition, int childPosition) {
		return this.notifyChildItemRangeChanged(groupPosition, childPosition, 1);
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemRangeChanged(int groupPosition, int childPositionStart, int itemCount) {
		return this.notifyChildItemRangeChanged(groupPosition, childPositionStart, itemCount, null);
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemRangeChanged(int groupPosition, int childPositionStart, int itemCount, Object payload) {
		int positionStart = this.getDataSourceAdapter().toLayoutPositionByChildrenPosition(groupPosition, childPositionStart);
		this.getDataSourceAdapter().notifyItemRangeChanged(positionStart, itemCount, payload);
		return this;
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemInserted(int groupPosition, int childPosition) {
		return this.notifyChildItemRangeInserted(groupPosition, childPosition, 1);
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemRangeInserted(int groupPosition, int childPositionStart, int itemCount) {
		int positionStart = this.getDataSourceAdapter().toLayoutPositionByChildrenPosition(groupPosition, childPositionStart);
		this.getDataSourceAdapter().notifyItemRangeInserted(positionStart, itemCount);
		return this;
	}

	@Override
	public DataSourceNotifyController3<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyChildItemMoved(int groupPosition, int fromChildPosition, int toChildPosition) {
		int fromChildPositionStart = this.getDataSourceAdapter().toLayoutPositionByChildrenPosition(groupPosition, fromChildPosition);
		int toChildPositionStart = this.getDataSourceAdapter().toLayoutPositionByChildrenPosition(groupPosition, toChildPosition);
		this.getDataSourceAdapter().notifyItemMoved(fromChildPositionStart, toChildPositionStart);
		return this;
	}

	@Override
	public DataSourceNotifyController<ExpandableRecyclerAdapter<DataSource>, DataSource> notifyDataSetChanged() {
		this.getDataSourceAdapter().notifyDataSetChanged();
		return this;
	}

	@NonNull
	@Override
	public final ExpandableRecyclerAdapter<DataSource> getDataSourceAdapter() {
		return this.mAdapter;
	}
}
