package com.cat.catnip.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * This class will display the about CAT text.
 * 
 * @author Michael Atanasio
 * 
 */
public class AboutCat extends Activity {
    private static TextView aboutCat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	if (Build.VERSION.SDK_INT >= 11) {
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setTitle("About CAT");
	}
	setContentView(R.layout.activity_about);
	aboutCat = (TextView) findViewById(R.id.aboutText);
	//Below is the about CAT text. It uses HTML tags to get some awesome formatting. Html.fromHtml parses the html tags. <b></b> makes things bold.
	aboutCat.setText(Html
		.fromHtml("<b>Mission Statement:</b><br><br>"
			+ "To provide quality application-based learning opportunities in a "
			+ "state-of-the-art supportive environment integrating mathematics, "
			+ "science and technology through student research.<br><br>"
			+ "<b>Overview:</b><br><br>"
			+ "The Center for Advanced Technologies is a public school magnet program housed at Lakewood Senior High School, "
			+ "located in the southernmost part of Pinellas County, on the west coast of Florida. "
			+ "Curricular offerings within the Center include mathematics, science, computer education, "
			+ "multimedia applications and research; other coursework is completed within Lakewood Senior High School. "
			+ "The Center opened its doors in August, 1990 to a class of eighty-five freshmen; "
			+ "each year an additional class of approximately one hundred fifty students is added. "
			+ "The enrollment for the 2009-2010 school year is around four-hundred fifty, grades nine through twelve, "
			+ "with one hundred percent of students planning to attend college. "
			+ "Funding for the program comes from "
			+ "Pinellas County Public Schools, local corporations, and partnerships. "
			+ "Last year, Newsweek magazine ranked the CAT Program 16th in the nation! Each year, "
			+ "the CAT program is striving to be better than ever, "
			+ "and is constantly growing and expanding thanks to dedicated faculty, parents, and students.<br><br>"
			+ "<b>Address:</b><br><br>"
			+ "1400 54th Ave. S. <br>Saint Petersburg, FL 33705<br><br>"
			+ "<b>Contact Information:</b><br><br>"
			+ "Main Contact Numbers <br>Voice: (727) 893-2926 <br>Fax: (727) 893-2613 <br><br>"
			+ "Program Director: <br>Peter Oberg <br>(727) 893-2926<br>extension: 2118 <br><br>"
			+ "Program Guidance Counselor: <br>Cheri Ashwood <br>(727) 893-2926<br>extension: 2117"));
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
}