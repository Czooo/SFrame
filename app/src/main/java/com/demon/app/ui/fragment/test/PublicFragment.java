package com.demon.app.ui.fragment.test;

import android.app.Activity;
import android.os.Bundle;

import com.demon.app.R;

import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsFragment;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class PublicFragment extends AbsFragment {

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.fragment_public;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getToolbarController()
				.getToolbarMethod()
				.setPopEnabled(true);

		this.requireActivity().setResult(Activity.RESULT_OK);
	}
}
