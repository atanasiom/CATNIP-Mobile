package com.cat.catnip.mobile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EmailListAdapter extends ArrayAdapter<String> {
    int layoutResourceId;

    public EmailListAdapter(Context context, int layoutResourceId) {
	super(context, layoutResourceId);
	this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
	convertView = inflater.inflate(layoutResourceId, parent, false);
	TextView tv = (TextView) convertView.findViewById(R.id.name);
	tv.setText(getItem(position));
	return convertView;
    }
}