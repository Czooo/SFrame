package androidx.sframe.tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.compat.LogCompat;
import androidx.sframe.compat.RxCompat;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Author create by ok on 2019-06-15
 * Email : ok@163.com.
 */
public final class AsyncRequest {

	public static AsyncRequest get() {
		return new AsyncRequest();
	}

	private Disposable mDisposable;

	AsyncRequest() {

	}

	public synchronized <Params> void execute(@Nullable Params params, @NonNull final Callback<Params> callback) {
		if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
			return;
		}
		this.mDisposable = Observable.just(new Data<>(params))
				.map(new Function<Data<Params>, Data<Params>>() {
					@Override
					public Data<Params> apply(Data<Params> data) throws Exception {
						synchronized (AsyncRequest.this) {
							callback.doInBackground(data.params);
						}
						return data;
					}
				})
				.compose(RxCompat.<Data<Params>>rxScheduler())
				.subscribe(new Consumer<Data<Params>>() {
					@Override
					public void accept(Data<Params> data) throws Exception {
						synchronized (AsyncRequest.this) {
							callback.onPostExecute(data.params);
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						LogCompat.e(throwable);
					}
				});
	}

	public synchronized void cancel() {
		if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
			this.mDisposable.dispose();
		}
	}

	public interface Callback<Params> {

		void doInBackground(@Nullable Params params);

		void onPostExecute(@Nullable Params params);
	}

	public static abstract class SimpleCallback<Params> implements Callback<Params> {

		@Override
		public void onPostExecute(@Nullable Params params) {
			// no-op
		}
	}

	private static final class Data<Params> {

		public Params params;

		Data(@Nullable Params params) {
			this.params = params;
		}
	}
}
