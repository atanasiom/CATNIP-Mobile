package com.cat.catnip.mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AnnouncementItem implements Comparable<AnnouncementItem> {
    /**
     * Used to format the dates brought in by the parser
     */
    private final SimpleDateFormat FORMATTER = new SimpleDateFormat(
	    "MM/dd/yyyy", Locale.US);// Used to foramt a date as MM/dd/yyyy
    /**
     * The description of the AnnouncementItem
     */
    private String description;
    /**
     * The date on which the AnnouncementItem expires
     */
    private Date expires;
    /**
     * The title of the AnnouncementItem
     */
    private String title;

    /**
     * Gets this AnnouncementItem's description.
     * 
     * @return this AnnouncementItem's description
     */
    public String getDescription() {
	return this.description;
    }

    /**
     * Sets this AnnouncementItem's description.
     * 
     * @param description
     *            this AnnouncementItem's description
     * @return this AnnouncementItem
     */
    public AnnouncementItem setDescription(String description) {
	this.description = description.trim();
	return this;
    }

    /**
     * Gets this AnnouncementItem's expiration date.
     * 
     * @return this AnnouncementItem's expiration date
     */
    public String getExpires() {
	return FORMATTER.format(this.expires);
    }

    /**
     * Gets this AnnouncementItem's expiration date as a {@link Date}.
     * 
     * @return this AnnouncementItem's expiration date
     * @see Date
     */
    public Date getExpirationDate() {
	return this.expires;
    }

    /**
     * Sets this AnnouncementItem's expiration date.
     * 
     * @param expires
     *            this AnnouncementItem's expiration date
     * @return this AnnouncementItem
     */
    public AnnouncementItem setExpires(String expires) {
	try {
	    this.expires = FORMATTER.parse(expires.trim());
	} catch (ParseException e) {
	    throw new RuntimeException(e);
	}
	return this;
    }

    /**
     * Gets this AnnouncementItem's title.
     * 
     * @return this AnnouncementItem's title
     */
    public String getTitle() {
	return this.title;
    }

    /**
     * Sets this AnnouncementItem's title.
     * 
     * @param title
     *            this AnnouncementItem 's title
     * @return this AnnouncementItem
     */
    public AnnouncementItem setTitle(String title) {
	this.title = title.trim();
	return this;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("<b>Expires: </b>").append(FORMATTER.format(this.expires))
		.append("<br><br>").append("<b>Description:</b> ")
		.append(this.description);
	return sb.toString();
    }

    @Override
    public int compareTo(AnnouncementItem item) {
	return this.expires.compareTo(item.expires);
    }
}