package androidx.sframe.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.UIViewControllerImpl;
import androidx.demon.widget.RefreshLayout;

/**
 * Author create by ok on 2018/12/28
 * Email : ok@163.com.
 */
public abstract class RefreshLoadView extends RefreshLayout.SimpleLoadView {

	private UIViewController mViewController;

	private boolean isRefreshTips = false;

	@Override
	public void onViewCreated(@NonNull RefreshLayout refreshLayout, @NonNull View container) {
		super.onViewCreated(refreshLayout, container);
		this.mViewController = new UIViewControllerImpl(container);
	}

	@Override
	public int onGetScrollDistance() {
		if(this.isRefreshTips) {
			return 0;
		}
		return super.onGetScrollDistance();
	}

	@NonNull
	public final UIViewController getViewController() {
		return this.mViewController;
	}

	public void setRefreshTips(boolean refreshTips) {
		this.isRefreshTips = refreshTips;
	}

	public boolean isRefreshTips() {
		return this.isRefreshTips;
	}
}
