package androidx.sframe.widget.adapter;

import android.os.Bundle;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Author create by ok on 2019/2/6
 * Email : ok@163.com.
 */
public class FragmentPager implements Serializable {

	private final String title;
	private final Bundle args;
	private final Class<? extends Fragment> fragmentClass;

	protected FragmentPager(@NonNull Builder builder) {
		this.title = builder.title;
		this.args = builder.args;
		this.fragmentClass = builder.fragmentClass;
	}

	public final String getTitle() {
		return title;
	}

	public final Bundle getData() {
		return args;
	}

	public final Class<? extends Fragment> getFragmentClass() {
		return fragmentClass;
	}

	public static class Builder {

		private String title;
		private Bundle args;
		private Class<? extends Fragment> fragmentClass;

		public Builder(Class<? extends Fragment> fragmentClass) {
			this.fragmentClass = fragmentClass;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setData(Bundle args) {
			this.args = args;
			return this;
		}

		public FragmentPager build() {
			if (this.args == null) {
				this.args = new Bundle();
			}
			return new FragmentPager(this);
		}
	}
}
