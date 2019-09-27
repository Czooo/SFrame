package androidx.sframe.ui.controller.impl;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;
import androidx.sframe.ui.controller.AppNavController;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
abstract class AppPageFragmentControllerImpl<Page extends Fragment> extends AbsPageControllerImpl<Page> {

	private AppNavController<Page> mAppNavController;

	AppPageFragmentControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mAppNavController = new AppNavControllerImpl<>(this);
		this.mAppNavController.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (this.mAppNavController != null) {
			this.mAppNavController.onSaveInstanceState(savedInstanceState);
		}
	}

	@NonNull
	@Override
	public AppNavController<Page> getNavController() {
		return this.mAppNavController;
	}

	/**
	 * Returns the Lifecycle of the provider.
	 *
	 * @return The lifecycle of the provider.
	 */
	@NonNull
	@Override
	public Lifecycle getLifecycle() {
		return this.getPageOwner().getLifecycle();
	}

	/**
	 * Returns owned {@link ViewModelStore}
	 *
	 * @return a {@code ViewModelStore}
	 */
	@NonNull
	@Override
	public ViewModelStore getViewModelStore() {
		return this.getPageOwner().getViewModelStore();
	}
}
