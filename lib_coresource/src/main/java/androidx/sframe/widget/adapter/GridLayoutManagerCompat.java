package androidx.sframe.widget.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @Author create by Zoran on 2019-09-13
 * @Email : 171905184@qq.com
 * @Description :
 */
public class GridLayoutManagerCompat extends GridLayoutManager {

	/**
	 * Creates a vertical GridLayoutManager
	 *
	 * @param context   Current context, will be used to access resources.
	 * @param spanCount The number of columns in the grid
	 */
	public GridLayoutManagerCompat(Context context, int spanCount) {
		super(context, spanCount);
	}

	/**
	 * @param context       Current context, will be used to access resources.
	 * @param spanCount     The number of columns or rows in the grid
	 * @param orientation   Layout orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
	 * @param reverseLayout When set to true, layouts from end to start.
	 */
	public GridLayoutManagerCompat(Context context, int spanCount, int orientation, boolean reverseLayout) {
		super(context, spanCount, orientation, reverseLayout);
	}

	/**
	 * Constructor used when layout manager is set in XML by RecyclerView attribute
	 * "layoutManager". If spanCount is not specified in the XML, it defaults to a
	 * single column.
	 *
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 * @param defStyleRes
	 * @attr ref androidx.recyclerview.R.styleable#RecyclerView_spanCount
	 */
	public GridLayoutManagerCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
		final GridLayoutManager.SpanSizeLookup oldSpanSizeLookup = this.getSpanSizeLookup();
		if (oldSpanSizeLookup instanceof SpanSizeLookup) {
			((SpanSizeLookup) oldSpanSizeLookup).setLayoutManager(null);
		}
		super.setSpanSizeLookup(spanSizeLookup);
		if (spanSizeLookup instanceof SpanSizeLookup) {
			((SpanSizeLookup) spanSizeLookup).setLayoutManager(this);
		}
	}

	public static abstract class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

		private final RecyclerAdapter<?> mRecyclerAdapter;
		private GridLayoutManager mLayoutManager;

		public SpanSizeLookup(@NonNull RecyclerAdapter<?> adapter) {
			this.mRecyclerAdapter = adapter;
		}

		/**
		 * Returns the number of span occupied by the item at <code>position</code>.
		 *
		 * @param position The adapter position of the item
		 * @return The number of spans occupied by the item at the provided position
		 */
		@Override
		public final int getSpanSize(int position) {
			final int realPosition = this.mRecyclerAdapter.getRealPosition(position);
			final int itemViewType = this.mRecyclerAdapter.getItemViewType(position);
			final int hostItemViewType = this.mRecyclerAdapter.getHostItemViewType(itemViewType);

			if (RecyclerAdapter.HOST_ITEM_TYPE_HEADER == hostItemViewType) {
				return this.getHeaderSpanSize(realPosition);
			} else if (RecyclerAdapter.HOST_ITEM_TYPE_FOOTER == hostItemViewType) {
				return this.getFooterSpanSize(realPosition);
			} else if (this.mRecyclerAdapter.hasSingleLayoutByType(hostItemViewType)) {
				return this.getSingleLayoutSpanSize(realPosition);
			} else {
				return this.getRealSpanSize(realPosition);
			}
		}

		public int getSpanCount() {
			return this.mLayoutManager.getSpanCount();
		}

		public int getHeaderSpanSize(int position) {
			return this.getSpanCount();
		}

		public int getFooterSpanSize(int position) {
			return this.getSpanCount();
		}

		public int getSingleLayoutSpanSize(int position) {
			return this.getSpanCount();
		}

		public abstract int getRealSpanSize(int position);

		final void setLayoutManager(@Nullable GridLayoutManager layoutManager) {
			synchronized (this) {
				this.mLayoutManager = layoutManager;
			}
		}
	}

	public static class SimpleSpanSizeLookup extends SpanSizeLookup {

		public SimpleSpanSizeLookup(@NonNull RecyclerAdapter<?> adapter) {
			super(adapter);
		}

		@Override
		public int getRealSpanSize(int position) {
			return 1;
		}
	}
}
