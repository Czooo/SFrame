package androidx.demon.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.demon.R;
import androidx.demon.ui.controller.UILayoutController;
import androidx.demon.ui.controller.UIToolbarController;
import androidx.demon.ui.controller.UIViewController;
import androidx.demon.ui.controller.impl.UILayout;
import androidx.demon.ui.controller.impl.UILayoutControllerImpl;

/**
 * Author create by ok on 2019-06-10
 * Email : ok@163.com.
 */
public class OverlapRelativeLayout extends DragRelativeLayout {

	private final UILayoutController mLayoutController;

	public OverlapRelativeLayout(Context context) {
		this(context, null);
	}

	public OverlapRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public OverlapRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mLayoutController = new UILayoutControllerImpl(this);

		final TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.OverlapRelativeLayout);
		final boolean preAsyncRefreshed = mTypedArray.getBoolean(R.styleable.OverlapRelativeLayout_preAsyncRefreshed, false);
		final boolean preManualLayoutMode = mTypedArray.getBoolean(R.styleable.OverlapRelativeLayout_preManualLayoutMode, false);
		final int preLayoutKey = mTypedArray.getInteger(R.styleable.OverlapRelativeLayout_preLayoutKey, UILayoutController.LayoutType.None.key);
		// default options
		this.getLayoutController()
				.setShouldManualLayoutMode(preManualLayoutMode)
				.setShouldAsyncRefreshed(preAsyncRefreshed)
				.layoutAt(preLayoutKey);
		// enabled default toolbar
		final String preTitleText = mTypedArray.getString(R.styleable.OverlapRelativeLayout_preTitleText);
		if (!TextUtils.isEmpty(preTitleText)) {
			this.getToolbarController()
					.getToolbarMethod()
					.setTitle(preTitleText);
		}
		final int preTitleImage = mTypedArray.getResourceId(R.styleable.OverlapRelativeLayout_preTitleImage, 0);
		if (preTitleImage != 0) {
			this.getToolbarController()
					.getToolbarMethod()
					.setTitleImageResource(preTitleImage);
		}
		mTypedArray.recycle();
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
		return layoutParams instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
		return new LayoutParams(layoutParams);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(this.getContext(), attrs);
	}

	@CallSuper
	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams layoutParams) {
		if (this.mLayoutController == null) {
			this.addViewInternal(child, index, layoutParams);
			return;
		}

		final LayoutParams nowLayoutParams = (LayoutParams) layoutParams;

		final int preViewId = child.getId();
		if (R.id.app_layout_toolbar_id == preViewId) {
			nowLayoutParams.layoutKey = UILayoutController.LayoutType.Toolbar.key;
		} else if (R.id.app_layout_content_id == preViewId) {
			nowLayoutParams.layoutKey = UILayoutController.LayoutType.Content.key;
		} else if (R.id.app_layout_loading_id == preViewId) {
			nowLayoutParams.layoutKey = UILayoutController.LayoutType.Loading.key;
		} else if (R.id.app_layout_error_id == preViewId) {
			nowLayoutParams.layoutKey = UILayoutController.LayoutType.Error.key;
		}
		final UILayout.Builder builder = new UILayout.Builder(nowLayoutParams)
				.setContentView(child)
				.setLayoutIndex(index)
				.setLayoutId(preViewId);

		if (UILayoutController.LayoutType.Content.key == nowLayoutParams.layoutKey) {
			builder.setLayoutIndex(0);
		}
		/*
		 * partial layout may be lost here, careful filling layout.
		 *
		 * eg : View key has been dispatched and used
		 *
		 * */
		this.mLayoutController.addLayoutInternal(builder.build());
	}

	public final void addViewInternal(@NonNull View child) {
		this.addViewInternal(child, -1);
	}

	public final void addViewInternal(@NonNull View child, int index) {
		ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
		if (layoutParams == null) {
			layoutParams = this.generateDefaultLayoutParams();
			if (layoutParams == null) {
				throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
			}
		}
		this.addViewInternal(child, index, layoutParams);
	}

	public final void addViewInternal(@NonNull View child, int index, @NonNull ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
	}

	@NonNull
	public final UIViewController getViewController() {
		return this.getLayoutController().getViewController();
	}

	@NonNull
	public final UILayoutController getLayoutController() {
		return this.mLayoutController;
	}

	@NonNull
	public final UIToolbarController getToolbarController() {
		return this.getLayoutController().getToolbarController();
	}

	@Nullable
	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState savedState = new SavedState(superState);
		final Bundle savedInstanceState = new Bundle();
		if (this.mLayoutController != null) {
			this.mLayoutController.onSaveInstanceState(savedInstanceState);
		}
		savedState.savedInstanceState = savedInstanceState;
		return savedState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof SavedState) {
			SavedState savedState = (SavedState) state;
			super.onRestoreInstanceState(savedState.getSuperState());
			if (this.mLayoutController != null) {
				this.mLayoutController.restoreState(savedState.savedInstanceState);
			}
			return;
		}
		super.onRestoreInstanceState(state);
	}

	public static class SavedState extends BaseSavedState {

		private Bundle savedInstanceState;

		SavedState(Parcelable superState) {
			super(superState);
		}

		@SuppressLint("ParcelClassLoader")
		private SavedState(Parcel parcel) {
			super(parcel);
			this.savedInstanceState = parcel.readBundle();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBundle(this.savedInstanceState);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	public static class LayoutParams extends RelativeLayout.LayoutParams {

		public int layoutKey = UILayout.NO_KEY;
		public int layoutEnterAnimation;
		public int layoutExitAnimation;
		public boolean mIsLayoutEnabled = true;
		public boolean mIsLayoutStableMode = true;

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(LayoutParams source) {
			super(source);
			this.layoutKey = source.layoutKey;
			this.layoutEnterAnimation = source.layoutEnterAnimation;
			this.layoutExitAnimation = source.layoutExitAnimation;
			this.mIsLayoutEnabled = source.mIsLayoutEnabled;
			this.mIsLayoutStableMode = source.mIsLayoutStableMode;
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(Context context, AttributeSet attrs) {
			super(context, attrs);
			final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OverlapRelativeLayout_Layout);
			this.layoutKey = typedArray.getInteger(R.styleable.OverlapRelativeLayout_Layout_layoutKey, this.layoutKey);
			this.layoutEnterAnimation = typedArray.getResourceId(R.styleable.OverlapRelativeLayout_Layout_layoutEnterAnimation, this.layoutEnterAnimation);
			this.layoutExitAnimation = typedArray.getResourceId(R.styleable.OverlapRelativeLayout_Layout_layoutExitAnimation, this.layoutExitAnimation);
			this.mIsLayoutEnabled = typedArray.getBoolean(R.styleable.OverlapRelativeLayout_Layout_layoutEnabled, this.mIsLayoutEnabled);
			this.mIsLayoutStableMode = typedArray.getBoolean(R.styleable.OverlapRelativeLayout_Layout_layoutStableMode, this.mIsLayoutStableMode);
			typedArray.recycle();
		}
	}
}
