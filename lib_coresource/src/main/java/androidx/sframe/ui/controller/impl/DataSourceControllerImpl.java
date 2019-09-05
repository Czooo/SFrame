package androidx.sframe.ui.controller.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.sframe.ui.controller.DataSourceController;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public class DataSourceControllerImpl<DataType> implements DataSourceController<DataType> {

	private List<DataType> mDataSourceList;

	private List<DataType> mMarkerDataSourceList;

	@Override
	public final DataSourceController<DataType> setDataSource(DataType dataType) {
		return setDataSource(dataType, size());
	}

	@Override
	public final DataSourceController<DataType> setDataSource(DataType dataType, int index) {
		List<DataType> mDataTypeList = new ArrayList<>();
		mDataTypeList.add(dataType);
		return setDataSourceList(mDataTypeList, index);
	}

	@Override
	public DataSourceController<DataType> setDataSourceList(Collection<? extends DataType> dataTypeList) {
		return setDataSourceList(dataTypeList, size());
	}

	@Override
	public DataSourceController<DataType> setDataSourceList(Collection<? extends DataType> dataTypeList, int index) {
		return removeAll().addDataSourceList(dataTypeList, index);
	}

	@Override
	public final DataSourceController<DataType> addDataSource(DataType dataType) {
		return addDataSource(dataType, size());
	}

	@Override
	public final DataSourceController<DataType> addDataSource(DataType dataType, int index) {
		List<DataType> mDataTypeList = new ArrayList<>();
		mDataTypeList.add(dataType);
		return addDataSourceList(mDataTypeList, index);
	}

	@Override
	public final DataSourceController<DataType> addDataSourceList(Collection<? extends DataType> dataTypeList) {
		return addDataSourceList(dataTypeList, size());
	}

	@Override
	@CallSuper
	public DataSourceController<DataType> addDataSourceList(Collection<? extends DataType> dataTypeList, int index) {
		getAllDataSource().addAll(Math.min(Math.max(index, 0), size()), dataTypeList);
		return this;
	}

	@Override
	@CallSuper
	public DataSourceController<DataType> removeDataSource(int position) {
		if (!assertIndexOutOfBounds(position)) {
			getAllDataSource().remove(position);
		}
		return this;
	}

	@Override
	public final DataSourceController<DataType> removeDataSource(DataType dataType) {
		return removeDataSource(indexOf(dataType));
	}

	@Override
	@CallSuper
	public DataSourceController<DataType> removeDataSource(Collection<? extends DataType> dataTypeList) {
		getAllDataSource().removeAll(dataTypeList);
		return this;
	}

	@Override
	@CallSuper
	public DataSourceController<DataType> removeAll() {
		if (mDataSourceList != null) {
			mDataSourceList.clear();
		}
		return this;
	}

	@Override
	@CallSuper
	public DataSourceController<DataType> moveDataSourceOf(int fromPosition, int toPosition) {
		if (!assertIndexOutOfBounds(fromPosition) &&
				!assertIndexOutOfBounds(toPosition)) {
			addDataSource(getAllDataSource().remove(fromPosition), toPosition);
		}
		return null;
	}

	@Override
	public final DataSourceController<DataType> moveDataSourceToStart(int fromPosition) {
		return moveDataSourceOf(fromPosition, 0);
	}

	@Override
	public final DataSourceController<DataType> moveDataSourceToEnd(int fromPosition) {
		return moveDataSourceOf(fromPosition, size() - 1);
	}

	@Override
	@CallSuper
	public DataSourceController<DataType> markerAll() {
		getAllMarkerDataSource().clear();
		getAllMarkerDataSource().addAll(getAllDataSource());
		return this;
	}

	@Override
	@CallSuper
	public DataSourceController<DataType> unmarkerAll() {
		getAllMarkerDataSource().clear();
		return this;
	}

	@Override
	public final List<DataType> getAllMarkerDataSource() {
		if (mMarkerDataSourceList == null) {
			synchronized (this) {
				mMarkerDataSourceList = new ArrayList<>();
			}
		}
		return mMarkerDataSourceList;
	}

	@Override
	public final List<DataType> getAllDataSource() {
		synchronized (this) {
			if (mDataSourceList == null) {
				mDataSourceList = new ArrayList<>();
			}
		}
		return mDataSourceList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final List<DataType> clone() {
		return (List<DataType>) ((ArrayList<DataType>) getAllDataSource()).clone();
	}

	@Override
	public final DataType findDataSourceByPosition(int position) {
		if (!assertIndexOutOfBounds(position)) {
			return getAllDataSource().get(position);
		}
		return null;
	}

	@Override
	public final int size() {
		return getAllDataSource().size();
	}

	@Override
	public final int indexOf(DataType dataType) {
		return getAllDataSource().indexOf(dataType);
	}

	@Override
	public final int lastIndexOf(DataType dataType) {
		return getAllDataSource().lastIndexOf(dataType);
	}

	@Override
	public final boolean isEmpty() {
		return getAllDataSource().isEmpty();
	}

	@Override
	public final boolean contains(DataType dataType) {
		return getAllDataSource().contains(dataType);
	}

	@Override
	public final boolean containsList(Collection<? extends DataType> dataTypeList) {
		return getAllDataSource().containsAll(dataTypeList);
	}

	@Override
	public final boolean marker(int position) {
		return marker(findDataSourceByPosition(position));
	}

	@Override
	@CallSuper
	public boolean marker(DataType dataType) {
		List<DataType> mDataTypeList = getAllMarkerDataSource();
		if (mDataTypeList.indexOf(dataType) == -1) {
			return mDataTypeList.add(dataType);
		} else {
			mDataTypeList.remove(dataType);
		}
		return false;
	}

	@Override
	public final boolean singleMarker(int position) {
		return singleMarker(findDataSourceByPosition(position));
	}

	@Override
	@CallSuper
	public boolean singleMarker(DataType dataType) {
		boolean isMarker = marker(dataType);

		if (isMarker) {
			List<DataType> mDataTypeList = getAllMarkerDataSource();
			mDataTypeList.clear();
			mDataTypeList.add(dataType);
		}
		return isMarker;
	}

	@Override
	public final boolean isMarker(int position) {
		return isMarker(findDataSourceByPosition(position));
	}

	@Override
	public final boolean isMarker(DataType dataType) {
		return getAllMarkerDataSource().indexOf(dataType) != -1;
	}

	private boolean assertIndexOutOfBounds(int positioin) {
		return positioin < 0 || positioin >= size();
	}
}
