package androidx.sframe.widget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl;
import androidx.sframe.ui.controller.impl.UIViewControllerImpl;
import androidx.sframe.widget.cache.IObjectCache;
import androidx.sframe.widget.cache.ViewCacheImpl;

/**
 * Author create by ok on 2019/2/6
 * Email : ok@163.com.
 */
public class PagerAdapter<DataSource> extends androidx.viewpager.widget.PagerAdapter {

	private final DataSourceNotifyController<PagerAdapter<DataSource>, DataSource> mDataSourceController;

	private final Delegate<DataSource> mDelegate;

	private final IObjectCache<View, ViewGroup> mViewCache;

	public PagerAdapter(Delegate<DataSource> delegate) {
		this.mDelegate = delegate;
		this.mDataSourceController = new DataSourceNotifyControllerImpl<PagerAdapter<DataSource>, DataSource>(this) {

			@Override
			public DataSourceNotifyController<PagerAdapter<DataSource>, DataSource> notifyDataSetChanged() {
				getDataSourceAdapter().notifyDataSetChanged();
				return this;
			}
		};
		this.mViewCache = new ViewCacheImpl() {

			@Override
			public View create(ViewGroup viewGroup, int position) {
				return mDelegate.onCreateItemView(PagerAdapter.this, LayoutInflater.from(viewGroup.getContext()), viewGroup);
			}
		};
	}

	@Override
	public final int getCount() {
		return getDataSourceController().size();
	}

	@NonNull
	@Override
	public final Object instantiateItem(@NonNull ViewGroup container, int position) {
		View children = mViewCache.obtain(container, position);
		container.addView(children);
		mDelegate.onBindItemView(this, new UIViewControllerImpl(children), position);
		return children;
	}

	@Override
	public final void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		View children = (View) object;
		container.removeView(children);
		mViewCache.recycle(children);
	}

	@Override
	public final boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	@Override
	public void finishUpdate(@NonNull ViewGroup container) {
		super.finishUpdate(container);
	}

	@NonNull
	public final DataSourceController<DataSource> getDataSourceController() {
		return getDataSourceNotifyController();
	}

	@NonNull
	public final DataSourceNotifyController<PagerAdapter<DataSource>, DataSource> getDataSourceNotifyController() {
		return mDataSourceController;
	}

	@NonNull
	public final DataSource findDataSourceByPosition(int position) {
		return getDataSourceController().findDataSourceByPosition(position);
	}

	public interface Delegate<DataSource> {

		View onCreateItemView(PagerAdapter<DataSource> adapter, LayoutInflater inflater, ViewGroup parent);

		void onBindItemView(PagerAdapter<DataSource> adapter, UIViewController controller, int position);
	}
}
