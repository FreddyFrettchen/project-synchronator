package com.swe.prototype.adapter;

import com.swe.prototype.R;
import com.swe.prototype.models.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {
	// View lookup cache
	private static class ViewHolder {
		TextView name;
		TextView number;
	}

	public ContactAdapter(Context context) {
		super(context, R.layout.list_item_contact);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Contact user = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.list_item_contact, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.number = (TextView) convertView.findViewById(R.id.number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		viewHolder.name.setText(user.getName());
		viewHolder.number.setText(user.getNumber());
		// Return the completed view to render on screen
		return convertView;
	}
}