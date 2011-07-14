package com.trial.phonegap.plugin.calendar;



import java.lang.reflect.Constructor;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import com.phonegap.api.PhonegapActivity;


public abstract class CalendarAccessorCreator {

	
	private static final boolean TEST_CALENDAR = false;
	private static final boolean GOOGLE_CALENDAR = true;
	
	
	
	
	
	
	
	private static final String LOG_TAG = "[Android:CalendarAccesor.java]";

    protected Activity mApp;
    protected WebView mView;
    
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

	public abstract JSONArray find(JSONObject options);

	public abstract boolean save(JSONObject jsonObject);

	

}
