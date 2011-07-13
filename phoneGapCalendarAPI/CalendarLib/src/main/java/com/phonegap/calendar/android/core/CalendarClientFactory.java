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
import com.phonegap.calendar.android.model.CalendarClient;


public class CalendarClientFactory {
	
	private static final String TAG = "CalendarClientFactory";
	static final String PREF_AUTH_TOKEN = "authToken";
	static final String PREF_GSESSIONID = "gsessionid";
	private static final String PREF = "MyPrefs";
	
	private static HttpTransport transport;	
	private static String authToken;	  	
	private static CalendarClient calendarClient = null;	
	private static  String gsessionid;
	private static SharedPreferences settings;
	private static GoogleAccountManager accountManager;
	  
	  static public CalendarClient getInstance(Context context){
		  
		  if (calendarClient==null){ 

			transport = AndroidHttp.newCompatibleTransport();
			accountManager = new GoogleAccountManager(context);
			settings = context.getSharedPreferences(PREF, 0);
			authToken = settings.getString(PREF_AUTH_TOKEN, null);
	
		  	if (authToken==null){
		  		AccountsUtils accountsUtils = new AccountsUtils();
		  		//here you must specify the account in which you want work on
		  		if (accountsUtils.getAccountsByType(context, "com.google").length>0){
		  			authToken = accountsUtils.selectAccount(context,accountsUtils.getAccountsByType(context, "com.google")[0]);
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
