package androidx.demon.compat;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Author create by ok on 2018/5/31 0031
 * Email : ok@163.com.
 */

public class RxCompat {

	/**
	 * 统一线程处理
	 * AndroidSchedulers.mainThread() 主线程
	 * Schedulers.immediate() 当前线程，即默认Scheduler
	 * Schedulers.newThread() 启用新线程
	 * Schedulers.io() IO线程，内部是一个数量无上限的线程池，可以进行文件、数据库和网络操作。
	 * Schedulers.computation() CPU计算用的线程，内部是一个数目固定为CPU核数的线程池，适合于CPU密集型计算，不能操作文件、数据库和网络。
	 */
	public static <T> ObservableTransformer<T, T> rxScheduler() {
		return new ObservableTransformer<T, T>() {

			@Override
			public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
				return upstream.subscribeOn(Schedulers.io())
						.unsubscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread());
			}
		};
	}

//	public static Function<? super Observable<? extends Throwable>, ? extends Observable<?>> rxRetryWhen() {
//		return new Function<Observable<? extends Throwable>, Observable<?>>() {
//
//			@Override
//			public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
//				return observable.zipWith(Observable.range(1, CoreConfig.MAX_RETRY_COUNT + 1), new BiFunction<Throwable, Integer, WrapperModel>() {
//
//					@Override
//					public WrapperModel apply(@NonNull Throwable throwable, @NonNull Integer integer) throws Exception {
//						return new WrapperModel(throwable, integer);
//					}
//				}).flatMap(new Function<WrapperModel, ObservableSource<?>>() {
//
//					@Override
//					public ObservableSource<?> apply(WrapperModel wrapperModel) throws Exception {
//						/**
//						 * 需求1：根据异常类型选择是否重试
//						 * 即，当发生的异常 = 网络异常 = IO异常 才选择重试
//						 */
//						Throwable mThrowable = wrapperModel.getThrowable();
//
//						/**
//						 * 需求2：限制重试次数
//						 * 即，当已重试次数 < 设置的重试次数，才选择重试
//						 */
//						if ((TimeoutException.class.isInstance(mThrowable) ||
//								ConnectException.class.isInstance(mThrowable) ||
//								UnknownHostException.class.isInstance(mThrowable) ||
//								SocketTimeoutException.class.isInstance(mThrowable)) &&
//								wrapperModel.getRetryIndex() < CoreConfig.MAX_RETRY_COUNT + 1) {
//							/**
//							 * 需求3：实现重试
//							 * 通过返回的Observable发送的事件 = Next事件，从而使得retryWhen（）重订阅，最终实现重试功能
//							 *
//							 * 需求4：延迟1段时间再重试
//							 * 采用delay操作符 = 延迟一段时间发送，以实现重试间隔设置
//							 *
//							 * 需求5：遇到的异常越多，时间越长
//							 * 在delay操作符的等待时间内设置 = 每重试1次，增多延迟重试时间1s
//							 */
//							return Observable.timer(CoreConfig.MAX_RETRY_DELAY + (wrapperModel.getRetryIndex() - 1) * 1000, TimeUnit.MILLISECONDS);
//						} else {
//							return Observable.error(mThrowable);
//						}
//					}
//				});
//			}
//		};
//	}
//
//	public static <T> Consumer<Disposable> rxOnSubscribe(final HttpObserver<T> observer) {
//		return new Consumer<Disposable>() {
//
//			@Override
//			public void accept(Disposable disposable) {
//				// network error/ page destroy to cancel
//				observer.onRequestStart(disposable);
//			}
//		};
//	}
//
//	public static <T> Function<Throwable, ObservableSource<T>> rxErrorResumeNext() {
//		return new Function<Throwable, ObservableSource<T>>() {
//
//			@Override
//			public ObservableSource<T> apply(final Throwable throwable) throws Exception {
//				return new Observable<T>() {
//
//					@Override
//					protected void subscribeActual(Observer<? super T> observer) {
//						// 执行这里, 取决执行rxOnError/rxOnComplete
//						observer.onError(throwable); // 执行 rxOnError
////						observer.onComplete(); // 执行 rxOnComplete
//					}
//				};
//			}
//		};
//	}
//
//	public static Consumer<Throwable> rxOnError(AbsHttpRequest request) {
//		return new Consumer<Throwable>() {
//
//			@Override
//			public void accept(Throwable throwable) throws Exception {
//				if (!isPageDestroyed(request.getPageController())) {
//					request.hideProgressDialog();
//				}
//			}
//		};
//	}
//
//	public static Action rxOnComplete(AbsHttpRequest request) {
//		return new Action() {
//
//			@Override
//			public void run() throws Exception {
//				if (!isPageDestroyed(request.getPageController())) {
//					request.hideProgressDialog();
//				}
//			}
//		};
//	}
//
//	public static <T> Consumer<T> rxSubscribe(AbsHttpRequest request, HttpObserver<T> observer) {
//		return new Consumer<T>() {
//
//			@Override
//			@MainThread
//			public void accept(T t) throws Exception {
//				if (!isPageDestroyed(request.getPageController()) && observer != null) {
//					observer.onRequestResult(t);
//				}
//			}
//		};
//	}
//
//	public static <T> Consumer<Throwable> rxErrorMissing(AbsHttpRequest request, HttpObserver<T> observer) {
//		return new Consumer<Throwable>() {
//
//			@Override
//			@MainThread
//			public void accept(Throwable throwable) throws Exception {
//				if (!isPageDestroyed(request.getPageController())) {
//
//					if (observer != null) {
//						observer.onRequestError(throwable);
//					}
//
//					if (request.getPageController() != null) {
//						request.getPageController().getLayoutController().doneShowError(throwable);
//					}
//				}
//			}
//		};
//	}
//
//	public static <R> Function<R, R> rxMap() {
//		return new Function<R, R>() {
//
//			@Override
//			public R apply(R r) throws Exception {
//				return r;
//			}
//		};
//	}
//
//	public static <T> Function<T, Observable<T>> rxMapResult() {
//		return new Function<T, Observable<T>>() {
//
//			@Override
//			public Observable<T> apply(T t) throws Exception {
//				if (HttpResult.class.isInstance(t)) {
//					final HttpResult mHttpResult = (HttpResult) t;
//					if (!mHttpResult.success()) {
//						return Observable.error(new HttpServiceException(mHttpResult.getMessage(), mHttpResult.getCode()));
//					}
//				} else if (String.class.isInstance(t)) {
//					if (TextUtils.isEmpty(String.valueOf(t))) {
//						return Observable.error(new HttpServiceException("The result is an empty string", 0));
//					}
//				}
//				return Observable.just(t);
//			}
//		};
//	}
//
//	public static <T> Function<Observable<T>, ObservableSource<T>> rxFlatMapResult() {
//		return new Function<Observable<T>, ObservableSource<T>>() {
//
//			@Override
//			public ObservableSource<T> apply(Observable<T> modelObservable) throws Exception {
//				return modelObservable.map(new Function<T, T>() {
//
//					@Override
//					public T apply(T model) throws Exception {
//						return model;
//					}
//				});
//			}
//		};
//	}
//
//	@NonNull
//	@MainThread
//	public static boolean isPageDestroyed(AppPageController<?> pageController) {
//		if (pageController == null) {
//			return false;
//		}
//		return pageController.getPageLifecycleController().isDestroy();
//	}
}
