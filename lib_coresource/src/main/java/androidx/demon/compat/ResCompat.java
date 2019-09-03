package androidx.demon.compat;

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
import androidx.demon.R;


/**
 * 作者：Administrator on 2016/9/23 15:11
 * 邮箱：Zoran@kewaimiao.com
 */
public class ResCompat {

	@ColorInt
	public static int getColorAppTheme() {
		return getColorAppTheme(CoreCompat.getContext());
	}

	@ColorInt
	public static int getColorAppTheme(@NonNull Context context) {
		return getColor(context, R.color.colorAppTheme);
	}

	@ColorInt
	public static int getColorAppBackground() {
		return getColorAppBackground(CoreCompat.getContext());
	}

	@ColorInt
	public static int getColorAppBackground(@NonNull Context context) {
		return getColor(context, R.color.colorAppBackground);
	}

	@ColorInt
	public static int getColorAppStatus() {
		return getColorAppStatus(CoreCompat.getContext());
	}

	@ColorInt
	public static int getColorAppStatus(@NonNull Context context) {
		return getColor(context, R.color.colorAppStatus);
	}

	@ColorInt
	public static int getColorAppToolbar() {
		return getColorAppToolbar(CoreCompat.getContext());
	}

	@ColorInt
	public static int getColorAppToolbar(@NonNull Context context) {
		return getColor(context, R.color.colorAppToolbar);
	}

	@ColorInt
	public static int getColorAppNavigation() {
		return getColorAppNavigation(CoreCompat.getContext());
	}

	@ColorInt
	public static int getColorAppNavigation(@NonNull Context context) {
		return getColor(context, R.color.colorAppNavigation);
	}

	@ColorInt
	public static int getColorAppLine() {
		return getColorAppLine(CoreCompat.getContext());
	}

	@ColorInt
	public static int getColorAppLine(@NonNull Context context) {
		return getColor(context, R.color.colorAppLine);
	}

	@ColorInt
	public static int getColorTransparent() {
		return getColorTransparent(CoreCompat.getContext());
	}

	@ColorInt
	public static int getColorTransparent(@NonNull Context context) {
		return getColor(context, R.color._00000000);
	}

	public static float dp2px(float dpValue) {
		return dp2px(CoreCompat.getContext(), dpValue);
	}

	public static float sp2px(float spValue) {
		return sp2px(CoreCompat.getContext(), spValue);
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
		return CoreCompat.getContext().getResources();
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
		return getColor(CoreCompat.getContext(), resId);
	}

	public static int getColor(@NonNull Context context, @ColorRes int resId) {
		return ContextCompat.getColor(context, resId);
	}

	public static Drawable getDrawable(@DrawableRes int resId) {
		return getDrawable(CoreCompat.getContext(), resId);
	}

	public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
		return ContextCompat.getDrawable(context, resId);
	}

//	public static List<LocationModel> findCityListByAssets(String assetsName) {
//		List<LocationModel> mList = new ArrayList<>();
//		try {
//			AssetManager mAssetManager = CoreCompat.getContext().getAssets();
//			InputStream mInputStream = mAssetManager.open(assetsName);
//			byte[] buffer = new byte[mInputStream.available()];
//			mInputStream.read(buffer);
//			String result = new String(buffer, "utf-8");
//			mInputStream.close();
//			List<LocationModel> resultList = new Gson().fromJson(result, new TypeToken<List<LocationModel>>() {
//			}.getType());
//			mList.addAll(resultList);
//			return mList;
//		} catch (Exception e) {
//			return mList;
//		}
//	}
}
