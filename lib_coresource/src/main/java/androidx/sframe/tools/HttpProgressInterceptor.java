package androidx.sframe.tools;

import com.bumptech.glide.util.Preconditions;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.sframe.listener.OnHttpProgressListener;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Author create by ok on 2019/2/14
 * Email : ok@163.com.
 */
public class HttpProgressInterceptor implements Interceptor {

	public static HttpProgressInterceptor create(OnHttpProgressListener listener) {
		return new HttpProgressInterceptor(listener);
	}

	private final OnHttpProgressListener mHttpProgressListener;

	HttpProgressInterceptor(OnHttpProgressListener listener) {
		this.mHttpProgressListener = listener;
	}

	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request request = chain.request();
		Response proceed = chain.proceed(request);
		if (proceed.isSuccessful()) {
			String url = request.url().toString();
			ResponseBody body = proceed.body();
			long contentLength = Preconditions.checkNotNull(body).contentLength();
			mHttpProgressListener.onHttpProgress(url, contentLength, 0);
			return proceed.newBuilder().body(new ProgressResponseBody(url, body, contentLength)).build();
		}
		return proceed;
	}

	final class ProgressResponseBody extends ResponseBody {

		final ResponseBody body;
		final BufferedSource source;

		ProgressResponseBody(String url, @NonNull ResponseBody body, long contentLength) {
			this.body = body;
			this.source = Okio.buffer(new ProgressSource(url, body.source(), contentLength));
		}

		@Override
		public MediaType contentType() {
			return body.contentType();
		}

		@Override
		public long contentLength() {
			return body.contentLength();
		}

		@Override
		public BufferedSource source() {
			return source;
		}
	}

	final class ProgressSource extends ForwardingSource {

		String url;
		long contentLength;
		long readSoFar;

		ProgressSource(String url, Source delegate, long contentLength) {
			super(delegate);
			this.url = url;
			this.contentLength = contentLength;
		}

		@Override
		public long read(@NonNull Buffer sink, long byteCount) throws IOException {
			long read = super.read(sink, byteCount);
			if (read != -1) {
				readSoFar += read;
				mHttpProgressListener.onHttpProgress(url, contentLength, readSoFar);
			}
			return read;
		}
	}
}
