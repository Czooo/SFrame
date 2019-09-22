package androidx.sframe.ui.controller.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.utils.Collections;

/**
 * @Author create by Zoran on 2019-09-12
 * @Email : 171905184@qq.com
 * @Description :
 */
public class DataSourceControllerImpl<DataSource> implements DataSourceController<DataSource> {

	private ArrayList<DataSource> mDataSources;
	private ArrayList<DataSource> mSelectedDataSources;

	@Override
	public final DataSourceController<DataSource> setDataSource(@NonNull DataSource dataSource) {
		return this.setDataSource(Collections.toList(dataSource));
	}

	@Override
	public final DataSourceController<DataSource> setDataSource(@NonNull Collection<? extends DataSource> dataSources) {
		synchronized (this) {
			this.removeAll();
		}
		return this.addDataSource(dataSources);
	}

	@Override
	public final DataSourceController<DataSource> addDataSource(@NonNull DataSource dataSource) {
		return this.addDataSource(Collections.toList(dataSource));
	}

	@Override
	public final DataSourceController<DataSource> addDataSource(@NonNull DataSource dataSource, int index) {
		return this.addDataSource(Collections.toList(dataSource), index);
	}

	@Override
	public final DataSourceController<DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources) {
		return this.addDataSource(dataSources, this.getDataSourceCount());
	}

	@Override
	public DataSourceController<DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources, int index) {
		synchronized (this) {
			this.getDataSourceList().addAll(Math.min(Math.max(index, 0), this.getDataSourceCount()), dataSources);
		}
		return this;
	}

	@Override
	public DataSourceController<DataSource> removeAll() {
		this.getDataSourceList().clear();
		return this;
	}

	@Override
	public DataSourceController<DataSource> removeDataSource(int position) {
		if (this.assertIndexOutOfBounds(position)) {
			return this;
		}
		this.getDataSourceList().remove(position);
		return this;
	}

	@Override
	public final DataSourceController<DataSource> removeDataSource(@Nullable DataSource dataSource) {
		return this.removeDataSource(this.indexOf(dataSource));
	}

	@Override
	public DataSourceController<DataSource> moveDataSourceOf(int fromPosition, int toPosition) {
		if (this.assertIndexOutOfBounds(fromPosition)
				|| this.assertIndexOutOfBounds(toPosition)) {
			return this;
		}
		this.addDataSource(this.getDataSourceList().remove(fromPosition), toPosition);
		return this;
	}

	@Override
	public final DataSourceController<DataSource> moveDataSourceToStart(int fromPosition) {
		return this.moveDataSourceOf(fromPosition, 0);
	}

	@Override
	public final DataSourceController<DataSource> moveDataSourceToEnd(int fromPosition) {
		return this.moveDataSourceOf(fromPosition, this.getDataSourceCount() - 1);
	}

	@NonNull
	@Override
	public final ArrayList<DataSource> getDataSourceList() {
		if (this.mDataSources == null) {
			synchronized (this) {
				if (this.mDataSources == null) {
					this.mDataSources = new ArrayList<>();
				}
			}
		}
		return this.mDataSources;
	}

	@NonNull
	@Override
	public final ArrayList<DataSource> getSelectedDataSourceList() {
		if (this.mSelectedDataSources == null) {
			synchronized (this) {
				if (this.mSelectedDataSources == null) {
					this.mSelectedDataSources = new ArrayList<>();
				}
			}
		}
		return this.mSelectedDataSources;
	}

	@NonNull
	@Override
	public final DataSource findDataSourceByPosition(int position) {
		if (this.assertIndexOutOfBounds(position)) {
			throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + this.getDataSourceCount());
		}
		return this.getDataSourceList().get(position);
	}

	@Override
	public final int getDataSourceCount() {
		return this.getDataSourceList().size();
	}

	@Override
	public final int indexOf(@Nullable DataSource dataSource) {
		if (dataSource == null) {
			return -1;
		}
		return this.getDataSourceList().indexOf(dataSource);
	}

	@Override
	public final int lastIndexOf(@Nullable DataSource dataSource) {
		if (dataSource == null) {
			return -1;
		}
		return this.getDataSourceList().lastIndexOf(dataSource);
	}

	@Override
	public final boolean contains(@Nullable DataSource dataSource) {
		if (dataSource == null) {
			return false;
		}
		return this.getDataSourceList().contains(dataSource);
	}

	@Override
	public final boolean isEmpty() {
		return this.mDataSources == null
				|| this.mDataSources.isEmpty();
	}

	@Override
	public boolean switchedSelectStateByAll() {
		if (this.getSelectedDataSourceList().isEmpty()) {
			return this.getSelectedDataSourceList().addAll(this.getDataSourceList());
		}
		this.getSelectedDataSourceList().clear();
		return false;
	}

	@Override
	public final boolean switchedSelectStateOf(int position) {
		return this.switchedSelectStateOf(this.findDataSourceByPosition(position));
	}

	@Override
	public boolean switchedSelectStateOf(@NonNull DataSource dataSource) {
		if (this.isSelectedState(dataSource)) {
			this.getSelectedDataSourceList().remove(dataSource);
			return false;
		}
		return this.getSelectedDataSourceList().add(dataSource);
	}

	@Override
	public final boolean switchedSingleSelectStateOf(int position) {
		return this.switchedSingleSelectStateOf(this.findDataSourceByPosition(position));
	}

	@Override
	public final boolean switchedSingleSelectStateOf(@NonNull DataSource dataSource) {
		for (DataSource oldDataSource : this.getSelectedDataSourceList()) {
			if (Objects.equals(oldDataSource, dataSource)) {
				continue;
			}
			this.switchedSelectStateOf(oldDataSource);
		}
		return this.switchedSelectStateOf(dataSource);
	}

	@Override
	public final boolean isSelectedStateByAll() {
		if (this.getSelectedDataSourceList().isEmpty()) {
			return false;
		}
		return this.getSelectedDataSourceList().size() == this.getDataSourceList().size();
	}

	@Override
	public final boolean isSelectedState(int position) {
		return this.isSelectedState(this.findDataSourceByPosition(position));
	}

	@Override
	public final boolean isSelectedState(@NonNull DataSource dataSource) {
		return this.getSelectedDataSourceList().indexOf(dataSource) != -1;
	}

	protected final boolean assertIndexOutOfBounds(int index) {
		return index < 0 || index >= this.getDataSourceCount();
	}
}
