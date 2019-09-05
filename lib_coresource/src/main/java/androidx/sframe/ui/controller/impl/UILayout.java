package androidx.sframe.ui.controller.impl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.AnimRes;
import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.widget.OverlapRelativeLayout;

/**
 * Author create by ok on 2019-06-10
 * Email : ok@163.com.
 */
public class UILayout {

	public static final int NO_ID = -1;
	public static final int NO_KEY = -1;
	public static final int NO_INDEX = -1;

	private final int layoutId;
	private final int layoutKey;
	private final int layoutIndex;
	private final int layoutResId;
	private final View contentView;
	private final int layoutEnterAnimation;
	private final int layoutExitAnimation;

	private final boolean mIsLayoutEnabled;
	private final boolean mIsLayoutStableMode;
	private final OverlapRelativeLayout.LayoutParams layoutParams;

	private boolean mIsShouldLayoutStableMode;

	protected UILayout(@NonNull Builder builder) {
		this.layoutId = builder.layoutId;
		this.layoutKey = builder.layoutKey;
		this.layoutIndex = builder.layoutIndex;
		this.layoutResId = builder.layoutResId;
		this.contentView = builder.contentView;
		this.layoutEnterAnimation = builder.layoutEnterAnimation;
		this.layoutExitAnimation = builder.layoutExitAnimation;
		this.mIsLayoutEnabled = builder.mIsLayoutEnabled;
		this.mIsLayoutStableMode = builder.mIsLayoutStableMode;
		this.mIsShouldLayoutStableMode = builder.mIsShouldLayoutStableMode;
		this.layoutParams = builder.layoutParams;
	}

	public final int getId() {
		return this.layoutId;
	}

	public final int getKey() {
		return this.layoutKey;
	}

	public final int getIndex() {
		return this.layoutIndex;
	}

	@NonNull
	public final View getContentView() {
		return this.contentView;
	}

	@NonNull
	public OverlapRelativeLayout.LayoutParams getLayoutParams() {
		return this.layoutParams;
	}

	public final int getEnterAnimation() {
		return this.layoutEnterAnimation;
	}

	public final int getExitAnimation() {
		return this.layoutExitAnimation;
	}

	public final boolean isEnabled() {
		return this.mIsLayoutEnabled;
	}

	public final boolean isLayoutStableMode() {
		return this.mIsLayoutStableMode;
	}

	public final boolean ignoreLayoutDirectPreview() {
		return this.layoutKey == NO_KEY;
	}

	@NonNull
	public final UILayout inflate(@NonNull ViewGroup container) {
		return inflate(container, false);
	}

	@NonNull
	public final UILayout inflate(@NonNull ViewGroup container, boolean attachToRoot) {
		return inflate(LayoutInflater.from(container.getContext()), container, attachToRoot);
	}

	@NonNull
	public final UILayout inflate(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, boolean attachToRoot) {
		if (this.contentView == null && this.layoutResId != 0) {
			final View contentView = inflater.inflate(this.layoutResId, container, attachToRoot);
			return this.build().setContentView(contentView).build();
		}
		return this;
	}

	private int findLayoutIdByKey(@NonNull UILayoutController layoutController, int key) {
		final UILayout nowLayout = layoutController.getLayoutAt(key);
		final int nowLayoutId;
		if (nowLayout == null
				|| !nowLayout.isEnabled()
				|| nowLayout.getContentView().getParent() == null) {
			nowLayoutId = 0;
		} else {
			nowLayoutId = nowLayout.getId();
		}
		return nowLayoutId;
	}

	private int getToolbarLayoutId(@NonNull UILayoutController layoutController) {
		return this.findLayoutIdByKey(layoutController, UILayoutController.LayoutType.Toolbar.key);
	}

	private int getContentLayoutId(@NonNull UILayoutController layoutController) {
		return this.findLayoutIdByKey(layoutController, UILayoutController.LayoutType.Content.key);
	}

