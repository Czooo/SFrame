package com.demon.app.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demon.app.R;
import com.demon.app.adapter.FooterDelegate;
import com.demon.app.adapter.HeaderDelegate;
import com.demon.app.model.TestModel;
import com.demon.app.ui.TestActivity;
import com.demon.app.ui.dialog.TestDialogFragment;
import com.demon.app.ui.dialog.TestDialogFragment2;
import com.demon.app.ui.dialog.TestListDialogFragment;
import com.demon.app.ui.dialog.TestListPopupWindow;
import com.demon.app.ui.dialog.TestPopupWindow;
import com.demon.app.ui.dialog.TestPopupWindow2;
import com.demon.app.ui.dialog.TestPopupWindow3;
import com.demon.app.ui.dialog.TestPopupWindow4;
import com.demon.app.ui.fragment.test.PublicFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.sframe.annotation.RunWithAsync;
import androidx.sframe.compat.DataCompat;
import androidx.sframe.compat.ToastCompat;
import androidx.sframe.model.AppMenuModel;
import androidx.sframe.ui.abs.AbsListFragment;
import androidx.sframe.ui.controller.AppNavigation;
import androidx.sframe.ui.controller.DialogFragmentPageController;
import androidx.sframe.ui.controller.RecyclerAdapterController;
import androidx.sframe.ui.controller.impl.AppToolbarMethod;
import androidx.sframe.widget.adapter.RecyclerAdapter;
import androidx.sframe.widget.adapter.RecyclerChildAdapter;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public class TestFragment extends AbsListFragment<TestModel> implements RecyclerAdapter.OnItemClickListener<TestModel> {

	private int[] colors = new int[]{
			Color.GRAY,
			Color.WHITE,
			Color.GREEN,
			Color.BLUE
	};

	@Override
	public View onCreateItemView(RecyclerAdapterController<TestModel> adapterController, LayoutInflater inflater, ViewGroup parent, int itemViewType) {
		return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
	}

	@Override
	public void onBindItemView(RecyclerAdapter.ViewHolder<TestModel> holder, int position, @Nullable List<Object> payloads) {
		final TestModel model = holder.findDataSourceByPosition(position);
		holder.getViewController()
				.findAt(android.R.id.text1)
				.setBackgroundColor(colors[position % colors.length])
				.methodAtTextView()
				.setText(model.getTitle());
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		super.onPageViewCreated(savedInstanceState);
		this.getToolbarController()
				.getToolbarMethod()
				.setTitle("TestFragment")
				.addMenu(new AppMenuModel(1, "Menu"))
				.setOnMenuClickListener(new AppToolbarMethod.OnMenuClickListener() {
					@Override
					public void onMenuClick(@NonNull View view, @NonNull RecyclerAdapter.ViewHolder<AppMenuModel> holder, int position) {
						TestPopupWindow popupWindow = new TestPopupWindow(getPageController());
						popupWindow.showAsDropDown(view, 0, 0, Gravity.CENTER);
					}
				});

		// 添加头部
		this.getObjectListController().setHeaderDelegate(new HeaderDelegate<TestModel>())
				.setOnItemClickListener(new RecyclerAdapterController.OnChildItemClickListener<String, TestModel>() {
					@Override
					public void onItemClick(View childView, RecyclerChildAdapter.ViewHolder<String, TestModel> holder, int position) {
						getObjectListController().removeHeaderViewAt(position);
					}
				})
				.getDataSourceController()
				.addDataSourceList(DataCompat.getStringByPrefix(1, 2, "Header"));
		// 添加底部
		this.getObjectListController().setFooterDelegate(new FooterDelegate<TestModel>())
				.setOnItemClickListener(new RecyclerAdapterController.OnChildItemClickListener<String, TestModel>() {
					@Override
					public void onItemClick(View childView, RecyclerChildAdapter.ViewHolder<String, TestModel> holder, int position) {
						getObjectListController().removeFooterViewAt(position);
					}
				})
				.getDataSourceController()
				.addDataSourceList(DataCompat.getStringByPrefix(1, 10, "Footer"));

	}

	@RunWithAsync
	@Override
	public void onPageDataSourceChanged(@Nullable Object params, int page, int limit) {
		final List<TestModel> models = new ArrayList<>();
		models.add(new TestModel(0, "测试 PushPage(Host:Activity)"));
		models.add(new TestModel(1, "测试 StartPage"));
		models.add(new TestModel(2, "测试 StartPageForResult"));
		models.add(new TestModel(3, "测试 StartActivity"));
		models.add(new TestModel(4, "测试 StartActivityForResult"));
		models.add(new TestModel(5, "测试 DialogFragment(显示：Push)"));
		models.add(new TestModel(6, "测试 DialogFragment(显示：手动)"));
		models.add(new TestModel(7, "测试 DialogFragment(指定在View下面)"));
		models.add(new TestModel(8, "测试 ListDialogFragment(非全屏)"));
		models.add(new TestModel(9, "测试 ListDialogFragment(全屏)"));
		models.add(new TestModel(10, "测试 PopupWindow2"));
		models.add(new TestModel(11, "测试 PopupWindow3"));
		models.add(new TestModel(12, "测试 PopupWindow4(全屏)"));
		models.add(new TestModel(13, "测试 ListPopupWindow"));
		models.add(new TestModel(14, "测试 ProgressDialogFragment"));

		this.getPageController()
				.getViewController()
				.postDelayed(new Runnable() {

					@Override
					public void run() {
						getObjectListController().addDataSourceList(models);
					}
				}, 2000);
	}

	@Override
	public void onItemClick(View childView, RecyclerAdapter.ViewHolder<TestModel> holder, int position) {
		final TestModel model = holder.findDataSourceByPosition(position);

		switch (model.getId()) {
			case 0:
				// push 界面到栈，不跳转新Activity
				this.getPageController()
						.getAppNavController()
						.pushPage(PublicFragment.class);
				break;
			case 1:
				// push 界面到栈，跳转新Activity
				this.getPageController()
						.getAppNavController()
						.startPage(PublicFragment.class);
				break;
			case 2:
				// push 界面到栈，跳转新Activity，并返回界面请求结果
				this.getPageController()
						.getAppNavController()
						.startPageForResult(PublicFragment.class, 1);
				break;
			case 3:
				// 跳转新的Activity
				this.getPageController()
						.getAppNavController()
						.startActivity(TestActivity.class);
				break;
			case 4:
				// 跳转新的Activity，并返回界面请求结果
				this.getPageController()
						.getAppNavController()
						.startActivityForResult(TestActivity.class, 1);
				break;
			case 5:
				// 调用DialogFragment方法A
				this.getPageController().getAppNavController().showPage(TestDialogFragment.class);
				break;
			case 6:
				// 调用DialogFragment方法B
				TestDialogFragment fragment = new TestDialogFragment(this.getPageController());
				DialogFragmentPageController<AppCompatDialogFragment> pageController = fragment.getPageController();
				pageController.setGravity(Gravity.CENTER);
				pageController.setBackgroundAlpha(0.22f);
				pageController.show();
				break;
			case 7:
				// DialogFragment.show(View) 用法
				TestDialogFragment2 fragment2 = new TestDialogFragment2(this.getPageController());
				DialogFragmentPageController<AppCompatDialogFragment> pageController2 = fragment2.getPageController();
				pageController2.setAnimationStyle(R.style.DialogAnimation_SlideInFromBottomOutToBottom);
				pageController2.show(childView);
				break;
			case 8:
				// ListDialogFragment 用法(非全屏)
				TestListDialogFragment listFragment = new TestListDialogFragment(this.getPageController());
				DialogFragmentPageController<AppCompatDialogFragment> listPageController = listFragment.getPageController();
				listPageController.setAnimationStyle(R.style.DialogAnimation_SlideInFromBottomOutToBottom);
				listPageController.show(childView);
				break;
			case 9:
				// ListDialogFragment 用法(全屏)
				TestListDialogFragment listFragment2 = new TestListDialogFragment(this.getPageController());
				DialogFragmentPageController<AppCompatDialogFragment> listPageController2 = listFragment2.getPageController();
				// 此处取决于是否全屏
				listPageController2.show();
				break;
			case 10:
				TestPopupWindow2 popupWindow2 = new TestPopupWindow2(this.getPageController());
				popupWindow2.showAsDropDown(this.getToolbarController().getToolbarMethod().getToolbarView());
				break;
			case 11:
				TestPopupWindow3 popupWindow3 = new TestPopupWindow3(this.getPageController());
				popupWindow3.showAsDropDown(childView);
				break;
			case 12: //全屏
				TestPopupWindow4 popupWindow4 = new TestPopupWindow4(this.getPageController());
				popupWindow4.showAtLocation(childView, Gravity.NO_GRAVITY, 0,0);
				break;
			case 13:
				TestListPopupWindow listPopupWindow = new TestListPopupWindow(this.getPageController());
				listPopupWindow.showAsDropDown(childView);
				break;
			case 14:
				AppNavigation.findPageController(childView)
						.getAppNavController()
						.showProgressPage();
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ToastCompat.toastDebug("Page " + this.getClass().getSimpleName() + " Result : " + requestCode + " , " + resultCode);
	}
}
