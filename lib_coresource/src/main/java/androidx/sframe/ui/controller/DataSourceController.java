package androidx.sframe.ui.controller;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author create by Zoran on 2019-09-12
 * @Email : 171905184@qq.com
 * @Description :
 */
public interface DataSourceController<DataSource> {

	DataSourceController<DataSource> setDataSource(@NonNull DataSource dataSource);

	DataSourceController<DataSource> setDataSource(@NonNull Collection<? extends DataSource> dataSources);

	DataSourceController<DataSource> addDataSource(@NonNull DataSource dataSource);

	DataSourceController<DataSource> addDataSource(@NonNull DataSource dataSource, int index);

	DataSourceController<DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources);

	DataSourceController<DataSource> addDataSource(@NonNull Collection<? extends DataSource> dataSources, int index);

	DataSourceController<DataSource> removeAll();

	DataSourceController<DataSource> removeDataSource(int position);

	DataSourceController<DataSource> removeDataSource(@Nullable DataSource dataSource);

	DataSourceController<DataSource> moveDataSourceOf(int fromPosition, int toPosition);

	DataSourceController<DataSource> moveDataSourceToStart(int fromPosition);

	DataSourceController<DataSource> moveDataSourceToEnd(int fromPosition);

	@NonNull
	ArrayList<DataSource> getDataSourceList();

	@NonNull
	ArrayList<DataSource> getSelectedDataSourceList();

	@NonNull
	DataSource findDataSourceByPosition(int position);

	int getDataSourceCount();

	int indexOf(@Nullable DataSource dataSource);

	int lastIndexOf(@Nullable DataSource dataSource);

	boolean contains(@Nullable DataSource dataSource);

	boolean isEmpty();

	boolean switchedSelectStateByAll();

	boolean switchedSelectStateOf(int position);

	boolean switchedSelectStateOf(@NonNull DataSource dataSource);

	boolean switchedSingleSelectStateOf(int position);

	boolean switchedSingleSelectStateOf(@NonNull DataSource dataSource);

	boolean isSelectedStateByAll();

	boolean isSelectedState(int position);

	boolean isSelectedState(@NonNull DataSource dataSource);
}
