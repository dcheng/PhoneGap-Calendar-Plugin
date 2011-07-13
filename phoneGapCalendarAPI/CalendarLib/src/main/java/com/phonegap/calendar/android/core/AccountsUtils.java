package com.phonegap.calendar.android.core;

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

public class AccountsUtils {

	private static final String PREF = "MyPrefs";
	
	private static final String TAG = "AccountsUtils";
	
	private static final String AUTH_TOKEN_TYPE = "cl";
	
	  static final String PREF_ACCOUNT_NAME = "accountName";
	  static final String PREF_AUTH_TOKEN = "authToken";
	  static final String PREF_GSESSIONID = "gsessionid";
	
	private Account account;
	String authToken;
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String selectAccount(Context context,Account account) {
		this.account = account;
		gotAccount(context, false, account.type);
		return authToken;
	}
	
	public String[] getAccountsNamesByType(Context context, String type){		
		Account[] accounts = getAccountsByType(context, type);
	    final int size = accounts.length;
	    String[] names = new String[size];
	    for (int i = 0; i < size; i++) {
	      names[i] = accounts[i].name;
	    }
	    return names;
	}
	
	public Account[] getAccountsByType(Context context, String type){
		final AccountManager manager = AccountManager.get(context);
	    final Account[] accounts = manager.getAccountsByType(type);	    
	    return accounts;
	}
	
	public Account getAccountByNameAndType(Context context, String type, String name){		
		Account[] accounts = getAccountsByType(context, type);
	    final int size = accounts.length;
	    for (int i = 0; i < size; i++) {
	      if (accounts[i].name.equals(name))
	    	  return accounts[i];
	    }
	    return null;
	}
	
	
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
	    gotAccount(context, manager, getAccountsByType(context,"com.google")[0]);
	  }

	  private void gotAccount(final Context context, final AccountManager manager, final Account account) {
	    SharedPreferences settings = context.getSharedPreferences(PREF, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(PREF_ACCOUNT_NAME, account.name);
	    editor.commit();
		try {
			Thread t = new Thread() {

				@Override
				public void run() {
					Log.i(TAG,"HELLOOOOOO" );
					try {
						final Bundle bundle = manager.getAuthToken(account, AUTH_TOKEN_TYPE, true, null, null).getResult();
						if (bundle.containsKey(AccountManager.KEY_INTENT)) {
							//User has not provided his credentials into selected account
							Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
							int flags = intent.getFlags();
							flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
							intent.setFlags(flags);
							Log.i(TAG,"HELLOOOOOO 1" );
							Log.e(TAG, "User has to write his credentials");
//							throw new AuthenticationException ("Not authenticated User in selected account.");

						} else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
							Log.i(TAG,"HELLOOOOOO 2" );
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
	  
	  private void authenticatedClientLogin(Context context,String authToken) {
		    this.authToken = authToken;	
		    SharedPreferences settings = context.getSharedPreferences(PREF, 0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString(PREF_AUTH_TOKEN, authToken);
		    editor.commit();
		    Log.i(TAG,"Access Token stored in shared_preferences ->"+authToken);
		  }
	  
}
