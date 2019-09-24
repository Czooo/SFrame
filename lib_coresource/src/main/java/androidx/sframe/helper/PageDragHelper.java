package androidx.sframe.helper;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.widget.DragRelativeLayout;
import androidx.demon.widget.RefreshLayout;
import androidx.demon.widget.helper.NestedHelper;
import androidx.demon.widget.helper.NestedScrollingHelper;
import androidx.sframe.ui.abs.AbsActivity;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.ui.abs.AbsFragment;
import androidx.sframe.ui.abs.AbsListActivity;
import androidx.sframe.ui.abs.AbsListDialogFragment;
import androidx.sframe.ui.abs.AbsListFragment;
import androidx.sframe.ui.abs.AbsListPopupWindow;
import androidx.sframe.ui.abs.AbsPopupWindow;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.UIObjectListController;
import androidx.sframe.ui.controller.impl.UILayout;

/**
 * Author create by ok on 2019-09-03
 * Email : ok@163.com.
 */
public class PageDragHelper<Page> extends DragRelativeLayout.DragManager {

	public static <P> PageDragHelper<P> attachToPage(@NonNull AppPageController<P> pageController) {
		return new PageDragHelper<>(pageController);
	}

	public interface OnDragCloseListener<Page> {

		void onDragClose(@NonNull PageDragHelper<Page> helper);
	}

	public static final int DRAG_DIRECTION_NONE = 0;
	public static final int DRAG_DIRECTION_LEFT = 1;
	public static final int DRAG_DIRECTION_TOP = 2;
	public static final int DRAG_DIRECTION_RIGHT = 3;
	public static final int DRAG_DIRECTION_BOTTOM = 4;

	@IntDef({DRAG_DIRECTION_LEFT, DRAG_DIRECTION_TOP, DRAG_DIRECTION_RIGHT, DRAG_DIRECTION_BOTTOM})
	@Retention(RetentionPolicy.SOURCE)
	@interface DragDirection {
	}

	private static final int DEFAULT_CLOSE_DISTANCE = 100;

	private final WeakReference<AppPageController<Page>> mWeakReference;

	@DragDirection
	private int mDragDirection = DRAG_DIRECTION_NONE;
	private int mPreDragCloseDistance = DEFAULT_CLOSE_DISTANCE;
	private boolean mIsFinishRollbackInProgress = false;
	private boolean mDragCloseEnabled = false;

	private View mDragView;
	private OnDragCloseListener<Page> mOnDragCloseListener;

	private PageDragHelper(final @NonNull AppPageController<Page> pageController) {
		this.mWeakReference = new WeakReference<>(pageController);

		final View parentView = pageController.getLayoutController().getParentView();
		if (parentView instanceof DragRelativeLayout) {
			((DragRelativeLayout) parentView).setShouldStartNestedScroll(true);
			((DragRelativeLayout) parentView).setDragManager(this);
			// default options
			this.setOnDragCloseListener(new SimpleDragCloseListener<Page>());
			this.setScrollingDuration(750);

			// check list page
			final Page pageOwner = pageController.getPageOwner();
			RefreshLayout mRefreshLayout = null;
			if (pageOwner instanceof AbsListDialogFragment) {
				mRefreshLayout = ((AbsListDialogFragment) pageOwner).getObjectListController().getRefreshLayout();
			} else if (pageOwner instanceof AbsListPopupWindow) {
				mRefreshLayout = ((AbsListPopupWindow) pageOwner).getObjectListController().getRefreshLayout();
			} else if (pageOwner instanceof AbsListActivity) {
				mRefreshLayout = ((AbsListActivity) pageOwner).getObjectListController().getRefreshLayout();
			} else if (pageOwner instanceof AbsListFragment) {
				mRefreshLayout = ((AbsListFragment) pageOwner).getObjectListController().getRefreshLayout();
			}
			if (mRefreshLayout == null) {
				return;
			}
			this.setChildDragRelativeLayout(mRefreshLayout);
		}
	}

	@Override
	public boolean shouldStartNestedScroll() {
		if (this.mDragView == null) {
			this.mDragView = this.getDragView();
		}
		return this.mDragView != null
				&& this.mDragView.isShown()
				&& !this.mIsFinishRollbackInProgress;
	}

