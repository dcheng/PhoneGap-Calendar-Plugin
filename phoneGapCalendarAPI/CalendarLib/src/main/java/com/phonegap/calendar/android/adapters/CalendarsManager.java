package com.phonegap.calendar.android.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpResponseException;

import com.phonegap.calendar.android.core.CalendarClientFactory;
import com.phonegap.calendar.android.core.CalendarOps;
import com.phonegap.calendar.android.model.CalendarClient;
import com.phonegap.calendar.android.model.CalendarEntry;

import android.content.Context;
import android.util.Log;


public class CalendarsManager {

	private static final String TAG = "CalendarsManager";
	private CalendarClient calendarClient;
	
	public CalendarsManager(Context context) {
		calendarClient = CalendarClientFactory.getInstance(context);
	}
	
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
	
	public Calendar getCalendarByTitle(String title){
		Calendar calendar = new Calendar(CalendarOps.getUserCalendarByTitle(calendarClient, title), calendarClient); 
		return calendar;
		
	}
	
	
	
	
	
	
}
