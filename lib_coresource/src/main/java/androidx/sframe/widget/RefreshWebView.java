package androidx.sframe.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.AppPageController;
import androidx.demon.widget.RefreshLayout;
import androidx.demon.widget.RefreshMode;

/**
 * Author create by ok on 2019/3/17
 * Email : ok@163.com.
 */
public class RefreshWebView extends RefreshLayout implements RefreshLayout.OnRefreshListener {

	private NestedWebView mNestedWebView;

	public RefreshWebView(Context context) {
		this(context, null);
	}

	public RefreshWebView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RefreshWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.setOnRefreshListener(this);
		this.addView(getWebView());
	}

	@Override
	public void onRefreshing(@NonNull RefreshLayout refreshLayout, @NonNull RefreshMode mode) {
		if (RefreshMode.REFRESH_MODE_START == mode) {
			getWebView().reload();
		}
	}


	public final NestedWebView getWebView() {
		if (mNestedWebView == null) {
			mNestedWebView = new NestedWebView(getContext());
		}
		return mNestedWebView;
	}

	public final void loadUrl(String url) {
		getWebView().loadUrl(url);
	}

	public final void loadData(String data, String mimeType, String encoding) {
		getWebView().loadData(data, mimeType, encoding);
	}

	public final void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl) {
		getWebView().loadDataWithBaseURL(baseUrl, data, mimeType, encoding, failUrl);
	}

	public void setWebChromeClient(WebChromeClient webChromeClient) {
		getWebView().setWebChromeClient(webChromeClient);
	}

	public void setWebViewClient(WebViewClient webViewClient) {
		getWebView().setWebViewClient(webViewClient);
	}

	public static class RefreshWebChromeClient extends WebChromeClient {

		final AppPageController<?> mPageController;

		@Override
		@CallSuper
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);

			if (newProgress >= 100) {
				ViewParent parent = view.getParent();
				if (RefreshWebView.class.isInstance(parent)) {
					((RefreshWebView) parent).setRefreshing(false, 700);
				}
			}
		}

		public RefreshWebChromeClient(AppPageController<?> pageController) {
			this.mPageController = pageController;
		}

		public final AppPageController<?> getPageController() {
			return mPageController;
		}
	}

	public static class RefreshWebViewClient extends WebViewClient {

		final AppPageController<?> mPageController;

		public RefreshWebViewClient(AppPageController<?> pageController) {
			this.mPageController = pageController;
		}

		@Override
		@CallSuper
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			getPageController()
					.getLayoutController()
					.layoutOfContent();

			ViewParent parent = view.getParent();
			if (RefreshWebView.class.isInstance(parent)) {
				((RefreshWebView) parent).setRefreshing(false, 700);
			}
		}

		@Override
		@CallSuper
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			this.getPageController()
					.getToolbarController()
					.getToolbarMethod()
					.setErrorText(String.valueOf(errorCode))
					.getLayoutController().layoutOfError(description);
		}

		@Override
		@CallSuper
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				this.getPageController()
						.getToolbarController()
						.getToolbarMethod()
						.setErrorText(String.valueOf(error.getErrorCode()))
						.getLayoutController().layoutOfError(error.getDescription());
			} else {
				super.onReceivedError(view, request, error);
			}
		}

		public final AppPageController<?> getPageController() {
			return mPageController;
		}
	}
}
