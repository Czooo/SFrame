package androidx.demon.helper;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.parent.refreshview.helper.DragHelper;
import org.parent.refreshview.widget.RefreshMode;
import org.parent.refreshview.widget.RefreshView;

import java.lang.ref.WeakReference;

import androidx.demon.ui.abs.AbsActivity;
import androidx.demon.ui.abs.AbsDialogFragment;
import androidx.demon.ui.abs.AbsFragment;
import androidx.demon.ui.abs.AbsListActivity;
import androidx.demon.ui.abs.AbsListDialogFragment;
import androidx.demon.ui.abs.AbsListFragment;
import androidx.demon.ui.abs.AbsListPopupWindow;
import androidx.demon.ui.abs.AbsPopupWindow;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.ui.controller.UILayoutController;
import androidx.demon.ui.controller.UIObjectListController;
import androidx.demon.widget.DragRelativeLayout;

/**
 * Author create by ok on 2019/3/17
 * Email : ok@163.com.
 */
public class AppPageDragHelper implements DragHelper.OnChildScrollCallback {

	public static AppPageDragHelper attachToPage(AppPageController<?> pageController) {
		return new AppPageDragHelper(pageController);
	}

	private final DragHelper mDragHelper;
	private final AppPageController<?> mPageController;

	private DragHelper.DragMode mDragMode = DragHelper.DragMode.DRAG_START;

