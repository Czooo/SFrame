package androidx.sframe.widget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.demon.widget.cache.RecycledPool;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl;
import androidx.sframe.ui.controller.impl.UIViewControllerImpl;

/**
 * Author create by ok on 2019/2/6
 * Email : ok@163.com.
 */
public class PagerAdapter<DataSource> extends androidx.viewpager.widget.PagerAdapter {

	private final RecycledPool<View> mRecycledPool = new RecycledPool<>();
	private final Delegate<DataSource> mDelegate;

	private DataSourceNotifyController<PagerAdapter<DataSource>, DataSource> mDataSourceController;

	public PagerAdapter(@NonNull Delegate<DataSource> delegate) {
		this.mDelegate = delegate;
	}

	@Override
	public int getCount() {
		return this.getDataSourceController().size();
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View recycledView = this.mRecycledPool.getRecycled(0);
		if (recycledView == null) {
			recycledView = this.mDelegate.onCreateItemView(PagerAdapter.this, LayoutInflater.from(container.getContext()), container);
		}
		container.addView(recycledView);
		this.mDelegate.onBindItemView(this, new UIViewControllerImpl(recycledView), position);
		return recycledView;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		View recycledView = (View) object;
		container.removeView(recycledView);
		this.mRecycledPool.putRecycled(0, recycledView);
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	@NonNull
	public final DataSourceController<DataSource> getDataSourceController() {
		return this.getDataSourceNotifyController();
	}

	@NonNull
	public DataSourceNotifyController<PagerAdapter<DataSource>, DataSource> getDataSourceNotifyController() {
		if (this.mDataSourceController == null) {
			this.mDataSourceController = new DataSourceNotifyControllerImpl<PagerAdapter<DataSource>, DataSource>(this) {

				@Override
				public DataSourceNotifyController<PagerAdapter<DataSource>, DataSource> notifyDataSetChanged() {
					this.getDataSourceAdapter().notifyDataSetChanged();
					return this;
				}
			};
		}
		return this.mDataSourceController;
	}

	@NonNull
	public final DataSource findDataSourceByPosition(int position) {
		return this.getDataSourceController().findDataSourceByPosition(position);
	}

	public interface Delegate<DataSource> {

		View onCreateItemView(PagerAdapter<DataSource> adapter, LayoutInflater inflater, ViewGroup parent);

		void onBindItemView(PagerAdapter<DataSource> adapter, UIViewController controller, int position);
	}
}
