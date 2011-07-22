package com.phonegap.calendar.android.model;

import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

/**
 * Moment in which an event takes place   
 * @author Sergio Martinez Rodriguez
 *
 */
public class When {

	/**
	 * Time the event is starting
	 */
	@Key("@startTime")
	public DateTime startTime;

	/**
	 * Time the event is finishing
	 */
	@Key("@endTime")
	public DateTime endTime;
	
	/**
	 * String with aditional information
	 */
	@Key("@valueString")
	public String valueString;

	/**
	 * List of reminders for the event, links with {@link Reminder}
	 */
	@Key("gd:reminder")
	public List<Reminder> reminders;
}
