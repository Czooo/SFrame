package com.demon.app.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;

import com.demon.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsPopupWindow;
import androidx.sframe.ui.controller.AppPageController;

/**
 * @Author create by Zoran on 2019-09-13
 * @Email : 171905184@qq.com
 * @Description :
 */
public class TestPopupWindow extends AbsPopupWindow {

	private Callback mCallback;

	public TestPopupWindow(@NonNull AppPageController<?> hostPageController) {
		super(hostPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.dialog_popupwindow_test;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getViewController()
				.findAt(R.id.text1)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mCallback.onClick(TestPopupWindow.this, view, 1);
					}
				})
				.findAt(R.id.text2)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mCallback.onClick(TestPopupWindow.this, view, 2);
					}
				})
				.findAt(R.id.text3)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mCallback.onClick(TestPopupWindow.this, view, 3);
					}
				})
				.findAt(R.id.text4)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mCallback.onClick(TestPopupWindow.this, view, 4);
					}
				})
				.findAt(R.id.text5)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						mCallback.onClick(TestPopupWindow.this, view, 5);
					}
				})
		;
		this.getPageController()
				.setWindowBackgroundAlpha(0.55f);
	}

	public void setCallback(@NonNull Callback callback) {
		this.mCallback = callback;
	}

	public interface Callback {

		void onClick(@NonNull PopupWindow popupWindow, @NonNull View view, int id);
	}
}
