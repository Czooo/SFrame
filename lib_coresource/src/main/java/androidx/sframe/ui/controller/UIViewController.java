package androidx.sframe.ui.controller;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.impl.UIViewMethod;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public interface UIViewController {

	<V extends View> V getView();

	<V extends View> V set(@NonNull Class<V> viewClass);

	<V extends View> V findViewById(@IdRes int id);

	Context getContext();

	UIViewMethod<View> findAtParent();

	UIViewMethod<View> findAt(@IdRes int id);

	UIViewMethod<View> findAt(@NonNull View preView);

	UIViewController addOnFindViewListener(@NonNull OnFindViewListener listener);

	UIViewController removeOnFindViewListener(@NonNull OnFindViewListener listener);

	UIViewController post(@NonNull Runnable runnable);

	UIViewController postDelayed(@NonNull Runnable runnable, long delayMillis);

	UIViewController gc();

	UIViewController gc(@Nullable View preView);

	UIViewController recycle();

	interface OnFindViewListener {

		<T extends View> T findViewById(@IdRes int id);
	}
}
