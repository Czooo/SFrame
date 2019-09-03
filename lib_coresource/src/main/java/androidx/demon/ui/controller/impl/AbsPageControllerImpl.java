package androidx.demon.ui.controller.impl;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.demon.annotation.RunWithAsync;
import androidx.demon.compat.LogCompat;
import androidx.demon.helper.AnnotationHelper;
import androidx.demon.ui.AppCompatNavAgentActivity;
import androidx.demon.ui.controller.AppNavController;
import androidx.demon.ui.controller.AppNavigation;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.ui.controller.UILayoutController;
import androidx.demon.ui.controller.UIToolbarController;
import androidx.demon.ui.controller.UIViewController;
import androidx.demon.widget.OverlapRelativeLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStore;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
abstract class AbsPageControllerImpl<Page> implements AppPageController<Page>, UILayoutController.OnDataSourceListener, AppToolbarMethod.OnPopClickListener, LifecycleEventObserver {

	private final PageProvider mPageProvider;
	private AppNavController<Page> mAppNavController;

	private LifecycleOwner mLifecycleOwner;
	private ViewModelStore mViewModelStore;

	AbsPageControllerImpl(@NonNull PageProvider pageProvider) {
		this.mPageProvider = pageProvider;
	}

	@CallSuper
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		this.mAppNavController = new AppNavControllerImpl<>(this);
	}

	private View mPageView;
	private UIViewController mViewController;
	private boolean mIsViewCreated;

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
				realContainer = new OverlapRelativeLayout(inflater.getContext());
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
				if (prePageView != null) {
					final boolean shouldAsyncRefreshedAnn = AnnotationHelper.isShouldRunInAsyncAnn(prePageOwner);
					((OverlapRelativeLayout) realContainer)
							.getLayoutController()
							.setContentLayout(prePageView)
							.setOnDataSourceListener(this)
							.setShouldAsyncRefreshed(shouldAsyncRefreshedAnn)
							.getToolbarController()
							.getToolbarMethod()
							.setOnPopClickListener(this);
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
				prePageView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
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
				LogCompat.i(TAG, e);
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
			LogCompat.i(TAG, e);
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

	@Override
	public final void setLifecycleOwner(@NonNull LifecycleOwner owner) {
		this.mLifecycleOwner = owner;

		if (Build.VERSION.SDK_INT >= 19) {
			this.mLifecycleOwner.getLifecycle().addObserver(this);
		}
	}

	@Override
	public final void setViewModelStore(@NonNull ViewModelStore viewModelStore) {
		this.mViewModelStore = viewModelStore;
	}

	@NonNull
	@Override
	public final Lifecycle getLifecycle() {
		return this.mLifecycleOwner.getLifecycle();
	}

	@NonNull
	@Override
	public final ViewModelStore getViewModelStore() {
		if (this.mViewModelStore == null) {
			throw new IllegalStateException("Your page " + this.getPageOwner() + " is not yet attached to the "
					+ "Application instance. You can't request ViewModel before onCreate call.");
		}
		return this.mViewModelStore;
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
		if (prePageView instanceof OverlapRelativeLayout) {
			final OverlapRelativeLayout preLayout = (OverlapRelativeLayout) prePageView;
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
			LogCompat.i(TAG, "onCreate");
		} else if (Lifecycle.Event.ON_START == event) {
			LogCompat.i(TAG, "onStart");
		} else if (Lifecycle.Event.ON_RESUME == event) {
			LogCompat.i(TAG, "onResume");
		} else if (Lifecycle.Event.ON_PAUSE == event) {
			LogCompat.i(TAG, "onPause");
		} else if (Lifecycle.Event.ON_STOP == event) {
			LogCompat.i(TAG, "onStop");
		} else if (Lifecycle.Event.ON_DESTROY == event) {
			LogCompat.i(TAG, "onDestory");
			this.recycle();
		}
	}

	@CallSuper
	protected void recycle() {
		final String TAG = this.getPageOwner().getClass().getName();
		try {
			this.getLayoutController().recycle();
		} catch (IllegalStateException e) {
			LogCompat.i(TAG, e);
		} finally {
			if (this.mViewController != null) {
				this.mViewController.recycle();
				this.mViewController = null;
			}
		}
	}
}