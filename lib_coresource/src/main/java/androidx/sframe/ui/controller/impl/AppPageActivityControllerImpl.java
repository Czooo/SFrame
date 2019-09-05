package androidx.sframe.ui.controller.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.AppCompatNavHostController;
import androidx.fragment.app.FragmentActivity;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPageActivityControllerImpl extends AbsPageControllerImpl<FragmentActivity> {

	private AppCompatNavHostController mNavHostController;

	public AppPageActivityControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final FragmentActivity preFragmentActivity = this.getPageOwner();
		this.setViewModelStore(preFragmentActivity.getViewModelStore());
		this.setLifecycleOwner(preFragmentActivity);

		this.mNavHostController = this.createAppCompatNavHostController(savedInstanceState);

		if (savedInstanceState == null) {
			final View prePageView = this.onCreateView(LayoutInflater.from(AppPageControllerHelper.requireContext(this)), null, null);
			if (prePageView != null) {
				preFragmentActivity.setContentView(prePageView);
			}
		}
		this.onViewCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (this.mNavHostController != null) {
			this.mNavHostController.onSaveInstanceState(savedInstanceState);
		}
	}

	@NonNull
	@Override
	public final FragmentActivity getPageOwner() {
		return (FragmentActivity) this.getPageProvider();
	}

	@NonNull
	protected AppCompatNavHostController createAppCompatNavHostController(@Nullable Bundle savedInstanceState) {
		final AppCompatNavHostController mAppCompatNavHostController = new AppCompatNavHostController(this);
		mAppCompatNavHostController.setViewNavHostController(this.getPageOwner());
		mAppCompatNavHostController.restoreState(savedInstanceState);
		return mAppCompatNavHostController;
	}
}
