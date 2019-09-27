package androidx.sframe.ui.controller.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;
import androidx.sframe.ui.controller.AppNavController;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPageActivityControllerImpl extends AbsPageControllerImpl<FragmentActivity> {

	private AppNavController<FragmentActivity> mAppNavController;

	public AppPageActivityControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mAppNavController = new AppNavControllerImpl<>(this);
		this.mAppNavController.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState == null) {
			final View prePageView = this.onCreateView(LayoutInflater.from(AppPageControllerHelper.requireContext(this)), null, null);
			if (prePageView != null) {
				this.getPageOwner().setContentView(prePageView);
			}
		}
		this.onViewCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (this.mAppNavController != null) {
			this.mAppNavController.onRestoreInstanceState(savedInstanceState);
		}
	}

	@NonNull
	@Override
	public AppNavController<FragmentActivity> getNavController() {
		return this.mAppNavController;
	}

	@NonNull
	@Override
	public final FragmentActivity getPageOwner() {
		return (FragmentActivity) this.getPageProvider();
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
