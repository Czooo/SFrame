package androidx.sframe.ui.controller;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public interface DataSourceNotifyController2<Adapter, DataSource> extends DataSourceNotifyController<Adapter, DataSource> {

	DataSourceNotifyController2<Adapter, DataSource> notifyItemRemoved(int position);

	DataSourceNotifyController2<Adapter, DataSource> notifyItemRangeRemoved(int positionStart, int itemCount);

	DataSourceNotifyController2<Adapter, DataSource> notifyItemChanged(int position);

	DataSourceNotifyController2<Adapter, DataSource> notifyItemRangeChanged(int positionStart, int itemCount);

	DataSourceNotifyController2<Adapter, DataSource> notifyItemRangeChanged(int positionStart, int itemCount, Object payload);

	DataSourceNotifyController2<Adapter, DataSource> notifyItemInserted(int position);

	DataSourceNotifyController2<Adapter, DataSource> notifyItemRangeInserted(int positionStart, int itemCount);

	DataSourceNotifyController2<Adapter, DataSource> notifyItemMoved(int fromPosition, int toPosition);
}
