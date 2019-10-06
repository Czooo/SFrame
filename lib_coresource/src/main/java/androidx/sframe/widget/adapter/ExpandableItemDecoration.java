package androidx.sframe.widget.adapter;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @Author create by Zoran on 2019-09-22
 * @Email : 171905184@qq.com
 * @Description :
 */
public class ExpandableItemDecoration extends RecyclerView.ItemDecoration {

	private final Rect mRect = new Rect();
	private OnItemTouchListener mOnItemTouchListener;
	private ExpandableRecyclerAdapter.ViewHolder<Object> mExpandableViewHolder;

	private boolean mIsShouldEnabled = true;

	@Override
	@SuppressLint("ClickableViewAccessibility")
	public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
		final RecyclerView.Adapter adapter = recyclerView.getAdapter();
		if (adapter == null) {
			return;
		}
		if (!(adapter instanceof ExpandableRecyclerAdapter)) {
			return;
		}

		final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		if (layoutManager == null) {
			return;
		}
		final int position;
		final int orientation;
		if (layoutManager instanceof LinearLayoutManager) {
			position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
			orientation = ((LinearLayoutManager) layoutManager).getOrientation();
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			position = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[0];
			orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
		} else {
			position = RecyclerView.NO_POSITION;
			orientation = RecyclerView.VERTICAL;
		}

		final ExpandableRecyclerAdapter<Object> recyclerAdapter = (ExpandableRecyclerAdapter<Object>) adapter;
		final int groupPosition = recyclerAdapter.getRealGroupPosition(position);
		if (this.mIsShouldEnabled && recyclerAdapter.hasGroupEnabled(groupPosition)) {
			if (this.mOnItemTouchListener == null) {
				this.mOnItemTouchListener = new OnItemTouchListener();
				recyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
			}
			// 保存画布参数到私有堆栈
			canvas.save();

			final int layoutPosition = recyclerAdapter.toLayoutPositionByGroupPosition(groupPosition);
			final int itemViewType = recyclerAdapter.getItemViewType(layoutPosition);

			ExpandableRecyclerAdapter.ViewHolder<Object> recyclerHolder = this.mExpandableViewHolder;
			if (recyclerHolder == null) {
				recyclerHolder = recyclerAdapter.onCreateGroupViewHolder(recyclerView, itemViewType);
				recyclerAdapter.bindAdapterByViewHolder(recyclerHolder);
			}
			recyclerHolder.setTempPosition(layoutPosition);
			recyclerAdapter.onBindViewHolder(recyclerHolder, layoutPosition, Collections.emptyList());

			// 依次调用 measure, layout, draw方法，将复杂头部显示在屏幕上
			final View itemView = recyclerHolder.getItemView();
			final int width = recyclerView.getWidth() - recyclerView.getPaddingLeft() - recyclerView.getPaddingRight();
			final int height = recyclerView.getHeight() - recyclerView.getPaddingTop() - recyclerView.getPaddingBottom();
			final int widthMeasureSpec = this.getWidthMeasureSpec(itemView, width);
			final int heightMeasureSpec = this.getHeightMeasureSpec(itemView, height);
			itemView.measure(widthMeasureSpec, heightMeasureSpec);
			itemView.layout(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingLeft() + itemView.getMeasuredWidth(), recyclerView.getPaddingTop() + itemView.getMeasuredHeight());

			int translateX = 0;
			int translateY = 0;
			if (RecyclerView.HORIZONTAL == orientation) {
				translateX = recyclerView.getPaddingLeft();
			} else if (RecyclerView.VERTICAL == orientation) {
				translateY = recyclerView.getPaddingTop();
			} else {
				throw new IllegalStateException("not orientation");
			}

			final RecyclerView.ViewHolder nextRecyclerHolder = recyclerView.findViewHolderForLayoutPosition(position);
			if (nextRecyclerHolder != null) {
				if (recyclerAdapter.getRealGroupPosition(position + 1) != groupPosition) {
					final View nextItemView = nextRecyclerHolder.itemView;
					final int scrollOffsetX = nextItemView.getWidth() + nextItemView.getLeft() - itemView.getMeasuredWidth();
					final int scrollOffsetY = nextItemView.getHeight() + nextItemView.getTop() - itemView.getMeasuredHeight();

					if (RecyclerView.HORIZONTAL == orientation) {
						if (scrollOffsetX <= translateX) {
							translateX = scrollOffsetX;
						}
					} else {
						if (scrollOffsetY <= translateY) {
							translateY = scrollOffsetY;
						}
					}
				}
			}
			canvas.translate(translateX, translateY);
			// 默认在视图顶部，无需平移，直接绘制
			itemView.draw(canvas);
			// 恢复画布到之前保存状态
			canvas.restore();
			// 手势可用区域
			final Rect rect = this.mRect;
			itemView.getGlobalVisibleRect(rect);
			if (RecyclerView.HORIZONTAL == orientation) {
				rect.left += recyclerView.getPaddingLeft();
				rect.right += translateX;
			} else {
				rect.top += recyclerView.getPaddingTop();
				rect.bottom += translateY;
			}
			// 当前显示的 Group
			this.mExpandableViewHolder = recyclerHolder;
		} else {
			this.clear();
		}
	}

	public void setShouldEnabled(boolean enabled) {
		this.mIsShouldEnabled = enabled;
	}

	private void clear() {
		this.mRect.left = 0;
		this.mRect.top = 0;
		this.mRect.right = 0;
		this.mRect.bottom = 0;
		this.mExpandableViewHolder = null;
	}

	private int getWidthMeasureSpec(@NonNull View view, int width) {
		final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
			return View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
		} else if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
			return View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
		} else {
			return View.MeasureSpec.makeMeasureSpec(layoutParams.width, View.MeasureSpec.EXACTLY);
		}
	}

	private int getHeightMeasureSpec(@NonNull View view, int height) {
		final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
			return View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
		} else if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
			return View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
		} else {
			return View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY);
		}
	}

	final class OnItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {

		private boolean preTouchClick;

		@Override
		public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {
			return this.dispatchTouchEvent(event);
		}

		@Override
		public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent event) {
			super.onTouchEvent(recyclerView, event);
			this.dispatchTouchEvent(event);
		}

		private boolean dispatchTouchEvent(@NonNull MotionEvent event) {
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					this.preTouchClick = this.contains(x, y);
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					if (this.preTouchClick) {
						this.preTouchClick = false;
						if (this.contains(x, y)) {
							this.performOnClick();
						}
					}
					break;
			}
			return this.preTouchClick;
		}

		private boolean performOnClick() {
			if (mExpandableViewHolder == null) {
				return false;
			}
			return mExpandableViewHolder.getItemView().performClick();
		}

		private boolean contains(int x, int y) {
			if (mExpandableViewHolder == null) {
				return false;
			}
			return mRect.contains(x, y);
		}
	}
}
