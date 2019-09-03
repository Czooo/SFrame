package com.demon.app.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.demon.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.model.AppMenuModel;
import androidx.demon.ui.abs.AbsDialogFragment;
import androidx.demon.ui.controller.AppNavigation;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.ui.controller.impl.AppToolbarMethod;
import androidx.demon.widget.adapter.RecyclerAdapter;

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
						AppNavigation.findPageController(view).getAppNavController()
								.showPage(TestDialogFragment3.class);
					}
				});
	}
}
