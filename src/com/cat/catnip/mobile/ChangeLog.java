package com.cat.catnip.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

public class ChangeLog {

    private final Context context;
    private String lastVersion, thisVersion;
    private static final String VERSION_KEY = "PREFS_VERSION_KEY";

    /**
     * Constructor
     * 
     * Retrieves the version names and stores the new version name in
     * SharedPreferences
     * 
     * @param context
     */
    public ChangeLog(Context context) {
	this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    /**
     * Constructor
     * 
     * Retrieves the version names and stores the new version name in
     * SharedPreferences
     * 
     * @param context
     * @param sp
     *            the shared preferences to store the last version name into
     */
    public ChangeLog(Context context, SharedPreferences sp) {
	this.context = context;
	this.lastVersion = sp.getString(VERSION_KEY, "");
	Log.d(TAG, "lastVersion: " + lastVersion);
	try {
	    this.thisVersion = context.getPackageManager().getPackageInfo(
		    context.getPackageName(), 0).versionName;
	} catch (NameNotFoundException e) {
	    this.thisVersion = "?";
	    Log.e(TAG, "could not get version name from manifest!");
	    e.printStackTrace();
	}
	Log.d(TAG, "appVersion: " + this.thisVersion);
	SharedPreferences.Editor editor = sp.edit();
	editor.putString(VERSION_KEY, this.thisVersion);
	editor.commit();
    }

    /**
     * @return The version name of the last installation of this app (as
     *         described in the former manifest). This will be the same as
     *         returned by <code>getThisVersion()</code> the second time this
     *         version of the app is launched (more precisely: the second time
     *         ChangeLog is instantiated).
     * @see AndroidManifest.xml#android:versionName
     */

    public String getLastVersion() {
	return this.lastVersion;
    }

    /**
     * @return The version name of this app as described in the manifest.
     * @see AndroidManifest.xml#android:versionName
     */
    public String getThisVersion() {
	return this.thisVersion;
    }

    /**
     * @return <code>true</code> if this version of your app is started the
     *         first time
     */
    public boolean firstRun() {
	return !this.lastVersion.equals(this.thisVersion);
    }

    /**
     * @return <code>true</code> if your app is started the first time ever.
     *         Also <code>true</code> if your app was deinstalled and installed
     *         again.
     */
    public boolean firstRunEver() {
	return "".equals(this.lastVersion);
    }

    /**
     * @return an AlertDialog displaying the changes since the previous
     *         installed version of your app (what's new).
     */
    public AlertDialog getLogDialog() {
	return this.getDialog(false);
    }

    /**
     * @return an AlertDialog with a full change log displayed
     */
    public AlertDialog getFullLogDialog() {
	return this.getDialog(true);
    }

    private AlertDialog getDialog(boolean full) {
	AlertDialog.Builder  builder = new AlertDialog.Builder(this.context);
	builder.setTitle("What's new")
		.setMessage(Html.fromHtml(this.getFullLog()))
		.setCancelable(false)
		.setPositiveButton("Close",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int which) {
				dialog.cancel();
			    }
			});
	return builder.create();
    }

    /**
     * @return HTML displaying the changes since the previous installed version
     *         of your app (what's new)
     */
    public String getLog() {
	return this.getLog(false);
    }

    /**
     * @return HTML which displays full change log
     */
    public String getFullLog() {
	return this.getLog(true);
    }

    /** modes for HTML-Lists (bullet, numbered) */
    private enum Listmode {
	NONE, ORDERED, UNORDERED,
    };

    private Listmode listMode = Listmode.NONE;
    private StringBuilder sb = null;
    private static final String EOCL = "END_OF_CHANGE_LOG";

    private String getLog(boolean full) {
	sb = new StringBuilder();
	try {
	    InputStream ins = context.getResources().openRawResource(
		    R.raw.changelog);
	    BufferedReader br = new BufferedReader(new InputStreamReader(ins));

	    String line = null;
	    boolean advanceToEOVS = false;
	    while ((line = br.readLine()) != null) {
		line = line.trim();
		char marker = line.length() > 0 ? line.charAt(0) : 0;
		if (marker == '$') {
		    this.closeList();
		    String version = line.substring(1).trim();
		    if (!full) {
			if (this.lastVersion.equals(version)) {
			    advanceToEOVS = true;
			} else if (version.equals(EOCL)) {
			    advanceToEOVS = false;
			}
		    }
		} else if (!advanceToEOVS) {
		    switch (marker) {
		    case '%':
			sb.append("<br><b>" + line.substring(1).trim() + "</b>");
			break;
		    case '_':
			sb.append(" (<b>" + line.substring(1).trim()
				+ "</b>)<br>");
			break;
		    case '*':
			sb.append("&bull; " + line.substring(1).trim() + "<br>");
			break;
		    default:
			sb.append(line + "<br>");
		    }
		}
	    }
	    this.closeList();
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return sb.toString();
    }

    private void closeList() {
	if (this.listMode == Listmode.ORDERED) {
	    sb.append("</ol></div>\n");
	} else if (this.listMode == Listmode.UNORDERED) {
	    sb.append("</ul></div>\n");
	}
	this.listMode = Listmode.NONE;
    }

    private static final String TAG = "ChangeLog";

    /**
     * manually set the last version name - for testing purposes only
     * 
     * @param lastVersion
     */
    void setLastVersion(String lastVersion) {
	this.lastVersion = lastVersion;
    }
}