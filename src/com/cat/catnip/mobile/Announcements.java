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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Announcements extends Activity {

    /**
     * Announcements URL
     */
    private final String URL = "http://www.cat.pcsb.org/_layouts/listfeed.aspx?List=%7BD8DE1D15-5D25-4E5C-A425-CBBC7BD72504%7D";
    private final String NAME = "Announcements";
    /**
     * List that contains all the announcements
     */
    private ListView announcementsList;
    /**
     * Displays when there are no announcements to view
     */
    private TextView noAnnouncementsView;
    private AnnouncementListAdapter announcementsAdapter;
    /**
     * Pops up when you click on an announcement item
     */
    private AlertDialog descriptionPopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_announcements);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	if (Build.VERSION.SDK_INT >= 11) {
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setTitle("Announcements");
	}
	announcementsAdapter = new AnnouncementListAdapter(this, R.layout.row);
	new LoadAnnouncements().execute();
    }

    @Override
    public void onBackPressed() {
	super.onBackPressed();
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
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
	    Intent parentActivityIntent = new Intent(this, MainActivity.class);
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
	    overridePendingTransition(android.R.anim.fade_in,
		    android.R.anim.fade_out);
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Runs when you click on a list item
     */
    private OnItemClickListener announcementsClickListener = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
		final int position, long id) {
	    descriptionPopup = new AlertDialog.Builder(Announcements.this)
		    .create();
	    descriptionPopup.setTitle(Html.fromHtml(announcementsAdapter
		    .getItem(position).getTitle()));
	    descriptionPopup.setMessage(Html.fromHtml(announcementsAdapter
		    .getItem(position).toString()));
	    descriptionPopup.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface descriptionPopup,
				int which) {
			    descriptionPopup.dismiss();
			}
		    });
	    //This is an add to calendar button
	    descriptionPopup.setButton(DialogInterface.BUTTON_NEGATIVE,
		    "Add to calendar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			    Intent intent = new Intent(Intent.ACTION_INSERT);
			    intent.setType("vnd.android.cursor.item/event");
			    intent.putExtra(
				    Events.TITLE,
				    Html.fromHtml(
					    announcementsAdapter.getItem(
						    position).getTitle())
					    .toString());
			    intent.putExtra(
				    Events.DESCRIPTION,
				    Html.fromHtml(
					    announcementsAdapter.getItem(
						    position).getDescription())
					    .toString().trim());
			    intent.putExtra(
				    CalendarContract.EXTRA_EVENT_ALL_DAY, false);
			    intent.putExtra(
				    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				    announcementsAdapter.getItem(position)
					    .getExpirationDate().getTime());
			    startActivity(intent);
			}
		    });
	    descriptionPopup.show();
	    ((TextView) descriptionPopup.findViewById(android.R.id.message))
		    .setMovementMethod(LinkMovementMethod.getInstance());
	}
    };

    private class LoadAnnouncements extends AsyncTask<Void, Void, Void> {
	private final ProgressDialog dialog = new ProgressDialog(
		Announcements.this);

	@Override
	protected void onPreExecute() {
	    dialog.setCanceledOnTouchOutside(false);
	    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode,
			KeyEvent event) {
		    if (keyCode == KeyEvent.KEYCODE_BACK) {
			Announcements.this.finish();
			LoadAnnouncements.this.cancel(true);
			return true;
		    }
		    return false;
		}
	    });
	    if (isNetworkConnected())
		dialog.setMessage("Loading announcements");
	    else
		dialog.setMessage("Loading announcements from cache");
	    dialog.show();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
	    getAnnouncements();
	    runOnUiThread(new Runnable() {
		public void run() {
		    displayAnnouncements();
		}
	    });
	    return null;
	}

	@Override
	protected void onPostExecute(Void unused) {
	    if (dialog.isShowing())
		dialog.dismiss();
	}
    }

    private void displayAnnouncements() {
	announcementsList = (ListView) findViewById(R.id.announcementsList);
	noAnnouncementsView = (TextView) findViewById(R.id.noAnnouncementsView);
	announcementsList.setAdapter(announcementsAdapter);
	announcementsList.setOnItemClickListener(announcementsClickListener);
	if (announcementsList.getCount() == 0)
	    noAnnouncementsView.setVisibility(TextView.VISIBLE);
    }

    private void getAnnouncements() {
	announcementsAdapter.clear();
	try {
	    for (AnnouncementItem item : new FeedParser(this, URL, NAME,
		    isNetworkConnected()).loadAnnouncements())
		if ((item.getExpirationDate().compareTo(Calendar.getInstance()
			.getTime())) >= 0)
		    announcementsAdapter.add(item);
	} catch (IOException e) {
	    Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT)
		    .show();
	}
    }

    /**
     * Checks to see if the device is connected to the Internet.
     * 
     * @return {@code true} if device is connected to the Internet or
     *         {@code false} if it is not
     */
    private boolean isNetworkConnected() {
	return !(((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
		.getActiveNetworkInfo() == null);
    }
}
