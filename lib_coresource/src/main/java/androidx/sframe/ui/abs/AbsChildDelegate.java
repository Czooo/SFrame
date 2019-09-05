package androidx.sframe.ui.abs;

import androidx.sframe.ui.controller.RecyclerAdapterController;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;

/**
 * Author create by ok on 2019/3/27
 * Email : ok@163.com.
 */
public abstract class AbsChildDelegate<DataSource, ParentDataSource> implements RecyclerAdapterController.ChildDelegate<DataSource, ParentDataSource> {

	@Override
	public int getItemViewType(RecyclerAdapterController<ParentDataSource> adapterController, RecyclerChildAdapter<DataSource, ParentDataSource> recyclerChildAdapter, int position) {
		return 0;
	}
}
