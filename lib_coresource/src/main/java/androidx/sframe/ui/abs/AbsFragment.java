package androidx.sframe.ui.abs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.sframe.manager.PageCacheManager;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.AppPageFragmentControllerImpl2;
import androidx.sframe.utils.AppNavigator;

/**
 * Author create by ok on 2019-06-03
 * Email : ok@163.com.
 */
public abstract class AbsFragment extends Fragment implements AppPageController.PageProvider {

	private AppPageController<Fragment> mPageController;

	@CallSuper
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PageCacheManager.getInstance()
				.getFragmentPageCache()
				.put(this);
		this.getPageController().onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return this.getPageController().onCreateView(inflater, container, savedInstanceState);
	}

	@CallSuper
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.getPageController().onViewCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		this.getPageController().onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		PageCacheManager.getInstance()
				.getFragmentPageCache()
				.remove(this);
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params) {

	}

	@NonNull
	public final UIViewController getViewController() {
		return this.getPageController().getViewController();
	}

	@NonNull
	public final UILayoutController getLayoutController() {
		return this.getPageController().getLayoutController();
	}

	@NonNull
	public final UIToolbarController getToolbarController() {
		return this.getPageController().getToolbarController();
	}

	@NonNull
	public final AppNavController<Fragment> getNavController() {
		return this.getPageController().getNavController();
	}

	@NonNull
	public final AppNavController<FragmentActivity> getSupportNavController() {
		return AppNavigator.findSupportNavController(this.getPageController());
	}

	@NonNull
	public AppPageController<Fragment> getPageController() {
		if (this.mPageController == null) {
			this.mPageController = new AppPageFragmentControllerImpl2(this);
		}
		return this.mPageController;
	}
}
