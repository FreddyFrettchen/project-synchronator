package com.swe.prototype.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.swe.prototype.R;
import com.swe.prototype.models.Note;

public class NoteAdapter extends MergeAdapter {
	private final static String TAG = "NoteAdapter";
	private Context context;

	private static class ViewHolder {
		protected TextView title;
		protected TextView account_tag;
	}

	public NoteAdapter(Context context) {
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
			view = li.inflate(R.layout.list_item_note, null);
			holder.title = (TextView) view.findViewById(R.id.list_note_title);
			holder.account_tag = (TextView) view.findViewById(R.id.list_note_account_tag);
			// the setTag is used to store the data within this view
			view.setTag(holder);
		} else {
			// the getTag returns the viewHolder object set as a tag to the view
			holder = (ViewHolder) view.getTag();
		}

		Note o = (Note) getItem(position);
		holder.title.setText(o.getTitle());
		holder.account_tag.setText(o.getAccountTag());

		return view;
	}
}
