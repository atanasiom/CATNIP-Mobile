package com.cat.catnip.mobile;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AssignmentListAdapter extends ArrayAdapter<FeedItem> {

    private int layoutResourceId;

    public AssignmentListAdapter(Context context, int layoutResourceId) {
	super(context, layoutResourceId);
	this.layoutResourceId = layoutResourceId;
    }

    /**
     * Adds a separator to the end of the list. This isn't necessary, however it
     * clears up and confusion by just using the default add method.
     * 
     * @param item
     *            the FeedItem to add
     */
    public void addSeparator(FeedItem item) {
	super.add(item);
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
	TextView date = (TextView) convertView.findViewById(R.id.dateView);
	if (getItem(position).isSeparator()) {
	    title.setVisibility(TextView.GONE);
	    description.setVisibility(TextView.GONE);
	    date.setVisibility(TextView.GONE);
	    separator.setText(getItem(position).getTitle());
	} else {
	    separator.setVisibility(TextView.GONE);
	    title.setText(Html.fromHtml(getItem(position).getTitle()));
	    description.setText(Html
		    .fromHtml(getItem(position).getDescription()).toString()
		    .trim());
	    date.setText(getItem(position).getEndTime().substring(0, 10));
	}
	return convertView;
    }
}