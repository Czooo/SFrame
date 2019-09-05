package androidx.sframe.ui.controller.impl;

import java.util.Collection;

import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.DataSourceNotifyController2;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public class DataSourceNotifyControllerImpl2<DataType, ParentDataType> extends DataSourceControllerImpl<DataType> implements DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> {

	private final RecyclerChildAdapter<DataType, ParentDataType> mAdapter;

	public DataSourceNotifyControllerImpl2(RecyclerChildAdapter<DataType, ParentDataType> adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public DataSourceController<DataType> addDataSourceList(Collection<? extends DataType> dataTypeList, int index) {
		super.addDataSourceList(dataTypeList, index);
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> removeDataSource(int position) {
		super.removeDataSource(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, size() - position);
		return this;
	}

	@Override
	public DataSourceController<DataType> removeDataSource(Collection<? extends DataType> dataTypeList) {
		super.removeDataSource(dataTypeList);
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> removeAll() {
		super.removeAll();
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> moveDataSourceOf(int fromPosition, int toPosition) {
		super.moveDataSourceOf(fromPosition, toPosition);
		if (fromPosition != toPosition) {
			notifyItemMoved(fromPosition, toPosition);
			notifyItemChanged(fromPosition);
			notifyItemChanged(toPosition);
		}
		return this;
	}

	@Override
	public DataSourceController<DataType> markerAll() {
		super.markerAll();
		notifyItemRangeChanged(0, size());
		return this;
	}

	@Override
	public DataSourceController<DataType> unmarkerAll() {
		super.unmarkerAll();
		notifyItemRangeChanged(0, size());
		return this;
	}

	@Override
	public boolean marker(DataType dataType) {
		boolean isMarker = super.marker(dataType);
		notifyItemChanged(indexOf(dataType));
		return isMarker;
	}

	@Override
	public boolean singleMarker(DataType dataType) {
		boolean isMarker = super.singleMarker(dataType);
		notifyItemChanged(indexOf(dataType));
		return isMarker;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemRemoved(int position) {
		return notifyItemRangeRemoved(position, 1);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemRangeRemoved(int positionStart, int itemCount) {
		getDataSourceAdapter().notifyItemRangeRemoved(positionStart, itemCount);
		return this;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemChanged(int position) {
		return notifyItemRangeChanged(position, 1);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemRangeChanged(int positionStart, int itemCount) {
		return notifyItemRangeChanged(positionStart, itemCount, null);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
		getDataSourceAdapter().notifyItemRangeChanged(positionStart, itemCount, payload);
		return this;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemInserted(int position) {
		return notifyItemRangeInserted(position, 1);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemRangeInserted(int positionStart, int itemCount) {
		getDataSourceAdapter().notifyItemRangeInserted(positionStart, itemCount);
		return this;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyItemMoved(int fromPosition, int toPosition) {
		getDataSourceAdapter().notifyItemMoved(fromPosition, toPosition);
		return this;
	}

	@Override
	public final DataSourceNotifyController<RecyclerChildAdapter<DataType, ParentDataType>, DataType> notifyDataSetChanged() {
		getDataSourceAdapter().notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerChildAdapter<DataType, ParentDataType> getDataSourceAdapter() {
		return mAdapter;
	}
}