	@Override
	public boolean canChildScroll(int direction) {
		if (this.canScrollHorizontally()) {
			return this.mDragView.canScrollHorizontally(direction);
		}
		if (this.canScrollVertically()) {
			return this.mDragView.canScrollVertically(direction);
		}
		return true;
	}

	@Override
	public void onScrollBy(@NonNull NestedScrollingHelper helper, int dx, int dy, @NonNull int[] consumed) {
		consumed[0] = dx;
		consumed[1] = dy;

		int direction = helper.getScrollDirection();
		if (this.canScrollHorizontally()) {
			direction = helper.getPreScrollDirection(dx);
		} else if (this.canScrollVertically()) {
			direction = helper.getPreScrollDirection(dy);
		}

		final int preScrollOffsetX = (int) ((helper.getScrollOffsetX() + consumed[0]) * this.getFrictionRatio() + NestedHelper.getDirectionDifference(direction));
		final int preScrollOffsetY = (int) ((helper.getScrollOffsetY() + consumed[1]) * this.getFrictionRatio() + NestedHelper.getDirectionDifference(direction));

		if (this.mDragCloseEnabled && -direction == this.getDragCloseDirection()) {
			this.getDragRelativeLayout().setTranslationX(-preScrollOffsetX);
			this.getDragRelativeLayout().setTranslationY(-preScrollOffsetY);
		} else {
			this.mDragView.scrollTo(preScrollOffsetX, preScrollOffsetY);
		}
	}

	@Override
	public void onScrollStateChanged(@NonNull NestedScrollingHelper helper, int scrollState) {
		if (NestedScrollingHelper.SCROLL_STATE_IDLE == scrollState) {
			final int direction = helper.getScrollDirection();

			int distanceX = 0;
			int distanceY = 0;

			if (this.mDragCloseEnabled && -direction == this.getDragCloseDirection()) {
				// identical direction
				if (this.mOnDragCloseListener != null) {
					final int maxDistanceX = (int) (this.getDragRelativeLayout().getWidth() / this.getFrictionRatio() * direction + NestedHelper.getDirectionDifference(direction));
					final int maxDistanceY = (int) (this.getDragRelativeLayout().getHeight() / this.getFrictionRatio() * direction + NestedHelper.getDirectionDifference(direction));

					if ((this.canScrollHorizontally() && Math.abs(maxDistanceX) <= Math.abs(helper.getScrollOffsetX()) && maxDistanceX != 0)
							|| (this.canScrollVertically() && Math.abs(maxDistanceY) <= Math.abs(helper.getScrollOffsetY()) && maxDistanceY != 0)) {
						this.mOnDragCloseListener.onDragClose(this);
						return;
					}

					if (Math.abs(helper.getScrollOffset()) >= this.getPreDragCloseDistance()) {
						// smooth to max distance
						if (this.canScrollHorizontally()) {
							distanceX = maxDistanceX;
						}
						if (this.canScrollVertically()) {
							distanceY = maxDistanceY;
						}
						if (distanceX != 0 || distanceY != 0) {
							this.mIsFinishRollbackInProgress = true;
						}
					}
				}
			}
			this.smoothScrollTo(distanceX, distanceY);
		}
	}

	public PageDragHelper<Page> setDragCloseEnabled(boolean dragCloseEnabled) {
		this.mDragCloseEnabled = dragCloseEnabled;
		return this;
	}

	public PageDragHelper<Page> setFrictionRatio(@FloatRange(from = 0.1f, to = 1.f) float frictionRatio) {
		this.getDragRelativeLayout().setFrictionRatio(frictionRatio);
		return this;
	}

	public PageDragHelper<Page> setScrollingDuration(int duration) {
		this.getDragRelativeLayout().getNestedScrollingHelper().setScrollingDuration(duration);
		return this;
	}

	public PageDragHelper<Page> setDragCloseDirection(@DragDirection int dragDirection) {
		if (this.mDragDirection != dragDirection) {
			this.mDragDirection = dragDirection;

			switch (dragDirection) {
				case DRAG_DIRECTION_LEFT:
				case DRAG_DIRECTION_RIGHT:
					this.getDragRelativeLayout().setOrientation(DragRelativeLayout.HORIZONTAL);
					break;
				case DRAG_DIRECTION_TOP:
				case DRAG_DIRECTION_BOTTOM:
					this.getDragRelativeLayout().setOrientation(DragRelativeLayout.VERTICAL);
					break;
			}
		}
		return this;
	}

