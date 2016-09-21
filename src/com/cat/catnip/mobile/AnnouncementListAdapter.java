package com.cat.catnip.mobile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AnnouncementListAdapter extends ArrayAdapter<AnnouncementItem> {

    /**
     * The ID of the row
     */
    private int layoutResourceId;

    /**
     * Creates a new ListAdapter for an Announcement
     * 
     * @param context
     *            this applications context
     * @param layoutResourceId
     *            the ID of the row
     */
    public AnnouncementListAdapter(Context context, int layoutResourceId) {
	super(context, layoutResourceId);
	this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
	convertView = inflater.inflate(layoutResourceId, parent, false);
	TextView title = (TextView) convertView.findViewById(R.id.titleView);
	TextView separator = (TextView) convertView
		.findViewById(R.id.separatorView);
	TextView description = (TextView) convertView
		.findViewById(R.id.descriptionView);
	separator.setVisibility(TextView.GONE);
	if (position % 2 == 1) {
	    //If position is odd, change cell color to be a translucent gray
	    convertView.setBackgroundColor(Color.argb(64, 128, 128, 128));
	} else {
	    //Otherwise, it even, make it transparent
	    convertView.setBackgroundColor(Color.TRANSPARENT);
	}
	title.setText(this.getItem(position).getTitle());
	description.setText(Html
		.fromHtml(this.getItem(position).getDescription()).toString()
		.trim());
	return convertView;
    }
}