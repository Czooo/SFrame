package androidx.sframe.ui.controller.impl;

import java.util.Collection;

import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public abstract class DataSourceNotifyControllerImpl<Adapter, DataType> extends DataSourceControllerImpl<DataType> implements DataSourceNotifyController<Adapter, DataType> {

	private final Adapter mAdapter;

	public DataSourceNotifyControllerImpl(Adapter adapter) {
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
		return notifyDataSetChanged();
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
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> markerAll() {
		super.markerAll();
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> unmarkerAll() {
		super.unmarkerAll();
		return notifyDataSetChanged();
	}

	@Override
	public boolean marker(DataType dataType) {
		boolean isMarker = super.marker(dataType);
		notifyDataSetChanged();
		return isMarker;
	}

	@Override
	public boolean singleMarker(DataType dataType) {
		boolean isMarker = super.singleMarker(dataType);
		notifyDataSetChanged();
		return isMarker;
	}

	public final Adapter getDataSourceAdapter() {
		return mAdapter;
	}
}
