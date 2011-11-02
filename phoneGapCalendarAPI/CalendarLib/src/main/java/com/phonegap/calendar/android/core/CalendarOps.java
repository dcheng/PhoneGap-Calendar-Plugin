/*
 *  Copyright 2011 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */

package com.phonegap.calendar.android.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.phonegap.calendar.android.model.CalendarEntry;
import com.phonegap.calendar.android.model.CalendarFeed;
import com.phonegap.calendar.android.model.CalendarUrl;
import com.phonegap.calendar.android.model.EventEntry;
import com.phonegap.calendar.android.model.EventFeed;
import com.phonegap.calendar.android.utils.DateUtils;

/**
 * All the methods designed in order to perform the calendar operations
 * are static methods and will be called from this class
 * @author Sergio Martinez Rodriguez
 *
 */
public class CalendarOps {

	private static final String TAG = "CalendarOps";
	
	/**
	 * Get the user's calendar in the user account selected on device
	 * @param client Calendar client object with access to the User calendar
	 * @return List of calendars as  List<CalendarEntry>
	 */
	 public static List<CalendarEntry> getUserCalendars(CalendarClient client){
		 
		  List<CalendarEntry> calendars = new ArrayList<CalendarEntry>();
		    calendars.clear();
		    try {
		      CalendarUrl url = CalendarUrl.forAllCalendarsFeed();
		      // page through results
		      while (true) {
		        CalendarFeed feed = client.executeGetCalendarFeed(url);
		        if (feed.calendars != null) {
		          calendars.addAll(feed.calendars);
		        }
		        String nextLink = feed.getNextLink();
		        if (nextLink == null) {
		          break;
		        }
		      }
		    } catch (IOException e) {
		    	Log.e(TAG, "Error getting calendars"+e.getMessage());		    	
		      calendars.clear();
		    }
		    return calendars;
	  }
	 
	 /**
		 * Get the user selected calendar events between the specified dates 
		 * @param client CalendarClient object with access to the User calendar
		 * @param minStart minimum start date (if null 1970-01-01T00:00:00)
		 * @param maxStart maximum start date (if null 2031-01-01T00:00:00)
		 * @return List of Events List<EventEntry>
		 * 
		 */
	 public static List<EventEntry> findUserEvents(CalendarClient client, Date minStart, Date maxStart){
		 
		  List<EventEntry> events = new ArrayList<EventEntry>();
		    events.clear();
		    try {
		    	String minString = "1970-01-01T00:00:00";
		    	String maxString = "2031-01-01T00:00:00";
		    	if (minStart!=null)
		    		minString = DateUtils.dateToStringCalendarDate(minStart, null);
		    	if (maxStart!=null)
		    		maxString = DateUtils.dateToStringCalendarDate(maxStart, null);

		    CalendarUrl url = CalendarUrl.forDefaultPrivateFullEventFeedBetweenDates(
		    					minString, 
		    					maxString);
		    	
		        EventFeed feed = client.executeGetEventFeed(url);
		        if (feed.events != null) {
		          events.addAll(feed.events);
		        }
		    } catch (IOException e) {
		    	Log.e(TAG, "Error getting calendars"+e.getMessage());
		    	e.printStackTrace();
		    	events.clear();
		    }
		  return events;
	  }
	 
