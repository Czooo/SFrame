package androidx.sframe.tools;

import android.annotation.SuppressLint;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.sframe.compat.FileCompat;

/**
 * Author create by ok on 2019/2/5
 * Email : ok@163.com.
 */
@GlideModule
@SuppressLint("CheckResult")
public final class ImageAppGlideModule extends AppGlideModule {

	@Override
	public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
		super.applyOptions(context, builder);
		// 未捕获异常策略
		UncaughtThrowableStrategy mUncaughtThrowableStrategy = new UncaughtThrowableStrategy();
		// 分配内存缓存机制
		MemorySizeCalculator mMemorySizeCalculator = new MemorySizeCalculator.Builder(context)
//				.setLowMemoryMaxSizeMultiplier()
				// 缓存2个屏幕的图片
				.setMemoryCacheScreens(2)
//				.setBitmapPoolScreens()
//				.setMaxSizeMultiplier()
//				.setArrayPoolSize()
				.build();

		builder.setMemorySizeCalculator(mMemorySizeCalculator)
				// 日志级别
				.setLogLevel(Log.DEBUG)
				// 默认请求选项
				.setDefaultRequestOptions(getRequestOptions())
				// 数组缓存池
				.setArrayPool(new LruArrayPool(mMemorySizeCalculator.getArrayPoolSizeInBytes()))
				// Bitmap 池
				.setBitmapPool(new LruBitmapPool(mMemorySizeCalculator.getBitmapPoolSize()))
				// 内存缓存
				.setMemoryCache(new LruResourceCache(mMemorySizeCalculator.getMemoryCacheSize()))
				// 磁盘缓存
				.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(mUncaughtThrowableStrategy))
				// 未捕获异常策略
				.setSourceExecutor(GlideExecutor.newSourceExecutor(mUncaughtThrowableStrategy))
				.setDiskCache(new DiskLruCacheFactory(FileCompat.getCacheImagePath(), DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
	}

	@Override
	public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
		super.registerComponents(context, glide, registry);
		// 高清
		glide.setMemoryCategory(MemoryCategory.HIGH);
		// 当内存比较低时, 释放一些不必要的资源
		glide.trimMemory(ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW);
		// 替换网络加载程序
		registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
	}

	@Override
	public boolean isManifestParsingEnabled() {
		// 禁止解析Manifest文件，提升初始化速度，避免一些潜在错误
		return false;
	}

	private RequestOptions getRequestOptions() {
		RequestOptions mRequestOptions = new RequestOptions();
		mRequestOptions
				// 定制缓存刷新策略
//				.signature(new ObjectKey())
				// error 请求图片加载错误的占位符
//				.error(R.mipmap.icon_photo_default)
				.error(new ColorDrawable(Color.GRAY))
				// Fallback 请求url/model为空的占位符
//				.fallback(R.mipmap.icon_photo_default)
				.fallback(new ColorDrawable(Color.GRAY))
				// placeholder 请求图片加载中的占位符
//				.placeholder(R.mipmap.icon_photo_default)
				.placeholder(new ColorDrawable(Color.GRAY))
				// 磁盘缓存策略
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				// 高清图片
//				.priority(Priority.HIGH)
				// 仅从缓存加载图片(比如省流量模式)
//				.onlyRetrieveFromCache(true)
				// 跳过缓存(比如图片验证码)
//				.skipMemoryCache(true)
				// 60秒请求超时
				.timeout(60_000)
				// 中间截取图片
//				.centerCrop()
				// scaleType 不自动切换(默认自动切换View的ScaleType)
//				.dontTransform()
				// 不启用动画
				.dontAnimate();
		return mRequestOptions;
	}

	private class UncaughtThrowableStrategy implements GlideExecutor.UncaughtThrowableStrategy {

		@Override
		public void handle(Throwable throwable) {

		}
	}
}
