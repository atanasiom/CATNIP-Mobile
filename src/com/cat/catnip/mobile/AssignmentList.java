package com.cat.catnip.mobile;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentList extends Activity {

    final static String ASSIGNMENT_INFORMATION = "com.cat.catnip.mobile.ASSIGNMENT_INFORMATION";
    private final String ALCOTT_URL = "http://mysite.cat.pcsb.org/personal/alcottl/_layouts/listfeed.aspx?List=%7BAF5AB7B9-40BE-4A5F-9334-FBA121495926%7D";
    private final String BORG_URL = "http://mysite.cat.pcsb.org/personal/borgc/_layouts/listfeed.aspx?List=%7BA70622AE-FE36-4213-862A-C3F5DE38DAC6%7D";
    private final String DICKMAN_URL = "http://mysite.cat.pcsb.org/personal/dickman/_layouts/listfeed.aspx?List=%7BCEFA27F9-9122-4F34-B154-1ECE1411D7B4%7D";
    private final String FREWIN_URL = "http://mysite.cat.pcsb.org/personal/frewinr/_layouts/listfeed.aspx?List=%7B9CFB690D-CDFC-4502-9075-ACB08CB6D04F%7D";
    private final String GARBUTT_URL = "http://mysite.cat.pcsb.org/personal/garbuttg/_layouts/listfeed.aspx?List=%7BBE0BEB91-C1AA-44A4-8498-C7D2FE1F79DA%7D";
    private final String LAKE_URL = "http://mysite.cat.pcsb.org/personal/lakel/_layouts/listfeed.aspx?List=%7B08BDC418%2D4C98%2D453D%2D9F26%2DDEFD8C96C38E%7D";
    private final String LEADBETTER_URL = "http://mysite.cat.pcsb.org/personal/leadbetterd/_layouts/listfeed.aspx?List=%7BA1F58067%2D0BBE%2D4FE0%2DAE4B%2D98D88A0AAA59%7D";
    private final String NESS_URL = "http://mysite.cat.pcsb.org/personal/nessj/_layouts/listfeed.aspx?List=%7B58D0B69C%2D4630%2D4A8B%2D95C9%2D50A2C92D82D0%7D";
    private final String PACOWTA_URL = "http://mysite.cat.pcsb.org/personal/pacowtaj/_layouts/listfeed.aspx?List=%7B970F7F2E-F4C5-4CCC-ADD7-CAB12356D66F%7D";
    private final String PENKETHMAN_URL = "http://mysite.cat.pcsb.org/personal/penkethmant/_layouts/listfeed.aspx?List=%7BA0C40F88%2D304E%2D485E%2D865C%2D52140D989C8C%7D";
    private final String SCHNEIDER_URL = "http://mysite.cat.pcsb.org/personal/schneiderch/_layouts/listfeed.aspx?List=%7BD4F91ECB%2D288D%2D4A01%2DA6CE%2D3C660D777245%7D";
    private final String TENCZA_URL = "http://mysite.cat.pcsb.org/personal/tenczar/_layouts/listfeed.aspx?List=%7B2C95827C%2DE15F%2D4515%2D8EDB%2D190FAC722264%7D";
    private final String ZAVADIL_URL = "http://mysite.cat.pcsb.org/personal/zavadilk/_layouts/listfeed.aspx?List=%7B04CDDCA2%2D90FC%2D4A51%2DA26C%2DFB11C61D674E%7D";
    private final String[] URLS = { ALCOTT_URL, BORG_URL, DICKMAN_URL,
	    FREWIN_URL, GARBUTT_URL, LAKE_URL, LEADBETTER_URL, NESS_URL,
	    PACOWTA_URL, PENKETHMAN_URL, SCHNEIDER_URL, TENCZA_URL, ZAVADIL_URL };
    private final String[] NAMES = { "Ms. Alcott", "Mr. Borg", "Mr. Dickman",
	    "Mr. Frewin", "Dr. Garbutt", "Mrs. Lake", "Ms. Leadbetter",
	    "Mr. Ness", "Ms. Pacowta", "Mr. Penkethman", "Mr. Schneider",
	    "Mr. Tencza", "Ms. Zavadil" };
    private Button currentButton;
    private Button pastButton;
    private ListView currentList;
    private ListView pastList;
    private TextView pastNoAssignmentView;
    private TextView currentNoAssignmentView;
    private TextView pastTeacher;
    private TextView currentTeacher;
    private AssignmentPagerAdapter adapter;
    private ViewPager assignmentPager;
    private AssignmentListAdapter currentAdapter;
    private AssignmentListAdapter pastAdapter;
    private AlertDialog descriptionPopup;
    private boolean currentDone;
    private boolean pastDone;

    @Override
    public void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	setContentView(R.layout.activity_assignment_list);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	if (Build.VERSION.SDK_INT >= 11) {
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setTitle("Assignments");
	}
	adapter = new AssignmentPagerAdapter();
	assignmentPager = (ViewPager) findViewById(R.id.listPager);
	assignmentPager.setAdapter(adapter);
	assignmentPager.setCurrentItem(0);
	currentButton = (Button) findViewById(R.id.currentButton);
	pastButton = (Button) findViewById(R.id.pastButton);
	final int width = getWindowManager().getDefaultDisplay().getWidth() / 2;
	currentButton.setWidth(width);
	pastButton.setWidth(width);
	currentButton.setVisibility(Button.INVISIBLE);
	pastButton.setVisibility(Button.INVISIBLE);
	currentAdapter = new AssignmentListAdapter(this, R.layout.row);
	pastAdapter = new AssignmentListAdapter(this, R.layout.row);
	new LoadAssignments().execute();
    }

    @Override
    public void onBackPressed() {
	super.onBackPressed();
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
    }

    private OnScrollListener currentScrollListener = new OnScrollListener() {
	public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
	    if (currentAdapter.getCount() != 0
		    && currentAdapter.getItem(firstVisibleItem) != null)
		currentTeacher.setText(currentAdapter.getItem(firstVisibleItem)
			.getAuthor());
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	    //Do nothing
	}
    };

    private OnScrollListener pastScrollListener = new OnScrollListener() {
	public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
	    if (pastAdapter.getCount() != 0
		    && pastAdapter.getItem(firstVisibleItem) != null)
		pastTeacher.setText(pastAdapter.getItem(firstVisibleItem)
			.getAuthor());
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	    //Do nothing
	}
    };

    private OnItemClickListener currentClickListener = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
		final int position, long id) {
	    if (!currentAdapter.getItem(position).isSeparator()) {

		descriptionPopup = new AlertDialog.Builder(AssignmentList.this)
			.create();
		descriptionPopup.setTitle(Html.fromHtml(currentAdapter.getItem(
			position).getTitle()));
		descriptionPopup.setMessage(Html.fromHtml(currentAdapter
			.getItem(position).toString()));
		descriptionPopup.setButton(DialogInterface.BUTTON_POSITIVE,
			"Ok", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(
				    DialogInterface descriptionPopup, int which) {
				descriptionPopup.dismiss();
			    }
			});
		descriptionPopup.setButton(DialogInterface.BUTTON_NEGATIVE,
			"Add to calendar",
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				Intent intent = new Intent(Intent.ACTION_INSERT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra(
					Events.TITLE,
					Html.fromHtml(
						currentAdapter
							.getItem(position)
							.getTitle()).toString());
				intent.putExtra(
					Events.DESCRIPTION,
					Html.fromHtml(
						currentAdapter
							.getItem(position)
							.getDescription())
						.toString().trim());
				intent.putExtra(
					CalendarContract.EXTRA_EVENT_ALL_DAY,
					false);
				intent.putExtra(
					CalendarContract.EXTRA_EVENT_BEGIN_TIME,
					currentAdapter.getItem(position)
						.getEndDate().getTime());
				startActivity(intent);
			    }
			});
		descriptionPopup.show();
		((TextView) descriptionPopup.findViewById(android.R.id.message))
			.setMovementMethod(LinkMovementMethod.getInstance());
	    }
	}
    };

    private OnItemClickListener pastClickListener = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
		final int position, long id) {
	    if (!pastAdapter.getItem(position).isSeparator()) {
		descriptionPopup = new AlertDialog.Builder(AssignmentList.this)
			.create();
		descriptionPopup.setTitle(Html.fromHtml(pastAdapter.getItem(
			position).getTitle()));
		descriptionPopup.setMessage(Html.fromHtml(pastAdapter.getItem(
			position).toString()));
		descriptionPopup.setButton(DialogInterface.BUTTON_POSITIVE,
			"Ok", new DialogInterface.OnClickListener() {
			    public void onClick(
				    DialogInterface descriptionPopup, int which) {
				descriptionPopup.dismiss();
			    }
			});
		descriptionPopup.setButton(DialogInterface.BUTTON_NEGATIVE,
			"Add to calendar",
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				Intent intent = new Intent(Intent.ACTION_INSERT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra(
					Events.TITLE,
					Html.fromHtml(
						pastAdapter.getItem(position)
							.getTitle()).toString());
				intent.putExtra(
					Events.DESCRIPTION,
					Html.fromHtml(
						pastAdapter.getItem(position)
							.getDescription())
						.toString().trim());
				intent.putExtra(
					CalendarContract.EXTRA_EVENT_ALL_DAY,
					false);
				intent.putExtra(
					CalendarContract.EXTRA_EVENT_BEGIN_TIME,
					pastAdapter.getItem(position)
						.getEndDate().getTime());
				startActivity(intent);
			    }
			});
		descriptionPopup.show();
		((TextView) descriptionPopup.findViewById(android.R.id.message))
			.setMovementMethod(LinkMovementMethod.getInstance());
	    }
	}
    };

    public void gotoPast(View view) {
	assignmentPager.setCurrentItem(1, true);
    }

    public void gotoCurrent(View view) {
	assignmentPager.setCurrentItem(0, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.home, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    Intent parentActivityIntent = new Intent(this, Teachers.class);
	    parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		    | Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(parentActivityIntent);
	    overridePendingTransition(android.R.anim.fade_in,
		    android.R.anim.fade_out);
	    finish();
	    return true;
	case R.id.refresh:
	    finish();
	    overridePendingTransition(android.R.anim.fade_in,
		    android.R.anim.fade_out);
	    startActivity(this.getIntent());
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    private class LoadAssignments extends AsyncTask<Void, Void, Void> {
	private final ProgressDialog dialog = new ProgressDialog(
		AssignmentList.this);

	@Override
	protected void onPreExecute() {
	    dialog.setCanceledOnTouchOutside(false);
	    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode,
			KeyEvent event) {
		    if (keyCode == KeyEvent.KEYCODE_BACK) {
			AssignmentList.this.finish();
			LoadAssignments.this.cancel(true);
			return true;
		    }
		    return false;
		}
	    });
	    if (isNetworkConnected())
		dialog.setMessage("Loading assignments");
	    else
		dialog.setMessage("Loading assignments from cache");
	    dialog.show();
	}

	@Override
	protected Void doInBackground(Void... unused) {
	    getAssignments();
	    runOnUiThread(new Runnable() {
		public void run() {
		    currentList = (ListView) findViewById(R.id.currentList);
		    pastList = (ListView) findViewById(R.id.pastList);
		    pastNoAssignmentView = (TextView) findViewById(R.id.pastNoAssignmentView);
		    currentNoAssignmentView = (TextView) findViewById(R.id.currentNoAssignmentView);
		    currentTeacher = (TextView) findViewById(R.id.currentTeacher);
		    pastTeacher = (TextView) findViewById(R.id.pastTeacher);
		    currentList.setAdapter(currentAdapter);
		    pastList.setAdapter(pastAdapter);
		    currentList.setOnScrollListener(currentScrollListener);
		    pastList.setOnScrollListener(pastScrollListener);
		    if (pastList.getCount() > 0)
			pastTeacher.setVisibility(TextView.VISIBLE);
		    else
			pastNoAssignmentView.setVisibility(TextView.VISIBLE);
		    if (currentList.getCount() > 0)
			currentTeacher.setVisibility(TextView.VISIBLE);
		    else
			currentNoAssignmentView.setVisibility(TextView.VISIBLE);
		}
	    });
	    return null;
	}

	@Override
	protected void onPostExecute(Void unused) {
	    if (dialog.isShowing())
		dialog.dismiss();
	    currentList.setOnScrollListener(currentScrollListener);
	    pastList.setOnScrollListener(pastScrollListener);
	    currentList.setOnItemClickListener(currentClickListener);
	    pastList.setOnItemClickListener(pastClickListener);
	    currentButton.setVisibility(Button.VISIBLE);
	    pastButton.setVisibility(Button.VISIBLE);
	    currentButton.setVisibility(Button.VISIBLE);
	    pastButton.setVisibility(Button.VISIBLE);
	}
    }

    private void getAssignments() {
	boolean[] selectedTeachers = getIntent().getBooleanArrayExtra(
		Teachers.SELECTED_TEACHERS);
	try {
	    for (int i = 0; i < selectedTeachers.length; i++) {
		if (selectedTeachers[i]) {
		    currentDone = false;
		    pastDone = false;
		    for (FeedItem item : new FeedParser(this, URLS[i],
			    NAMES[i], isNetworkConnected()).loadAssignments()) {
			if ((item.getEndDate().compareTo(Calendar.getInstance()
				.getTime())) < 0) {
			    if (!pastDone) {
				pastAdapter.addSeparator(new FeedItem(NAMES[i],
					true));
				pastDone = true;
			    }
			    pastAdapter.add(item);
			}
			if ((item.getEndDate().compareTo(Calendar.getInstance()
				.getTime())) >= 0) {
			    if (!currentDone) {
				currentAdapter.addSeparator(new FeedItem(
					NAMES[i], true));
				currentDone = true;
			    }
			    currentAdapter.add(item);
			}
		    }
		}
	    }
	} catch (IOException e) {
	    Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT)
		    .show();
	}
    }

    private boolean isNetworkConnected() {
	return ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
		.getActiveNetworkInfo() == null ? false : true;
    }
}
