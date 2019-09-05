package androidx.sframe.widget.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl;

/**
 * Author create by ok on 2019/2/6
 * Email : ok@163.com.
 */
public class FragmentPagerAdapterCompat extends androidx.demon.widget.adapter.FragmentPagerAdapter {

	private DataSourceNotifyController<FragmentPagerAdapterCompat, FragmentPager> mDataSourceController;

	public FragmentPagerAdapterCompat(@NonNull FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@NonNull
	@Override
	public Fragment onCreateFragment(@NonNull ViewGroup container, int position) {
		FragmentPager fragmentPager = findDataSourceByPosition(position);
		Fragment fragment = this.getFragmentManager().getFragmentFactory()
				.instantiate(container.getContext().getClassLoader(), fragmentPager.getFragmentClass().getName());
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
	public int getItemCount() {
		return this.getDataSourceController().size();
	}

	@NonNull
	public final DataSourceController<FragmentPager> getDataSourceController() {
		return this.getDataSourceNotifyController();
	}

	@NonNull
	public DataSourceNotifyController<FragmentPagerAdapterCompat, FragmentPager> getDataSourceNotifyController() {
		if (this.mDataSourceController == null) {
			this.mDataSourceController = new DataSourceNotifyControllerImpl<FragmentPagerAdapterCompat, FragmentPager>(this) {
				@Override
				public DataSourceNotifyController<FragmentPagerAdapterCompat, FragmentPager> notifyDataSetChanged() {
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
