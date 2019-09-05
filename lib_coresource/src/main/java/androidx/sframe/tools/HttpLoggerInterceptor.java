package androidx.sframe.tools;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.sframe.compat.LogCompat;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author create by ok on 2019/2/14
 * Email : ok@163.com.
 */
public class HttpLoggerInterceptor implements Interceptor {

	public static HttpLoggerInterceptor create() {
		return new HttpLoggerInterceptor();
	}

	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request request = chain.request();
		StringBuilder builder = new StringBuilder();
		builder
				.append("Http Request Start ========================================\n")
				.append("Request URL : " + request.url().toString() + "\n")
				.append("Request Header : " + request.headers().toString() + "\n")
				.append("Request Method : " + request.method() + "\n")
				.append("Request : " + request.toString() + "\n")
				.append("Http Request End ========================================\n");
		LogCompat.e(builder.toString());
		return chain.proceed(request);
	}
}
