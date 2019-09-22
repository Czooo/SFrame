package androidx.sframe.ui.controller.impl;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.sframe.R;
import androidx.sframe.adapter.ToolbarMenuDelegate;
import androidx.sframe.model.AppMenuModel;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.utils.ResCompat;
import androidx.sframe.utils.SystemCompat;
import androidx.sframe.widget.SRelativeLayout;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019-06-13
 * Email : ok@163.com.
 */
public class AppToolbarMethod extends UIToolbarMethod implements UILayoutController.OnLayoutListener, RecyclerAdapter.OnItemClickListener<AppMenuModel> {

	public AppToolbarMethod(@NonNull UILayoutController layoutController) {
		super(layoutController);
		layoutController
				.setBackgroundColor(ResCompat.getColorAppBackground(this.getContext()))
				.setToolbarLayout(R.layout.layout_toolbar_default)
				.setLoadingLayout(R.layout.layout_loading_default)
				.setErrorLayout(R.layout.layout_error_default)
				.addOnLayoutListener(this);

		final DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(this.getContext());
		arrowDrawable.setColor(Color.WHITE);
		arrowDrawable.setProgress(1);

		// default options
		this.setEnabled(true)
				.setBackgroundColor(ResCompat.getColorAppToolbar(this.getContext()))
				.setStatusBackgroundColor(ResCompat.getColorAppStatus(this.getContext()))
				.setTitleBackgroundColor(ResCompat.getColorTransparent(this.getContext()))
				.setLineColor(ResCompat.getColorAppLine(this.getContext()))
				.setPopImageDrawable(arrowDrawable)
				.setShadowEnabled(true)
				.setStatusEnabled(false)
				.setLineEnabled(false)
				.setTitleEnabled(true)
				.setPopEnabled(false);
	}

	@Override
	public void onLayoutChanged(@NonNull UILayoutController layoutController, @NonNull UILayout layout, @Nullable Object object) {
		if (UILayoutController.LayoutType.Error.key == layout.getLayoutKey()) {
			if (object instanceof Throwable) {
				this.setErrorText(((Throwable) object).getMessage());
			} else if (object instanceof String) {
				this.setErrorText(String.valueOf(object));
			}
		}
	}

	public int getMeasuredWidth() {
		return this.getToolbarView().getMeasuredWidth();
	}

	public int getMeasuredHeight() {
		return this.getToolbarView().getMeasuredHeight();
	}

	public int getTitleMeasuredWidth() {
		return this.getTitleView().getMeasuredWidth();
	}

	public int getTitleMeasuredHeight() {
		return this.getTitleView().getMeasuredHeight();
	}

	@NonNull
	public View getToolbarView() {
		return this.getLayoutController().requireLayoutAt(UILayoutController.LayoutType.Toolbar.key).getContentView();
	}

	@NonNull
	public View getTitleView() {
		return this.findViewById(R.id.app_toolbar_title_id);
	}

	@NonNull
	public View getStatusView() {
		return this.findViewById(R.id.app_toolbar_status_id);
	}

	@NonNull
	private View getContainerPopView() {
		return this.findViewById(R.id.app_container_pop_id);
	}

	@NonNull
	private View getContainerTitleView() {
		return this.findViewById(R.id.app_container_title_id);
	}

	@NonNull
	private View getContainerMenuView() {
		return this.findViewById(R.id.app_container_menu_id);
	}

	@NonNull
	private View inflate(@NonNull View container, @LayoutRes int layoutId) {
		return this.getViewController().findAtParent().inflate(layoutId, (ViewGroup) container, false);
	}

	public AppToolbarMethod setEnabled(boolean flag) {
		final View preView = this.getToolbarView();
		if (flag) {
			preView.setVisibility(View.VISIBLE);
		} else {
			preView.setVisibility(View.GONE);
		}
		return this;
	}

	public AppToolbarMethod setTitleEnabled(boolean flag) {
		final View preView = this.getTitleView();
		if (flag) {
			preView.setVisibility(View.VISIBLE);
		} else {
			preView.setVisibility(View.GONE);
		}
		return this;
	}

	public AppToolbarMethod setStatusEnabled(boolean flag) {
		final View preView = this.getStatusView();
		if (flag) {
			final int statusHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? SystemCompat.getStatusBarHeight(this.getContext()) : 0;
			preView.setVisibility(View.VISIBLE);
			preView.getLayoutParams().height = statusHeight;
		} else {
			preView.setVisibility(View.GONE);
		}
		return this;
	}

