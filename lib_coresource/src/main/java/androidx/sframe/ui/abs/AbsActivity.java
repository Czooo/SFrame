package androidx.sframe.ui.abs;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.sframe.manager.PageCacheManager;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.AppPageActivityControllerImpl;

/**
 * Author create by ok on 2019-06-03
 * Email : ok@163.com.
 */
public abstract class AbsActivity extends AppCompatActivity implements AppPageController.PageProvider {

	private AppPageController<FragmentActivity> mPageController;

	@CallSuper
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PageCacheManager.getInstance()
				.getActivityPageCache()
				.put(this);
		this.getPageController().onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		this.getPageController().onViewCreated(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PageCacheManager.getInstance()
				.getActivityPageCache()
				.remove(this);
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params) {

	}

	@NonNull
	public final UIViewController getViewController() {
		return this.getPageController().getViewController();
	}

	@NonNull
	public final UILayoutController getLayoutController() {
		return this.getPageController().getLayoutController();
	}

	@NonNull
	public final UIToolbarController getToolbarController() {
		return this.getPageController().getToolbarController();
	}

	@NonNull
	public final AppNavController<FragmentActivity> getNavController() {
		return this.getPageController().getNavController();
	}

	@NonNull
	public AppPageController<FragmentActivity> getPageController() {
		if (this.mPageController == null) {
			this.mPageController = new AppPageActivityControllerImpl(this);
		}
		return this.mPageController;
	}

	private final OnKeyDownDispatcher mOnKeyDownDispatcher = new OnKeyDownDispatcher(new OnKeyDownCallback() {
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			return AbsActivity.this.dispatchOnKeyDown(keyCode, event);
		}
	});

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return this.mOnKeyDownDispatcher.onKeyDown(keyCode, event);
	}

	@NonNull
	public OnKeyDownDispatcher getOnKeyDownDispatcher() {
		return this.mOnKeyDownDispatcher;
	}

	private boolean dispatchOnKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode
				|| KeyEvent.KEYCODE_ESCAPE == keyCode) {
			if (this.getNavController().navigateUp()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
