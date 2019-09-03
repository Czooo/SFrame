package androidx.demon.ui.abs;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.demon.ui.controller.AppPageController;
import androidx.demon.ui.controller.RecyclerAdapterController;
import androidx.demon.ui.controller.UIObjectListController;
import androidx.demon.ui.controller.impl.UIObjectListControllerImpl;

/**
 * Author create by ok on 2019-06-03
 * Email : ok@163.com.
 */
public abstract class AbsListDialogFragment<DataSource> extends AbsDialogFragment implements UIObjectListController.OnDataSourceListener, RecyclerAdapterController.Delegate<DataSource> {

	private UIObjectListController<AppCompatDialogFragment, DataSource> mObjectListController;

	public AbsListDialogFragment(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

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
	public final UIObjectListController<AppCompatDialogFragment, DataSource> getObjectListController() {
		if (this.mObjectListController == null) {
			this.mObjectListController = new UIObjectListControllerImpl<>(this.getPageController());
		}
		return this.mObjectListController;
	}

	@Override
	public int getItemViewType(RecyclerAdapterController<DataSource> adapterController, int position) {
		return 0;
	}
}
