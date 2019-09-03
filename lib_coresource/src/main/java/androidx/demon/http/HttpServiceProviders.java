package androidx.demon.http;

import androidx.annotation.NonNull;
import androidx.demon.compat.LogCompat;
import androidx.demon.listener.OnHttpProgressListener;
import androidx.demon.tools.HttpCacheInterceptor;
import androidx.demon.tools.HttpHeaderInterceptor;
import androidx.demon.tools.HttpLoggerInterceptor;
import androidx.demon.tools.HttpProgressInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author create by ok on 2019-06-20
 * Email : ok@163.com.
 */
public class HttpServiceProviders {

	public static HttpServiceProvider of(@NonNull String hostUrl) {
		return of(hostUrl, getInternalClient());
	}

	public static HttpServiceProvider of(@NonNull String hostUrl, @NonNull OkHttpClient client) {
		return new HttpServiceProvider(hostUrl, client);
	}

	private static volatile OkHttpClient internalClient;

	public static OkHttpClient getInternalClient() {
		if (internalClient == null) {
			synchronized (HttpServiceProviders.class) {
				if (internalClient == null) {

					HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor();
					mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

					internalClient = new OkHttpClient.Builder()
							.retryOnConnectionFailure(true)
//							.readTimeout(CoreConfig.TIME_OUT_READ, TimeUnit.SECONDS)
//							.connectTimeout(CoreConfig.TIME_OUT_CONNECT, TimeUnit.SECONDS)
//							.connectionPool(new ConnectionPool(CoreConfig.MAX_IDLE_CONNECT, CoreConfig.TIME_KEEP_ALIVE, TimeUnit.SECONDS))
//							.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(CoreCompat.getContext())))
//							.cache(new Cache(new File(FileCompat.getCacheFilePath()), CoreConfig.MAX_CACHE_SIZE))
							/*
							 * addInterceptor 和 addNetworkInterceptor 区别：
							 * 1.前者作用于Application和OkhttpCore之间，后者作用于OkhttpCore和Network之间。
							 * 2.前者只会被执行一次，后者会执行两次。
							 * 3.前者不需要考虑重定向等问题，后者根据需求操作重定向。
							 * 4.后者可以获取 Connection 携带的请求信息。
							 * 5.前者关注原始的request，而不关心注入的headers。
							 * */
							// 客户端拦截
							.addInterceptor(mHttpLoggingInterceptor)
							.addInterceptor(HttpCacheInterceptor.create())
							.addInterceptor(HttpLoggerInterceptor.create())
							.addInterceptor(HttpHeaderInterceptor.create())
							// 服务端拦截
							.addNetworkInterceptor(HttpProgressInterceptor.create(new OnHttpProgressListener() {

								@Override
								public void onHttpProgress(String url, long contentLength, long readSoFar) {
									LogCompat.e(url + " : " + contentLength + " = " + readSoFar);
								}
							}))
							.build();
				}
			}
		}
		return internalClient;
	}
}
