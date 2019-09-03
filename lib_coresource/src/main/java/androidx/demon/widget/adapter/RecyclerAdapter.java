package androidx.demon.widget.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.demon.compat.CoreCompat;
import androidx.demon.ui.controller.DataSourceNotifyController2;
import androidx.demon.ui.controller.RecyclerAdapterController;
import androidx.demon.ui.controller.UIViewController;
import androidx.demon.ui.controller.impl.DataSourceRecyclerControllerImpl;
import androidx.demon.ui.controller.impl.RecyclerAdapterControllerImpl;
import androidx.demon.ui.controller.impl.UIViewControllerImpl;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author create by ok on 2019/1/24
 * Email : ok@163.com.
 */
public class RecyclerAdapter<DataSource> extends RecyclerAdapterControllerImpl<DataSource> {

	private DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> mDataSourceController;

	@Override
	public RecyclerAdapter<DataSource> getAdapter() {
		return this;
	}

	@Override
	public DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> getDataSourceNotifyController() {
		if (mDataSourceController == null) {
			mDataSourceController = new DataSourceRecyclerControllerImpl<>(this);
		}
		return mDataSourceController;
	}

	public static class ViewHolder<DataSource> extends RecyclerView.ViewHolder {

		private final RecyclerView mRecyclerView;
		private final UIViewController mViewController;
		private final RecyclerAdapterController<DataSource> mRecyclerAdapterController;

		public ViewHolder(@NonNull RecyclerAdapterController<DataSource> recyclerAdapterController, @NonNull ViewGroup parent, @NonNull View itemView) {
			super(CoreCompat.removeInParentView(itemView));
			this.mRecyclerView = (RecyclerView) parent;
			this.mViewController = new UIViewControllerImpl(itemView);
			this.mRecyclerAdapterController = recyclerAdapterController;
		}

		public final void notifyDataSetChanged() {
			this.notifyItemChanged(getAdapterPosition());
		}

		public final void notifyItemChanged(int adapterPosition) {
			getRecyclerAdapterController().getAdapter().notifyItemChanged(adapterPosition);
		}

		public final View getItemView() {
			return this.itemView;
		}

		public final int getDataSourcePosition() {
			return getRecyclerAdapterController().getRealPosition(getAdapterPosition());
		}

		public final int getDataSourceItemType() {
			return getRecyclerAdapterController().getRealItemViewType(getAdapterPosition());
		}

		public final DataSource findDataSourceByPosition(int position) {
			return getDataSourceNotifyController().findDataSourceByPosition(position);
		}

		public final DataSourceNotifyController2<RecyclerAdapter<DataSource>, DataSource> getDataSourceNotifyController() {
			return getRecyclerAdapterController().getDataSourceNotifyController();
		}

		public final RecyclerAdapterController<DataSource> getRecyclerAdapterController() {
			return mRecyclerAdapterController;
		}

		public UIViewController getViewController() {
			return mViewController;
		}

		public RecyclerView getRecyclerView() {
			return mRecyclerView;
		}
	}
}
