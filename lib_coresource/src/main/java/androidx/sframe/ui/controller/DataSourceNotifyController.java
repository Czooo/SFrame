package androidx.sframe.ui.controller;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public interface DataSourceNotifyController<Adapter, DataType> extends DataSourceController<DataType> {

	DataSourceNotifyController<Adapter, DataType> notifyDataSetChanged();

	Adapter getDataSourceAdapter();
}
