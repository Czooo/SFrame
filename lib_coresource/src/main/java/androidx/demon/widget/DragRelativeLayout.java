package androidx.demon.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import org.parent.refreshview.helper.DragHelper;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;

/**
 * Author create by ok on 2019-06-14
 * Email : ok@163.com.
 */
public class DragRelativeLayout extends RelativeLayout implements NestedScrollingParent, NestedScrollingChild {

	private final DragHelper mDragHelper;

	public DragRelativeLayout(Context context) {
		this(context, null);
	}

	public DragRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mDragHelper = DragHelper.attachToView(this);
		this.mDragHelper.setDraggedScrollEnabled(false);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.mDragHelper.onInterceptTouchEvent(event)) {
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		if (this.mDragHelper.onTouch(event)) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes) {
		return this.mDragHelper.onStartNestedScroll(child, target, nestedScrollAxes);
	}

	@Override
	public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
		this.mDragHelper.onNestedScrollAccepted(child, target, axes);
	}

	@Override
	public int getNestedScrollAxes() {
		return this.mDragHelper.getNestedScrollAxes();
	}

	@Override
	public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
		this.mDragHelper.onNestedPreScroll(target, dx, dy, consumed);
	}

	@Override
	public void onNestedScroll(@NonNull final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed) {
		this.mDragHelper.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
	}

	@Override
	public void onStopNestedScroll(@NonNull View target) {
		this.mDragHelper.onStopNestedScroll(target);
	}

	// NestedScrollingChild

	@Override
	public void setNestedScrollingEnabled(boolean enabled) {
		this.mDragHelper.setNestedScrollingEnabled(enabled);
	}

	@Override
	public boolean isNestedScrollingEnabled() {
		return this.mDragHelper.isNestedScrollingEnabled();
	}

	@Override
	public boolean startNestedScroll(int axes) {
		return this.mDragHelper.startNestedScroll(axes);
	}

	@Override
	public void stopNestedScroll() {
		this.mDragHelper.stopNestedScroll();
	}

	@Override
	public boolean hasNestedScrollingParent() {
		return this.mDragHelper.hasNestedScrollingParent();
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
		return this.mDragHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
		return this.mDragHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
	}

	@Override
	public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
		return this.dispatchNestedPreFling(velocityX, velocityY);
	}

	@Override
	public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
		return this.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
		return this.mDragHelper.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@Override
	public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
		return this.mDragHelper.dispatchNestedPreFling(velocityX, velocityY);
	}

	@NonNull
	public final DragHelper getDragHelper() {
		return this.mDragHelper;
	}
}
