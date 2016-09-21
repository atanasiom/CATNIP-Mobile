package com.cat.catnip.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Michael Atanasio
 * 
 */
public class MainActivity extends Activity {

    /**
     * URL of the bell schedule
     */
    private final String URL = "http://www.lakewood-hs.pinellas.k12.fl.us/bellschedule";
    /**
     * The Button that takes you to the announcements
     */
    private Button announcementsButton;
    /**
     * The Button that takes you to the teacher selection page
     */
    private Button teachersButton;
    /**
     * The button that takes you to the email faculty page
     */
    private Button emailButton;
    /**
     * The Button that takes you to the about CAT page
     */
    private Button aboutCatButton;
    /**
     * Dialog that shows up if you have no Internet
     */
    private AlertDialog networkDialog;
    /**
     * The Changelog that appears when the app is run after an update
     */
    private ChangeLog cl;
    /**
     * The TextView that displays the bell schedule
     */
    private TextView scheduleTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	setContentView(R.layout.schedule);
	announcementsButton = (Button) findViewById(R.id.announcementsButton);
	teachersButton = (Button) findViewById(R.id.teachersButton);
	emailButton = (Button) findViewById(R.id.emailButton);
	aboutCatButton = (Button) findViewById(R.id.aboutcatButton);
	final int WIDTH = getWindowManager().getDefaultDisplay().getWidth() / 4;// Gets the width of the phone and divides it by four
	announcementsButton.setMinimumWidth(WIDTH);
	emailButton.setMinimumWidth(WIDTH);
	teachersButton.setMinimumWidth(WIDTH);
	aboutCatButton.setMinimumWidth(WIDTH);
	cl = new ChangeLog(this);// Creates a new Changelog
	new LoadSchedule().execute();// Starts the 
	if (cl.firstRun())
	    cl.getLogDialog().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.home, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.refresh:
	    new LoadSchedule().execute();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public void onBackPressed() {
	super.onBackPressed();
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);// Overrides the default animation to a fade
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

    /**
     * Class to load the bell schedule asynchronously.
     * 
     * @author Michael Atanasio
     * 
     */
    private class LoadSchedule extends AsyncTask<Void, Void, Void> {

	/**
	 * The dialog that shows up when you either refresh or start the
	 * application
	 */
	private final ProgressDialog dialog = new ProgressDialog(
		MainActivity.this);

	@Override
	protected void onPreExecute() {
	    if (!isNetworkConnected()) {
		networkDialog = new AlertDialog.Builder(MainActivity.this)
			.create();// Creates a new AlertDialog
		networkDialog.setTitle("Network error");// Sets the dailog's title
		networkDialog.setMessage("No active connections found");// Sets the dialog's message
		networkDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",// Gives the dialog a "yes" button and sets a listener for the button
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface networkDialog,
				    int which) {
				networkDialog.dismiss();// Dismisses the dialog
			    }
			});
		networkDialog.show();// Shows the dialog
		runOnUiThread(new Runnable() {// Starts a UI thread
		    public void run() {
			scheduleTextView = (TextView) findViewById(R.id.scheduleTextView);
			scheduleTextView.setText("No active connections found");// Sets the text in the TextView
		    }
		});
	    } else {
		dialog.setMessage("Loading bell schedule");// Sets the dialog's message text
		dialog.show();// Shows the dialog
	    }
	}

	@Override
	protected Void doInBackground(Void... unused) {
	    try {
		final String data = getData(URL).replaceAll("<p>", "")
			.replaceAll("</p>", "\n\n");
		runOnUiThread(new Runnable() {// Starts a UI thread
		    public void run() {
			scheduleTextView = (TextView) findViewById(R.id.scheduleTextView);
			scheduleTextView.setText(Html.fromHtml(data));// Parses HTML code to properly display bellschedule
		    }
		});
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return null;
	}

	@Override
	protected void onPostExecute(Void unused) {
	    if (dialog.isShowing())// Checks if the dialog is still showing
		dialog.dismiss();// Dismisses the dialog if it is still showing
	}
    }

    /**
     * Starts the teacher activity.
     * 
     * @param view
     */
    public void showTeachers(View view) {
	Intent intent = new Intent(this, Teachers.class);
	startActivity(intent);// Starts the teacher activity.
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);// Overrides the default animation to a fade
    }

    /**
     * Starts the email activity.
     * 
     * @param view
     */
    public void emailTeachers(View view) {
	Intent intent = new Intent(this, Email.class);
	startActivity(intent);// Starts the email activity
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);// Overrides the default animation to a fade
    }

    /**
     * Starts the about CAT activity.
     * 
     * @param view
     */
    public void showAbout(View view) {
	Intent intent = new Intent(this, AboutCat.class);
	startActivity(intent);// Starts the about CAT activity.
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);// Overrides the default animation to a fade
    }

    /**
     * Starts the Announcements activity. Checks for an internet connection, and
     * if one exists, it opens the activity.
     * 
     * @param view
     */
    public void showAnnouncements(View view) {
	if (!isNetworkConnected()) {
	    networkDialog = new AlertDialog.Builder(this).create();
	    networkDialog.setTitle("Network error");
	    networkDialog
		    .setMessage("No active connections found. Load announcements from cache?");
	    networkDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface networkDialog,
				int which) {
			    Intent intent = new Intent(MainActivity.this,
				    Announcements.class);
			    startActivity(intent);
			    overridePendingTransition(android.R.anim.fade_in,
				    android.R.anim.fade_out);
			}
		    });
	    networkDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface networkDialog,
				int which) {
			    networkDialog.dismiss();
			}
		    });
	    networkDialog.show();
	} else {
	    Intent intent = new Intent(this, Announcements.class);
	    startActivity(intent);// Starts the announcements activity.
	    overridePendingTransition(android.R.anim.fade_in,
		    android.R.anim.fade_out);// Overrides the default animation to a fade
	}
    }

    /**
     * Given a URL, returns a String representation of that data presented on
     * the website.
     * 
     * @param myUrl
     *            the URL that you need to get data from
     * @return a string representation of the data on the website
     * @throws IOException
     *             if an I/O error occurs
     */
    private String getData(String myUrl) throws IOException {
	InputStream is = null;
	try {
	    URL url = new URL(myUrl);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setDoInput(true);
	    conn.connect();// Connects to the URL
	    is = conn.getInputStream();// Gets the InputStream
	    Reader reader = null;// Declares a Reader
	    reader = new InputStreamReader(is, "UTF-8");// Creates the Reader as an InputStreamReader with UTF-8 encoding
	    int data = reader.read();
	    StringBuilder sb = new StringBuilder();// Creates a new StringBuilder
	    while (data != -1) {
		sb.append((char) data);
		data = reader.read();
	    }
	    return sb.toString();
	} finally {
	    if (is != null) {
		is.close();
	    }
	}
    }
}