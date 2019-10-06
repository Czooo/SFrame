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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.sframe.annotation.RunWithAsync;
import androidx.sframe.helper.AnnotationHelper;
import androidx.sframe.ui.NavAgentActivity;
import androidx.sframe.ui.controller.AppNavController;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIToolbarController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.utils.AppNavigator;
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
			final boolean appPageInterface = AnnotationHelper
					.isAppPageInterface(pageOwner.getClass());

			final ViewGroup realContainer;
			if (appPageInterface) {
				realContainer = new SRelativeLayout(inflater.getContext());
			} else {
				realContainer = container;
			}

			if (pageOwner instanceof AppPageController.PageViewProvider) {
				pageView = ((PageViewProvider) pageOwner).onPageCreateView(inflater, realContainer, savedInstanceState);
			}
			if (pageView == null) {
				final int layoutId = this.getPageProvider().onPageLayoutId(savedInstanceState);
				if (layoutId != 0) {
					pageView = inflater.inflate(layoutId, realContainer, false);
				}
			}

			if (appPageInterface) {
				final boolean runWithAsync = AnnotationHelper.isRunWithAsync(this.getClass())
						|| AnnotationHelper.isRunWithAsync(pageOwner.getClass());
				((SRelativeLayout) realContainer)
						.getLayoutController()
						.setOnDataSourceListener(this)
						.setShouldRunWithAsync(runWithAsync)
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
		AppNavigator.setAppPageController(this.getPageView(), this);
		AppNavigator.setAppNavController(this.getPageView(), this.getNavController());
		try {
			if (this.mIsViewCreated) {
				this.onPreViewCreated(savedInstanceState);
				final PageProvider provider = this.getPageProvider();
				provider.onPageViewCreated(savedInstanceState);
				this.mIsViewCreated = false;
			}
		} catch (Exception e) {
			Logger.e(e);
		} finally {
			final UILayoutController layoutController = this.getPreLayoutController();
			if (layoutController != null) {
				if (UILayoutController.LayoutType.None.key == layoutController.getCurLayoutKey()) {
					layoutController.layoutOfLoading(savedInstanceState);
				} else {
					layoutController.refreshed(savedInstanceState);
				}
			}
		}
	}

	@CallSuper
	protected void onPreViewCreated(@Nullable Bundle savedInstanceState) throws Exception {
		final UILayoutController layoutController = this.getPreLayoutController();
		if (layoutController != null) {
			final Bundle arguments = AppPageControllerHelper.getArguments(this);
			final boolean shouldPagePopEnabled = NavAgentActivity.isShouldPagePopEnabled(arguments);
			layoutController
					.setToolbarLayoutStableMode(false)
					.getToolbarController()
					.getToolbarMethod()
					.setPopEnabled(shouldPagePopEnabled);
		}
	}

	@CallSuper
	@Override
	public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
		// no-op
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
		final UILayoutController layoutController = this.getPreLayoutController();
		if (layoutController == null) {
			if (this.mViewController == null) {
				this.mViewController = new UIViewControllerImpl(this.requirePageView());
			}
			return this.mViewController;
		}
		return layoutController.getViewController();
	}

	@Nullable
	public UILayoutController getPreLayoutController() {
		final View pageView = this.getPageView();
		if (pageView instanceof SRelativeLayout) {
			final SRelativeLayout sRelativeLayout = (SRelativeLayout) pageView;
			return sRelativeLayout.getLayoutController();
		}
		return null;
	}

	@NonNull
	@Override
	public final UILayoutController getLayoutController() {
		final UILayoutController layoutController = this.getPreLayoutController();
		if (layoutController == null) {
			throw new IllegalStateException("AppPageController " + this + " does not have a UILayoutController set");
		}
		return layoutController;
	}

	@NonNull
	@Override
	public final UIToolbarController getToolbarController() {
		return this.getLayoutController().getToolbarController();
	}

	@NonNull
	@Override
	public AppNavController<Page> getNavController() {
		if (this.mAppNavController == null) {
			this.mAppNavController = new AppNavControllerImpl<>(this);
		}
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
		if (!this.getNavController().navigateUp()) {
			this.getNavController().finishActivity();
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
		final UILayoutController layoutController = this.getPreLayoutController();
		if (layoutController != null) {
			layoutController.recycled();
		}
		if (this.mViewController != null) {
			this.mViewController.recycled();
			this.mViewController = null;
		}
	}

	@NonNull
	final PageProvider getPageProvider() {
		return this.mPageProvider;
	}
}
