package com.phonegap.calendar.android.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.phonegap.calendar.android.core.CalendarClient;
import com.phonegap.calendar.android.core.CalendarClientFactory;
import com.phonegap.calendar.android.core.CalendarOps;
import com.phonegap.calendar.android.model.CalendarEntry;

import android.content.Context;
import android.util.Log;

/**
 * This class represents the CalendarsManager object that get an instance of the google
 * CalendarClient and give us the possibility of having a list of user's calendars, add
 * a new calendar, modify or delete.
 * @author Sergio Martinez Rodriguez
 */
public class CalendarsManager {

	private static final String TAG = "CalendarsManager";
	/**
	 * 
	 */
	private CalendarClient calendarClient;
	
	/**
	 * Builder that gets an instance of the google ClientCalendar with
	 * access into the user's google account calendar 
	 * @param context
	 */
	public CalendarsManager(Context context) {
		calendarClient = CalendarClientFactory.getInstance(context);
	}
	
	/**
	 * Get a List with all the user calendars
	 * @return List of user's calendars
	 */
	public List<Calendar> getUserCalendars(){
		 
		List<Calendar> result = new ArrayList<Calendar>();
		
		try{		        
		        
		        List<CalendarEntry> calendars = CalendarOps.getUserCalendars(calendarClient);		        
		        for (CalendarEntry calendarEntry : calendars){
		        	result.add(new Calendar(calendarEntry, calendarClient));
		        }
		        
		        return result;
		        
		    }catch(NullPointerException nullPointerException){
	        	Log.e(TAG,"NullPointerException produced getting the CalendarClient --> "+nullPointerException.getMessage());
	        	nullPointerException.printStackTrace();
	        	return null;
	        }catch(Throwable throwable){
	        	Log.e(TAG,"Unknown Throwable exception produced --> "+throwable.getMessage());
	        	throwable.printStackTrace();
	        	return null;
	        }
	}
	
	/**
	 * Deletes the given calendar into the User account
	 * @param calendar Calendar object that will be deleted
	 * @return True if success, false otherwise
	 * @throws IOException if any error happens while deleting calendar
	 */
	public boolean deleteCalendar(CalendarEntry calendar)
			throws IOException {
		Log.i(TAG, "Delete Calendar");
		try {
			CalendarOps.deleteCalendar(calendarClient, calendar);
			Log.i(TAG, "Deleted Calendar");
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
  }
	
	/**
	 * Persist the given calendar into the User account
	 * @param calendar Calendar object that will be created
	 * @return True if success, false otherwise
	 * @throws IOException if any error happens while deleting calendar
	 */
	public boolean createCalendar(Calendar calendar){
		Log.i(TAG, "Add Calendar");
		try {
			CalendarOps.addCalendar(calendarClient, calendar.getCalendarEntry());
			Log.i(TAG, "Added Calendar");
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Updates the given calendar into the User account
	 * @param modified Updated Calendar object
	 * @param original original Calendar object to be updated
	 * @return True if success, false otherwise
	 */
	public boolean modifyCalendar(Calendar calendar, Calendar modified, Calendar original){
		Log.i(TAG, "Modifying Calendar");
		try {
			CalendarOps.updateCalendar(calendarClient, modified.getCalendarEntry(), original.getCalendarEntry());
			Log.i(TAG, "Modified Calendar");
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Get a Calendar object with the specified calendar in the title param
	 * @param title String with the name of calendar
	 * @return Calendar object with the requested calendar
	 */
	public Calendar getCalendarByTitle(String title){
		Calendar calendar = new Calendar(CalendarOps.getUserCalendarByTitle(calendarClient, title), calendarClient); 
		return calendar;
		
	}
	
	
	
	
	
	
}
