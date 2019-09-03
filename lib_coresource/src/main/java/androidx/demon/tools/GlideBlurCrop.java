package androidx.demon.tools;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.demon.compat.CoreCompat;

/**
 * Author create by ok on 2018/3/1 0001
 * Email : ok@163.com.
 */

public class GlideBlurCrop extends BitmapTransformation {

	private static final int VERSION = 1;

	private static final String ID = GlideRoundCrop.class.getName() + VERSION;

	private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

	private RenderScript rs;

	private int blur;

	public GlideBlurCrop(int blur) {
		this.blur = blur;
		// 创建RenderScript内核对象
		this.rs = RenderScript.create(CoreCompat.getContext());
	}

	@Override
	protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
		return blurBitmap(toTransform, blur, outWidth, outHeight);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof GlideBlurCrop;
	}

	@Override
	public int hashCode() {
		return ID.hashCode();
	}

	@Override
	public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
		messageDigest.update(ID_BYTES);
	}

	/**
	 * @param toTransform 需要模糊的图片
	 * @param outWidth    输入出的宽度
	 * @param outHeight   输出的高度
	 * @return 模糊处理后的Bitmap
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private Bitmap blurBitmap(Bitmap toTransform, @FloatRange(from = 0, to = 25) float blurRadius, int outWidth, int outHeight) {
		// 将缩小后的图片做为预渲染的图片
		Bitmap inputBitmap = Bitmap.createScaledBitmap(toTransform, outWidth, outHeight, false);
		// 创建一张渲染后的输出图片
		Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
		// 创建一个模糊效果的RenderScript的工具对象
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		// 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
		// 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
		Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
		Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
		// 设置渲染的模糊程度, 25f是最大模糊度
		blurScript.setRadius(blurRadius);
		// 设置blurScript对象的输入内存
		blurScript.setInput(tmpIn);
		// 将输出数据保存到输出内存中
		blurScript.forEach(tmpOut);
		// 将数据填充到Allocation中
		tmpOut.copyTo(outputBitmap);
		return outputBitmap;
	}
}
