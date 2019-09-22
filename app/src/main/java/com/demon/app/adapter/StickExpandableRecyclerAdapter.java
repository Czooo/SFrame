package com.demon.app.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.demon.app.R;
import com.demon.app.model.Contact;
import com.demon.app.model.ContactGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.sframe.widget.adapter.ExpandableRecyclerAdapter;

/**
 * @Author create by Zoran on 2019-09-15
 * @Email : 171905184@qq.com
 * @Description :
 */
public class StickExpandableRecyclerAdapter extends ExpandableRecyclerAdapter<ContactGroup> {

	@Override
	public ViewHolder<ContactGroup> onCreateGroupViewHolder(@NonNull ViewGroup parent, int itemViewType) {
		return new ViewHolder<>(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
	}

	@Override
	public ViewHolder<ContactGroup> onCreateChildrenViewHolder(@NonNull ViewGroup parent, int itemViewType) {
		if (1 == itemViewType) {
			return new ViewHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_test_1, parent, false));
		}
		return new ViewHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_test, parent, false));
	}

	@Override
	public void onBindGroupViewHolder(@NonNull ViewHolder<ContactGroup> holder, int groupPosition, @NonNull List<Object> payloads) {
		ColorDrawable drawable = new ColorDrawable(Color.WHITE);
		drawable.setAlpha((int) (0.88F * 255));

		holder.getViewController()
				.findAt(android.R.id.text1)
				.setBackground(drawable)
				.methodAtTextView()
				.setText(holder.findDataSourceByPosition(groupPosition).getTitle());
	}

	@Override
	public void onBindChildrenViewHolder(@NonNull ViewHolder<ContactGroup> holder, int groupPosition, int position, @NonNull List<Object> payloads) {
		ContactGroup contactGroup = holder.findDataSourceByPosition(groupPosition);
		Contact contact = contactGroup.getContacts().get(position);

		holder.getViewController()
				.findAt(R.id.text1)
				.methodAtTextView()
				.setGravity(Gravity.CENTER)
				.setText(contact.getUserName() + " : " + contact.getPhoneNo());

		if (1 == holder.getRealItemViewType()) {
			holder.getViewController()
					.findAt(R.id.text1)
					.setBackgroundColor(Color.GRAY);
		}
	}

	@Override
	public int getChildrenItemViewType(int groupPosition, int position) {
		Contact contact = this.findDataSourceByPosition(groupPosition).getContacts().get(position);
		if (1 == contact.getIdentiy()) {
			return 1;
		}
		return super.getChildrenItemViewType(groupPosition, position);
	}

	@Override
	public int getGroupCount() {
		return this.getDataSourceController().getDataSourceCount();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.getDataSourceController().findDataSourceByPosition(groupPosition).size();
	}

	@Override
	public boolean hasGroupEnabled(int groupPosition) {
		if (groupPosition % 2 == 0) {
//			return false;
		}
		return super.hasGroupEnabled(groupPosition);
	}
}