	public AppToolbarMethod setShadowEnabled(boolean flag) {
		View preView = this.findViewById(R.id.app_toolbar_shadow_id);
		if (flag && preView == null) {
			final AppCompatImageView appCompatImageView = new AppCompatImageView(this.getContext());
			appCompatImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			appCompatImageView.setImageResource(R.mipmap.icon_shadow_bottom);
			appCompatImageView.setId(R.id.app_toolbar_shadow_id);

			final UILayout nowLayout = this.getLayoutController().requireLayoutAt(UILayoutController.LayoutType.Toolbar.key);
			final int nowLayoutId = nowLayout.getLayoutId();

			final SRelativeLayout.LayoutParams layoutParams = new SRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.height = (int) ResCompat.dp2px(this.getContext(), 4);
			layoutParams.addRule(RelativeLayout.BELOW, nowLayoutId);
			layoutParams.addRule(RelativeLayout.ALIGN_LEFT, nowLayoutId);
			layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, nowLayoutId);
			this.getLayoutController().addLayoutInternal(new UILayout.Builder(UILayout.NO_KEY)
					.setContentView(appCompatImageView)
					.build(), layoutParams);
			// set view
			preView = appCompatImageView;
		}
		if (preView != null) {
			if (flag) {
				preView.setVisibility(View.VISIBLE);
			} else {
				preView.setVisibility(View.GONE);
			}
		}
		return this;
	}

	public AppToolbarMethod setLineEnabled(boolean flag) {
		if (flag) {
			this.findViewById(R.id.app_toolbar_line_id).setVisibility(View.VISIBLE);
		} else {
			this.findViewById(R.id.app_toolbar_line_id).setVisibility(View.GONE);
		}
		return this;
	}

	public AppToolbarMethod setPopEnabled(boolean flag) {
		final View preView = this.getContainerPopView();
		if (flag) {
			preView.setVisibility(View.VISIBLE);
		} else {
			preView.setVisibility(View.GONE);
		}
		return this;
	}

	public AppToolbarMethod setAlpha(@FloatRange(from = 0, to = 1.f) float alpha) {
		final Drawable mDrawable = this.getToolbarView().getBackground();
		if (mDrawable != null) {
			mDrawable.setAlpha((int) (alpha * 255));
		}
		return this;
	}

	public AppToolbarMethod setTitleAlpha(@FloatRange(from = 0, to = 1.f) float alpha) {
		final Drawable mDrawable = this.getTitleView().getBackground();
		if (mDrawable != null) {
			mDrawable.setAlpha((int) (alpha * 255));
		}
		return this;
	}

	public AppToolbarMethod setStatusAlpha(@FloatRange(from = 0, to = 1.f) float alpha) {
		final Drawable mDrawable = this.getStatusView().getBackground();
		if (mDrawable != null) {
			mDrawable.setAlpha((int) (alpha * 255));
		}
		return this;
	}

	public AppToolbarMethod setBackground(Drawable background) {
		this.getToolbarView().setBackground(background);
		return this;
	}

	public AppToolbarMethod setBackgroundColor(@ColorInt int color) {
		this.getToolbarView().setBackgroundColor(color);
		return this;
	}

	public AppToolbarMethod setBackgroundResource(@DrawableRes int resId) {
		this.getToolbarView().setBackgroundResource(resId);
		return this;
	}

	public AppToolbarMethod setTitleBackground(Drawable background) {
		this.getTitleView().setBackground(background);
		return this;
	}

	public AppToolbarMethod setTitleBackgroundColor(@ColorInt int color) {
		this.getTitleView().setBackgroundColor(color);
		return this;
	}

	public AppToolbarMethod setTitleBackgroundResource(@DrawableRes int resId) {
		this.getTitleView().setBackgroundResource(resId);
		return this;
	}

	public AppToolbarMethod setStatusBackground(Drawable background) {
		this.getStatusView().setBackground(background);
		return this;
	}

	public AppToolbarMethod setStatusBackgroundColor(@ColorInt int color) {
		this.getStatusView().setBackgroundColor(color);
		return this;
	}

	public AppToolbarMethod setStatusBackgroundResource(@DrawableRes int resId) {
		this.getStatusView().setBackgroundResource(resId);
		return this;
	}

	public AppToolbarMethod setLineColor(@ColorInt int color) {
		this.findAt(R.id.app_toolbar_line_id).setBackgroundColor(color);
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public AppToolbarMethod setCustomTitleView(@LayoutRes int layoutId) {
		return this.setCustomTitleView(this.inflate(this.getTitleView(), layoutId));
	}

	public AppToolbarMethod setCustomTitleView(@NonNull View preView) {
		final ViewGroup preViewGroup = (ViewGroup) this.getTitleView();
		preViewGroup.removeAllViews();
		preViewGroup.addView(preView);
		((RelativeLayout.LayoutParams) preView.getLayoutParams())
				.addRule(RelativeLayout.BELOW, R.id.app_toolbar_status_id);
		return this;
	}

	public AppToolbarMethod setPopText(@NonNull CharSequence title) {
		this.findAt(R.id.app_pop_icon_id).setVisibility(View.GONE)
				.findAt(R.id.app_pop_text_id).setVisibility(View.VISIBLE).methodAtTextView().setText(title);
		return this;
	}

	public AppToolbarMethod setPopTextColor(@ColorInt int color) {
		this.findAt(R.id.app_pop_text_id).methodAtTextView().setTextColor(color);
		return this;
	}

	public AppToolbarMethod setPopTextSize(float size) {
		this.findAt(R.id.app_pop_text_id).methodAtTextView().setTextSize(size);
		return this;
	}

	public AppToolbarMethod setPopImageResource(@DrawableRes int resId) {
		this.findAt(R.id.app_pop_text_id).setVisibility(View.GONE)
				.findAt(R.id.app_pop_icon_id).setVisibility(View.VISIBLE).methodAtImageView().setImageResource(resId);
		return this;
	}

	public AppToolbarMethod setPopImageDrawable(Drawable drawable) {
		this.findAt(R.id.app_pop_text_id).setVisibility(View.GONE)
				.findAt(R.id.app_pop_icon_id).setVisibility(View.VISIBLE).methodAtImageView().setImageDrawable(drawable);
		return this;
	}

	public AppToolbarMethod setTitle(@NonNull CharSequence title) {
		this.findAt(R.id.app_title_icon_id).setVisibility(View.GONE)
				.findAt(R.id.app_title_text_id).setVisibility(View.VISIBLE).methodAtTextView().setText(title);
		return this;
	}

	public AppToolbarMethod setTitleTextColor(@ColorInt int color) {
		this.findAt(R.id.app_title_text_id).methodAtTextView().setTextColor(color);
		return this;
	}

	public AppToolbarMethod setTitleTextSize(float size) {
		this.findAt(R.id.app_title_text_id).methodAtTextView().setTextSize(size);
		return this;
	}

	public AppToolbarMethod setTitleImageResource(@DrawableRes int resId) {
		this.findAt(R.id.app_title_text_id).setVisibility(View.GONE)
				.findAt(R.id.app_title_icon_id).setVisibility(View.VISIBLE).methodAtImageView().setImageResource(resId);
		return this;
	}

	public AppToolbarMethod setTitleImageDrawable(Drawable drawable) {
		this.findAt(R.id.app_title_text_id).setVisibility(View.GONE)
				.findAt(R.id.app_title_icon_id).setVisibility(View.VISIBLE).methodAtImageView().setImageDrawable(drawable);
		return this;
	}

	private RecyclerAdapter<AppMenuModel> mToolbarMenuAdapter;
	private OnMenuClickListener mMenuClickListener;

	public AppToolbarMethod addMenu(@NonNull AppMenuModel menuModel) {
		if (this.mToolbarMenuAdapter == null) {
			this.mToolbarMenuAdapter = new RecyclerAdapter<>(new ToolbarMenuDelegate());
			this.mToolbarMenuAdapter.setOnItemClickListener(this);

			this.getViewController()
					.findAt(R.id.app_menu_List_id)
					.methodAtRecyclerView()
					.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false))
					.setAdapter(this.mToolbarMenuAdapter);
		}
		this.mToolbarMenuAdapter.getDataSourceController().addDataSource(menuModel);
		return this;
	}

	public AppToolbarMethod removeMenuById(int menuId) {
		if (this.mToolbarMenuAdapter != null) {
			int position = 0;
			while (position < this.mToolbarMenuAdapter.getItemCount()) {
				final AppMenuModel model = this.mToolbarMenuAdapter.findDataSourceByPosition(position);
				if (menuId == model.getId()) {
					this.removeMenuByPosition(position);
					break;
				}
				position++;
			}
		}
		return this;
	}

	public AppToolbarMethod removeMenuByPosition(int position) {
		if (this.mToolbarMenuAdapter != null) {
			this.mToolbarMenuAdapter.getDataSourceController().removeDataSource(position);
		}
		return this;
	}

	@Override
	public void onItemClick(@NonNull RecyclerAdapter.ViewHolder<AppMenuModel> holder, int position) {
		if (this.mMenuClickListener != null) {
			this.mMenuClickListener.onMenuClick(holder.getItemView(), holder, position);
		}
	}

	public AppToolbarMethod setOnPopClickListener(@NonNull final OnPopClickListener listener) {
		this.findViewById(R.id.app_container_pop_id).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listener.onPopClick(view);
			}
		});
		return this;
	}

	public AppToolbarMethod setOnMenuClickListener(@NonNull OnMenuClickListener listener) {
		this.mMenuClickListener = listener;
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public AppToolbarMethod setErrorText(@NonNull CharSequence text) {
		this.findAt(R.id.app_error_text_id).methodAtTextView().setText(text);
		return this;
	}

	public AppToolbarMethod setErrorImage(@Nullable Object url) {
		this.findAt(R.id.app_error_icon_id).methodAtImageView().setImage(url);
		return this;
	}

	public AppToolbarMethod setErrorImageResource(@DrawableRes int resId) {
		this.findAt(R.id.app_error_icon_id).methodAtImageView().setImageResource(resId);
		return this;
	}

	public AppToolbarMethod setErrorImageDrawable(Drawable drawable) {
		this.findAt(R.id.app_error_icon_id).methodAtImageView().setImageDrawable(drawable);
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public interface OnPopClickListener {

		void onPopClick(@NonNull View view);
	}

	public interface OnMenuClickListener {

		void onMenuClick(@NonNull View view, @NonNull RecyclerAdapter.ViewHolder<AppMenuModel> holder, int position);
	}
}
