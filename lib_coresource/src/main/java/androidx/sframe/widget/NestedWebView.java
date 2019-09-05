package androidx.sframe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;

/**
 * Author create by ok on 2019-09-03
 * Email : ok@163.com.
 */
public class NestedWebView extends WebView implements NestedScrollingParent, NestedScrollingChild {

	/**
	 * Construct a new WebView with a Context object.
	 *
	 * @param context A Context object used to access application assets.
	 */
	public NestedWebView(Context context) {
		super(context);
	}

	/**
	 * Construct a new WebView with layout parameters.
	 *
	 * @param context A Context object used to access application assets.
	 * @param attrs   An AttributeSet passed to our parent.
	 */
	public NestedWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Construct a new WebView with layout parameters and a default style.
	 *
	 * @param context      A Context object used to access application assets.
	 * @param attrs        An AttributeSet passed to our parent.
	 * @param defStyleAttr
	 */
	public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
