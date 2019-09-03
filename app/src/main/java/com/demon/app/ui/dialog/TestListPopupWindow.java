package com.demon.app.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demon.app.R;

import org.parent.refreshview.helper.DragHelper;
import org.parent.refreshview.widget.RefreshMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.compat.DataCompat;
import androidx.demon.helper.AppPageDragHelper;
import androidx.demon.ui.abs.AbsListPopupWindow;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.ui.controller.RecyclerAdapterController;
import androidx.demon.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public class TestListPopupWindow extends AbsListPopupWindow<String> {

	private int[] colors = new int[]{
			Color.GRAY,
			Color.WHITE,
			Color.GREEN,
			Color.BLUE
	};

	public TestListPopupWindow(@NonNull AppPageController<?> hostPageController) {
		super(hostPageController);
	}

	@Override
	public View onCreateItemView(RecyclerAdapterController<String> adapterController, LayoutInflater inflater, ViewGroup parent, int itemViewType) {
		return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
	}

	@Override
	public void onBindItemView(RecyclerAdapter.ViewHolder<String> holder, int position, @Nullable List<Object> payloads) {
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
				.setTitle("ListPopupWindow");

		this.getObjectListController()
				.setRefreshMode(RefreshMode.REFRESH_MODE_END)
				.setDragStartEnabled(false);

		AppPageDragHelper.attachToPage(this.getPageController())
				.setDraggedCloseDirection(DragHelper.DragMode.DRAG_START);

		this.getPageController()
				.setBackgroundAlpha(0.55f)
				.setAnimationStyle(R.style.PopupWindowAnimation_SlideInFromBottomOutToBottom);
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params, int page, int limit) {
		final List<String> models = new ArrayList<>(DataCompat.getString(page, limit));

		this.getPageController()
				.getViewController()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						getObjectListController().addDataSourceList(models);
					}
				}, 2000);
	}
}
