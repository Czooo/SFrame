package androidx.sframe.model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Author create by ok on 2019-06-05
 * Email : ok@163.com.
 */
public class AgentNavModel extends AbsModel {

	private final Class<? extends Fragment> mFragmentClass;
	private final Bundle mArguments;

	public AgentNavModel(@NonNull Class<? extends Fragment> fragment) {
		this(fragment, null);
	}

	public AgentNavModel(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args) {
		this.mFragmentClass = fragment;
		this.mArguments = args;
	}

	@NonNull
	public Bundle toBundle(@NonNull Bundle args) {
		if (this.mArguments == null) {
			return new Bundle(args);
		}
		this.mArguments.putAll(args);
		return this.mArguments;
	}

	@Nullable
	public Class<? extends Fragment> getFragmentClass() {
		return this.mFragmentClass;
	}
}