	public PageDragHelper<Page> setPreDragCloseDistance(int dragCloseDistance) {
		this.mPreDragCloseDistance = dragCloseDistance;
		return this;
	}

	public PageDragHelper<Page> setOnDragCloseListener(@NonNull OnDragCloseListener<Page> listener) {
		this.mOnDragCloseListener = listener;
		return this;
	}

	public PageDragHelper<Page> setUIObjectListController(@NonNull UIObjectListController<?, ?> controller) {
		return this.setChildDragRelativeLayout(controller.getRefreshLayout());
	}

	public PageDragHelper<Page> setChildDragRelativeLayout(@NonNull DragRelativeLayout dragRelativeLayout) {
		return this.setOnChildScrollCallback(new SimpleChildScrollCallback(dragRelativeLayout));
	}

	public PageDragHelper<Page> setOnChildScrollCallback(@NonNull DragRelativeLayout.OnChildScrollCallback callback) {
		this.getDragRelativeLayout().setOnChildScrollCallback(callback);
		return this;
	}

	private float getPreDragCloseDistance() {
		return Math.abs(this.mPreDragCloseDistance) / this.getFrictionRatio();
	}

	@SuppressLint("RtlHardcoded")
	private int getDragCloseDirection() {
		if (DRAG_DIRECTION_LEFT == this.mDragDirection
				|| DRAG_DIRECTION_TOP == this.mDragDirection) {
			return -1;
		}
		if (DRAG_DIRECTION_RIGHT == this.mDragDirection
				|| DRAG_DIRECTION_BOTTOM == this.mDragDirection) {
			return 1;
		}
		return 0;
	}

	private void testDragClose() {
		final AppPageController<Page> pageController = this.getPageController();
		final Page pageOwner = pageController.getPageOwner();
		if (pageOwner instanceof AbsDialogFragment) {
			((AbsDialogFragment) pageOwner).dismiss();
		} else if (pageOwner instanceof AbsPopupWindow) {
			((AbsPopupWindow) pageOwner).dismiss();
		} else if (pageOwner instanceof AbsActivity) {
			pageController.getAppNavController().popBackStack();
		} else if (pageOwner instanceof AbsFragment) {
			pageController.getAppNavController().popBackStack();
		}
	}

	@Nullable
	private View getDragView() {
		final UILayout uiLayout = this.getPageController().getLayoutController().getLayoutAt(UILayoutController.LayoutType.Content.key);
		if (uiLayout == null) {
			return null;
		}
		return uiLayout.getContentView();
	}

	@NonNull
	private AppPageController<Page> getPageController() {
		return this.mWeakReference.get();
	}

	public static final class SimpleDragCloseListener<Page> implements OnDragCloseListener<Page> {

		@Override
		public void onDragClose(@NonNull PageDragHelper<Page> helper) {
			helper.testDragClose();
		}
	}

	public static final class SimpleChildScrollCallback implements DragRelativeLayout.OnChildScrollCallback {

		private final DragRelativeLayout mDragRelativeLayout;

		public SimpleChildScrollCallback(@NonNull DragRelativeLayout dragRelativeLayout) {
			this.mDragRelativeLayout = dragRelativeLayout;
		}

		@Override
		public boolean canChildScroll(@NonNull ViewGroup container, int direction) {
			final DragRelativeLayout mDragRelativeLayout = (DragRelativeLayout) container;
			if ((this.mDragRelativeLayout.isDraggingToStart() && direction < 0)
					|| (this.mDragRelativeLayout.isDraggingToEnd() && direction > 0)) {
				return true;
			}
			if (mDragRelativeLayout.getOrientation() == this.mDragRelativeLayout.getOrientation()) {
				final DragRelativeLayout.DragManager mDragManager = this.mDragRelativeLayout.getDragManager();
				if (mDragManager != null) {
					return mDragManager.canChildScroll(direction);
				}
			}
			return false;
		}
	}
}
