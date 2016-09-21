package com.cat.catnip.mobile;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class AssignmentPagerAdapter extends PagerAdapter {
    public int getCount() {
	return 2;
    }

    public Object instantiateItem(View collection, int position) {
	LayoutInflater inflater = (LayoutInflater) collection.getContext()
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	int resId = 0;
	switch (position) {
	case 0:
	    resId = R.layout.current_assignments;
	    break;
	case 1:
	    resId = R.layout.past_assignments;
	    break;
	}
	View view = inflater.inflate(resId, null);
	((ViewPager) collection).addView(view, 0);
	return view;
    }

    @Override
    public void destroyItem(View view, int arg1, Object arg2) {
	((ViewPager) view).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View view, Object arg1) {
	return view == ((View) arg1);
    }

    public Parcelable saveState() {
	return null;
    }
}