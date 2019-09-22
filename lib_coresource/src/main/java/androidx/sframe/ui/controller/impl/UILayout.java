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
import androidx.sframe.R;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.widget.SRelativeLayout;

/**
 * Author create by ok on 2019-06-10
 * Email : ok@163.com.
 */
public class UILayout {

	public static final int NO_ID = -1;
	public static final int NO_KEY = -1;

	private final int layoutKey;
	private final int layoutResId;
	private final int layoutSpareId;
	private final int layoutEnterAnimation;
	private final int layoutExitAnimation;
	private final View contentView;
	private final boolean mIsLayoutEnabled;
	private final boolean mIsLayoutStableMode;

	protected UILayout(@NonNull Builder builder) {
		this.layoutKey = builder.layoutKey;
		this.layoutResId = builder.layoutResId;
		this.layoutSpareId = builder.layoutSpareId;
		this.layoutEnterAnimation = builder.layoutEnterAnimation;
		this.layoutExitAnimation = builder.layoutExitAnimation;
		this.contentView = builder.contentView;
		this.mIsLayoutEnabled = builder.mIsLayoutEnabled;
		this.mIsLayoutStableMode = builder.mIsLayoutStableMode;
	}

	public final int getLayoutId() {
		return this.contentView.getId();
	}

	public final int getLayoutKey() {
		return this.layoutKey;
	}

	@NonNull
	public final View getContentView() {
		return this.contentView;
	}

	@NonNull
	public final SRelativeLayout.LayoutParams getLayoutParams() {
		return (SRelativeLayout.LayoutParams) this.contentView.getLayoutParams();
	}

	public final int getEnterAnimation() {
		return this.layoutEnterAnimation;
	}

	public final int getExitAnimation() {
		return this.layoutExitAnimation;
	}

	public final boolean isLayoutEnabled() {
		return this.mIsLayoutEnabled;
	}

	public final boolean isLayoutStableMode() {
		return this.mIsLayoutStableMode;
	}

	public final boolean ignoreLayoutDirectPreview() {
		return this.layoutKey == NO_KEY;
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
			} else if (UILayoutController.LayoutType.Loading.key == this.layoutKey
					|| UILayoutController.LayoutType.Error.key == this.layoutKey) {
				this.requestLayoutOfOther(layoutController);
			}
		}
	}

	@NonNull
	public Builder build() {
		return new Builder(this);
	}

	private int findLayoutIdByKey(@NonNull UILayoutController layoutController, int key) {
		if (this.isLayoutStableMode()) {
			final UILayout nowLayout = layoutController.getLayoutAt(key);
			if (nowLayout == null) {
				return View.NO_ID;
			}
			return nowLayout.getLayoutId();
		}
		return View.NO_ID;
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

	private void requestLayoutOfContent(@NonNull UILayoutController layoutController) {
		final int toolbarLayoutId = this.getToolbarLayoutId(layoutController);
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();
		nowLayoutParams.addRule(RelativeLayout.BELOW, toolbarLayoutId);
		this.contentView.setLayoutParams(nowLayoutParams);
	}

	private void requestLayoutOfOther(@NonNull UILayoutController layoutController) {
		final UILayout contentLayout = layoutController.getLayoutAt(UILayoutController.LayoutType.Content.key);
		final int toolbarLayoutId = this.getToolbarLayoutId(layoutController);
		final int contentLayoutId = this.getContentLayoutId(layoutController);
		final RelativeLayout.LayoutParams nowLayoutParams = this.getLayoutParams();

		if (contentLayout == null
				|| contentLayoutId == View.NO_ID
				|| !this.isLayoutStableMode()) {
			if (toolbarLayoutId == View.NO_ID) {
				// align parent
				nowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				nowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				nowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				nowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			} else {
				// below toolbar
				nowLayoutParams.addRule(RelativeLayout.BELOW, toolbarLayoutId);
				nowLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, toolbarLayoutId);
				nowLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, toolbarLayoutId);
				nowLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			}
		} else {
			// align content
			nowLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, contentLayoutId);
			nowLayoutParams.addRule(RelativeLayout.ALIGN_TOP, contentLayoutId);
			nowLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, contentLayoutId);
			nowLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, contentLayoutId);
		}
		this.contentView.setLayoutParams(nowLayoutParams);
	}

	@NonNull
	final UILayout inflate(@NonNull ViewGroup container) {
		if (this.contentView == null) {
			return this.build()
					.setContentView(LayoutInflater.from(container.getContext()).inflate(this.layoutResId, container, false))
					.build();
		}
		return this;
	}

	public static class Builder {

		private int layoutKey;
		private int layoutResId;
		private int layoutSpareId;
		private int layoutEnterAnimation;
		private int layoutExitAnimation;
		private View contentView;
		private boolean mIsLayoutEnabled = true;
		private boolean mIsLayoutStableMode = true;

		public Builder(int layoutKey) {
			this.layoutKey = layoutKey;

			if (UILayoutController.LayoutType.Toolbar.key == layoutKey) {
				this.layoutSpareId = R.id.app_layout_toolbar_id;
			} else if (UILayoutController.LayoutType.Content.key == layoutKey) {
				this.layoutSpareId = R.id.app_layout_content_id;
			} else if (UILayoutController.LayoutType.Loading.key == layoutKey) {
				this.layoutSpareId = R.id.app_layout_loading_id;
			} else if (UILayoutController.LayoutType.Error.key == layoutKey) {
				this.layoutSpareId = R.id.app_layout_error_id;
			} else {
				this.layoutSpareId = NO_ID;
			}
		}

		public Builder(@NonNull UILayout layout) {
			this.layoutKey = layout.layoutKey;
			this.layoutResId = layout.layoutResId;
			this.layoutSpareId = layout.layoutSpareId;
			this.layoutEnterAnimation = layout.layoutEnterAnimation;
			this.layoutExitAnimation = layout.layoutExitAnimation;
			this.contentView = layout.contentView;
			this.mIsLayoutEnabled = layout.mIsLayoutEnabled;
			this.mIsLayoutStableMode = layout.mIsLayoutStableMode;
		}

		public Builder setLayoutSpareId(@IdRes int layoutSpareId) {
			if (View.NO_ID == layoutSpareId) {
				return this;
			}
			this.layoutSpareId = layoutSpareId;
			return this;
		}

		public Builder setContentView(@LayoutRes int layoutResId) {
			this.layoutResId = layoutResId;
			return this;
		}

		public Builder setContentView(@NonNull View contentView) {
			this.contentView = contentView;
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
		public UILayout build() {
			if (this.contentView == null) {
				if (this.layoutResId == 0) {
					throw new IllegalStateException("UILayout.Builder " + this + " does not have a contentView set");
				}
				return new UILayout(this);
			}
			if (this.contentView.getId() == View.NO_ID) {
				this.contentView.setId(this.layoutSpareId);
			}
			return new UILayout(this);
		}
	}
}
