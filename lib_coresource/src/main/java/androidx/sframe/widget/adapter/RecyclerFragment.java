package androidx.sframe.widget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author create by Zoran on 2019-09-13
 * @Email : 171905184@qq.com
 * @Description :
 */
public abstract class RecyclerFragment {

	private int tempItemViewType = RecyclerView.INVALID_TYPE;
	private View itemView;

	@NonNull
	public abstract View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

	@CallSuper
	public void onViewCreated(@NonNull View view, int position, @Nullable List<Object> payloads) {
		this.itemView = view;
	}

	public final int getTempItemViewType() {
		return this.tempItemViewType;
	}

	@Nullable
	public final View getView() {
		return this.itemView;
	}

	/* package */
	final void setTempItemViewType(int itemViewType) {
		this.tempItemViewType = itemViewType;
	}
}
