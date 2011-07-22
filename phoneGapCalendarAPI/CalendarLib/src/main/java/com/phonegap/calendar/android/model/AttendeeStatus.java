package com.phonegap.calendar.android.model;

import com.google.api.client.util.Key;

/**
 * @link http://gdata-java-client-ext.googlecode.com/svn/trunk/src/com/google/api/data/
* This class represents if the persons invited to event 
* are going to assist or not. Called form @{link Who.java}  
* @author Sergio Martinez
*/
public class AttendeeStatus {
	
	/**
	 * Assistence status
	 */
	@Key("@value")
	  public String description;
	
}
