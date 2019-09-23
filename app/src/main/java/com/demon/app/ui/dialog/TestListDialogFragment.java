package com.demon.app.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.utils.DataCompat;
import androidx.sframe.ui.abs.AbsListDialogFragment;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public class TestListDialogFragment extends AbsListDialogFragment<String> {

	private int[] colors = new int[]{
			Color.GRAY,
			Color.WHITE,
			Color.GREEN,
			Color.BLUE
	};

	public TestListDialogFragment(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

	@NonNull
	@Override
	public RecyclerAdapter.ViewHolder<String> onCreateViewHolder(@NonNull RecyclerAdapter<String> adapter, @NonNull ViewGroup parent, int itemViewType) {
		return RecyclerAdapter.createViewHolder(android.R.layout.simple_list_item_1, parent);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder<String> holder, int position, @Nullable List<Object> payloads) {
		holder.getViewController()
				.findAt(android.R.id.text1)
				.setBackgroundColor(colors[position % colors.length])
				.methodAtTextView()
				.setText(holder.findDataSourceByPosition(position));
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		super.onPageViewCreated(savedInstanceState);
		this.getPageController()
				.getToolbarController()
				.getToolbarMethod()
				.setTitleText("ListDialogFragment");
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params, int page, int limit) {
		final List<String> models = new ArrayList<>(DataCompat.getString(page, limit));

		this.getPageController()
				.getViewController()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						getObjectListController().addDataSource(models);
					}
				}, 2000);
	}
}
