package com.demon.app.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demon.app.R;
import com.demon.app.ui.fragment.test.PublicFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.ui.controller.AppNavigation;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.utils.ToastCompat;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class TestDialogFragment extends AbsDialogFragment {

	public TestDialogFragment(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.dialog_fragment_test;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getViewController()
				.findAt(R.id.text1)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						AppNavigation.findPageController(view)
								.getAppNavController()
								.showPage(TestDialogFragment2.class);
					}
				})
				.findAt(R.id.text2)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						AppNavigation.findPageController(view)
								.getAppNavController()
								.startPageForResult(PublicFragment.class, 1);
					}
				})
				.findAt(R.id.text3)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {

					}
				})
		;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ToastCompat.toastDebug("Page " + this.getClass().getSimpleName() + " Result : " + requestCode + " , " + resultCode);
	}
}
