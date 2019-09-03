package androidx.demon.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.compat.LogCompat;
import androidx.demon.model.AgentNavModel;
import androidx.demon.ui.abs.AbsActivity;
import androidx.demon.ui.controller.AppPageController;

/**
 * Author create by ok on 2019-06-04
 * Email : ok@163.com.
 */
public class AppCompatNavAgentActivity extends AbsActivity implements AppPageController.ContentViewInterface {

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
		final Bundle args = intent.getExtras();
		try {
			if (args == null) {
				throw new IllegalStateException("AppCompatNavAgentActivity " + this + " not set args");
			}
			final AgentNavModel mAgentNavModel = (AgentNavModel) args.getSerializable(KEY_MODEL);

			if (mAgentNavModel == null || mAgentNavModel.errorArgs()) {
				throw new IllegalStateException("AppCompatNavAgentActivity " + this + " not set model");
			}

			if (mAgentNavModel.getPageClass() == null) {
				this.getPageController().getAppNavController().setGraph(mAgentNavModel.getNavResId(), mAgentNavModel.toBundle(args));
				return;
			}
			this.getPageController().getAppNavController()
					.pushPage(mAgentNavModel.getPageClass(), mAgentNavModel.toBundle(args));

			final int navResId = mAgentNavModel.getNavResId();
			if (navResId != 0) {
				this.getPageController().getAppNavController()
						.addGraph(navResId);
			}
		} catch (Exception e) {
			LogCompat.e(e.getMessage(), e);
		}
	}
}
