package com.trial.phonegap.plugin.calendar;



import java.lang.reflect.Constructor;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import com.phonegap.api.PhonegapActivity;

/**
 * This abstract class defines an API for communication with Calendar APIS like Google
 * Yahoo, native apks etc. The class is in charge of creating an instance of a class 
 * implementing this API. To create the instance there are two options. 
 * Either Passing a string with the name of the class or use a method to create  a default instance
 * The implementations of this class must working with JSON for communication with javaScript plugin
 */
public abstract class CalendarAccessorCreator {

	/**
	 * 
	 */
	private static final boolean TEST_CALENDAR = false;
	private static final boolean GOOGLE_CALENDAR = true;
	
	
	
	private static final String LOG_TAG = "[Android:CalendarAccesor.java]";

    protected Activity mApp;
    protected WebView mView;
    
    /**
     * This static method returns a new Instance of the implementation class given by parameter
     * @param className Is a String object with the name of the implementation class
     * @param webView
     * @param ctx
     * @return An instance of CalendarAccessorCreator 
     */
    public static CalendarAccessorCreator getInstance(String className, WebView webView,PhonegapActivity ctx){
		try {			
			Class<? extends CalendarAccessorCreator> clazz = Class.forName(className).asSubclass(CalendarAccessorCreator.class);
			
			// Grab constructor class dynamically.
			Constructor<? extends CalendarAccessorCreator> classConstructor = clazz.getConstructor(	Class.forName("android.webkit.WebView"),
																									Class.forName("android.app.Activity"));
			return classConstructor.newInstance(webView, ctx);
			
		} catch (ClassNotFoundException e) {
    	 	Log.e(LOG_TAG,"Instantiation Error, class not found:" + className);
            e.printStackTrace();
		} catch (InstantiationException e) {
    	 	Log.e(LOG_TAG,"Instantiation Error:" + className);
            e.printStackTrace();
     	} catch (IllegalAccessException e) {
    	 	Log.e(LOG_TAG,"Instantiation Error, illegal access:" + className);
            e.printStackTrace();
     	} catch (Exception e) {
			throw new IllegalStateException(e);
     	}
     	return null;
    }
    
    
    /**
     * This static method return a default instance of the implementation class
     * @param webView
     * @param ctx
     * @return An instance of CalendarAccesorCreator
     */
	public static CalendarAccessorCreator getInstance(WebView webView,	PhonegapActivity ctx) {	
	
		CalendarAccessorCreator calendarInstance = null;
		String className = null;
			
		/* 
		 * Check the implementation we want to use. Choose a mock implementation
		 * or an other reeal implementation
		 */				
		Log.d(LOG_TAG,"1.1 - Has not yet created any instance of calendar, instatiate one");
		Log.d(LOG_TAG,"1.2 - Check the CalendarAccesor implementation class ");
		if (TEST_CALENDAR){	
			Log.d(LOG_TAG,"1.3 - Select com.trial.phonegap.plugin.calendar.CalendarAccessorMock like CalendarAccesor implementation class ");	
			className = "com.trial.phonegap.plugin.calendar.CalendarAccessorMock";
		} else if (GOOGLE_CALENDAR){
			className = "com.trial.phonegap.plugin.calendar.CalendarAccessorGoogle";
		}else{
			Log.d(LOG_TAG,"1.3 - Select OTHER like CalendarAccesor implementation class ");	
		}			
		Log.d(LOG_TAG,"1.4 - Instantiate the class name selected ");
		
		calendarInstance = getInstance(className, webView,ctx);
		
		Log.d(LOG_TAG,"1.5 - The instantiate was successful");
		Log.d(LOG_TAG,"1.6 - return the instance");
		return calendarInstance;
	}

	/**
	 * This  method finds the calendar events matching with the options given by parameter, and
	 * returns a JSONArray with the events found.
	 * @param options The options are a JSONObject with the following String fields:	
	 * 				- startBefore: events that start before this date 
	 * 				- startAfter: events that start after this date
	 * 				- endBefore: events that end before this date
	 * 				- endAfter: events that end after this date
	 * 				
	 * 				The union of all fields is a great filter for finding the events
	 * @return A JSONArray object with the events calendar found
	 */
	public abstract JSONArray find(JSONObject options);
	
	/**
	 * This method save a calendar event given by parameter. The method is used either to update or create one event.
	 * @param jsonObject Is a JSONObject object with all calendar event fields.	
	 * @see calendar.js 
	 * @return A boolean with the success of the method.
	 */
	public abstract boolean save(JSONObject jsonObject);

	

}
