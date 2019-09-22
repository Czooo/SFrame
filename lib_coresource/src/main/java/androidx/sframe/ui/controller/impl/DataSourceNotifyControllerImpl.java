package androidx.sframe.ui.controller.impl;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public abstract class DataSourceNotifyControllerImpl<Adapter, DataSource> extends DataSourceControllerImpl<DataSource> implements DataSourceNotifyController<Adapter, DataSource> {

	private final Adapter mAdapter;

	public DataSourceNotifyControllerImpl(@NonNull Adapter adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public DataSourceController<DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources, int index) {
		super.addDataSource(dataSources, index);
		return this.notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataSource> removeAll() {
		super.removeAll();
		return this.notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataSource> removeDataSource(int position) {
		super.removeDataSource(position);
		return this.notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataSource> moveDataSourceOf(int fromPosition, int toPosition) {
		super.moveDataSourceOf(fromPosition, toPosition);
		return this.notifyDataSetChanged();
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
		this.notifyDataSetChanged();
		return state;
	}

	@NonNull
	@Override
	public Adapter getDataSourceAdapter() {
		return this.mAdapter;
	}
}
