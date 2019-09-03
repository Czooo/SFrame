package androidx.demon.ui.controller;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.demon.ui.controller.impl.AppToolbarMethod;
import androidx.demon.ui.controller.impl.UIToolbarMethod;
import androidx.demon.ui.controller.impl.UIViewMethod;

/**
 * Author create by ok on 2019-06-13
 * Email : ok@163.com.
 */
public interface UIToolbarController {

	Context getContext();

	<V extends View> V findViewById(@IdRes int id);

	UIViewMethod<View> findAt(@IdRes int id);

	UIViewController getViewController();

	UILayoutController getLayoutController();

	AppToolbarMethod getToolbarMethod();

	<Method extends UIToolbarMethod> Method get(Class<Method> method);
}
