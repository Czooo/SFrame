package androidx.demon.widget;

import android.view.View;

import org.parent.refreshview.AbsRefreshLoadCallback;
import org.parent.refreshview.widget.RefreshView;

import androidx.annotation.NonNull;
import androidx.demon.ui.controller.UIViewController;
import androidx.demon.ui.controller.impl.UIViewControllerImpl;

/**
 * Author create by ok on 2018/12/28
 * Email : ok@163.com.
 */
public abstract class RefreshLoadCallback extends AbsRefreshLoadCallback {

	private UIViewController mViewController;

	@Override
	public void onViewCreate(@NonNull RefreshView refreshView, @NonNull View container) {
		mViewController = new UIViewControllerImpl(refreshView);
	}

	public final UIViewController getViewController() {
		return mViewController;
	}
}
