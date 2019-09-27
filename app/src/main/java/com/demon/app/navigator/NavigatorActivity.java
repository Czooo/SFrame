package com.demon.app.navigator;

import android.os.Bundle;
import android.view.View;

import com.demon.app.R;
import com.demon.app.ui.fragment.test.PublicFragment;

import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsActivity;
import androidx.sframe.ui.dialog.ProgressDialogFragment;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public class NavigatorActivity extends AbsActivity implements View.OnClickListener {

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.activity_navigator_1;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getViewController()
				.findAt(R.id.startActivity).setOnClickListener(this)
				.findAt(R.id.startFragment).setOnClickListener(this)
				.findAt(R.id.pushFragment).setOnClickListener(this)
				.findAt(R.id.showFragment).setOnClickListener(this)
		;
		this.getNavController()
				.pushFragment(R.id.content, PublicFragment.class);
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
				this.getNavController().pushFragment(R.id.content, PublicFragment.class, this.getNavController().getDefaultOptions());
				break;
			case R.id.showFragment:
				this.getNavController().showFragment(ProgressDialogFragment.class);
				break;
		}
	}
}
