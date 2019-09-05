package androidx.sframe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.sframe.R;
import androidx.sframe.model.AppMenuModel;
import androidx.sframe.ui.abs.AbsDelegate;
import androidx.sframe.ui.controller.RecyclerAdapterController;
import androidx.sframe.widget.adapter.RecyclerAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author create by ok on 2019-06-15
 * Email : ok@163.com.
 */
public class MenuDelegate extends AbsDelegate<AppMenuModel> {

	@Override
	public View onCreateItemView(RecyclerAdapterController<AppMenuModel> adapterController, LayoutInflater inflater, ViewGroup parent, int itemViewType) {
		return inflater.inflate(R.layout.item_menu_layout, parent, false);
	}

	@Override
	public void onBindItemView(RecyclerAdapter.ViewHolder<AppMenuModel> holder, int position, @Nullable List<Object> payloads) {
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

		final int count = holder.getDataSourceNotifyController().size();
		final int sizeOf12 = 12;
		final RecyclerView.LayoutParams mLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

		if (count > 1) {

			if (position == 0) {
				// first position
				mLayoutParams.leftMargin = sizeOf12 * 2;
				mLayoutParams.rightMargin = sizeOf12;
			} else if (position >= holder.getDataSourceNotifyController().size() - 1) {
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
