package com.phonegap.calendar.android.adapters;

import java.util.Date;

import com.phonegap.calendar.android.utils.DateUtils;

/**
 * This class instances are objects that represent the info relative to
 * DTSTART and DTEND tags in the recurrence string of EventEntry, we have here parse
 * methods for the conversion EventEntry - > Event and viceversa
 * This class attributes and methods are implemented by following the Ical Specifications
 * described by IETF in :  @link http://www.ietf.org/rfc/rfc2445.txt
 * @author Sergio Martinez Rodriguez 
 */
public class Dt {

	/**
	 * Time Zone of Calendar for recurrence
	 */
	private String TimeZone;
	/**
	 * End - Start date for the recurrence event
	 */
	private Date date;
	
	/**
	 * Gets timeZone Attribute
	 * @return timeZone String
	 */
	public String getTimeZone() {
		return TimeZone;
	}
	/**
	 * Sets timeZone Attribute
	 * @param timeZone timeZone String
	 */
	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}
	/**
	 * Gets date Attribute
	 * @return date Date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * Sets date Attribute
	 * @param date date Date 
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Writes the Dt object as String with the rfc2445 ietf format
	 * for the DTSTART or DTEND labels in ICal spec format
	 * @return result String with rfc2445 format
	 */
	public String getICalFormat(String type){
		
		String result = type;
		
		if (TimeZone!=null)
			result.concat(";"+TimeZone+":");
		else
			result.concat(":");
		
		result.concat(DateUtils.dateToStringCalendarDate(date, "yyyyMMdd'T'HHmmss")+"\n");
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Dt [TimeZone=" + TimeZone + ", date=" + date + "]";
	}
	
	
	
}
