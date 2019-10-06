package androidx.sframe.ui.controller.impl;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
abstract class AppPageFragmentControllerImpl<Page extends Fragment> extends AbsPageControllerImpl<Page> {

	AppPageFragmentControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
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
