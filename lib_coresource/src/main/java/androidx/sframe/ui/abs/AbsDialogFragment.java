package androidx.sframe.ui.abs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.DialogFragmentPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.AppPageDialogFragmentControllerImpl;

/**
 * Author create by ok on 2019-06-03
 * Email : ok@163.com.
 */
public abstract class AbsDialogFragment extends AppCompatDialogFragment implements AppPageController.PageProvider {

	private final DialogFragmentPageController<AppCompatDialogFragment> mPageController;

	public AbsDialogFragment(@NonNull AppPageController<?> hostPageController) {
		this.mPageController = new AppPageDialogFragmentControllerImpl(this, hostPageController);
	}

	@CallSuper
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	public final AppNavController<AppCompatDialogFragment> getAppNavController() {
		return this.getPageController().getAppNavController();
	}

	@NonNull
	public final AppPageController<?> getHostPageController() {
		return this.getPageController().getHostPageController();
	}

	@NonNull
	public DialogFragmentPageController<AppCompatDialogFragment> getPageController() {
		return this.mPageController;
	}

	public void setOnDismissListener(@NonNull DialogFragmentPageController.OnDismissListener listener) {
		this.getPageController().setOnDismissListener(listener);
	}
}
