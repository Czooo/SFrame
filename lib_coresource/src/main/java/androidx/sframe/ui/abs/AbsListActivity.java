package androidx.sframe.ui.abs;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.UIObjectListController;
import androidx.sframe.ui.controller.impl.UIObjectListControllerImpl;
import androidx.fragment.app.FragmentActivity;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019-06-03
 * Email : ok@163.com.
 */
public abstract class AbsListActivity<DataSource> extends AbsActivity implements UIObjectListController.OnDataSourceListener, RecyclerAdapter.Delegate<DataSource> {

	private UIObjectListController<FragmentActivity, DataSource> mObjectListController;

	@Override
	public final int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return this.getObjectListController().onPageLayoutId(savedInstanceState);
	}

	@CallSuper
	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		this.getObjectListController().onPageViewCreated(savedInstanceState);
	}

	@CallSuper
	@Override
	public void onPageDataSourceChanged(@Nullable Object params) {
		this.getObjectListController().onPageDataSourceChanged(params);
	}

	@NonNull
	public final UIObjectListController<FragmentActivity, DataSource> getObjectListController() {
		if (this.mObjectListController == null) {
			this.mObjectListController = new UIObjectListControllerImpl<>(this.getPageController());
		}
		return this.mObjectListController;
	}

	@Override
	public int getItemViewType(@NonNull RecyclerAdapter<DataSource> adapter, int position) {
		return 0;
	}
}
