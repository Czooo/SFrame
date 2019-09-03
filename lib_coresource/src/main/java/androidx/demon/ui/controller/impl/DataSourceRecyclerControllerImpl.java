package androidx.demon.ui.controller.impl;

import java.util.Collection;

import androidx.demon.ui.controller.DataSourceController;
import androidx.demon.ui.controller.DataSourceNotifyController;
import androidx.demon.ui.controller.DataSourceNotifyController2;
import androidx.demon.widget.adapter.RecyclerAdapter;

/**
 * Author create by ok on 2019/2/9
 * Email : ok@163.com.
 */
public class DataSourceRecyclerControllerImpl<DataType> extends DataSourceControllerImpl<DataType> implements DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> {

	private final RecyclerAdapter<DataType> mAdapter;

	public DataSourceRecyclerControllerImpl(RecyclerAdapter<DataType> adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public DataSourceController<DataType> addDataSourceList(Collection<? extends DataType> dataTypeList, int index) {
		super.addDataSourceList(dataTypeList, index);
		// 添加数据时，必须调用这个重新刷新整个列表，否则再调用其他方法(notifyItemRemoved等)会报错，出现不可预知的RecyclerView异常
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> removeDataSource(int position) {
		super.removeDataSource(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, size() - position);
		return this;
	}

	@Override
	public DataSourceController<DataType> removeDataSource(Collection<? extends DataType> dataTypeList) {
		super.removeDataSource(dataTypeList);
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> removeAll() {
		super.removeAll();
		return notifyDataSetChanged();
	}

	@Override
	public DataSourceController<DataType> moveDataSourceOf(int fromPosition, int toPosition) {
		super.moveDataSourceOf(fromPosition, toPosition);
		if (fromPosition != toPosition) {
			notifyItemMoved(fromPosition, toPosition);
			notifyItemChanged(fromPosition);
			notifyItemChanged(toPosition);
		}
		return this;
	}

	@Override
	public DataSourceController<DataType> markerAll() {
		super.markerAll();
		notifyItemRangeChanged(0, size());
		return this;
	}

	@Override
	public DataSourceController<DataType> unmarkerAll() {
		super.unmarkerAll();
		notifyItemRangeChanged(0, size());
		return this;
	}

	@Override
	public boolean marker(DataType dataType) {
		boolean isMarker = super.marker(dataType);
		notifyItemChanged(indexOf(dataType));
		return isMarker;
	}

	@Override
	public boolean singleMarker(DataType dataType) {
		boolean isMarker = super.singleMarker(dataType);
		notifyItemChanged(indexOf(dataType));
		return isMarker;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemRemoved(int position) {
		return notifyItemRangeRemoved(position, 1);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemRangeRemoved(int positionStart, int itemCount) {
		getDataSourceAdapter().notifyItemRangeRemoved(getRealPosition(positionStart), itemCount);
		return this;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemChanged(int position) {
		return notifyItemRangeChanged(position, 1);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemRangeChanged(int positionStart, int itemCount) {
		return notifyItemRangeChanged(positionStart, itemCount, null);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
		getDataSourceAdapter().notifyItemRangeChanged(getRealPosition(positionStart), itemCount, payload);
		return this;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemInserted(int position) {
		return notifyItemRangeInserted(position, 1);
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemRangeInserted(int positionStart, int itemCount) {
		getDataSourceAdapter().notifyItemRangeInserted(getRealPosition(positionStart), itemCount);
		return this;
	}

	@Override
	public final DataSourceNotifyController2<RecyclerAdapter<DataType>, DataType> notifyItemMoved(int fromPosition, int toPosition) {
		getDataSourceAdapter().notifyItemMoved(getRealPosition(fromPosition), getRealPosition(toPosition));
		return this;
	}

	@Override
	public final DataSourceNotifyController<RecyclerAdapter<DataType>, DataType> notifyDataSetChanged() {
		getDataSourceAdapter().notifyDataSetChanged();
		return this;
	}

	@Override
	public RecyclerAdapter<DataType> getDataSourceAdapter() {
		return mAdapter;
	}

	private int getRealPosition(int position) {
		int realPosition = position;

		RecyclerAdapter mAdapter = getDataSourceAdapter();
		if (mAdapter.hasHeaderAdapter()) {
			realPosition += mAdapter.getHeaderAdapter().getItemCount();
		}
		return realPosition;
	}
}
