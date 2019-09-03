package androidx.demon.http;

import androidx.annotation.NonNull;
import androidx.demon.tools.StringConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author create by ok on 2019-06-20
 * Email : ok@163.com.
 */
public class HttpServiceProvider {

	private final String mHostUrl;
	private final OkHttpClient mHttpClient;

	public HttpServiceProvider(@NonNull String hostUrl, @NonNull OkHttpClient client) {
		this.mHttpClient = client;
		this.mHostUrl = hostUrl;
	}

	public <Service> Service get(@NonNull Class<Service> serviceClass) {
		final Retrofit preRetrofit = new Retrofit.Builder()
				// 增加返回值为String的支持
				.addConverterFactory(StringConverterFactory.create())
				// 增加返回值为Gson实体的支持
				.addConverterFactory(GsonConverterFactory.create())
				// 增加返回值为Oservable<T>的支持
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.baseUrl(this.mHostUrl)
				.client(this.mHttpClient)
				.build();
		return preRetrofit.create(serviceClass);
	}
}
