package com.demon.app.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;

import com.demon.app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsChildDelegate;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;

/**
 * Author create by ok on 2019-06-19
 * Email : ok@163.com.
 */
public class FooterDelegate<ParentData> extends AbsChildDelegate<String, ParentData> {

	private int[] colors = new int[]{
			Color.GREEN,
			Color.BLUE,
			Color.GRAY,
			Color.WHITE
	};

	@NonNull
	@Override
	public RecyclerChildAdapter.ViewHolder<String, ParentData> onCreateViewHolder(@NonNull RecyclerChildAdapter<String, ParentData> adapter, @NonNull ViewGroup parent, int itemViewType) {
		if (itemViewType == 1) {
			return RecyclerChildAdapter.createViewHolder(android.R.layout.simple_list_item_1, parent);
		}
		return RecyclerChildAdapter.createViewHolder(R.layout.item_simple_test, parent);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerChildAdapter.ViewHolder<String, ParentData> holder, int position, @Nullable List<Object> payloads) {
		if (1 == holder.getRealItemViewType()) {
			holder.getViewController()
					.findAt(android.R.id.text1)
					.methodAtTextView()
					.setGravity(Gravity.CENTER)
					.setText(holder.findChildDataSourceByPosition(position))
					.setBackgroundResource(R.color.colorAppTheme);
			return;
		}
		holder.getViewController()
				.findAt(R.id.text1)
				.setBackgroundColor(colors[position % colors.length])
				.methodAtTextView()
				.setText(holder.findChildDataSourceByPosition(position));
	}

	@Override
	public int getItemViewType(@NonNull RecyclerChildAdapter<String, ParentData> adapter, int position) {
		if (position % 2 == 0) {
			return 1;
		}
		return 0;
	}
}
