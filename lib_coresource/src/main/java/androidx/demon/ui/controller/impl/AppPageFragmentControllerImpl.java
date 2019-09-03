package androidx.demon.ui.controller.impl;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
abstract class AppPageFragmentControllerImpl<Page extends Fragment> extends AbsPageControllerImpl<Page> {

	AppPageFragmentControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Fragment preFragment = this.getPageOwner();
		this.setViewModelStore(preFragment.getViewModelStore());
		this.setLifecycleOwner(preFragment);
	}
}
