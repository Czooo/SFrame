package androidx.sframe.ui.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public interface AppPageController<Page> extends LifecycleOwner, ViewModelStoreOwner {

	void onCreate(@Nullable Bundle savedInstanceState);

	View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

	void onViewCreated(@Nullable Bundle savedInstanceState);

	void onSaveInstanceState(@NonNull Bundle savedInstanceState);

	@Nullable
	Context getContext();

	@NonNull
	Context requireContext();

	@Nullable
	FragmentActivity getFragmentActivity();

	@NonNull
	FragmentActivity requireFragmentActivity();

	@NonNull
	Page getPageOwner();

	@Nullable
	View getPageView();

	@NonNull
	View requirePageView();

	@NonNull
	UIViewController getViewController();

	@NonNull
	UILayoutController getLayoutController();

	@NonNull
	UIToolbarController getToolbarController();

	@NonNull
	AppNavController<Page> getNavController();

	interface PageProvider {

		@LayoutRes
		int onPageLayoutId(@Nullable Bundle savedInstanceState);

		void onPageViewCreated(@Nullable Bundle savedInstanceState);

		void onPageDataSourceChanged(@Nullable Object params);
	}

	interface PageViewProvider {

		@Nullable
		View onPageCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
	}
}
