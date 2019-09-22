package androidx.sframe.ui.controller;

/**
 * @Author create by Zoran on 2019-09-15
 * @Email : 171905184@qq.com
 * @Description :
 */
public interface DataSourceNotifyController3<Adapter, DataSource> extends DataSourceNotifyController<Adapter, DataSource> {

	DataSourceNotifyController3<Adapter, DataSource> notifyGroupItemChanged(int groupPosition);

	DataSourceNotifyController3<Adapter, DataSource> notifyGroupItemRangeChanged(int groupPositionStart, int itemCount);

	DataSourceNotifyController3<Adapter, DataSource> notifyGroupItemRangeChanged(int groupPositionStart, int itemCount, Object payload);

	DataSourceNotifyController3<Adapter, DataSource> notifyGroupItemInserted(int groupPosition);

	DataSourceNotifyController3<Adapter, DataSource> notifyGroupItemRangeInserted(int groupPositionStart, int itemCount);

	DataSourceNotifyController3<Adapter, DataSource> notifyGroupItemMoved(int fromGroupPosition, int toGroupPosition);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemRemoved(int groupPosition, int childPosition);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemRangeRemoved(int groupPosition, int childPositionStart, int itemCount);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemChanged(int groupPosition, int childPosition);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemRangeChanged(int groupPosition, int childPositionStart, int itemCount);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemRangeChanged(int groupPosition, int childPositionStart, int itemCount, Object payload);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemInserted(int groupPosition, int childPosition);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemRangeInserted(int groupPosition, int childPositionStart, int itemCount);

	DataSourceNotifyController3<Adapter, DataSource> notifyChildItemMoved(int groupPosition, int fromChildPosition, int toChildPosition);
}
