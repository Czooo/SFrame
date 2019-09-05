package androidx.sframe.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public interface AppPageController<Page> extends LifecycleOwner, ViewModelStoreOwner {

	void onCreate(@Nullable Bundle savedInstanceState);

	@Nullable
	View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

	void onViewCreated(@Nullable Bundle savedInstanceState);

	void onSaveInstanceState(@NonNull Bundle savedInstanceState);

	void setLifecycleOwner(@NonNull LifecycleOwner owner);

	void setViewModelStore(@NonNull ViewModelStore viewModelStore);

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
	AppNavController<Page> getAppNavController();

	interface PageProvider {

		int onPageLayoutId(@Nullable Bundle savedInstanceState);

		void onPageViewCreated(@Nullable Bundle savedInstanceState);

		void onPageDataSourceChanged(@Nullable Object params);
	}

	interface WindowPageProvider extends PageProvider {

		@NonNull
		AppPageController<?> getHostPageController();
	}

	interface PageViewInterface {

		@Nullable
		View onPageCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
	}

	interface ContentViewInterface {

	}
}
