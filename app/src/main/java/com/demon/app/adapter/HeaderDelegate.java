package com.demon.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demon.app.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsChildDelegate;
import androidx.sframe.ui.controller.RecyclerAdapterController;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;

/**
 * Author create by ok on 2019-06-19
 * Email : ok@163.com.
 */
public class HeaderDelegate<ParentData> extends AbsChildDelegate<String, ParentData> {

	private int[] colors = new int[]{
			Color.GREEN,
			Color.BLUE,
			Color.GRAY,
			Color.WHITE
	};

	@Override
	public View onCreateItemView(RecyclerAdapterController<ParentData> adapterController, RecyclerChildAdapter<String, ParentData> recyclerChildAdapter, LayoutInflater inflater, ViewGroup parent, int itemViewType) {
		return inflater.inflate(R.layout.item_simple_test_layout, parent, false);
	}

	@Override
	public void onBindItemView(RecyclerChildAdapter.ViewHolder<String, ParentData> holder, int position, @Nullable List<Object> payloads) {
		holder.getViewController()
				.findAt(android.R.id.text1)
				.setBackgroundColor(colors[position % colors.length])
				.methodAtTextView()
				.setText(holder.findChildDataSourceByPosition(position));
	}
}