	private void requestLayoutOfToolbar(@NonNull UILayoutController layoutController) {
		final int contentLayoutId = this.getContentLayoutId(layoutController);
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
		nowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		nowLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, contentLayoutId);
		nowLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, contentLayoutId);
		this.contentView.setLayoutParams(nowLayoutParams);
	}

	private void unrequestLayoutOfToolbar() {
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
		nowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
		nowLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, 0);
		nowLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, 0);
		this.contentView.setLayoutParams(nowLayoutParams);
	}

	private void requestLayoutOfContent(@NonNull UILayoutController layoutController) {
		final int toolbarLayoutId = this.getToolbarLayoutId(layoutController);
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
		nowLayoutParams.addRule(RelativeLayout.BELOW, toolbarLayoutId);
		this.contentView.setLayoutParams(nowLayoutParams);
	}

	private void unrequestLayoutOfContent() {
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
		nowLayoutParams.addRule(RelativeLayout.BELOW, 0);
		this.contentView.setLayoutParams(nowLayoutParams);
	}

	private void requestLayoutOfOther(@NonNull UILayoutController layoutController) {
		final UILayout contentLayout = layoutController.getLayoutAt(UILayoutController.LayoutType.Content.key);
		final int toolbarLayoutId = this.getToolbarLayoutId(layoutController);
		final int contentLayoutId = this.getContentLayoutId(layoutController);
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
		this.unrequestLayoutOfOther();
		if (contentLayoutId == 0
				|| contentLayout == null
				|| !contentLayout.isLayoutStableMode()) {
			if (toolbarLayoutId == 0) {
				// align parent
				nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_LEFT);
				nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_TOP);
				nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_RIGHT);
				nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_BOTTOM);
			} else {
				// below toolbar
				nowLayoutParams.addRule(OverlapRelativeLayout.BELOW, toolbarLayoutId);
				nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_LEFT, toolbarLayoutId);
				nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_RIGHT, toolbarLayoutId);
				nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_BOTTOM);
			}
		} else {
			// align content
			nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_LEFT, contentLayoutId);
			nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_TOP, contentLayoutId);
			nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_RIGHT, contentLayoutId);
			nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_BOTTOM, contentLayoutId);
		}
		this.contentView.setLayoutParams(nowLayoutParams);
	}


	private void unrequestLayoutOfOther() {
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
		nowLayoutParams.addRule(OverlapRelativeLayout.BELOW, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_LEFT, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_TOP, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_RIGHT, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_BOTTOM, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_LEFT, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_TOP, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_RIGHT, 0);
		nowLayoutParams.addRule(OverlapRelativeLayout.ALIGN_PARENT_BOTTOM, 0);
		this.contentView.setLayoutParams(nowLayoutParams);
	}

	@CallSuper
	public void requestLayout(@NonNull UILayoutController layoutController) {
		if (this.contentView == null) {
			return;
		}

		if (this.isLayoutStableMode()) {
			if (UILayoutController.LayoutType.Toolbar.key == this.layoutKey) {
				this.requestLayoutOfToolbar(layoutController);
			} else if (UILayoutController.LayoutType.Content.key == this.layoutKey) {
				this.requestLayoutOfContent(layoutController);
			} else {
				this.requestLayoutOfOther(layoutController);
			}
			this.mIsShouldLayoutStableMode = true;
		} else {
			if (this.mIsShouldLayoutStableMode) {
				// clear params
				if (UILayoutController.LayoutType.Toolbar.key == this.layoutKey) {
					this.unrequestLayoutOfToolbar();
				} else if (UILayoutController.LayoutType.Content.key == this.layoutKey) {
					this.unrequestLayoutOfContent();
				} else {
					this.unrequestLayoutOfOther();
				}
				this.mIsShouldLayoutStableMode = false;
			}
		}
	}

	@NonNull
	public Builder build() {
		return new Builder(this);
	}

	public static class Builder {

		protected int layoutId;
		protected int layoutKey;
		protected int layoutIndex;
		protected int layoutResId;
		protected View contentView;
		protected int layoutEnterAnimation;
		protected int layoutExitAnimation;

		protected boolean mIsLayoutEnabled;
		protected boolean mIsLayoutStableMode;
		protected boolean mIsShouldLayoutStableMode;
		protected OverlapRelativeLayout.LayoutParams layoutParams;

		public Builder(int layoutKey) {
			this.layoutId = NO_ID;
			this.layoutKey = layoutKey;
			this.layoutIndex = NO_INDEX;
			this.mIsLayoutEnabled = true;
			this.mIsLayoutStableMode = true;
		}

		public Builder(@NonNull OverlapRelativeLayout.LayoutParams layoutParams) {
			this.layoutId = NO_ID;
			this.layoutKey = layoutParams.layoutKey;
			this.layoutIndex = NO_INDEX;
			this.layoutEnterAnimation = layoutParams.layoutEnterAnimation;
			this.layoutExitAnimation = layoutParams.layoutExitAnimation;
			this.mIsLayoutEnabled = layoutParams.mIsLayoutEnabled;
			this.mIsLayoutStableMode = layoutParams.mIsLayoutStableMode;
			this.layoutParams = layoutParams;
		}

		public Builder(@NonNull UILayout layout) {
			this.layoutId = layout.layoutId;
			this.layoutKey = layout.layoutKey;
			this.layoutIndex = layout.layoutIndex;
			this.layoutResId = layout.layoutResId;
			this.contentView = layout.contentView;
			this.layoutEnterAnimation = layout.layoutEnterAnimation;
			this.layoutExitAnimation = layout.layoutExitAnimation;
			this.mIsLayoutEnabled = layout.mIsLayoutEnabled;
			this.mIsLayoutStableMode = layout.mIsLayoutStableMode;
			this.mIsShouldLayoutStableMode = layout.mIsShouldLayoutStableMode;
			this.layoutParams = layout.layoutParams;
		}

		public Builder setLayoutId(@IdRes int layoutId) {
			this.layoutId = layoutId;
			return this;
		}

		public Builder setLayoutIndex(int layoutIndex) {
			this.layoutIndex = layoutIndex;
			return this;
		}

		public Builder setContentView(@LayoutRes int resId) {
			this.layoutResId = resId;
			return this;
		}

		public Builder setContentView(@NonNull View preView) {
			this.contentView = preView;
			return this;
		}

		public Builder setLayoutWidth(int width) {
			this.getLayoutParams().width = width;
			return this;
		}

		public Builder setLayoutHeight(int height) {
			this.getLayoutParams().height = height;
			return this;
		}

		public Builder addRule(int verb) {
			this.getLayoutParams().addRule(verb);
			return this;
		}

		public Builder addRule(int verb, int subject) {
			this.getLayoutParams().addRule(verb, subject);
			return this;
		}

		public Builder setMarginLeft(int value) {
			this.getLayoutParams().leftMargin = value;
			return this;
		}

		public Builder setMarginTop(int value) {
			this.getLayoutParams().topMargin = value;
			return this;
		}

		public Builder setMarginRight(int value) {
			this.getLayoutParams().rightMargin = value;
			return this;
		}

		public Builder setMarginBottom(int value) {
			this.getLayoutParams().bottomMargin = value;
			return this;
		}

		public Builder setMarginStart(int value) {
			this.getLayoutParams().setMarginStart(value);
			return this;
		}

		public Builder setMarginEnd(int value) {
			this.getLayoutParams().setMarginEnd(value);
			return this;
		}

		public Builder setLayoutEnabled(boolean layoutEnabled) {
			this.mIsLayoutEnabled = layoutEnabled;
			return this;
		}

		public Builder setLayoutStableMode(boolean layoutStableMode) {
			this.mIsLayoutStableMode = layoutStableMode;
			return this;
		}

		public Builder setLayoutEnterAnimation(@AnimRes int layoutEnterAnimation) {
			this.layoutEnterAnimation = layoutEnterAnimation;
			return this;
		}

		public Builder setLayoutExitAnimation(@AnimRes int layoutExitAnimation) {
			this.layoutExitAnimation = layoutExitAnimation;
			return this;
		}

		@NonNull
		public OverlapRelativeLayout.LayoutParams getLayoutParams() {
			if (this.contentView != null && this.contentView.getLayoutParams() != null) {
				this.layoutParams = (OverlapRelativeLayout.LayoutParams) this.contentView.getLayoutParams();
			}
			if (this.layoutParams == null) {
				this.layoutParams = new OverlapRelativeLayout.LayoutParams(-2, -2);
			}
			return this.layoutParams;
		}

		@NonNull
		public UILayout build() {
			if (this.contentView == null && this.layoutResId == 0) {
				throw new IllegalStateException("UILayout.Builder " + this + " does not have a contentView set");
			}
			if (this.contentView != null) {
				if (this.contentView.getId() == View.NO_ID) {
					this.contentView.setId(this.layoutId);
				} else {
					this.layoutId = this.contentView.getId();
				}
				OverlapRelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
				nowLayoutParams.layoutKey = this.layoutKey;
				nowLayoutParams.layoutEnterAnimation = this.layoutEnterAnimation;
				nowLayoutParams.layoutExitAnimation = this.layoutExitAnimation;
				nowLayoutParams.mIsLayoutEnabled = this.mIsLayoutEnabled;
				nowLayoutParams.mIsLayoutStableMode = this.mIsLayoutStableMode;
				this.contentView.setLayoutParams(nowLayoutParams);
			}
			return new UILayout(this);
		}
	}
}
