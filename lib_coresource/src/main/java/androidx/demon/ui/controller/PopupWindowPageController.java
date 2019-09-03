package androidx.demon.ui.controller;

import androidx.annotation.AnimRes;
import androidx.annotation.FloatRange;
import androidx.annotation.StyleRes;

/**
 * Author create by ok on 2019-06-19
 * Email : ok@163.com.
 */
public interface PopupWindowPageController<Page> extends AppPageController<Page> {

	PopupWindowPageController<Page> setAnimationStyle(@StyleRes int animationStyle);

	PopupWindowPageController<Page> setCustomAnimation(@AnimRes int enterAnimResId, @AnimRes int exitAnimResId);

	/**
	 * 非全屏透明度(结合Margin)
	 * */
	PopupWindowPageController<Page> setBackgroundViewAlpha(@FloatRange(from = 0, to = 1.f) float alpha);

	/**
	 * 全屏透明度
	 * */
	PopupWindowPageController<Page> setBackgroundAlpha(@FloatRange(from = 0, to = 1.f) float alpha);

	PopupWindowPageController<Page> setMarginLeft(int marginLeft);

	PopupWindowPageController<Page> setMarginTop(int marginTop);

	PopupWindowPageController<Page> setMarginRight(int marginRight);

	PopupWindowPageController<Page> setMarginBottom(int marginBottom);
}
