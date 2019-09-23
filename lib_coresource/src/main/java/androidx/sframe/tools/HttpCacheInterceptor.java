package androidx.sframe.tools;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.sframe.utils.NetCompat;
import androidx.sframe.utils.Logger;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author create by ok on 2018/6/6 0006
 * Email : ok@163.com.
 */
public class HttpCacheInterceptor implements Interceptor {

	public static HttpCacheInterceptor create() {
		return new HttpCacheInterceptor();
	}

	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request mRequest = chain.request();

		if (NetCompat.isConnected()) {
//			return chain.proceed(mRequest).newBuilder()
//					.removeHeader("Pragma")
//					.removeHeader("Cache-Control")
//					.header("Cache-Control", "public, max-age=" + 0)
//					.build();

			// 有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
			final String cacheControl = mRequest.cacheControl().toString();
			Logger.e("RequestCacheInterceptor: " + cacheControl);

			return chain.proceed(mRequest).newBuilder()
					.header("Cache-Control", cacheControl)
					.removeHeader("Pragma")
					.build();
		} else {
			mRequest = mRequest.newBuilder()
					.cacheControl(CacheControl.FORCE_CACHE)
					.build();

			return chain.proceed(mRequest).newBuilder()
					.removeHeader("Pragma")
					.removeHeader("Cache-Control")
					.header("Cache-Control", "public, only-if-cached, max-stale=2419200, max-age=640000")
					.build();
		}
	}
}
