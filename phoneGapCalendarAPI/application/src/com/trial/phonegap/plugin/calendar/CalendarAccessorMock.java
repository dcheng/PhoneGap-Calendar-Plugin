package com.trial.phonegap.plugin.calendar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

public class CalendarAccessorMock extends CalendarAccessorCreator{
	private static final String LOG_TAG = "[Android:CalendarAccesorMock.java]";
	private static List<JSONObject> calendar1; 
	
	 /**
     * Create an contact accessor.
     */
    public CalendarAccessorMock(WebView view, Activity app) {
		mApp = app;
		mView = view;
		
		init();
	}
    /**
     * This auxiliary method sets up the calendar
     */
	private void init() {
	calendar1 = new ArrayList<JSONObject>();
		
	
		String description= "Meeting with Joe's team";
		String location= "Conf call #+4402000000001";
		String summary= "Agenda:\\n\\n\\t* Introductions\\n\\t* AoB";
		String start= "2011-03-24 09:00:00";
		String end= "2011-03-24 10:00:00";
		String status= "pending";
		String transparency= "transparent";		
		String reminder= "2011-03-24T13:00:00+00:00";
		String frequency= "yearly";
		JSONArray daysInMonth = (new JSONArray()).put(24);
		JSONArray monthsInYear = (new JSONArray()).put(3);
		String expires = "2011-06-11T16:00:00Z";
		addCalendar(calendar1, description, location, summary, start,
				end, status, transparency, reminder, 
				frequency, daysInMonth, monthsInYear, expires);
		
		
		description= "Meeting with dentist";
		location= "Conf call #+222222222222";
		summary= "Agenda:\\n\\n\\t* Introductions\\n\\t* AoB";
		start= "2011-04-24 09:00:00";
		end= "2011-04-24 10:00:00";
		status= "pending";
		transparency= "transparent";		
		reminder= "2011-04-24T13:00:00+00:00";
		frequency= "monthly";
		daysInMonth = (new JSONArray()).put(25);
		monthsInYear = (new JSONArray()).put(4);
		expires = "2011-06-11T16:00:00Z";
		addCalendar(calendar1, description, location, summary, start,
				end, status, transparency, reminder, 
				frequency, daysInMonth, monthsInYear, expires);
		
	}

	@Override	
	public JSONArray find(JSONObject options) {	
		Date dateAfter = null;
		Date dateBefore = null;
		JSONArray events = new JSONArray();	
		try {
			
			JSONObject filter = options.getJSONObject("filter");	
			Log.i(LOG_TAG,"Date After: " + filter.getString("startAfter"));
			Log.i(LOG_TAG,"Date Before: " + filter.getString("startBefore"));			
			dateAfter = stringToDate(filter.getString("startAfter"));
			dateBefore = stringToDate(filter.getString("startBefore"));
			
			for (JSONObject event : calendar1){
				if (stringToDate(event.getString("start")).after(dateAfter) &&
					stringToDate(event.getString("start")).before(dateBefore)){
					events.put(event);
				}
			}
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return events;	
		
	}
	/**
	 * 
	 */
	@Override
	public boolean save(JSONObject newCalendarEvent) {
		try {
			if (exists(newCalendarEvent)) {
				calendar1.set(	Integer.parseInt(newCalendarEvent.getString("id")),
							  	newCalendarEvent);
			} else {
				calendar1.add(newCalendarEvent);
				newCalendarEvent.put("id", String.valueOf(calendar1.indexOf(newCalendarEvent)));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Return a true boolean value if the given parameter is an eventCalendar and it is in the current calendar
	 * @param eventCalendar
	 * @return a boolean
	 */
	private boolean exists(JSONObject eventCalendar){
	
		try {
			
			if ((eventCalendar.isNull("id")) ||
				(Integer.parseInt(eventCalendar.getString("id")) >= calendar1.size()) || 
				(calendar1.get(Integer.parseInt(eventCalendar.getString("id"))) == null)){
				return false;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} 
		return true;
		
	}
	/**
	 * Add an event to calendar with the given paramaters
	 * @param calendar
	 * @param description
	 * @param location
	 * @param summary
	 * @param start
	 * @param end
	 * @param status
	 * @param transparency
	 * @param reminder
	 * @param frequency
	 * @param daysInMonth
	 * @param monthsInYear
	 * @param expires
	 */
	private static void addCalendar(List<JSONObject> calendar,									
									String description,
									String location,
									String summary,
									String start,
									String end,
									String status,
									String transparency,
									String reminder,
									String frequency,
									JSONArray daysInMonth,
									JSONArray monthsInYear,
									String expires){
		
		JSONObject event = new JSONObject();
		JSONObject recurrence= new JSONObject();
		try {
			recurrence.put("frequency", frequency);
			recurrence.put("daysInMonth", daysInMonth);
			recurrence.put("monthsInYear", monthsInYear);
			recurrence.put("expires", expires);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {			
			event.put("description", description);
			event.put("location", location);		
			event.put("summary", summary);
			event.put("start", start);
			event.put("end", end);
			event.put("status", status);
			event.put("transparency", transparency);
			event.put("reminder", reminder);
			event.put("recurrence", recurrence);
			
			calendar.add(event);
			event.put("id", String.valueOf(calendar.indexOf(event)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	/**
	 * Parse a String into Date, with format yyyy-MM-dd HH:mm:ss
	 * @param dateString
	 * @return
	 */
	private static Date stringToDate(String dateString) {
		Date date = null;

		SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = dformat.parse(dateString);
			//its normal having an parsing exception here, for instance, with "No Credit" as string
		} catch (ParseException parseException) {
			Log.i(LOG_TAG, parseException.getMessage());
			return null;			
		}
		return date;
	}
	
	/**
	 * Parse any date into string format yyyy/MM/dd-HH:mm:ss 
	 * @param date
	 * @return String date
	 */
	private static String dateToString(Date date){		
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dformat.format(date);
		
	}

	

}