	 //TODO This method is never called
	  public static void shutdown(CalendarClient client) {
		    try {
		      client.shutdown();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  }

	  /**
	   * Add a new calendar into selected user account in device
	   * @param client CalendarClient object with access to the User calendar
	   * @param calendar CalendarEntry of new calendar will be added 
	   * @return CalendarEntry object corresponding to added calendar 
	   * @throws IOException
	   */
	  public static CalendarEntry addCalendar(CalendarClient client, CalendarEntry calendar) throws IOException {
	    Log.i(TAG, "Add Calendar");
	    CalendarUrl url = CalendarUrl.forOwnCalendarsFeed();
	    CalendarEntry result = client.executeInsertCalendar(calendar, url);
	    Log.i(TAG, "Added "+ result.title +" Calendar");
	    return result;
	  }

	  /**
	   * Updates the given calendar into selected user account in device
	   * @param client CalendarClient object with access to the User calendar
	   * @param calendar updated CalendarEntry object that is going to replace the original
	   * @param original original CalendarEntry object that is going to be replaced
	   * @return result CalendarEntry object with the updated calendar
	   * @throws IOException
	   */
	public static CalendarEntry updateCalendar(CalendarClient client,
			CalendarEntry calendar, CalendarEntry original) throws IOException {
		Log.i(TAG, "Update Calendar");
		CalendarEntry result = client.executePatchCalendarRelativeToOriginal(
				calendar, original);
		Log.i(TAG, "Updated: " + original.title + " Calendar into: "
				+ result.title + " Calendar");
		return result;
	}

	/**
	   * Updates the given event into selected user account in device
	   * @param client CalendarClient object with access to the User calendar
	   * @param event updated EventEntry object that is going to replace the original
	   * @param original original EventEntry object that is going to be replaced
	   * @return result EventEntry object with the updated event
	   * @throws IOException
	   */
	public static EventEntry updateEvent(CalendarClient client,
			EventEntry event, EventEntry original) throws IOException {
		Log.i(TAG, "Update Calendar");
		EventEntry result = client.executePatchEventRelativeToOriginal(event,
				original);
		Log.i(TAG, "Updated: " + original.title + " Calendar into: "
				+ result.title + " Calendar");
		return result;
	}
		  
		  public static void addEvent(CalendarClient client, CalendarEntry calendar, EventEntry event) throws IOException {
			  Log.i(TAG, "Add Event");
		    CalendarUrl url = new CalendarUrl(calendar.getEventsFeedLink());
		    EventEntry result = client.executeInsertEvent(event, url);
		    Log.i(TAG, "Added "+result.title+" Event");
		  }

		  public static void deleteCalendar(CalendarClient client, CalendarEntry calendar)
		      throws IOException {
			  Log.i(TAG,"Delete Calendar");
		    client.executeDelete(calendar);
		  }
		  
		  public static void deleteEvent(CalendarClient client, EventEntry event)
	      throws IOException {
			  Log.i(TAG,"Delete Calendar");
			  client.executeDelete(event);
		  }
	 
		/**
		 * Searches the matching calendar with the provided name 
		 * @param client CalendarClient object with access to the User calendar
		 * @param title String with the calendar that will be searched
		 * @return calendar CalendarEntry object with result
		 */
		  public static CalendarEntry getUserCalendarByTitle(CalendarClient client, String title){
				 
			  List<CalendarEntry> calendars = new ArrayList<CalendarEntry>();
			    calendars.clear();
			    try {
			      CalendarUrl url = CalendarUrl.forAllCalendarsFeed();
			      // page through results
			      while (true) {
			        CalendarFeed feed = client.executeGetCalendarFeed(url);
			        if (feed.calendars != null) {
			          calendars.addAll(feed.calendars);
			        }
			        String nextLink = feed.getNextLink();
			        if (nextLink == null) {
			          break;
			        }
			      }
			      int numCalendars = calendars.size();

			      for (int i = 0; i < numCalendars; i++) {
			        if (calendars.get(i).title.equals(title))
			        	return calendars.get(i);
			      }
			    } catch (IOException e) {
			    	Log.e(TAG, "Error getting calendars"+e.getMessage());
			      calendars.clear();
			      return null;
			    }
			    return null;
		  }


/**
 * Inserts more than one event in the same operation
 * @param  client CalendarClient for performing the operation 
 * @param Calendar CalendarEntry calendar entry object for the new events
 */
		  /*************************************************************
			At the moment this operation is not implemented yet
		  public static void batchAddEvents(CalendarClient client, CalendarEntry calendar)
	      throws IOException {
		  Log.i(TAG, "Batch Add Events");
	    EventFeed feed = new EventFeed();
	    for (int i = 0; i < 3; i++) {
	      try {
	        Thread.sleep(1000);
	      } catch (InterruptedException e) {
	      }
	      EventEntry event = newEvent();
	      event.batchId = Integer.toString(i);
	      event.batchOperation = BatchOperation.INSERT;
	      feed.events.add(event);
	    }
	    EventFeed result = client.executeBatchEventFeed(feed, calendar);
	    for (EventEntry event : result.events) {
	      BatchStatus batchStatus = event.batchStatus;
	      if (batchStatus != null && !HttpResponse.isSuccessStatusCode(batchStatus.code)) {
	        System.err.println("Error posting event: " + batchStatus.reason);
	      }else {
	    	  Log.i(TAG, "Added "+event.title+" Event");
	      }
	    }
	  //TODO SHOW result
	  }
************************************************************************/	 
}
