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
public class FooterHorLoadCallback extends RefreshLoadCallback {

	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @NonNull View container) {
		return inflater.inflate(R.layout.layout_footer_hor_loading_default, (ViewGroup) container);
	}

	@Override
	public void onRefreshPull(@NonNull View container, float scrollOffset, float offset) {
		if (isRefreshTips()) {
			// 不回调刷新通知
			getViewController().findAt(R.id.refreshFooterTextView).methodAtTextView().setText("暂无更多");
			return;
		}
		if (offset >= 1.f) {
			getViewController().findAt(R.id.refreshFooterTextView).methodAtTextView().setText("释放加载");
		} else {
			getViewController().findAt(R.id.refreshFooterTextView).methodAtTextView().setText("加载更多");
		}
	}

	@Override
	public void onRefreshing(@NonNull View container) {
		getViewController().findAt(R.id.refreshFooterTextView).methodAtTextView().setText("加载中");
	}

	@Override
	public void onRefreshed(@NonNull View container) {
		getViewController().findAt(R.id.refreshFooterTextView).methodAtTextView().setText("加载完成");
	}
}
