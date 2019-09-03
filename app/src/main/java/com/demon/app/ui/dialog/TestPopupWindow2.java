package com.demon.app.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.demon.app.R;
import com.demon.app.ui.fragment.test.PublicFragment;

import org.parent.refreshview.helper.DragHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.helper.AppPageDragHelper;
import androidx.demon.ui.abs.AbsPopupWindow;
import androidx.demon.ui.controller.AppNavigation;
import androidx.demon.ui.controller.AppPageController;

/**
 * Author create by ok on 2019-06-19
 * Email : ok@163.com.
 */
public class TestPopupWindow2 extends AbsPopupWindow {

	public TestPopupWindow2(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.dialog_popupwindow_test_2;
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
								.showPage(TestDialogFragment.class);
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
		;

		/* 阻尼效果：DragHelper.DragMode.NONE(取消滑动移除，却有阻尼效果)*/
		AppPageDragHelper.attachToPage(this.getPageController())
				.setDraggedCloseDirection(DragHelper.DragMode.DRAG_END);
		// animation
		this.getPageController()
				.setMarginBottom(250)
				.setBackgroundViewAlpha(0.55f)
				.setCustomAnimation(R.anim.slide_in_from_top, R.anim.slide_out_to_top);
	}
}
