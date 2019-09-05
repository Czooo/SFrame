package androidx.sframe.ui.controller;

import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public interface DialogFragmentPageController<Page> extends AppPageController<Page> {

	DialogFragmentPageController<Page> setWidth(int width);

	DialogFragmentPageController<Page> setHeight(int height);

	DialogFragmentPageController<Page> setGravity(int gravity);

	DialogFragmentPageController<Page> setAnimationStyle(@StyleRes int animationStyleResId);

	DialogFragmentPageController<Page> setBackgroundAlpha(@FloatRange(from = 0, to = 1.f) float alpha);

	DialogFragmentPageController<Page> setOnDismissListener(@NonNull OnDismissListener listener);

	DialogFragmentPageController<Page> show();

	DialogFragmentPageController<Page> show(@NonNull View anchor);

	DialogFragmentPageController<Page> showNow();

	DialogFragmentPageController<Page> showNow(@NonNull View anchor);

	interface OnDismissListener {

		void onDismiss();
	}
}
