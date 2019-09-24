package androidx.sframe.ui.controller.impl;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.sframe.annotation.RunWithAsync;
import androidx.sframe.helper.AnnotationHelper;
import androidx.sframe.ui.NavAgentActivity;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppNavigation;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.utils.Logger;
import androidx.sframe.widget.SRelativeLayout;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
abstract class AbsPageControllerImpl<Page> implements AppPageController<Page>, LifecycleObserver, UILayoutController.OnDataSourceListener, AppToolbarMethod.OnPopClickListener {

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
		View pageView = this.mPageView;
		if (pageView == null) {
			final Page pageOwner = this.getPageOwner();

			final ViewGroup realContainer;
			if (pageOwner instanceof ContentViewInterface) {
				realContainer = container;
			} else {
				realContainer = new SRelativeLayout(inflater.getContext());
			}

			if (pageOwner instanceof PageViewInterface) {
				pageView = ((PageViewInterface) pageOwner).onPageCreateView(inflater, realContainer, savedInstanceState);
			}
			if (pageView == null) {
				final int layoutId = this.getPageProvider().onPageLayoutId(savedInstanceState);
				if (layoutId != 0) {
					pageView = inflater.inflate(layoutId, realContainer, false);
				}
			}

			if (pageOwner instanceof ContentViewInterface) {
				// no-op
			} else {
				((SRelativeLayout) realContainer)
						.getLayoutController()
						.setOnDataSourceListener(this)
						.setShouldRunWithAsync(AnnotationHelper.isShouldRunInAsyncAnn(pageOwner))
						// toolbar options
						.getToolbarController()
						.getToolbarMethod()
						.setOnPopClickListener(this);
				if (pageView != null) {
					((SRelativeLayout) realContainer)
							.getLayoutController()
							.setContentLayout(pageView);
				}
				pageView = realContainer;
			}
			this.mIsViewCreated = true;
		}
		if (pageView != null) {
			if (pageView.getParent() != null) {
				((ViewGroup) pageView.getParent()).removeView(pageView);
			}
			if (pageView.getLayoutParams() == null) {
				pageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			}
		}
		return (this.mPageView = pageView);
	}

	@CallSuper
	@Override
	public void onViewCreated(@Nullable Bundle savedInstanceState) {
		AppNavigation.setViewPageController(this.getPageView(), this);
		if (this.mIsViewCreated) {
			try {
				this.onPreViewCreated(savedInstanceState);
			} catch (Exception e) {
				Logger.e(e);
			} finally {
				this.getPageProvider().onPageViewCreated(savedInstanceState);
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
			Logger.e(e);
		}
	}

	@CallSuper
	protected void onPreViewCreated(@Nullable Bundle savedInstanceState) throws Exception {
		final Bundle arguments = AppPageControllerHelper.getArguments(this);
		this.getLayoutController()
				.setToolbarLayoutStableMode(false)
				.getToolbarController()
				.getToolbarMethod()
				.setPopEnabled(NavAgentActivity.isShouldPagePopEnabled(arguments));
	}

	@CallSuper
	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		// NO-OP
	}

	@Nullable
	@Override
	public Context getContext() {
		return AppPageControllerHelper.getContext(this);
	}

	@NonNull
	@Override
	public final Context requireContext() {
		final Context context = this.getContext();
		if (context == null) {
			throw new IllegalStateException("Page " + this.getPageOwner() + " not attached to an context.");
		}
		return context;
	}

	@Nullable
	@Override
	public FragmentActivity getFragmentActivity() {
		return AppPageControllerHelper.getFragmentActivity(this);
	}

	@NonNull
	@Override
	public final FragmentActivity requireFragmentActivity() {
		final FragmentActivity fragmentActivity = this.getFragmentActivity();
		if (fragmentActivity == null) {
			throw new IllegalStateException("Page " + this.getPageOwner() + " not attached to an activity.");
		}
		return fragmentActivity;
	}

	@Nullable
	@Override
	public View getPageView() {
		return this.mPageView;
	}

	@NonNull
	@Override
	public final View requirePageView() {
		final View pageView = this.getPageView();
		if (pageView == null) {
			throw new IllegalStateException("Page " + this.getPageOwner() + " View does not have a create.");
		}
		return pageView;
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
	public UILayoutController getLayoutController() {
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
			ActivityCompat.finishAfterTransition(this.requireFragmentActivity());
		}
	}

	@CallSuper
	@OnLifecycleEvent(Lifecycle.Event.ON_START)
	protected void onStart() {
		Logger.i(this.getPageOwner().getClass().getName() + " ====== onStart()");
	}

	@CallSuper
	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
	protected void onResume() {
		Logger.i(this.getPageOwner().getClass().getName() + " ====== onResume()");
	}

	@CallSuper
	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
	protected void onPause() {
		Logger.i(this.getPageOwner().getClass().getName() + " ====== onPause()");
	}

	@CallSuper
	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	protected void onStop() {
		Logger.i(this.getPageOwner().getClass().getName() + " ====== onStop()");
	}

	@CallSuper
	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
	protected void onDestroy() {
		Logger.i(this.getPageOwner().getClass().getName() + " ====== onDestroy()");
		try {
			this.getLayoutController().recycled();
		} catch (IllegalStateException e) {
			Logger.e(e);
		} finally {
			if (this.mViewController != null) {
				this.mViewController.recycled();
				this.mViewController = null;
			}
		}
	}

	@NonNull
	final PageProvider getPageProvider() {
		return this.mPageProvider;
	}
}
