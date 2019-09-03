package androidx.demon.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.demon.R;

/**
 * Author create by ok on 2019/3/26
 * Email : ok@163.com.
 */
public class HeaderLoadCallback extends RefreshLoadCallback {

	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @NonNull View container) {
		return inflater.inflate(R.layout.layout_header_loading_default, (ViewGroup) container);
	}

	@Override
	public void onRefreshPull(@NonNull View container, float scrollOffset, float offset) {
		if (isRefreshTips()) {
			// 不回调刷新通知
			getViewController().findAt(R.id.refreshHeaderTextView).methodAtTextView().setText("无更多刷新");
			return;
		}
		if (offset >= 1.f) {
			getViewController().findAt(R.id.refreshHeaderTextView).methodAtTextView().setText("释放刷新");
		} else {
			getViewController().findAt(R.id.refreshHeaderTextView).methodAtTextView().setText("下拉刷新");
		}
	}

	@Override
	public void onRefreshing(@NonNull View container) {
		getViewController().findAt(R.id.refreshHeaderTextView).methodAtTextView().setText("刷新中");
	}

	@Override
	public void onRefreshed(@NonNull View container) {
		getViewController().findAt(R.id.refreshHeaderTextView).methodAtTextView().setText("刷新完成");
	}
}
