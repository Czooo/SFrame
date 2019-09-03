package androidx.demon.ui.controller;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public interface DataSourceNotifyController2<Adapter, DataType> extends DataSourceNotifyController<Adapter, DataType> {

	DataSourceNotifyController2<Adapter, DataType> notifyItemRemoved(int position);

	DataSourceNotifyController2<Adapter, DataType> notifyItemRangeRemoved(int positionStart, int itemCount);

	DataSourceNotifyController2<Adapter, DataType> notifyItemChanged(int position);

	DataSourceNotifyController2<Adapter, DataType> notifyItemRangeChanged(int positionStart, int itemCount);

	DataSourceNotifyController2<Adapter, DataType> notifyItemRangeChanged(int positionStart, int itemCount, Object payload);

	DataSourceNotifyController2<Adapter, DataType> notifyItemInserted(int position);

	DataSourceNotifyController2<Adapter, DataType> notifyItemRangeInserted(int positionStart, int itemCount);

	DataSourceNotifyController2<Adapter, DataType> notifyItemMoved(int fromPosition, int toPosition);
}
