package androidx.demon.widget.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Author create by ok on 2019/2/15
 * Email : ok@163.com.
 */
public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

	private static final int VERTICAL = OrientationHelper.VERTICAL;

	private int orientation = -1;
	private int spanCount = -1;
	private int verticalSpacing;
	private int horizontalSpacing;
	private int ignoreFirstCount;

	public RecyclerItemDecoration(int spacing) {
		this(spacing, spacing);
	}

	public RecyclerItemDecoration(int verticalSpacing, int horizontalSpacing) {
		this.verticalSpacing = verticalSpacing;
		this.horizontalSpacing = horizontalSpacing;
	}

	public RecyclerItemDecoration setIgnoreFirstCount(int ignoreFirstCount) {
		this.ignoreFirstCount = ignoreFirstCount;
		return this;
	}

	@Override
	public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		if (orientation == -1) {
			orientation = getOrientation(parent);
		}

		if (spanCount == -1) {
			spanCount = getTotalSpan(parent);
		}

		int childCount = parent.getLayoutManager().getItemCount();
		int childIndex = parent.getChildAdapterPosition(view);

		/* INVALID SPAN */
		if (spanCount < 1) return;

		int realIgnoreFirstCount = ignoreFirstCount;

		RecyclerView.Adapter<?> mAdapter = parent.getAdapter();
		if (RecyclerAdapter.class.isInstance(mAdapter)) {
			final RecyclerAdapter<?> mRecyclerAdapter = (RecyclerAdapter<?>) mAdapter;
			final int itemViewType = mRecyclerAdapter.getItemViewType(childIndex);
			final int hostItemViewType = mRecyclerAdapter.getHostItemViewType(itemViewType);

			if (RecyclerAdapter.HOST_ITEM_TYPE_HEADER == hostItemViewType ||
					RecyclerAdapter.HOST_ITEM_TYPE_FOOTER == hostItemViewType ||
					RecyclerAdapter.HOST_ITEM_TYPE_LOAD == hostItemViewType ||
					RecyclerAdapter.HOST_ITEM_TYPE_EMPTY == hostItemViewType) {
				return;
			}

			if (mRecyclerAdapter.hasHeaderAdapter()) {
				realIgnoreFirstCount = ignoreFirstCount + mRecyclerAdapter.getHeaderAdapter().getItemCount();
			}
		}

		if (childIndex < realIgnoreFirstCount) {
			return;
		}

		int itemSpanSize = getItemSpanSize(parent, childIndex);
		int spanIndex = getItemSpanIndex(parent, childIndex);

		setSpacings(outRect, parent, childCount, childIndex, itemSpanSize, spanIndex);
	}

	protected void setSpacings(Rect outRect, RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {
		outRect.top = verticalSpacing / 2;
		outRect.bottom = verticalSpacing / 2;
		outRect.left = horizontalSpacing / 2;
		outRect.right = horizontalSpacing / 2;

		if (isTopEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
			outRect.top = verticalSpacing;
		}

		if (isLeftEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
			outRect.left = horizontalSpacing;
		}

		if (isRightEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
			outRect.right = horizontalSpacing;
		}

		if (isBottomEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
			outRect.bottom = verticalSpacing;
		}
	}

	@SuppressWarnings("all")
	protected int getTotalSpan(RecyclerView parent) {
		RecyclerView.LayoutManager mgr = parent.getLayoutManager();
		if (mgr instanceof GridLayoutManager) {
			return ((GridLayoutManager) mgr).getSpanCount();
		} else if (mgr instanceof StaggeredGridLayoutManager) {
			return ((StaggeredGridLayoutManager) mgr).getSpanCount();
		} else if (mgr instanceof LinearLayoutManager) {
			return 1;
		}
		return -1;
	}

	@SuppressWarnings("all")
	protected int getItemSpanSize(RecyclerView parent, int childIndex) {
		RecyclerView.LayoutManager mgr = parent.getLayoutManager();
		if (mgr instanceof GridLayoutManager) {
			return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanSize(childIndex);
		} else if (mgr instanceof StaggeredGridLayoutManager) {
			return 1;
		} else if (mgr instanceof LinearLayoutManager) {
			return 1;
		}
		return -1;
	}

	@SuppressWarnings("all")
	protected int getItemSpanIndex(RecyclerView parent, int childIndex) {
		RecyclerView.LayoutManager mgr = parent.getLayoutManager();
		if (mgr instanceof GridLayoutManager) {
			return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanIndex(childIndex, spanCount);
		} else if (mgr instanceof StaggeredGridLayoutManager) {
			return childIndex % spanCount;
		} else if (mgr instanceof LinearLayoutManager) {
			return 0;
		}
		return -1;
	}

	@SuppressWarnings("all")
	protected int getOrientation(RecyclerView parent) {
		RecyclerView.LayoutManager mgr = parent.getLayoutManager();
		if (mgr instanceof LinearLayoutManager) {
			return ((LinearLayoutManager) mgr).getOrientation();
		} else if (mgr instanceof GridLayoutManager) {
			return ((GridLayoutManager) mgr).getOrientation();
		} else if (mgr instanceof StaggeredGridLayoutManager) {
			return ((StaggeredGridLayoutManager) mgr).getOrientation();
		}
		return VERTICAL;
	}

	protected boolean isLeftEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {
		if (orientation == VERTICAL) {
			return spanIndex == 0;
		} else {
			return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);
		}
	}

	protected boolean isRightEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {
		if (orientation == VERTICAL) {
			return (spanIndex + itemSpanSize) == spanCount;
		} else {
			return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);
		}
	}

	protected boolean isTopEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {
		if (orientation == VERTICAL) {
			return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);
		} else {
			return spanIndex == 0;
		}
	}

	protected boolean isBottomEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {
		if (orientation == VERTICAL) {
			return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);
		} else {
			return (spanIndex + itemSpanSize) == spanCount;
		}
	}

	protected boolean isFirstItemEdgeValid(boolean isOneOfFirstItems, RecyclerView parent, int childIndex) {
		int totalSpanArea = 0;
		if (isOneOfFirstItems) {
			for (int i = childIndex; i >= 0; i--) {
				totalSpanArea = totalSpanArea + getItemSpanSize(parent, i);
			}
		}
		return isOneOfFirstItems && totalSpanArea <= spanCount;
	}

	protected boolean isLastItemEdgeValid(boolean isOneOfLastItems, RecyclerView parent, int childCount, int childIndex, int spanIndex) {
		int totalSpanRemaining = 0;
		if (isOneOfLastItems) {
			for (int i = childIndex; i < childCount; i++) {
				totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i);
			}
		}
		return isOneOfLastItems && (totalSpanRemaining <= spanCount - spanIndex);
	}
}
