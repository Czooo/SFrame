package androidx.sframe.ui.controller.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelStore;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPageActivityControllerImpl extends AbsPageControllerImpl<FragmentActivity> {

	public AppPageActivityControllerImpl(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			final View prePageView = this.onCreateView(LayoutInflater.from(AppPageControllerHelper.requireContext(this)), null, null);
			if (prePageView != null) {
				this.getPageOwner().setContentView(prePageView);
			}
		}
		this.onViewCreated(savedInstanceState);
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
