package androidx.sframe.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.sframe.R;
import androidx.sframe.manager.SFrameManager;

/**
 * 作者：Administrator on 2016/9/23 15:11
 * 邮箱：Zoran@kewaimiao.com
 */
public class ResCompat {

	@ColorInt
	public static int getColorAppTheme() {
		return getColorAppTheme(SFrameManager.getInstance().getContext());
	}

	@ColorInt
	public static int getColorAppTheme(@NonNull Context context) {
		return getColor(context, R.color.colorAppTheme);
	}

	@ColorInt
	public static int getColorAppBackground() {
		return getColorAppBackground(SFrameManager.getInstance().getContext());
	}

	@ColorInt
	public static int getColorAppBackground(@NonNull Context context) {
		return getColor(context, R.color.colorAppBackground);
	}

	@ColorInt
	public static int getColorAppStatus() {
		return getColorAppStatus(SFrameManager.getInstance().getContext());
	}

	@ColorInt
	public static int getColorAppStatus(@NonNull Context context) {
		return getColor(context, R.color.colorAppStatus);
	}

	@ColorInt
	public static int getColorAppToolbar() {
		return getColorAppToolbar(SFrameManager.getInstance().getContext());
	}

	@ColorInt
	public static int getColorAppToolbar(@NonNull Context context) {
		return getColor(context, R.color.colorAppToolbar);
	}

	@ColorInt
	public static int getColorAppNavigation() {
		return getColorAppNavigation(SFrameManager.getInstance().getContext());
	}

	@ColorInt
	public static int getColorAppNavigation(@NonNull Context context) {
		return getColor(context, R.color.colorAppNavigation);
	}

	@ColorInt
	public static int getColorAppLine() {
		return getColorAppLine(SFrameManager.getInstance().getContext());
	}

	@ColorInt
	public static int getColorAppLine(@NonNull Context context) {
		return getColor(context, R.color.colorAppLine);
	}

	@ColorInt
	public static int getColorTransparent() {
		return getColorTransparent(SFrameManager.getInstance().getContext());
	}

	@ColorInt
	public static int getColorTransparent(@NonNull Context context) {
		return getColor(context, R.color._00000000);
	}

	public static float dp2px(float dpValue) {
		return dp2px(SFrameManager.getInstance().getContext(), dpValue);
	}

	public static float sp2px(float spValue) {
		return sp2px(SFrameManager.getInstance().getContext(), spValue);
	}

	public static float dp2px(@NonNull Context context, float dpValue) {
		return unit2px(context, TypedValue.COMPLEX_UNIT_DIP, dpValue);
	}

	public static float sp2px(@NonNull Context context, float spValue) {
		return unit2px(context, TypedValue.COMPLEX_UNIT_SP, spValue);
	}

	public static float unit2px(@NonNull Context context, int unit, float unitValue) {
		return TypedValue.applyDimension(unit, unitValue, context.getResources().getDisplayMetrics());
	}

	public static Resources getResources() {
		return SFrameManager.getInstance().getContext().getResources();
	}

	public static Resources getResources(@NonNull Context context) {
		return context.getResources();
	}

	public static int getInteger(@IntegerRes int id) {
		return getResources().getInteger(id);
	}

	public static int getInteger(@NonNull Context context, @IntegerRes int id) {
		return getResources(context).getInteger(id);
	}

	public static float getDimension(@DimenRes int resId) {
		return getResources().getDimension(resId);
	}

	public static float getDimension(@NonNull Context context, @DimenRes int resId) {
		return getResources(context).getDimension(resId);
	}

	public static int getDimensionPixelOffset(@DimenRes int resId) {
		return getResources().getDimensionPixelOffset(resId);
	}

	public static int getDimensionPixelOffset(@NonNull Context context, @DimenRes int resId) {
		return getResources(context).getDimensionPixelOffset(resId);
	}

	public static String getString(@StringRes int resId) {
		return getResources().getString(resId);
	}

	public static String getString(@NonNull Context context, @StringRes int resId) {
		return getResources(context).getString(resId);
	}

	public static String getString(@StringRes int resId, Object... formatArgs) {
		return getResources().getString(resId, formatArgs);
	}

	public static String getString(@NonNull Context context, @StringRes int resId, Object... formatArgs) {
		return getResources(context).getString(resId, formatArgs);
	}

	public static int getColor(@ColorRes int resId) {
		return getColor(SFrameManager.getInstance().getContext(), resId);
	}

	public static int getColor(@NonNull Context context, @ColorRes int resId) {
		return ContextCompat.getColor(context, resId);
	}

	public static Drawable getDrawable(@DrawableRes int resId) {
		return getDrawable(SFrameManager.getInstance().getContext(), resId);
	}

	public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
		return ContextCompat.getDrawable(context, resId);
	}
}
