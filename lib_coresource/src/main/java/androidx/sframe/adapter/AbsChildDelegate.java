package androidx.sframe.adapter;

import androidx.annotation.NonNull;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;

/**
 * Author create by ok on 2019/3/27
 * Email : ok@163.com.
 */
public abstract class AbsChildDelegate<DataSource, ParentDataSource> implements RecyclerChildAdapter.Delegate<DataSource, ParentDataSource> {

	@Override
	public int getItemViewType(@NonNull RecyclerChildAdapter<DataSource, ParentDataSource> adapter, int position) {
		return 0;
	}
}
