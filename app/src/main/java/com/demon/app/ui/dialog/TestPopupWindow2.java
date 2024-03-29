package com.demon.app.ui.dialog;

import android.os.Bundle;
import android.view.View;

import com.demon.app.R;
import com.demon.app.ui.fragment.test.PublicFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.helper.PageDragHelper;
import androidx.sframe.ui.abs.AbsPopupWindow;
import androidx.sframe.utils.AppNavigator;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.utils.ToastCompat;

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
		Bundle args = this.requireArguments();
		ToastCompat.toastDebug(args.getString("Test",""));

		this.getViewController()
				.findAt(R.id.text1)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						AppNavigator.findNavController(view)
								.showFragment(TestDialogFragment.class);
					}
				})
				.findAt(R.id.text2)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						AppNavigator.findNavController(view)
								.startFragmentForResult(PublicFragment.class, 1);
					}
				})
		;

		/* 阻尼效果：DragHelper.DragMode.NONE(取消滑动移除，却有阻尼效果)*/
		PageDragHelper.attachToPage(this.getPageController())
				.setDragCloseDirection(PageDragHelper.DRAG_DIRECTION_TOP)
				.setDragCloseEnabled(true);

		this.setBackgroundAlpha(0.55F);
		this.setBackgroundMarginBottom(250);
		this.setLayoutEnterAnimation(R.anim.slide_in_from_top);
		this.setLayoutExitAnimation(R.anim.slide_out_to_top);
	}
}
