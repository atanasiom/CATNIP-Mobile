package com.cat.catnip.mobile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.Html;

public class FeedParser {

    private final String START_AUTHOR = "<author>";
    private final String START_ATTACHMENTS = "<div><b>Attachments:</b>";
    private final String START_BODY = "<div><b>Body:</b>";
    private final String START_COURSE = "<div><b>Course:</b>";
    private final String START_DESCRIPTION = "<div><b>Description:</b>";
    private final String START_ENDTIME = "<div><b>End Time:</b>";
    private final String START_EXPIRES = "<div><b>Expires:</b>";
    private final String START_ITEM = "<item>";
    private final String START_STARTTIME = "Start Time:</b>";
    private final String START_TITLE = "<title>";
    private final String END_AUTHOR = "</author>";
    private final String END_DESCRIPTION = "</description>";
    private final String END_DIV = "</div>";
    private final String END_TITLE = "</title>";
    private boolean isNetworkConnected;
    private File file;
    private Context context;
    private String URL;
    private OutputStream out = null;
    private InputStream in = null;

    public FeedParser(String URL) {
	this.URL = URL;
    }

    public FeedParser(Context context, String URL, String name,
	    boolean isNetworkConnected) {
	this.context = context;
	this.URL = URL;
	ContextWrapper cw = new ContextWrapper(this.context);
	this.file = cw.getDir(name + ".cache", Context.MODE_PRIVATE);
	this.isNetworkConnected = isNetworkConnected;
    }

