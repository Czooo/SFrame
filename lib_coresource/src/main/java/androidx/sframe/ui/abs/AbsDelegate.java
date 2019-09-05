package androidx.sframe.ui.abs;

import androidx.sframe.ui.controller.RecyclerAdapterController;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019/1/24
 * Email : ok@163.com.
 */
public abstract class AbsDelegate<DataSource> implements RecyclerAdapter.Delegate<DataSource> {

	@Override
	public int getItemViewType(RecyclerAdapterController<DataSource> adapterController, int position) {
		return 0;
	}
}
