package androidx.sframe.tools;

import android.annotation.SuppressLint;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;

/**
 * Author create by ok on 2019/2/5
 * Email : ok@163.com.
 */
@GlideExtension
@SuppressLint("CheckResult")
public final class ImageGlideExtension {

	private ImageGlideExtension() {
		// 此方法必须
	}

	@GlideOption
	public static void roundCrop(RequestOptions options, int radius) {
//		options.downsample(DownsampleStrategy.CENTER_INSIDE)
//				.transform(new GlideRoundCrop(radius));
		options.transform(new MultiTransformation<>(new CenterInside(), new GlideRoundCrop(radius)));
	}

	@GlideOption
	public static void blurCrop(RequestOptions options, int blur) {
//		options.downsample(DownsampleStrategy.CENTER_INSIDE)
//				.transform(new GlideBlurCrop(blur));
		options.transform(new MultiTransformation<>(new CenterInside(), new GlideBlurCrop(blur)));
	}

	public static void test(RequestOptions options) {
		// transition : (过渡选项)
//		BitmapTransitionOptions
//		DrawableTransitionOptions

		// apply :
//		RequestOptions.fitCenterTransform();
//		RequestOptions.circleCropTransform();

	}

//	private static final RequestOptions DECODE_TYPE_GIF = decodeTypeOf(GifDrawable.class).lock();
//
//	@GlideType(GifDrawable.class)
//	public static void asGif(RequestBuilder<GifDrawable> requestBuilder) {
//		requestBuilder
//				.transition(new DrawableTransitionOptions())
//				.apply(DECODE_TYPE_GIF);
//	}
}
