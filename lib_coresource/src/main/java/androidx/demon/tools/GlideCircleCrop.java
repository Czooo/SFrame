package androidx.demon.tools;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * Author create by ok on 2018/3/1 0001
 * Email : ok@163.com.
 */

public class GlideCircleCrop extends BitmapTransformation {

	private static final int VERSION = 1;

	private static final String ID = GlideCircleCrop.class.getName() + VERSION;

	private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

	@Override
	protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
		return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof GlideCircleCrop;
	}

	@Override
	public int hashCode() {
		return ID.hashCode();
	}

	@Override
	public void updateDiskCacheKey(MessageDigest messageDigest) {
		messageDigest.update(ID_BYTES);
	}
}
