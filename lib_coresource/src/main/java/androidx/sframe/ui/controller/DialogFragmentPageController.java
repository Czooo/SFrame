package androidx.sframe.ui.controller;

import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public interface DialogFragmentPageController<Page> extends AppPageController<Page> {

	DialogFragmentPageController<Page> setGravity(int gravity);

	DialogFragmentPageController<Page> setAnimationStyle(@StyleRes int animationStyleResId);

	DialogFragmentPageController<Page> setWindowBackgroundAlpha(@FloatRange(from = 0, to = 1.f) float alpha);

	DialogFragmentPageController<Page> setOnKeyDownListener(@NonNull OnKeyDownListener listener);

	DialogFragmentPageController<Page> setOnDismissListener(@NonNull OnDismissListener listener);

	DialogFragmentPageController<Page> show();

	DialogFragmentPageController<Page> show(@NonNull View anchor);

	DialogFragmentPageController<Page> showNow();

	DialogFragmentPageController<Page> showNow(@NonNull View anchor);

	interface OnKeyDownListener {

		boolean onKeyDown(@NonNull DialogFragment dialogFragment, int keyCode, @NonNull KeyEvent event);
	}

	interface OnDismissListener {

		void onDismiss();
	}
}
