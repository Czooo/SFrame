package androidx.sframe.adapter;

import androidx.annotation.NonNull;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019/1/24
 * Email : ok@163.com.
 */
public abstract class AbsRecyclerDelegate<DataSource> implements RecyclerAdapter.Delegate<DataSource> {

	@Override
	public int getItemViewType(@NonNull RecyclerAdapter<DataSource> adapter, int position) {
		return 0;
	}
}
