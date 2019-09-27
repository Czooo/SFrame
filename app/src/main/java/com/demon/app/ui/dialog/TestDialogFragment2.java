package com.demon.app.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.demon.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.model.AppMenuModel;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.utils.AppNavigator;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.impl.AppToolbarMethod;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class TestDialogFragment2 extends AbsDialogFragment {

	public TestDialogFragment2(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.dialog_fragment_test_2;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getToolbarController()
				.getToolbarMethod()
				.addMenu(new AppMenuModel(1, "Show"))
				.setOnMenuClickListener(new AppToolbarMethod.OnMenuClickListener() {

					@Override
					public void onMenuClick(@NonNull View view, @NonNull RecyclerAdapter.ViewHolder<AppMenuModel> holder, int position) {
						AppNavigator.findNavController(view)
								.showFragment(TestDialogFragment3.class);
					}
				});
	}
}
