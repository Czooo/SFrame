package androidx.sframe.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.annotation.AppPageInterface;
import androidx.sframe.model.AgentNavModel;
import androidx.sframe.ui.abs.AbsActivity;
import androidx.sframe.utils.Logger;

/**
 * Author create by ok on 2019-06-04
 * Email : ok@163.com.
 */
@AppPageInterface(value = false)
public class NavAgentActivity extends AbsActivity {

	private static final String KEY_MODEL = "androidx-support-nav:agentActivity:model";
	private static final String KEY_POP = "androidx-support-nav:popEnabled";

	@NonNull
	public static Bundle create(@NonNull AgentNavModel agentNavModel) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(KEY_POP, true);
		bundle.putSerializable(KEY_MODEL, agentNavModel);
		return bundle;
	}

	public static boolean isShouldPagePopEnabled(@Nullable Bundle arguments) {
		if (arguments == null) {
			return false;
		}
		return arguments.getBoolean(KEY_POP, false);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return 0;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		final Intent intent = this.getIntent();
		final Bundle arguments = intent.getExtras();
		try {
			if (arguments == null) {
				throw new IllegalStateException("NavAgentActivity " + this + " not set args");
			}
			final AgentNavModel model = (AgentNavModel) arguments.getSerializable(KEY_MODEL);
			if (model == null || model.getFragmentClass() == null) {
				throw new IllegalStateException("NavAgentActivity " + this + " not set model");
			}
			this.getPageController().getNavController()
					.pushFragment(android.R.id.content, model.getFragmentClass(), model.toBundle(arguments));
		} catch (Exception e) {
			Logger.e(e);
		}
	}
}
