package com.demon.app.ui.fragment.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demon.app.R;
import com.demon.app.adapter.StickExpandableRecyclerAdapter;
import com.demon.app.model.Contact;
import com.demon.app.model.ContactGroup;
import com.demon.app.ui.fragment.agent.BannerRecyclerFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.abs.AbsListFragment;
import androidx.sframe.utils.ToastCompat;
import androidx.sframe.widget.adapter.ExpandableRecyclerAdapter;
import androidx.sframe.widget.adapter.RecyclerAdapter;

/**
 * @Author create by Zoran on 2019-09-14
 * @Email : 171905184@qq.com
 * @Description :
 */
public class StickyFragment extends AbsListFragment<ContactGroup> {

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {
		super.onPageViewCreated(savedInstanceState);

		View loadingView = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_page_loading_default, this.getObjectListController().getRecyclerView(), false);
		View emptyView = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_page_empty_default, this.getObjectListController().getRecyclerView(), false);

		final StickExpandableRecyclerAdapter expandableRecyclerAdapter = new StickExpandableRecyclerAdapter();
		expandableRecyclerAdapter.setLoadingView(loadingView);
		expandableRecyclerAdapter.setEmptyView(emptyView);
		expandableRecyclerAdapter.setShouldLoadingEnabled(true);
		expandableRecyclerAdapter.setShouldEmptyEnabled(true);

		expandableRecyclerAdapter.setOnItemClickListener(new ExpandableRecyclerAdapter.OnItemClickListener<ContactGroup>() {
			@Override
			public void onItemClick(@NonNull ExpandableRecyclerAdapter.ViewHolder<ContactGroup> holder, int groupPosition, int position) {
				ToastCompat.toastDebug(holder.findDataSourceByPosition(groupPosition).getContacts().get(position).toString());

				ContactGroup contactGroup = holder.findDataSourceByPosition(groupPosition);

//				for (Contact contact : contactGroup.getContacts()) {
//					contact.setPhoneNo("1380013800 - " + position);
//				}
//				holder.getDataSourceController().notifyGroupItemChanged(groupPosition);

				contactGroup.getContacts().remove(position);
				holder.getDataSourceController().notifyChildItemRemoved(groupPosition, position);
				holder.getDataSourceController().notifyChildItemRangeChanged(groupPosition, position, contactGroup.size() - position);

//				contactGroup.getContacts().obtain(position)
//						.setPhoneNo("1380013800 - " + position);
//				holder.getDataSourceController().notifyChildItemChanged(groupPosition, position);

//				Contact contact = new Contact("Zoran", "10086 -" + position);
//				contactGroup.addContact(position, contact);
//				holder.getDataSourceController().notifyChildItemInserted(groupPosition, position);
//				holder.getDataSourceController().notifyChildItemRangeChanged(groupPosition, position, contactGroup.size() - position);

//				int fromPosition = position;
//				int toPosition = 0;
//				Contact contact = contactGroup.getContacts().remove(fromPosition);
//				contactGroup.addContact(toPosition, contact);
//				holder.getDataSourceController().notifyChildItemMoved(groupPosition, fromPosition, toPosition);
//				holder.getDataSourceController().notifyChildItemChanged(groupPosition, fromPosition);
//				holder.getDataSourceController().notifyChildItemChanged(groupPosition, toPosition);
			}
		});

		expandableRecyclerAdapter.setOnGroupClickListener(new ExpandableRecyclerAdapter.OnGroupClickListener<ContactGroup>() {
			@Override
			public void onGroupClick(@NonNull ExpandableRecyclerAdapter.ViewHolder<ContactGroup> holder, int groupPosition) {
				ToastCompat.toastDebug(holder.findDataSourceByPosition(groupPosition).getTitle());

//				holder.getDataSourceController()
//						.removeAll();

				holder.getDataSourceController()
						.removeDataSource(groupPosition);

//				holder.getDataSourceController()
//						.moveDataSourceToStart(groupPosition);
			}
		});

		this.getObjectListController()
				.setAdapter(expandableRecyclerAdapter)
				.addHeaderView(new BannerRecyclerFragment(this.getPageController()))
				.addFooterView(R.layout.item_simple_test_footer);
	}

	@Override
	public void onPageDataSourceChanged(@Nullable Object params, int page, int limit) {
		final ArrayList<ContactGroup> contactGroups = new ArrayList<>();
		for (int groupPosition = 5 * (page - 1); groupPosition < 5 * page; groupPosition++) {
			ContactGroup contactGroup = new ContactGroup("Group Position : " + groupPosition);
			for (int childPosition = 0; childPosition < 10; childPosition++) {
				String userName = "Child [" + groupPosition + " , " + childPosition + "]";
				String phoneNo = "1356039830 - " + childPosition;
				Contact contact = new Contact(userName, phoneNo);

				if ((groupPosition == 1 && childPosition == 2)
						|| (groupPosition == 2 && childPosition == 3)) {
					contact.setIdentiy(1);
				}
				contactGroup.addContact(contact);
			}
			contactGroups.add(contactGroup);
		}

		this.getViewController()
				.postDelayed(new Runnable() {
					@Override
					public void run() {
						getObjectListController().addDataSource(contactGroups);
					}
				}, 2000);
	}

	@NonNull
	@Override
	public RecyclerAdapter.ViewHolder<ContactGroup> onCreateViewHolder(@NonNull RecyclerAdapter<ContactGroup> adapter, @NonNull ViewGroup parent, int itemViewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder<ContactGroup> holder, int position, @Nullable List<Object> payloads) {
	}
}
