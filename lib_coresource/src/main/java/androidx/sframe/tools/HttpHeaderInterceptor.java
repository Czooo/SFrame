package androidx.sframe.tools;

import android.os.Build;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author create by ok on 2019/2/14
 * Email : ok@163.com.
 */
public class HttpHeaderInterceptor implements Interceptor {

	public static HttpHeaderInterceptor create() {
		return new HttpHeaderInterceptor();
	}

	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request request = chain.request();
		Response proceed = chain.proceed(request);
		return proceed.newBuilder()
				.header("X-Oc-TimeStamp", String.valueOf(System.currentTimeMillis()))
				.header("X-Oc-Device-IModel", Build.MODEL)
				.header("X-Oc-Os-IModel", "Android " + Build.VERSION.RELEASE)
//				.header("X-Oc-App-Bundle", SystemCompat.getPackageName())
//				.header("X-Oc-App-Version", SystemCompat.getVersionName())
				.build();
//		return chain.proceed(request.newBuilder()
//				.header("X-Oc-TimeStamp", String.valueOf(System.currentTimeMillis()))
//				.header("X-Oc-Device-IModel", Build.MODEL)
//				.header("X-Oc-Os-IModel", "Android " + Build.VERSION.RELEASE)
//				.header("X-Oc-App-Bundle", CoreCompat.getPackageName())
//				.header("X-Oc-App-Version", CoreCompat.getVersionName())
//				.build());
	}
}
