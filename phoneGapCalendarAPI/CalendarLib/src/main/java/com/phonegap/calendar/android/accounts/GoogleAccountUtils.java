package com.phonegap.calendar.android.accounts;

import java.io.IOException;

//import javax.naming.AuthenticationException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * This class is implements some useful operations in order to get
 * the access token for google calendar accounts
 * @author sergio
 *
 */
public class GoogleAccountUtils {

	private static final String TAG = "GoogleAccountUtils";
	
	/**
	 * Shared preferences file name
	 */
	private static final String PREF = "MyPrefs";
		
	/**
	 * type of auth we are requesting for
	 */
	private static final String AUTH_TOKEN_TYPE = "cl";
	
	/**
	 * AccountName tag for Shared_preferences file
	 */
	  static final String PREF_ACCOUNT_NAME = "accountName";

	  /**
		 * authToken tag for Shared_preferences file
		 */
	  static final String PREF_AUTH_TOKEN = "authToken";

	  /**
		 * Session Id
		 */
	  static final String PREF_GSESSIONID = "gsessionid";
	
	  /**
	   * Current user acount selected
	   */
	private Account account;
	
	/**
	 * Access token for current User account selected
	 */
	String authToken;
	
	/**
	 * Gets the current user account selected 
	 * @return account current user account selected
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Sets the current user account selected
	 * @param account User account Account object
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * Selects an user account, that includes, modify the shared preferences
	 * file with the new access token and change in the current object
	 * the current account 
	 * @param context Android app context
	 * @param account Account object with the new account that will be selected
	 * @return authToken String object with the new access token
	 */
	public String selectAccount(Context context,Account account) {
		this.account = account;
		gotAccount(context, false, account.type);
		return authToken;
	}
	
	/**
	 * Invalidate access token if has expired and get a new one by getting 
	 * a new account
	 * @param context Android app Context
	 * @param tokenExpired Boolean, true if token expired, else false
	 * @param accountType Type of accounts we are requesting for
	 */
	private void gotAccount(Context context, boolean tokenExpired, String accountType) {
	    SharedPreferences settings = context.getSharedPreferences(PREF, 0);
	    String accountName = settings.getString("accountName", null);
	    if (accountName != null) {
	      AccountManager manager = AccountManager.get(context);
	      Account[] accounts = manager.getAccountsByType(accountType);
	      int size = accounts.length;
	      for (int i = 0; i < size; i++) {
	        Account account = accounts[i];
	        if (accountName.equals(account.name)) {
	          if (tokenExpired) {
	            manager.invalidateAuthToken(accountType, this.authToken);
	            Log.i("Tokken","---->token Invalidate<-----"+authToken);
	          }
	          gotAccount(context, manager, account);
	          return;
	        }
	      }
	    }
	    //As default, the first account in array will be chosen
	    AccountManager manager = AccountManager.get(context);
	    gotAccount(context, manager, AccountsUtils.getAccountsByType(context,"com.google")[0]);
	  }

	/**
	 * Starts the proccess of getting a new google account 
	 * @param context Android App context
	 * @param manager Android AccountManager object  
	 * @param account Account will be selected
	 */
	  private void gotAccount(final Context context, final AccountManager manager, final Account account) {
	    SharedPreferences settings = context.getSharedPreferences(PREF, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(PREF_ACCOUNT_NAME, account.name);
	    editor.commit();
		try {
			Thread t = new Thread() {

				/*
				 * (non-Javadoc)
				 * @see java.lang.Thread#run()
				 * This thread is launched in order to perform the http request and get the access token 
				 * the method manager.getAuthToken(account, AUTH_TOKEN_TYPE, true, null, null).getResult();
				 * will return us the token in case of success or one messsage that inform us that app
				 * has not permission in order to access the user accounts. In case of success we call the
				 * authenticatedClientLogin() method in order to store the access token otherwise
				 * whe have to inform user that he has to give the app the requested permissions
				 */
				@Override
				public void run() {
					try {
						final Bundle bundle = manager.getAuthToken(account, AUTH_TOKEN_TYPE, true, null, null).getResult();
						if (bundle.containsKey(AccountManager.KEY_INTENT)) {
							//User has not provided his credentials into selected account
							Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
							int flags = intent.getFlags();
							flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
							intent.setFlags(flags);
							Log.e(TAG, "User has to write his credentials");
//							throw new AuthenticationException ("Not authenticated User in selected account.");

						} else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {

							authenticatedClientLogin(context,bundle.getString(AccountManager.KEY_AUTHTOKEN));
							
						}
	    	}catch (IOException ioException){
	    		Log.e(TAG,"IOException produced getting the authToken --> "+ioException.getMessage());              
			}catch (OperationCanceledException operationCanceledException) {
				Log.e(TAG,"operationCanceledException produced getting the authToken --> "+operationCanceledException.getMessage());
			} catch (AuthenticatorException authenticatorException) {
				Log.e(TAG,"AuthenticatorException produced getting the authToken --> "+authenticatorException.getMessage());
//			} catch (AuthenticationException authenticationException) {
//          	  	Log.e(TAG,"An AuthenticationException was thrown because user has to introduce his credentials (username, pass) into the selected accout: "+account.name+". Error description: "+authenticationException.getMessage());
			}
	      }
	    };
	    t.start();

			t.join();
		 
  	    } catch (InterruptedException interruptedException) {			
  	    	Log.e(TAG,"interruptedException produced in the thread in charge of authentication process --> "+interruptedException.getMessage());
		}catch (Exception exception){
			Log.e(TAG,"Exception produced in authentication process --> "+exception.getMessage());
		}
	  }
	  
	  /**
	   * This method stores the access token and selected account in a shared preferences
	   * file 
	   * @param context android App context
	   * @param authToken access token for the requested acoount
	   */
	  private void authenticatedClientLogin(Context context,String authToken) {
		    this.authToken = authToken;	
		    SharedPreferences settings = context.getSharedPreferences(PREF, 0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString(PREF_AUTH_TOKEN, authToken);
		    editor.commit();
		    Log.i(TAG,"Access Token stored in shared_preferences ->"+authToken);
		  }
	  
}
