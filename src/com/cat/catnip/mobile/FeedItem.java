package com.cat.catnip.mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedItem implements Comparable<FeedItem> {

    private final SimpleDateFormat FORMATTER = new SimpleDateFormat(
	    "MM/dd/yyyy hh:mm a", Locale.US);
    private Date endTime;
    private Date pubDate;
    private Date startTime;
    private String attachments;
    private String author;
    private String course;
    private String description;
    private String title;
    private boolean isSeparator;

    /**
     * Creates an empty FeedItem and sets it to not be a separator.
     */
    public FeedItem() {
	this.isSeparator = false;
    }

    /**
     * Creates a new FeedItem based on the title passed to it.
     * 
     * @param title
     *            the FeedItem's title
     * @param isSeparator
     *            is this FeedItem set to be a separator?
     */
    public FeedItem(String title, boolean isSeparator) {
	this.title = title;
	this.author = title;
	this.isSeparator = isSeparator;
    }

    /**
     * @return whether or not this FeedItem is a separator
     */
    public boolean isSeparator() {
	return this.isSeparator;
    }

    /**
     * Gets this FeedItem's author.
     * 
     * @return this FeedItem's author
     */
    public String getAuthor() {
	return this.author;
    }

    /**
     * Sets the author of this FeedItem.
     * 
     * @param author
     * @return this FeedItem
     */
    public FeedItem setAuthor(String author) {
	this.author = author.trim();
	return this;
    }

    /**
     * Gets this FeedItem's description.
     * 
     * @return this FeedItem's description
     */
    public String getDescription() {
	return this.description;
    }

    /**
     * Sets the desctription of this FeedItem.
     * 
     * @param description
     *            this FeedItem's description
     * @return this FeedItem
     */
    public FeedItem setDescription(String description) {
	this.description = description.trim();
	return this;
    }

    /**
     * Gets this FeedItem's end time as a string.
     * 
     * @return this FeedItem's end time
     */
    public String getEndTime() {
	return FORMATTER.format(this.endTime);
    }

    /**
     * Gets this FeedItem's end time as a {@link Date}.
     * 
     * @return this FeedItem's end time
     * @see Date
     */
    public Date getEndDate() {
	return this.endTime;
    }

    /**
     * Sets this FeedItem's end time.
     * 
     * @param endTime
     *            this FeedItem's end time
     * @return this FeedItem
     */
    public FeedItem setEndTime(String endTime) {
	try {
	    this.endTime = FORMATTER.parse(endTime.trim());
	} catch (ParseException e) {
	    throw new RuntimeException(e);
	}
	return this;
    }

    /**
     * Gets the published date of this FeedItem.
     * 
     * @return the published date of this FeedItem
     */
    public String getPubDate() {
	return FORMATTER.format(this.pubDate);
    }

    /**
     * Sets the published date of this FeedItem.
     * 
     * @param pubDate
     *            this FeedItem's published date
     * @return this FeedItem
     */
    public FeedItem setPubDate(String pubDate) {
	try {
	    this.pubDate = FORMATTER.parse(pubDate.trim());
	} catch (ParseException e) {
	    throw new RuntimeException(e);
	}
	return this;
    }

    /**
     * Gets this FeedItem's start time.
     * 
     * @return this FeedItem's start time
     */
    public String getStartTime() {
	return FORMATTER.format(this.startTime);
    }

    /**
     * Sets this FeedItem's start time.
     * 
     * @param startTime
     *            this FeedItem's start time
     * @return this FeedItem
     */
    public FeedItem setStartTime(String startTime) {
	try {
	    this.startTime = FORMATTER.parse(startTime.trim());
	} catch (ParseException e) {
	    throw new RuntimeException(e);
	}
	return this;
    }

    /**
     * Gets this FeedItem's title.
     * 
     * @return this FeedItem's title
     */
    public String getTitle() {
	return this.title;
    }

    /**
     * Sets this FeedItem's title
     * 
     * @param title
     *            this FeedItem's title
     * @return this FeedItem
     */
    public FeedItem setTitle(String title) {
	this.title = title.trim();
	return this;
    }

    /**
     * Gets this FeedItem's attachments.
     * 
     * @return this FeedItem's attachments
     */
    public String getAttachments() {
	return this.attachments;
    }

    /**
     * Sets this FeedItem's attachments.
     * 
     * @param attachments
     *            this FeedItem's attachments
     * @return this FeedItem
     */
    public FeedItem setAttachments(String attachments) {
	this.attachments = attachments.replaceAll("<a href=\"\"></a>", "");
	return this;
    }

    /**
     * Gets this FeedItem's course.
     * 
     * @return this FeedItem's course
     */
    public String getCourse() {
	return this.course;
    }

    /**
     * Sets this FeedItem's course.
     * 
     * @param course
     *            this FeedItem's course
     * @return this FeedItem
     */
    public FeedItem setCourse(String course) {
	this.course = course;
	return this;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("<b>Author:</b> ").append(this.author).append("<br><br>")
		.append("<b>Start Time:</b> ")
		.append(FORMATTER.format(this.startTime)).append("<br><br>")
		.append("<b>End Time:</b> ")
		.append(FORMATTER.format(this.endTime)).append("<br><br>")
		.append("<b>Description:</b> ").append(this.description)
		.append("<b>Course:</b> ").append(this.course)
		.append("<br><br>").append("<b>Attachments:</b> ")
		.append(this.attachments);
	return sb.toString();
    }

    @Override
    public int compareTo(FeedItem item) {
	return endTime.compareTo(item.endTime);
    }
}