package androidx.sframe.ui.controller;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.impl.UIViewMethod;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public interface UIViewMethodController<PreView extends View> {

	PreView getPreView();

	<V extends View> V set(@NonNull Class<V> viewClass);

	<V extends View> V findViewById(@IdRes int id);

	<V extends View> V findChildViewById(@IdRes int id);

	Context getContext();

	UIViewController getViewController();

	UIViewMethod<View> findAtParent();

	UIViewMethod<View> findAt(@IdRes int id);

	UIViewMethod<View> findAt(@NonNull View preView);

	UIViewMethod<View> findChildAt(@IdRes int id);

	UIViewMethod<View> findChildAt(@NonNull View preView);

	<Method extends UIViewMethod<V>, V extends View> Method get(@NonNull Class<Method> methodClass);

	UIViewMethodController<PreView> recycle();
}
