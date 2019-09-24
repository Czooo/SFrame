package androidx.sframe.ui.controller;

import androidx.annotation.FloatRange;

/**
 * Author create by ok on 2019-06-19
 * Email : ok@163.com.
 */
public interface PopupWindowPageController<Page> extends AppPageController<Page> {

	// 全屏透明度
	PopupWindowPageController<Page> setWindowBackgroundAlpha(@FloatRange(from = 0, to = 1.f) float alpha);
}
