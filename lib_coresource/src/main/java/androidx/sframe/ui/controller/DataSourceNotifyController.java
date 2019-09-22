package androidx.sframe.ui.controller;

import androidx.annotation.NonNull;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public interface DataSourceNotifyController<Adapter, DataSource> extends DataSourceController<DataSource> {

	DataSourceNotifyController<Adapter, DataSource> notifyDataSetChanged();

	@NonNull
	Adapter getDataSourceAdapter();
}