    public ArrayList<AnnouncementItem> loadAnnouncements() throws IOException {
	ArrayList<AnnouncementItem> announcementData = new ArrayList<AnnouncementItem>();
	try {
	    if (isNetworkConnected) {
		if (file.exists()) {
		    file.delete();
		    file.createNewFile();
		    file.mkdir();
		}
		out = new BufferedOutputStream(new FileOutputStream(file));
		String xmlData = getData(URL);
		out.write(xmlData.getBytes());
		while (xmlData.contains(START_ITEM)) {
		    AnnouncementItem item = new AnnouncementItem();
		    xmlData = xmlData.substring(xmlData.indexOf(START_ITEM)
			    + START_ITEM.length());
		    item.setTitle(getTitle(xmlData));
		    item.setDescription(getBody(xmlData));
		    item.setExpires(getExpires(xmlData));
		    announcementData.add(item);
		}
	    } else {
		in = new BufferedInputStream(new FileInputStream(file));
		int data = in.read();
		StringBuilder sb = new StringBuilder();
		while (data != -1) {
		    sb.append((char) data);
		    data = in.read();
		}
		String xmlData = sb.toString();
		while (xmlData.contains(START_ITEM)) {
		    AnnouncementItem item = new AnnouncementItem();
		    xmlData = xmlData.substring(xmlData.indexOf(START_ITEM)
			    + START_ITEM.length());
		    item.setTitle(getTitle(xmlData));
		    item.setDescription(getBody(xmlData));
		    item.setExpires(getExpires(xmlData));
		    announcementData.add(item);
		}
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (out != null)
		out.close();
	    if (in != null)
		in.close();
	}
	Collections.sort(announcementData);
	return announcementData;
    }

    public ArrayList<FeedItem> loadAssignments() throws IOException {
	ArrayList<FeedItem> itemData = new ArrayList<FeedItem>();
	try {
	    if (isNetworkConnected) {
		if (file.exists()) {
		    file.delete();
		    file.createNewFile();
		    file.mkdir();
		}
		out = new BufferedOutputStream(new FileOutputStream(file));
		String xmlData = getData(URL);
		out.write(xmlData.getBytes());
		while (xmlData.contains(START_ITEM)) {
		    FeedItem item = new FeedItem();
		    xmlData = xmlData.substring(xmlData.indexOf(START_ITEM)
			    + START_ITEM.length());
		    item.setTitle(getTitle(xmlData));
		    item.setStartTime(getStartTime(xmlData));
		    item.setEndTime(getEndTime(xmlData));
		    item.setAuthor(getAuthor(xmlData));
		    item.setDescription(getDescription(xmlData));
		    item.setCourse(getCourse(xmlData));
		    item.setAttachments(getAttachments(xmlData));
		    itemData.add(item);
		}
	    } else {
		in = new BufferedInputStream(new FileInputStream(file));
		int data = in.read();
		StringBuilder sb = new StringBuilder();
		while (data != -1) {
		    sb.append((char) data);
		    data = in.read();
		}
		String xmlData = sb.toString();
		while (xmlData.contains(START_ITEM)) {
		    FeedItem item = new FeedItem();
		    xmlData = xmlData.substring(xmlData.indexOf(START_ITEM)
			    + START_ITEM.length());
		    item.setTitle(getTitle(xmlData));
		    item.setStartTime(getStartTime(xmlData));
		    item.setEndTime(getEndTime(xmlData));
		    item.setAuthor(getAuthor(xmlData));
		    item.setDescription(getDescription(xmlData));
		    item.setCourse(getCourse(xmlData));
		    item.setAttachments(getAttachments(xmlData));
		    itemData.add(item);
		}
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (out != null)
		out.close();
	    if (in != null)
		in.close();
	}
	Collections.sort(itemData);
	return itemData;
    }

    private String getAuthor(String string) {
	return changeNames(string.substring(
		string.indexOf(START_AUTHOR) + START_AUTHOR.length(),
		string.indexOf(END_AUTHOR)).trim());
    }

    private String getBody(String string) {
	return string.substring(
		string.indexOf(START_BODY) + START_BODY.length(),
		string.indexOf(END_DIV,
			string.indexOf(START_BODY) + START_BODY.length()))
		.trim();

    }

    private String getStartTime(String string) {
	return string.substring(
		string.indexOf(START_STARTTIME) + START_STARTTIME.length(),
		string.indexOf(END_DIV, string.indexOf(START_STARTTIME)
			+ START_STARTTIME.length())).trim();
    }

    private String getEndTime(String string) {
	return string.substring(
		string.indexOf(START_ENDTIME) + START_ENDTIME.length(),
		string.indexOf(END_DIV, string.indexOf(START_ENDTIME)
			+ START_ENDTIME.length())).trim();
    }

    private String getExpires(String string) {
	return string.substring(
		string.indexOf(START_EXPIRES) + START_EXPIRES.length(),
		string.indexOf(END_DIV, string.indexOf(START_EXPIRES)
			+ START_EXPIRES.length())).trim();
    }

    private String getCourse(String string) {
	return string.contains(START_COURSE) ? string.substring(
		string.indexOf(START_COURSE) + START_COURSE.length(),
		string.indexOf(END_DIV, string.indexOf(START_COURSE)
			+ START_COURSE.length())).trim() : "No course";
    }

    private String getAttachments(String string) {
	return string.contains(START_ATTACHMENTS) ? string.substring(
		string.indexOf(START_ATTACHMENTS) + START_ATTACHMENTS.length(),
		string.indexOf(END_DIV, string.indexOf(START_ATTACHMENTS)
			+ START_ATTACHMENTS.length())).trim()
		: "No attachments";
    }

    private String getDescription(String string) {
	if (string.contains(START_DESCRIPTION)) {
	    string = string
		    .substring(
			    string.indexOf(START_DESCRIPTION)
				    + START_DESCRIPTION.length(),
			    string.indexOf(END_DESCRIPTION,
				    string.indexOf(START_DESCRIPTION)
					    + START_DESCRIPTION.length()))
		    .replaceAll("<b>Course:.*?</div>", "")
		    .replaceAll("<b>Attachments:.*?</div>", "")
		    .replaceAll("]]>", "").trim();
	    return Html.fromHtml(string).toString().equals("") ? "<br><br>No description<br><br>"
		    : string;
	}
	return "<br><br>No description<br><br>";
    }

    private String getTitle(String string) {
	return string.substring(
		string.indexOf(START_TITLE) + START_TITLE.length(),
		string.indexOf(END_TITLE)).trim();
    }

    private String changeNames(String string) {
	return string.equalsIgnoreCase("Lisa Alcott") ? "Ms. Alcott"
		: string.equalsIgnoreCase("Chris Borg") ? "Mr. Borg"
			: string.equalsIgnoreCase("Paul Dickman") ? "Mr. Dickman"
				: string.equalsIgnoreCase("Ryan P. Frewin") ? "Mr. Frewin"
					: string.equalsIgnoreCase("George J. Garbutt") ? "Dr. Garbutt"
						: string.equalsIgnoreCase("Laura Lake") ? "Mrs. Lake"
							: string.equalsIgnoreCase("Jason Ness") ? "Mr. Ness"
								: string.equalsIgnoreCase("Jennifer Pacowta") ? "Mrs. pacowta"
									: string.equalsIgnoreCase("Thomas E. Penkethman") ? "Mr. Penkethman"
										: string.equalsIgnoreCase("C David Schneider") ? "Mr. Schneider"
											: string.equalsIgnoreCase("Robert Tencza") ? "Mr. Tencza"
												: string.equalsIgnoreCase("Kathy Zavadil") ? "Ms. Zavadil"
													: string;
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
	    conn.connect();
	    is = conn.getInputStream();
	    Reader reader = null;
	    reader = new InputStreamReader(is, "UTF-8");
	    int data = reader.read();
	    StringBuilder sb = new StringBuilder();
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
