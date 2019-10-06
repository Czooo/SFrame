package com.demon.app.navigator;

import android.os.Bundle;
import android.view.View;

import com.demon.app.R;
import com.demon.app.ui.fragment.test.PublicFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.ui.controller.AppPageController;

/**
 * @Author create by Zoran on 2019-10-03
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavigatorDialogFragment extends AbsDialogFragment implements View.OnClickListener {

	public NavigatorDialogFragment(@NonNull AppPageController<?> hostPageController) {
		super(hostPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.fragment_navigator_1;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getToolbarController()
				.getToolbarMethod()
				.setTitleText("NavigatorDialogFragment");

		this.getViewController()
				.findAt(R.id.startActivity).setOnClickListener(this)
				.findAt(R.id.startFragment).setOnClickListener(this)
				.findAt(R.id.pushFragment).setOnClickListener(this)
				.findAt(R.id.showFragment).setOnClickListener(this)
		;
		this.getNavController()
				.pushFragment(R.id.content, PublicFragment.class, this.getNavController().getNavOptions());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.startActivity:
				this.getNavController().startActivity(NavigatorActivity.class);
				break;
			case R.id.startFragment:
				this.getNavController().startFragment(NavigatorFragment.class);
				break;
			case R.id.pushFragment:
				this.getNavController().pushFragment(R.id.content, PublicFragment.class, this.getNavController().getNavOptions());
				break;
			case R.id.showFragment:
				this.dismiss();
				this.getNavController().showFragment(NavigatorDialogFragment.class);
				break;
		}
	}
}
