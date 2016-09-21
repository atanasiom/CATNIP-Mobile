package com.cat.catnip.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Teachers extends Activity {

    final static String SELECTED_TEACHERS = "com.cat.catnip.mobile.SELECTED_TEACHERS";
    private final String SELECTED_TEACHERS_FILE = "SelectedTeachersFile";
    private boolean[] selectedTeachers = new boolean[13];
    private final String[] NAMES = { "alcott", "borg", "dickman", "frewin",
	    "garbutt", "lake", "leadbetter", "ness", "Pacowta", "penkethman",
	    "schneider", "tencza", "zavadil" };
    private CheckBox[] boxes = new CheckBox[13];
    private AlertDialog teacherDialog;
    private AlertDialog networkDialog;
    private Button assignmentsButton;
    private Button scheduleButton;
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.teachers);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	if (Build.VERSION.SDK_INT >= 11) {
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setTitle("Select teachers");
	}
	assignmentsButton = (Button) findViewById(R.id.assignmentsButton);
	scheduleButton = (Button) findViewById(R.id.scheduleButton);
	boxes[0] = (CheckBox) findViewById(R.id.assignmentBox1);
	boxes[1] = (CheckBox) findViewById(R.id.assignmentBox2);
	boxes[2] = (CheckBox) findViewById(R.id.assignmentBox3);
	boxes[3] = (CheckBox) findViewById(R.id.assignmentBox4);
	boxes[4] = (CheckBox) findViewById(R.id.assignmentBox5);
	boxes[5] = (CheckBox) findViewById(R.id.assignmentBox6);
	boxes[6] = (CheckBox) findViewById(R.id.assignmentBox7);
	boxes[7] = (CheckBox) findViewById(R.id.assignmentBox8);
	boxes[8] = (CheckBox) findViewById(R.id.assignmentBox9);
	boxes[9] = (CheckBox) findViewById(R.id.assignmentBox10);
	boxes[10] = (CheckBox) findViewById(R.id.assignmentBox11);
	boxes[11] = (CheckBox) findViewById(R.id.assignmentBox12);
	boxes[12] = (CheckBox) findViewById(R.id.assignmentBox13);
	final int WIDTH = getWindowManager().getDefaultDisplay().getWidth() / 2;
	final int HEIGHT = (getWindowManager().getDefaultDisplay().getHeight() - assignmentsButton
		.getHeight()) / 9;
	assignmentsButton.setWidth(WIDTH);
	scheduleButton.setWidth(WIDTH);
	sp = getSharedPreferences(SELECTED_TEACHERS_FILE, Context.MODE_PRIVATE);
	spe = sp.edit();
	for (int i = 0; i < selectedTeachers.length; i++) {
	    boxes[i].setWidth(WIDTH);
	    boxes[i].setHeight(HEIGHT);
	    boxes[i].setChecked(sp.getBoolean(NAMES[i], false));
	}

    }

    @Override
    public void onBackPressed() {
	super.onBackPressed();
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
    }

    /**
     * Starts the Schedule activity
     * 
     * @param view
     */
    public void showSchedule(View view) {
	Intent intent = new Intent(this, MainActivity.class);
	startActivity(intent);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	finish();
	saveSelected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.teachers, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    saveSelected();
	    Intent parentActivityIntent = new Intent(this, MainActivity.class);
	    parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		    | Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(parentActivityIntent);
	    overridePendingTransition(android.R.anim.fade_in,
		    android.R.anim.fade_out);
	    finish();
	case R.id.select_all:
	    for (CheckBox cb : boxes)
		cb.setChecked(true);
	    return true;
	case R.id.deselect:
	    for (CheckBox cb : boxes)
		cb.setChecked(false);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /**
     * Shows the assignments if there is a network connection
     * 
     * @param view
     */
    public void showAssignments(View view) {
	if (isNetworkConnected()) {
	    saveSelected();
	    if (!checkSelected()) {
		//Creates a dialog that tells you that there are no teachers selected
		teacherDialog = new AlertDialog.Builder(this).create();
		teacherDialog.setTitle("No teachers selected");
		teacherDialog
			.setMessage("Please select at least one teacher to view assignments");
		teacherDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface teacherDialog,
				    int which) {
				teacherDialog.dismiss();
			    }
			});
		teacherDialog.show();
	    } else {
		//Starts the assignment activity
		Intent intent = new Intent(this, AssignmentList.class);
		intent.putExtra(SELECTED_TEACHERS, selectedTeachers);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
			android.R.anim.fade_out);
	    }
	} else {
	    //Creates a dialog that tells you that there is no network connection then prompts to load assignments from the cache
	    networkDialog = new AlertDialog.Builder(this).create();
	    networkDialog.setTitle("Network error");
	    networkDialog
		    .setMessage("No active connections found. Load assignments from cache? Some assignments may be outdated");
	    networkDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface networkDialog,
				int which) {
			    saveSelected();
			    Intent intent = new Intent(Teachers.this,
				    AssignmentList.class);
			    intent.putExtra(SELECTED_TEACHERS, selectedTeachers);
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

    /**
     * Saves the selected teachers into a preferences file
     */
    private void saveSelected() {
	//Loops through the checkboxes and writes true or false to 
	//a preferences file based on whether the box was checked or not
	for (int i = 0; i < selectedTeachers.length; i++)
	    spe.putBoolean(NAMES[i], selectedTeachers[i] = boxes[i].isChecked());
	spe.commit();
    }

    /**
     * Checks to make sure at least one teacher is selected
     * 
     * @return {@code true} if at least one teacher is selected or {@code false}
     *         if one is not
     */
    private boolean checkSelected() {
	for (boolean b : selectedTeachers)
	    if (b)
		return true;
	return false;
    }
}