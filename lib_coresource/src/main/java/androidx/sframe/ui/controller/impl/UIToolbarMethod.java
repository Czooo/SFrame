package androidx.sframe.ui.controller.impl;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.helper.ViewModelProviders;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.lifecycle.ViewModel;

/**
 * Author create by ok on 2019-06-13
 * Email : ok@163.com.
 */
public abstract class UIToolbarMethod extends ViewModel implements UIToolbarController {

	private final UILayoutController mLayoutController;

	public UIToolbarMethod(@NonNull UILayoutController layoutController) {
		this.mLayoutController = layoutController;
	}

	@NonNull
	public final Context getContext() {
		return this.getViewController().getContext();
	}

	@NonNull
	public final UIViewMethod<View> findAt(@IdRes int id) {
		return this.getViewController().findAt(id);
	}

	@Nullable
	public final <V extends View> V findViewById(@IdRes int id) {
		return this.getViewController().findViewById(id);
	}

	@NonNull
	@Override
	public final UIViewController getViewController() {
		return this.getLayoutController().getViewController();
	}

	@NonNull
	@Override
	public final UILayoutController getLayoutController() {
		return this.mLayoutController;
	}

	@NonNull
	@Override
	public final AppToolbarMethod getToolbarMethod() {
		return this.get(AppToolbarMethod.class);
	}

	@NonNull
	@Override
	public final <Method extends UIToolbarMethod> Method get(Class<Method> method) {
		return ViewModelProviders.of(this.getLayoutController()).get(method);
	}
}
