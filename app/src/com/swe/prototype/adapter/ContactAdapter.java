package com.swe.prototype.adapter;

import com.swe.prototype.R;
import com.swe.prototype.models.Contact;
import com.swe.prototype.models.server.ServerContact;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;

public class ContactAdapter extends MergeAdapter {
	private final static String TAG = "ContactAdapter";
	private Context context;

	private static class ViewHolder {
		protected TextView name;
		protected TextView number;
		protected TextView account_tag;
	}

	public ContactAdapter(Context context) {
		super();
		this.context = context;
	}

	public View getView(int position, View view, ViewGroup viewGroup) {

		// create a ViewHolder reference
		ViewHolder holder;

		// check to see if the reused view is null or not, if is not null then
		// reuse it
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater li = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = li.inflate(R.layout.list_item_contact, null);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.number = (TextView) view.findViewById(R.id.number);
			holder.account_tag = (TextView) view
					.findViewById(R.id.list_contact_account_tag);
			// the setTag is used to store the data within this view
			view.setTag(holder);
		} else {
			// the getTag returns the viewHolder object set as a tag to the view
			holder = (ViewHolder) view.getTag();
		}

		Contact o = (Contact) getItem(position);

		holder.name.setText(o.toString());
		holder.number.setText(o.getPhoneumber());
		holder.account_tag.setText(o.getAccountTag());

		return view;
	}
}