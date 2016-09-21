package com.cat.catnip.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * The email class. It creates a list of all the CAT faculty allows one to email
 * them.
 * 
 * @author Michael Atanasio
 * 
 */
public class Email extends Activity {
    /**
     * List of faculty names.
     */
    private final String NAMES[] = { "Ms. Lisa Alcott", "Ms. Cheri Ashwood",
	    "Mr. Chris Borg", "Mr. Paul Dickman", "Mr. Ryan Frewin",
	    "Dr. George Garbutt", "Mrs. Laura Lake", "Ms. Debbie Leadbetter",
	    "Mr. Jason Ness", "Ms. Jessica Pacaowta", "Mr. Peter Oberg",
	    "Mr. Thomas Penkethman", "Mr. C David Schneider",
	    "Mr. Robert Tencza", "Ms. Kathy Zavadil", "Mr. Lou Zulli Jr." };
    /**
     * List of faculty email addresses.
     */
    private final String ADDRESS[] = { "alcottl@cat.pcsb.org",
	    "ashwoodc@cat.pcsb.org", "borgc@cat.pcsb.org",
	    "dickman@cat.pcsb.org", "frewinr@cat.pcsb.org",
	    "garbuttg@cat.pcsb.org", "lakel@cat.pcsb.org",
	    "leadbetterd@cat.pcsb.org", "nessj@cat.pcsb.org",
	    "obergp@cat.pcsb.org", "pacowtaj@cat.pcsb.org",
	    "penkethmant@cat.pcsb.org", "schneiderch@cat.pcsb.org",
	    "tenczar@cat.pcsb.org", "zavadilk@cat.pcsb.org",
	    "zullil@cat.pcsb.org" };
    private ListView list;
    private EmailListAdapter adapter;
    private AlertDialog popup;
    private int pos;
    private Vibrator v;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
	super.onCreate(SavedInstanceState);
	setContentView(R.layout.activity_email);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	if (Build.VERSION.SDK_INT >= 11) {
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setTitle("Email faculty");
	}
	list = (ListView) findViewById(R.id.email_list);
	adapter = new EmailListAdapter(this, R.layout.email_row);
	for (String s : NAMES)
	    adapter.add(s);
	list.setAdapter(adapter);
	list.setOnItemClickListener(listener);
	list.setOnItemLongClickListener(longListener);
	v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onStart() {
	super.onStart();
	if (Build.VERSION.SDK_INT >= 11)
	    getActionBar().setTitle("Email Faculty");
    }

    @Override
    public void onResume() {
	super.onResume();
	if (Build.VERSION.SDK_INT >= 11) {
	    getActionBar().setTitle("Email Faculty");
	}
    }

    @Override
    public void onBackPressed() {
	super.onBackPressed();
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
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
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    private OnItemClickListener listener = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {
	    Intent i = new Intent(Intent.ACTION_SEND);
	    i.setType("message/rfc822");
	    System.out.println(ADDRESS[position]);
	    i.putExtra(Intent.EXTRA_EMAIL, new String[] { ADDRESS[position] });
	    i.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n"
		    + "Sent using CATNIP Mobile");
	    try {
		startActivity(Intent.createChooser(i, "Send mail..."));
	    } catch (android.content.ActivityNotFoundException ex) {
		Toast.makeText(Email.this,
			"There are no email clients installed.",
			Toast.LENGTH_SHORT).show();
	    }
	}
    };

    private OnItemLongClickListener longListener = new OnItemLongClickListener() {

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
		int position, long id) {
	    v.vibrate(35);
	    popup = new AlertDialog.Builder(Email.this).create();
	    popup.setTitle("Choose action");
	    LinearLayout ll = new LinearLayout(Email.this);
	    ListView options = new ListView(Email.this);
	    EmailListAdapter optionsA = new EmailListAdapter(Email.this,
		    R.layout.option_row);
	    optionsA.add("Email " + list.getItemAtPosition(position));
	    optionsA.add("Add to contacts");
	    optionsA.add("Copy to clipboard");
	    options.setAdapter(optionsA);
	    options.setOnItemClickListener(optionsListener);
	    ll.setOrientation(LinearLayout.VERTICAL);
	    ll.addView(options);
	    popup.setView(ll);
	    popup.show();
	    pos = position;
	    return false;
	}
    };

    private OnItemClickListener optionsListener = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {
	    switch (position) {
	    case 0:
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL,
			new String[] { ADDRESS[position] });
		i.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n"
			+ "Sent using CATNIP Mobile");
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(Email.this,
			    "There are no email clients installed.",
			    Toast.LENGTH_SHORT).show();
		}
		break;
	    case 1:
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
		intent.putExtra(ContactsContract.Intents.Insert.NAME,
			NAMES[pos]);
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL,
			ADDRESS[pos]);
		startActivity(intent);
		break;
	    case 2:
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
		    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		    clipboard.setText(ADDRESS[pos]);
		} else {
		    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		    android.content.ClipData clip = android.content.ClipData
			    .newPlainText("Email address", ADDRESS[pos]);
		    clipboard.setPrimaryClip(clip);
		}
		popup.dismiss();
		Toast.makeText(Email.this, "Address copied to clipboard",
			Toast.LENGTH_SHORT).show();
		break;
	    }
	}
    };
}
