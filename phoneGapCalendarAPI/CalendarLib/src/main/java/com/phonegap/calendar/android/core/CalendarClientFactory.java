package com.phonegap.calendar.android.core;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.phonegap.calendar.android.accounts.AccountsUtils;
import com.phonegap.calendar.android.accounts.GoogleAccountUtils;

/**
 * The aim of this class is to get an instance of the Google CalendarClient
 * and the CalendarClient object obtained must be able to access to
 * user google account calendar information.  
 * @author Sergio Martinez Rodriguez
 *
 */
public class CalendarClientFactory {
	
	private static final String TAG = "CalendarClientFactory";
	
	/**
	 * google calendar User account access token 
	 */
	static final String PREF_AUTH_TOKEN = "authToken";
	
	/**
	 * Session Id
	 */
	static final String PREF_GSESSIONID = "gsessionid";
	/**
	 * Name of file for shared_preferences
	 */
	private static final String PREF = "MyPrefs";
	
	/**
	 * HttpTransport attribute for obtaining the HttpRequest
	 */
	private static HttpTransport transport;	
	/**
	 * User google Calendar account acces token
	 */
	private static String authToken;	  	
	/**
	 * Google CalendarClient object will be returned as new instance 
	 */
	private static CalendarClient calendarClient = null;	
	/**
	 * Session id
	 */
	private static  String gsessionid;
	/**
	 * Shared preferences object for storing the user info
	 */
	private static SharedPreferences settings;
	/**
	 * Google Account Manager object for accessing into google authentication service  
	 */
	private static GoogleAccountManager accountManager;
	  
	/**
	 * This method creates an instance of Google CalendarClient
	 * if it does not exists, for this aim we need use the 
	 * account manager and the {@link GoogleAccountUtils} methods
	 * in order to get the access token and fill the corresponding 
	 * init values into the new ClientCalendar instance 
	 * @param context ANdroid app context
	 * @return CalendarClient instance with access into the google calendar account 
	 */
	  static public CalendarClient getInstance(Context context){
		  
		  if (calendarClient==null){ 

			transport = AndroidHttp.newCompatibleTransport();
			accountManager = new GoogleAccountManager(context);
			settings = context.getSharedPreferences(PREF, 0);
			authToken = settings.getString(PREF_AUTH_TOKEN, null);
	
		  	if (authToken==null){
		  		GoogleAccountUtils googleAccountsUtils = new GoogleAccountUtils();
		  		//here you must specify the account in which you want work on
		  		if (AccountsUtils.getAccountsByType(context, "com.google").length>0){
		  			authToken = googleAccountsUtils.selectAccount(context,AccountsUtils.getAccountsByType(context, "com.google")[0]);
		  		}
		  		else {
		  			Log.e(TAG, "You have not any account asociated into this device");
		  			return null;
		  		}
		  	}
		  	
		    gsessionid = settings.getString(PREF_GSESSIONID, null);
		    
		    final MethodOverride override = new MethodOverride(); // needed for PATCH
		    
		    HttpRequestInitializer initializer = new HttpRequestInitializer() {
				
				@Override
				public void initialize(HttpRequest request) throws IOException {
					
			          GoogleHeaders headers = new GoogleHeaders();
			          headers.setApplicationName("Google-CalendarAndroidSample/1.0");
			          headers.gdataVersion = "2";
			          request.headers = headers;
			          calendarClient.initializeParser(request);
			          
			          request.interceptor = new HttpExecuteInterceptor() {
			          
			          		            public void intercept(HttpRequest request) throws IOException {
			          		              GoogleHeaders headers = (GoogleHeaders) request.headers;
			          		              headers.setGoogleLogin(authToken);
			          		              request.url.set("gsessionid", gsessionid);
			          		              override.intercept(request);
			          		            }
			          		          };
			          		          request.unsuccessfulResponseHandler = new HttpUnsuccessfulResponseHandler() {
			          
			          		            public boolean handleResponse(
			          		                HttpRequest request, HttpResponse response, boolean retrySupported) {
			          		              switch (response.statusCode) {
			          		                case 302:
			          		                  GoogleUrl url = new GoogleUrl(response.headers.location);
			          		                  gsessionid = (String) url.getFirst("gsessionid");
			          		                  SharedPreferences.Editor editor = settings.edit();
			          		                  editor.putString(PREF_GSESSIONID, gsessionid);
			          		                  editor.commit();
			          		                  return true;
			          		                case 401:
			          		                  accountManager.invalidateAuthToken(authToken);
			          		                  authToken = null;
			          		                  SharedPreferences.Editor editor2 = settings.edit();
			          		                  editor2.remove(PREF_AUTH_TOKEN);
			          		                  editor2.commit();
			          		                  return false;
			          		              }
			          		              return false;
			          		            }
			          		          };
			          
					
				}
			};
		    
			calendarClient = new CalendarClient(transport.createRequestFactory(initializer));		  
		  }
			return calendarClient;
	  }	 
	
}
