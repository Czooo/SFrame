package androidx.sframe.widget.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl;

/**
 * Author create by ok on 2019-09-05
 * Email : ok@163.com.
 */
public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

	private final FragmentManager mFragmentManager;
	private final Context mContext;

	private DataSourceNotifyController<FragmentPagerAdapter, FragmentPager> mDataSourceController;

	public FragmentPagerAdapter(@NonNull Context context, @NonNull FragmentManager fragmentManager) {
		this(context, fragmentManager, BEHAVIOR_SET_USER_VISIBLE_HINT);
	}

	public FragmentPagerAdapter(@NonNull final Context context, @NonNull final FragmentManager fragmentManager, int behavior) {
		super(fragmentManager, behavior);
		this.mContext = context;
		this.mFragmentManager = fragmentManager;
	}

	@NonNull
	@Override
	public Fragment getItem(int position) {
		FragmentPager fragmentPager = this.findDataSourceByPosition(position);
		Fragment fragment = this.mFragmentManager.getFragmentFactory()
				.instantiate(this.mContext.getClassLoader(), fragmentPager.getFragmentClass().getName());
		Bundle args = new Bundle(fragmentPager.getData());
		args.putInt("Position", position);
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return this.findDataSourceByPosition(position).getTitle();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return this.getDataSourceController().size();
	}

	@NonNull
	public final DataSourceController<FragmentPager> getDataSourceController() {
		return this.getDataSourceNotifyController();
	}

	@NonNull
	public DataSourceNotifyController<FragmentPagerAdapter, FragmentPager> getDataSourceNotifyController() {
		if(this.mDataSourceController == null) {
			this.mDataSourceController = new DataSourceNotifyControllerImpl<FragmentPagerAdapter, FragmentPager>(this) {
				@Override
				public DataSourceNotifyController<FragmentPagerAdapter, FragmentPager> notifyDataSetChanged() {
					this.getDataSourceAdapter().notifyDataSetChanged();
					return this;
				}
			};
		}
		return this.mDataSourceController;
	}

	@NonNull
	public final FragmentPager findDataSourceByPosition(int position) {
		return this.getDataSourceController().findDataSourceByPosition(position);
	}
}
