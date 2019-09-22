package androidx.sframe.ui.controller.impl;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.sframe.annotation.RunWithAsync;
import androidx.sframe.helper.AnnotationHelper;
import androidx.sframe.ui.AppCompatNavAgentActivity;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppNavigation;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.utils.LoggerCompat;
import androidx.sframe.widget.SRelativeLayout;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
abstract class AbsPageControllerImpl<Page> implements AppPageController<Page>, UILayoutController.OnDataSourceListener, AppToolbarMethod.OnPopClickListener, LifecycleEventObserver {

	private final PageProvider mPageProvider;

	private View mPageView;
	private UIViewController mViewController;
	private AppNavController<Page> mAppNavController;
	private boolean mIsViewCreated;

	AbsPageControllerImpl(@NonNull PageProvider pageProvider) {
		this.mPageProvider = pageProvider;
	}

	@CallSuper
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		this.mAppNavController = new AppNavControllerImpl<>(this);

		if (Build.VERSION.SDK_INT >= 19) {
			this.getLifecycle().addObserver(this);
		}
	}

	@Nullable
	@Override
	public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View prePageView = this.mPageView;

		if (prePageView == null) {
			final Page prePageOwner = this.getPageOwner();

			final ViewGroup realContainer;
			if (prePageOwner instanceof ContentViewInterface) {
				realContainer = container;
			} else {
				realContainer = new SRelativeLayout(inflater.getContext());
			}

			if (prePageOwner instanceof PageViewInterface) {
				prePageView = ((PageViewInterface) prePageOwner).onPageCreateView(inflater, realContainer, savedInstanceState);
			}
			if (prePageView == null) {
				final int layoutId = this.getPageProvider().onPageLayoutId(savedInstanceState);
				if (layoutId != 0) {
					prePageView = inflater.inflate(layoutId, realContainer, false);
				}
			}

			if (prePageOwner instanceof ContentViewInterface) {
				// no-op
			} else {
				((SRelativeLayout) realContainer)
						.getLayoutController()
						.setOnDataSourceListener(this)
						.setShouldRunWithAsync(AnnotationHelper.isShouldRunInAsyncAnn(prePageOwner))
						// toolbar options
						.getToolbarController()
						.getToolbarMethod()
						.setOnPopClickListener(this);
				if (prePageView != null) {
					((SRelativeLayout) realContainer)
							.getLayoutController()
							.setContentLayout(prePageView);
				}
				prePageView = realContainer;
			}
			this.mIsViewCreated = true;
		}
		if (prePageView != null) {
			if (prePageView.getParent() != null) {
				((ViewGroup) prePageView.getParent()).removeView(prePageView);
			}
			if (prePageView.getLayoutParams() == null) {
				prePageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			}
		}
		return (this.mPageView = prePageView);
	}

	@CallSuper
	@Override
	public void onViewCreated(@Nullable Bundle savedInstanceState) {
		AppNavigation.setViewPageController(this.getPageView(), this);

		final String TAG = this.getPageOwner().getClass().getName();
		final PageProvider prePageProvider = this.getPageProvider();
		if (this.mIsViewCreated) {
			try {
				final Bundle arguments = AppPageControllerHelper.getArguments(this);
				final boolean isShouldPagePopEnabled;
				final boolean isShouldLayoutStableMode;

				if (prePageProvider instanceof AppPageController.WindowPageProvider) {
					isShouldPagePopEnabled = true;
					isShouldLayoutStableMode = true;
				} else {
					isShouldPagePopEnabled = AppCompatNavAgentActivity.isShouldPagePopEnabled(arguments);
					isShouldLayoutStableMode = false;
				}

				this.getLayoutController()
						.setToolbarLayoutStableMode(isShouldLayoutStableMode)
						.getToolbarController()
						.getToolbarMethod()
						.setPopEnabled(isShouldPagePopEnabled);
			} catch (IllegalStateException e) {
				LoggerCompat.i(TAG, e);
			} finally {
				prePageProvider.onPageViewCreated(savedInstanceState);
				this.mIsViewCreated = false;
			}
		}
		try {
			final UILayoutController layoutController = this.getLayoutController();
			if (UILayoutController.LayoutType.None.key == layoutController.getCurLayoutKey()) {
				layoutController.layoutOfLoading(savedInstanceState);
			} else {
				layoutController.refreshed(savedInstanceState);
			}
		} catch (IllegalStateException e) {
			LoggerCompat.i(TAG, e);
		}
	}

	@CallSuper
	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		// NO-OP
	}

	@CallSuper
	@Override
	@RunWithAsync(value = false)
	public void onDataSourceChanged(@NonNull UILayoutController layoutController, @Nullable Object params) {
		// default run in mainThread
		this.getPageProvider().onPageDataSourceChanged(params);
	}

	@Override
	public void onPopClick(@NonNull View view) {
		if (!this.getAppNavController().navigateUp()) {
			ActivityCompat.finishAfterTransition(AppPageControllerHelper.requireActivity(this));
		}
	}

	@NonNull
	final PageProvider getPageProvider() {
		return this.mPageProvider;
	}

	@Nullable
	@Override
	public final View getPageView() {
		return this.mPageView;
	}

	@NonNull
	@Override
	public final View requirePageView() {
		final View prePageView = this.getPageView();
		if (prePageView == null) {
			throw new IllegalStateException("Page " + this.getPageOwner() + " View does not have a create.");
		}
		return prePageView;
	}

	@NonNull
	@Override
	public final UIViewController getViewController() {
		try {
			return this.getLayoutController().getViewController();
		} catch (IllegalStateException e) {
			if (this.mViewController == null) {
				this.mViewController = new UIViewControllerImpl(this.requirePageView());
			}
			return this.mViewController;
		}
	}

	@NonNull
	@Override
	public final UILayoutController getLayoutController() {
		final View prePageView = this.requirePageView();
		if (prePageView instanceof SRelativeLayout) {
			final SRelativeLayout preLayout = (SRelativeLayout) prePageView;
			return preLayout.getLayoutController();
		}
		throw new IllegalStateException("AppPageController " + this + " does not have a UILayoutController set");
	}

	@NonNull
	@Override
	public final UIToolbarController getToolbarController() {
		return this.getLayoutController().getToolbarController();
	}

	@NonNull
	@Override
	public final AppNavController<Page> getAppNavController() {
		return this.mAppNavController;
	}

	/**
	 * Called when a state transition event happens.
	 *
	 * @param source The source of the event
	 * @param event  The event
	 */
	@CallSuper
	@Override
	public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
		final String TAG = this.getPageOwner().getClass().getName();
		if (Lifecycle.Event.ON_CREATE == event) {
			LoggerCompat.i(TAG, "onCreate");
		} else if (Lifecycle.Event.ON_START == event) {
			LoggerCompat.i(TAG, "onStart");
		} else if (Lifecycle.Event.ON_RESUME == event) {
			LoggerCompat.i(TAG, "onResume");
		} else if (Lifecycle.Event.ON_PAUSE == event) {
			LoggerCompat.i(TAG, "onPause");
		} else if (Lifecycle.Event.ON_STOP == event) {
			LoggerCompat.i(TAG, "onStop");
		} else if (Lifecycle.Event.ON_DESTROY == event) {
			LoggerCompat.i(TAG, "onDestory");
			this.recycle();
		}
	}

	@CallSuper
	protected void recycle() {
		final String TAG = this.getPageOwner().getClass().getName();
		try {
			this.getLayoutController().recycle();
		} catch (IllegalStateException e) {
			LoggerCompat.i(TAG, e);
		} finally {
			if (this.mViewController != null) {
				this.mViewController.recycle();
				this.mViewController = null;
			}
		}
	}
}
