package androidx.sframe.ui.controller;

import java.util.Collection;
import java.util.List;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public interface DataSourceController<DataType> {

	DataSourceController<DataType> setDataSource(DataType dataType);

	DataSourceController<DataType> setDataSource(DataType dataType, int index);

	DataSourceController<DataType> setDataSourceList(Collection<? extends DataType> dataTypeList);

	DataSourceController<DataType> setDataSourceList(Collection<? extends DataType> dataTypeList, int index);

	DataSourceController<DataType> addDataSource(DataType dataType);

	DataSourceController<DataType> addDataSource(DataType dataType, int index);

	DataSourceController<DataType> addDataSourceList(Collection<? extends DataType> dataTypeList);

	DataSourceController<DataType> addDataSourceList(Collection<? extends DataType> dataTypeList, int index);

	DataSourceController<DataType> removeDataSource(int position);

	DataSourceController<DataType> removeDataSource(DataType dataType);

	DataSourceController<DataType> removeDataSource(Collection<? extends DataType> dataTypeList);

	DataSourceController<DataType> removeAll();

	DataSourceController<DataType> moveDataSourceOf(int fromPosition, int toPosition);

	DataSourceController<DataType> moveDataSourceToStart(int fromPosition);

	DataSourceController<DataType> moveDataSourceToEnd(int fromPosition);

	DataSourceController<DataType> markerAll();

	DataSourceController<DataType> unmarkerAll();

	List<DataType> getAllMarkerDataSource();

	List<DataType> getAllDataSource();

	List<DataType> clone();

	DataType findDataSourceByPosition(int position);

	int size();

	int indexOf(DataType dataType);

	int lastIndexOf(DataType dataType);

	boolean isEmpty();

	boolean contains(DataType dataType);

	boolean containsList(Collection<? extends DataType> dataTypeList);

	boolean marker(int position);

	boolean marker(DataType dataType);

	boolean singleMarker(int position);

	boolean singleMarker(DataType dataType);

	boolean isMarker(int position);

	boolean isMarker(DataType dataType);
}
