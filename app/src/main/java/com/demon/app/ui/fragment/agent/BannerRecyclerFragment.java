package com.demon.app.ui.fragment.agent;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.demon.app.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.demon.widget.BannerLayout;
import androidx.demon.widget.IndicatorView;
import androidx.demon.widget.adapter.PagerAdapter;
import androidx.demon.widget.transformers.HorDepthPageTransformer;
import androidx.sframe.ui.controller.AppPageController;
import androidx.sframe.widget.adapter.RecyclerFragment;

/**
 * @Author create by Zoran on 2019-09-13
 * @Email : 171905184@qq.com
 * @Description :
 */
public class BannerRecyclerFragment extends RecyclerFragment {

	private final AppPageController<?> mPageController;

	public BannerRecyclerFragment(@NonNull AppPageController<?> pageController) {
		this.mPageController = pageController;
	}

	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
		return inflater.inflate(R.layout.fragment_banner_layout, parent, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, int position, @Nullable List<Object> payloads) {
		super.onViewCreated(view, position, payloads);
		final IndicatorView mIndicatorView = view.findViewById(R.id.indicatorView);
		final BannerLayout mBannerLayout = view.findViewById(R.id.bannerLayout);

		if(mBannerLayout.getAdapter() != null) {
			return;
		}

		final ArrayList<String> data = new ArrayList<>();
		data.add("http://img0.imgtn.bdimg.com/it/u=3106526341,3733396167&fm=26&gp=0.jpg");
		data.add("http://img1.imgtn.bdimg.com/it/u=795421968,2817681607&fm=11&gp=0.jpg");
		data.add("http://img0.imgtn.bdimg.com/it/u=1732553485,3379133703&fm=26&gp=0.jpg");
		data.add("http://img0.imgtn.bdimg.com/it/u=3043400348,2388911000&fm=15&gp=0.jpg");
		final PagerAdapter<PagerAdapter.ViewHolder> mAdapter = new PagerAdapter<PagerAdapter.ViewHolder>() {

			@Override
			public int getItemCount() {
				return data.size();
			}

			@NonNull
			@Override
			public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, int itemViewType) {
				final View itemView = inflater.inflate(R.layout.item_banner_layout, container, false);
				return new ViewHolder(itemView) {
				};
			}

			@Override
			@SuppressLint("CheckResult")
			public void onBindViewHolder(@NonNull ViewHolder holder, int position, @Nullable Object object) {
				final AppCompatImageView imageView = holder.getItemView().findViewById(R.id.icon);
				RequestOptions options = new RequestOptions();
				options.error(new ColorDrawable(Color.BLACK));

				Glide.with(imageView)
						.load(data.get(position))
						.apply(options)
						.into(imageView);
			}
		};

		// 自动管理生命周期
		mBannerLayout.setLifecycleOwner(this.mPageController);
		// 滚动方向
		mBannerLayout.setPlayScrollDirection(BannerLayout.PLAY_SCROLL_DIRECTION_START);
		// 滚动动画
		mBannerLayout.setPageTransformer(new HorDepthPageTransformer());
		// 指示器
		mBannerLayout.addPlayIndicator(mIndicatorView);
		// 用户手势操作
		mBannerLayout.setAllowUserScrollable(true);
		// 播放间隔时间：毫秒
		mBannerLayout.setAutoPlayDelayMillis(2500);
		// 控件滚动间隔时间：毫秒
		mBannerLayout.setScrollingDuration(600);
		// 自动循环播放
		mBannerLayout.setAutoPlayFlags(true);
		// 资源适配器
		mBannerLayout.setAdapter(mAdapter);
		// 开始循环播放
		mBannerLayout.startPlay();
	}
}
