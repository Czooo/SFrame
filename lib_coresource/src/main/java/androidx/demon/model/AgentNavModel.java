package androidx.demon.model;

import android.os.Bundle;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Author create by ok on 2019-06-05
 * Email : ok@163.com.
 */
public class AgentNavModel extends AbsModel {

	@Nullable
	private final Class<? extends Fragment> pageClass;
	@Nullable
	private final Bundle args;
	@NavigationRes
	private final int navResId;

	public AgentNavModel(@NonNull Class<? extends Fragment> pageClass) {
		this(pageClass, null);
	}

	public AgentNavModel(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args) {
		this(pageClass, 0, args);
	}

	public AgentNavModel(@NavigationRes int navResId) {
		this(navResId, null);
	}

	public AgentNavModel(@NavigationRes int navResId, @Nullable Bundle args) {
		this(null, navResId, args);
	}

	public AgentNavModel(@Nullable Class<? extends Fragment> pageClass, @NavigationRes int navResId) {
		this(pageClass, navResId, null);
	}

	public AgentNavModel(@Nullable Class<? extends Fragment> pageClass, @NavigationRes int navResId, @Nullable Bundle args) {
		this.pageClass = pageClass;
		this.navResId = navResId;
		this.args = args;
	}

	@NonNull
	public Bundle toBundle(@NonNull Bundle args) {
		if (this.args == null) {
			return new Bundle(args);
		}
		this.args.putAll(args);
		return this.args;
	}

	public int getNavResId() {
		return this.navResId;
	}

	@Nullable
	public Class<? extends Fragment> getPageClass() {
		return this.pageClass;
	}

	public boolean errorArgs() {
		return this.pageClass == null && navResId == 0;
	}
}
