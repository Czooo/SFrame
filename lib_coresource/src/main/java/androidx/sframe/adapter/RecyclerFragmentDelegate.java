package androidx.sframe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;
import androidx.sframe.widget.adapter.RecyclerFragment;

/**
 * @Author create by Zoran on 2019-09-13
 * @Email : 171905184@qq.com
 * @Description :
 */
public class RecyclerFragmentDelegate<ParentDataSource> extends AbsChildDelegate<RecyclerFragment, ParentDataSource> {

	@NonNull
	@Override
	public RecyclerChildAdapter.ViewHolder<RecyclerFragment, ParentDataSource> onCreateViewHolder(@NonNull RecyclerChildAdapter<RecyclerFragment, ParentDataSource> adapter, @NonNull ViewGroup parent, int itemViewType) {
		for (RecyclerFragment dataSource : adapter.getDataSourceController().getDataSourceList()) {
			if (dataSource.getTempItemViewType() == itemViewType) {
				return RecyclerChildAdapter.createViewHolder(dataSource.onCreateView(LayoutInflater.from(parent.getContext()), parent));
			}
		}
		throw new IllegalStateException("RecyclerFragmentDelegate " + this + " not RecyclerFragment by itemViewType : " + itemViewType);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerChildAdapter.ViewHolder<RecyclerFragment, ParentDataSource> holder, int position, @Nullable List<Object> payloads) {
		holder.findChildDataSourceByPosition(position).onViewCreated(holder.getItemView(), position, payloads);
	}

	@Override
	public int getItemViewType(@NonNull RecyclerChildAdapter<RecyclerFragment, ParentDataSource> adapter, int position) {
		final RecyclerFragment dataSourceByPosition = adapter.findDataSourceByPosition(position);
		return dataSourceByPosition.getTempItemViewType();
	}
}