	AppPageDragHelper(AppPageController<?> pageController) {
		this.mPageController = pageController;

		if (DragRelativeLayout.class.isInstance(pageController.getLayoutController().getParentView())) {
			this.mDragHelper = ((DragRelativeLayout) pageController.getLayoutController().getParentView()).getDragHelper();
		} else {
			this.mDragHelper = DragHelper.attachToView((ViewGroup) pageController.getLayoutController().getParentView());
			// touch
			pageController.getLayoutController().getParentView().setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					return mDragHelper.onTouch(event);
				}
			});
		}
		// 启动拖动
		this.mDragHelper.setCallback(new Callback());
		this.mDragHelper.setOrientation(DragHelper.VERTICAL);
		this.mDragHelper.setDraggedScrollEnabled(true);
		this.mDragHelper.setOnChildScrollCallback(this);
		this.ensurePageRefreshView();
	}

	@Override
	public boolean canChildScrollStart(DragHelper helper) {
		return false;
	}

	@Override
	public boolean canChildScrollEnd(DragHelper helper) {
		return false;
	}

	public AppPageDragHelper setOnChildScrollCallback(DragHelper.OnChildScrollCallback callback) {
		mDragHelper.setOnChildScrollCallback(callback);
		return this;
	}

	public AppPageDragHelper addOnScrollListener(DragHelper.OnScrollListener listener) {
		mDragHelper.addOnScrollListener(listener);
		return this;
	}

	public AppPageDragHelper removeOnScrollListener(DragHelper.OnScrollListener listener) {
		mDragHelper.removeOnScrollListener(listener);
		return this;
	}

	public AppPageDragHelper setRefreshView(RefreshView refreshView) {
		mDragHelper.setOnChildScrollCallback(new OnRefreshChildScrollCallback(refreshView));
		return this;
	}

	public AppPageDragHelper setOrientation(int orientation) {
		mDragHelper.setOrientation(orientation);
		return this;
	}

	public AppPageDragHelper setDraggedCloseDirection(DragHelper.DragMode dragMode) {
		mDragMode = dragMode;
		return this;
	}

	private void ensurePageRefreshView() {
		UIObjectListController<?, ?> objectListController = null;

		if (AbsListActivity.class.isInstance(mPageController.getPageOwner())) {
			objectListController = ((AbsListActivity) mPageController.getPageOwner()).getObjectListController();
		} else if (AbsListFragment.class.isInstance(mPageController.getPageOwner())) {
			objectListController = ((AbsListFragment) mPageController.getPageOwner()).getObjectListController();
		} else if (AbsListDialogFragment.class.isInstance(mPageController.getPageOwner())) {
			objectListController = ((AbsListDialogFragment) mPageController.getPageOwner()).getObjectListController();
		} else if (AbsListPopupWindow.class.isInstance(mPageController.getPageOwner())) {
			objectListController = ((AbsListPopupWindow) mPageController.getPageOwner()).getObjectListController();
		}

		if (objectListController != null) {
			setRefreshView(objectListController.getRefreshView());
		}
	}

	private final class Callback implements DragHelper.Callback, DragHelper.OnSmoothScrollListener {

		@Override
		public void onScrollStateChanged(DragHelper helper, DragHelper.DragMode dragMode, int newState, int scrollOffset) {
			final View targetDragView;

			if(dragMode == mDragMode) {
				targetDragView = mPageController.getLayoutController().getParentView();
			} else {
				targetDragView = mPageController.getLayoutController().requireLayoutAt(UILayoutController.LayoutType.Content.key).getContentView();
			}

			if (newState == DragHelper.SCROLL_STATE_IDLE) {

				final int absScrollOffset = Math.abs(scrollOffset);
				final int totalScrollOffset;

				// 同一个方向
				if(dragMode == mDragMode) {
					// 达到条件，移除
					if(absScrollOffset >= 100) {

						final int direction;

						if(dragMode.hasDragStart()) {
							direction = 1;
						} else {
							direction = -1;
						}

						if (DragHelper.VERTICAL == helper.getOrientation()) {
							totalScrollOffset = targetDragView.getMeasuredHeight() * direction;
						} else {
							totalScrollOffset = targetDragView.getMeasuredWidth() * direction;
						}
					} else {
						totalScrollOffset = 0;
					}
				} else {
					totalScrollOffset = 0;
				}

				if(totalScrollOffset != 0) {
					helper.setDraggedScrollEnabled(false);
					helper.smoothScrollTo(targetDragView, totalScrollOffset, this);
				} else {
					helper.smoothScrollTo(targetDragView, totalScrollOffset);
				}
			} else {
				helper.scrollTo(targetDragView, scrollOffset);
			}
		}

		@Override
		public void onSmoothScrollFinished(float oldScrollOffset, float newScrollOffset) {
			if (AbsPopupWindow.class.isInstance(mPageController.getPageOwner())) {
				((AbsPopupWindow) mPageController.getPageOwner()).dismiss();
			} else if (AbsDialogFragment.class.isInstance(mPageController.getPageOwner())) {
				((AbsDialogFragment) mPageController.getPageOwner()).dismiss();
			} else if (AbsFragment.class.isInstance(mPageController.getPageOwner())) {
				mPageController.getAppNavController().popBackStack();
			} else if (AbsActivity.class.isInstance(mPageController.getPageOwner())) {
				mPageController.getAppNavController().popBackStack();
			}
		}
	}

	final class OnRefreshChildScrollCallback implements DragHelper.OnChildScrollCallback {

		private WeakReference<RefreshView> mWeakReference;

		OnRefreshChildScrollCallback(RefreshView refreshView) {
			mWeakReference = new WeakReference<>(refreshView);
		}

		@Override
		public boolean canChildScrollStart(DragHelper helper) {
			final RefreshView refreshView = mWeakReference.get();
			final int orientation = ensureRefreshOrientation();

			if (canChildScroll(orientation, true)) {
				return true;
			}

			if (DragHelper.VERTICAL == orientation) {
				return refreshView.getTargetDragView().canScrollVertically(-1);
			} else {
				return refreshView.getTargetDragView().canScrollHorizontally(-1);
			}
		}

		@Override
		public boolean canChildScrollEnd(DragHelper helper) {
			final RefreshView refreshView = mWeakReference.get();
			final int orientation = ensureRefreshOrientation();

			if (canChildScroll(orientation, false)) {
				return true;
			}

			if (DragHelper.VERTICAL == orientation) {
				return refreshView.getTargetDragView().canScrollVertically(1);
			} else {
				return refreshView.getTargetDragView().canScrollHorizontally(1);
			}
		}

		private boolean canChildScroll(int orientation, boolean canChildScrollStart) {
			final RefreshView refreshView = mWeakReference.get();
			final RefreshMode refreshMode = ensureRefreshMode();
			final int refreshOrientation = ensureRefreshOrientation();

			if (refreshView.isRefreshing()) {
				return true;
			}

			boolean canChildScroll = true;

			if (DragHelper.VERTICAL == orientation) {
				if (canChildScrollStart) {
					canChildScroll = refreshView.getTargetDragView().canScrollVertically(-1);
				} else {
					canChildScroll = refreshView.getTargetDragView().canScrollVertically(1);
				}
			} else {
				if (canChildScrollStart) {
					canChildScroll = refreshView.getTargetDragView().canScrollHorizontally(-1);
				} else {
					canChildScroll = refreshView.getTargetDragView().canScrollHorizontally(1);
				}
			}

			if (!canChildScroll && refreshOrientation == orientation) {
				// 可进行阻尼移除操作
				if (canChildScrollStart) {
					return refreshMode.hasStartMode();
				}
				return refreshMode.hasEndMode();
			}
			return true;
		}

		private int ensureRefreshOrientation() {
			if (mWeakReference != null) {
				return mWeakReference.get().getOrientation();
			}
			return DragHelper.VERTICAL;
		}

		private RefreshMode ensureRefreshMode() {
			if (mWeakReference != null) {
				return mWeakReference.get().getRefreshMode();
			}
			return RefreshMode.REFRESH_MODE_NONE;
		}
	}
}
