package androidx.sframe.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sframe.R;
import androidx.sframe.model.AppMenuModel;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019-06-15
 * Email : ok@163.com.
 */
public class ToolbarMenuDelegate extends AbsRecyclerDelegate<AppMenuModel> {

	@NonNull
	@Override
	public RecyclerAdapter.ViewHolder<AppMenuModel> onCreateViewHolder(@NonNull RecyclerAdapter<AppMenuModel> adapter, @NonNull ViewGroup parent, int itemViewType) {
		return RecyclerAdapter.createViewHolder(R.layout.item_page_menu_layout, parent);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder<AppMenuModel> holder, int position, @Nullable List<Object> payloads) {
		final AppMenuModel mMenuModel = holder.findDataSourceByPosition(position);
		mMenuModel.setPosition(position);

		if (mMenuModel.isImage()) {
			holder.getViewController()
					.findAt(R.id.picImageView).methodAtImageView().setImageResource(mMenuModel.getResId()).setVisibility(View.VISIBLE)
					.findAt(R.id.titleTextView).setVisibility(View.GONE);
		} else if (mMenuModel.isText()) {
			holder.getViewController()
					.findAt(R.id.picImageView).setVisibility(View.GONE)
					.findAt(R.id.titleTextView).methodAtTextView().setText(mMenuModel.getTitle()).setVisibility(View.VISIBLE);
		} else {
			holder.getViewController()
					.findAt(R.id.picImageView).setVisibility(View.GONE)
					.findAt(R.id.titleTextView).setVisibility(View.GONE);
		}

		final int count = holder.getDataSourceController().getDataSourceCount();
		final int sizeOf12 = 10;
		final RecyclerView.LayoutParams mLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

		if (count > 1) {
			if (position == 0) {
				// first position
				mLayoutParams.leftMargin = sizeOf12 * 2;
				mLayoutParams.rightMargin = sizeOf12;
			} else if (position >= holder.getDataSourceController().getDataSourceCount() - 1) {
				// last position
				mLayoutParams.leftMargin = sizeOf12;
				mLayoutParams.rightMargin = sizeOf12 * 2;
			} else {
				// other
				mLayoutParams.leftMargin = sizeOf12;
				mLayoutParams.rightMargin = sizeOf12;
			}
		} else {
			mLayoutParams.leftMargin = sizeOf12 * 2;
			mLayoutParams.rightMargin = sizeOf12 * 2;
		}
	}
}
