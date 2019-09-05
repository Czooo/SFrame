package androidx.sframe.widget.adapter;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

/**
 * Author create by ok on 2019/2/6
 * Email : ok@163.com.
 */
public class FragmentPager {

	private final int iconResId;
	private final String title;
	private final Bundle data;
	private final Class<? extends Fragment> fragmentClass;

	FragmentPager(Builder builder) {
		this.iconResId = builder.iconResId;
		this.title = builder.title;
		this.data = builder.data;
		this.fragmentClass = builder.fragmentClass;
	}

	public int getIconResId() {
		return iconResId;
	}

	public final String getTitle() {
		return title;
	}

	public final Bundle getData() {
		return data;
	}

	public final Class<? extends Fragment> getFragmentClass() {
		return fragmentClass;
	}

	public static class Builder {

		private int iconResId;
		private String title;
		private Bundle data;
		private Class<? extends Fragment> fragmentClass;

		public Builder(Class<? extends Fragment> fragmentClass) {
			this.fragmentClass = fragmentClass;
		}

		public Builder setIconResId(int iconResId) {
			this.iconResId = iconResId;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setData(Bundle data) {
			this.data = data;
			return this;
		}

		public FragmentPager build() {
			if (TextUtils.isEmpty(title)) {
				title = fragmentClass.getName();
			}
			if (null == data) {
				data = new Bundle();
			}
			return new FragmentPager(this);
		}
	}
}
