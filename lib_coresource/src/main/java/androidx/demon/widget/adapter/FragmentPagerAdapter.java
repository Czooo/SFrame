package androidx.demon.widget.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.ui.controller.DataSourceController;
import androidx.demon.ui.controller.DataSourceNotifyController;
import androidx.demon.ui.controller.impl.DataSourceNotifyControllerImpl;
import androidx.demon.widget.cache.FragmentCacheImpl;
import androidx.demon.widget.cache.IObjectCache;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Author create by ok on 2019/2/6
 * Email : ok@163.com.
 */
public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

	private final IObjectCache<Fragment, FragmentPager> mFragmentCache;
	private final DataSourceNotifyController<FragmentPagerAdapter, FragmentPager> mDataSourceController;

	public FragmentPagerAdapter(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
		this(context, fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT);
	}

	public FragmentPagerAdapter(@NonNull final Context context, @NonNull final FragmentManager fragmentManager, int behavior) {
		super(fragmentManager, behavior);
		this.mFragmentCache = new FragmentCacheImpl() {

			@Override
			public Fragment create(FragmentPager pager, int position) {
				Fragment fragment = fragmentManager.getFragmentFactory()
						.instantiate(context.getClassLoader(), pager.getFragmentClass().getName());
				Bundle args = new Bundle(pager.getData());
				args.putInt("Position", position);
				fragment.setArguments(args);
				return fragment;
			}
		};
		this.mDataSourceController = new DataSourceNotifyControllerImpl<FragmentPagerAdapter, FragmentPager>(this) {

			@Override
			public DataSourceNotifyController<FragmentPagerAdapter, FragmentPager> notifyDataSetChanged() {
				getDataSourceAdapter().notifyDataSetChanged();
				return this;
			}
		};
	}

	@Nullable
	@Override
	public final CharSequence getPageTitle(int position) {
		return findDataSourceByPosition(position).getTitle();
	}

	@Override
	public final long getItemId(int position) {
		return position;
	}

	@NonNull
	@Override
	public final Fragment getItem(int position) {
		return mFragmentCache.obtain(this.findDataSourceByPosition(position), position);
	}

	@Override
	public final int getCount() {
		return getDataSourceController().size();
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		super.destroyItem(container, position, object);
		mFragmentCache.recycle((Fragment) object);
	}

	@NonNull
	public final DataSourceController<FragmentPager> getDataSourceController() {
		return getDataSourceNotifyController();
	}

	@NonNull
	public final DataSourceNotifyController<FragmentPagerAdapter, FragmentPager> getDataSourceNotifyController() {
		return mDataSourceController;
	}

	@NonNull
	public final FragmentPager findDataSourceByPosition(int position) {
		return getDataSourceController().findDataSourceByPosition(position);
	}
}
