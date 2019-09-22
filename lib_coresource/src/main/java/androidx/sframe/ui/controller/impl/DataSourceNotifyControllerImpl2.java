package androidx.sframe.ui.controller.impl;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.DataSourceNotifyController2;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * @Author create by Zoran on 2019-09-12
 * @Email : 171905184@qq.com
 * @Description :
 */
public class DataSourceNotifyControllerImpl2<DataSource> extends DataSourceControllerImpl<DataSource> implements DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> {

	private final RecyclerAdapter<DataSource> mAdapter;

	public DataSourceNotifyControllerImpl2(@NonNull RecyclerAdapter<DataSource> adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public DataSourceController<DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources, int index) {
		int positionStart = this.getDataSourceCount();
		if (index >= 0 && index <= positionStart) {
			positionStart = index;
		}
		super.addDataSource(dataSources, index);
		this.notifyItemRangeInserted(positionStart, dataSources.size());
		this.notifyItemRangeChanged(positionStart, this.getDataSourceCount() - positionStart);
		return this;
	}

	@Override
	public DataSourceController<DataSource> removeAll() {
		int positionStart = 0;
		int removeItemCount = this.getDataSourceCount();
		super.removeAll();
		this.notifyItemRangeRemoved(positionStart, removeItemCount);
		this.notifyItemRangeChanged(positionStart, this.getDataSourceCount() - positionStart);
		return this;
	}

	@Override
	public DataSourceController<DataSource> removeDataSource(int position) {
		super.removeDataSource(position);
		this.notifyItemRemoved(position);
		this.notifyItemRangeChanged(position, this.getDataSourceCount() - position);
		return this;
	}

	@Override
	public DataSourceController<DataSource> moveDataSourceOf(int fromPosition, int toPosition) {
		super.moveDataSourceOf(fromPosition, toPosition);
		if (fromPosition != toPosition) {
			this.notifyItemMoved(fromPosition, toPosition);
			this.notifyItemChanged(fromPosition);
			this.notifyItemChanged(toPosition);
		}
		return this;
	}

	@Override
	public boolean switchedSelectStateByAll() {
		boolean state = super.switchedSelectStateByAll();
		int positionStart = 0;
		this.notifyItemRangeChanged(positionStart, this.getDataSourceCount() - positionStart);
		return state;
	}

	@Override
	public boolean switchedSelectStateOf(@NonNull DataSource dataSource) {
		boolean state = super.switchedSelectStateOf(dataSource);
		this.notifyItemChanged(this.indexOf(dataSource));
		return state;
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemRemoved(int position) {
		return this.notifyItemRangeRemoved(position, 1);
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemRangeRemoved(int positionStart, int itemCount) {
		this.getDataSourceAdapter().notifyItemRangeRemoved(this.toRealPosition(positionStart), itemCount);
		return this;
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemChanged(int position) {
		return this.notifyItemRangeChanged(position, 1);
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemRangeChanged(int positionStart, int itemCount) {
		return this.notifyItemRangeChanged(positionStart, itemCount, null);
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
		this.getDataSourceAdapter().notifyItemRangeChanged(this.toRealPosition(positionStart), itemCount, payload);
		return this;
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemInserted(int position) {
		return this.notifyItemRangeInserted(position, 1);
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemRangeInserted(int positionStart, int itemCount) {
		this.getDataSourceAdapter().notifyItemRangeInserted(this.toRealPosition(positionStart), itemCount);
		return this;
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> notifyItemMoved(int fromPosition, int toPosition) {
		this.getDataSourceAdapter().notifyItemMoved(this.toRealPosition(fromPosition), this.toRealPosition(toPosition));
		return this;
	}

	@Override
	public DataSourceNotifyController<RecyclerAdapter<DataSource>, DataSource> notifyDataSetChanged() {
		this.getDataSourceAdapter().notifyDataSetChanged();
		return this;
	}

	@NonNull
	@Override
	public final RecyclerAdapter<DataSource> getDataSourceAdapter() {
		return this.mAdapter;
	}

	private int toRealPosition(int position) {
		int realPosition = position;
		if (this.getDataSourceAdapter().isShouldHeaderEnabled()) {
			realPosition += this.getDataSourceAdapter().getHeaderRecyclerAdapter().getItemCount();
		}
		return realPosition;
	}
}
