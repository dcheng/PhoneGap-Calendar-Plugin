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


package com.phonegap.calendar.android.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * This class provides some static methods in order to manage with 
 * user accounts that user has synchronized into his device 
 * @author Sergio Martinez Rodriguez
 */
public class AccountsUtils {

	/**
	 * Gets the Name of synchronized user accounts into the device
	 * @param context Android app Context
	 * @param type String with kind of accounts we want to obtain (Ex. com.google) 
	 * @return String[] with  synchronized user accounts into the device
	 */
	public static String[] getAccountsNamesByType(Context context, String type){		
		Account[] accounts = getAccountsByType(context, type);
	    final int size = accounts.length;
	    String[] names = new String[size];
	    for (int i = 0; i < size; i++) {
	      names[i] = accounts[i].name;
	    }
	    return names;
	}

	/**
	 * Gets the synchronized Accounts in device of user.
	 * @param context Android app Context
	 * @param type String with kind of accounts we want to obtain (Ex. com.google) 
	 * @return Account[] with  synchronized user accounts into the device
	 */
	public static Account[] getAccountsByType(Context context, String type){
		final AccountManager manager = AccountManager.get(context);
	    final Account[] accounts = manager.getAccountsByType(type);	    
	    return accounts;
	}
	
	/**
	 * Gets the synchronized Accounts specified by name
	 * @param context Android app Context
	 * @param type String with kind of accounts we want to obtain (Ex. com.google)
	 * @param name String with desired account name 
	 * @return Account with  matching with given name, null in case of not matching
	 */
	public static Account getAccountByNameAndType(Context context, String type, String name){		
		Account[] accounts = getAccountsByType(context, type);
	    final int size = accounts.length;
	    for (int i = 0; i < size; i++) {
	      if (accounts[i].name.equals(name))
	    	  return accounts[i];
	    }
	    return null;
	}

}
