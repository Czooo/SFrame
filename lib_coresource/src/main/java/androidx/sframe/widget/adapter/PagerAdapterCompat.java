package androidx.sframe.widget.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.demon.widget.adapter.PagerAdapter;
import androidx.sframe.ui.controller.DataSourceController;
import androidx.sframe.ui.controller.DataSourceNotifyController;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.impl.DataSourceNotifyControllerImpl;
import androidx.sframe.ui.controller.impl.UIViewControllerImpl;

/**
 * Author create by ok on 2019-09-05
 * Email : ok@163.com.
 */
public abstract class PagerAdapterCompat<VH extends PagerAdapterCompat.ViewHolder, DataSource> extends PagerAdapter<VH> {

	private DataSourceNotifyController<PagerAdapterCompat<VH, DataSource>, DataSource> mDataSourceController;

	@Override
	public int getItemCount() {
		return this.getDataSourceController().size();
	}

	@NonNull
	public final DataSourceController<DataSource> getDataSourceController() {
		return this.getDataSourceNotifyController();
	}

	@NonNull
	public DataSourceNotifyController<PagerAdapterCompat<VH, DataSource>, DataSource> getDataSourceNotifyController() {
		if(this.mDataSourceController == null) {
			this.mDataSourceController = new DataSourceNotifyControllerImpl<PagerAdapterCompat<VH, DataSource>, DataSource>(this) {
				@Override
				public DataSourceNotifyController<PagerAdapterCompat<VH, DataSource>, DataSource> notifyDataSetChanged() {
					this.getDataSourceAdapter().notifyDataSetChanged();
					return this;
				}
			};
		}
		return this.mDataSourceController;
	}

	@NonNull
	public final DataSource findDataSourceByPosition(int position) {
		return this.getDataSourceController().findDataSourceByPosition(position);
	}

	public static abstract class ViewHolder extends PagerAdapter.ViewHolder {

		private final UIViewController mViewController;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			this.mViewController = new UIViewControllerImpl(itemView);
		}

		@NonNull
		public UIViewController getViewController() {
			return this.mViewController;
		}
	}
}
