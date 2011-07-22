package com.trial.phonegap.plugin.calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;


public class CalendarPlugin extends Plugin{
	
	private static final String ACTION_FIND = "find";
	public static CalendarAccessorCreator calendarAccessor;
	private static final String LOG_TAG = "[Android:CalendarPlugin.java]";

	
	/**
	 * Executes the request and returns PluginResult.
	 * 
	 * @param action 		The action to execute.
	 * @param args 			JSONArry of arguments for the plugin.
	 * @param callbackId	The callback id used when calling back into JavaScript.
	 * @return 				A PluginResult object with a status and message.
	 */
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		Log.d(LOG_TAG, "1 - Plugin calendar called");
		
		if (calendarAccessor == null) {				
			calendarAccessor = CalendarAccessorCreator.getInstance(webView, ctx);
		}
		
		
		;
		String result = "";
		Log.i(LOG_TAG, "Plugin result OK: " + PluginResult.Status.OK );
		Log.i(LOG_TAG, "Plugin result ERROR: " + PluginResult.Status.ERROR );
		Log.i(LOG_TAG, "Plugin result INVALID ACTION: " + PluginResult.Status.INVALID_ACTION );
		try{
			if (action.equals(ACTION_FIND)) {
				Log.d(LOG_TAG, "2 - Action service find");
				JSONObject options = args.getJSONObject(0);
				JSONArray res = calendarAccessor.find(options);
				Log.d(LOG_TAG, "3 - return plugin result for service find");
				return new PluginResult(PluginResult.Status.OK, res, "window.plugins.calendar.cast");
			}else if (action.equals("save")) {
				Log.d(LOG_TAG, "2 - Action service save");
				if (calendarAccessor.save(args.getJSONObject(0))) {
					return new PluginResult(PluginResult.Status.OK, result);					
				}
				else {
					JSONObject r = new JSONObject();
					r.put("code", 0);
					return new PluginResult(PluginResult.Status.ERROR, r);
				}
			}
			
			return new PluginResult(PluginResult.Status.INVALID_ACTION, result);
		}catch (JSONException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);		
		}
	}

}
