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

package com.trial.phonegap.plugin.calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;


public class CalendarPlugin extends Plugin{
	
	private static final String ACTION_FIND = "find";
	private static final String ACTION_SAVE = "save";
	private static final String ACTION_REMOVE = "remove";
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
		try{
			if (action.equals(ACTION_FIND)) {
				Log.d(LOG_TAG, "2 - Action service find");
				JSONObject options = args.getJSONObject(0);
				JSONArray res = calendarAccessor.find(options);
				Log.d(LOG_TAG, "3 - return plugin result for service find");
				return new PluginResult(PluginResult.Status.OK, res, "window.plugins.calendar.cast");
			}else if (action.equals(ACTION_SAVE)) {
				Log.d(LOG_TAG, "2 - Action service save");
				if (calendarAccessor.save(args.getJSONObject(0))) {
					Log.d(LOG_TAG, "3 - return plugin result for service save");
					return new PluginResult(PluginResult.Status.OK, result);					
				} else {
					JSONObject r = new JSONObject();
					r.put("code", 0);
					Log.d(LOG_TAG, "3 - return ERROR for service save");
					return new PluginResult(PluginResult.Status.ERROR, r);
				}
			}else if (action.equals(ACTION_REMOVE)){
				Log.d(LOG_TAG, "2 - Action service remove");
				if (calendarAccessor.remove(args.getJSONObject(0))) {
					Log.d(LOG_TAG, "3 - return plugin result for service remove");
					return new PluginResult(PluginResult.Status.OK, result);					
				} else {
					JSONObject r = new JSONObject();
					r.put("code", 0);
					Log.d(LOG_TAG, "3 - return ERROR for service remove");
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
