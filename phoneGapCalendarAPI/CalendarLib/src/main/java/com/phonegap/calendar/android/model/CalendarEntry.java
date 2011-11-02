/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */


/*
 *  Modified by Sergio Martinez. Copyright 2011 Vodafone Group Services Ltd.
 * 
 */

package com.phonegap.calendar.android.model;

import com.google.api.client.util.Key;

/**
 * Calendar object for new calendar entries in calendar  
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public class CalendarEntry extends Entry {

	/**
	 * EVENTS_FEED constant URL
	 */
	public static final String EVENTS_FEED = "http://schemas.google.com/gCal/2005#eventFeed";
	
	/**
	 * ACCESS_CONTROL_LIST constant URL
	 */
	public static final String ACCESS_CONTROL_LIST = "http://schemas.google.com/acl/2007#accessControlList";

	/** Current user has no access to the calendar. */
	public static final String ACCESS_LEVEL_NONE = "none";
	/** Current user has read-only access to the calendar. */
	public static final String ACCESS_LEVEL_READ = "read";
	/**
	 * Current user can see only the free/busy information on the calendar, not
	 * the details of events.
	 */
	public static final String ACCESS_LEVEL_FREEBUSY = "freebusy";
	/**
	 * Current user has full edit access to the calendar, except that they can't
	 * change the calendar's access control settings.
	 */
	public static final String ACCESS_LEVEL_EDITOR = "editor";
	/** Current user has full owner access to the calendar. */
	public static final String ACCESS_LEVEL_OWNER = "owner";
	/**
	 * Available only in Google Apps domains. User is a domain administrator,
	 * and therefore has full owner access to the calendar regardless of access
	 * control settings.
	 */
	public static final String ACCESS_LEVEL_ROOT = "root";

	/**
	 * Category tag in Calendar feed
	 */
	@Key("category")
	public Category category;

	
	/**
	 * Indicates what level of access the current user (the one whose
	 * credentials are being used to request the metafeed) has to the calendar.
	 * Possible values are {@link #ACCESS_LEVEL_EDITOR},
	 * {@link #ACCESS_LEVEL_FREEBUSY}, {@link #ACCESS_LEVEL_NONE},
	 * {@link #ACCESS_LEVEL_OWNER}, {@link #ACCESS_LEVEL_READ} and
	 * {@link #ACCESS_LEVEL_ROOT}.
	 */
	@Key("gCal:accesslevel")
	public Value accessLevel;

	/**
	 * The color used to highlight a calendar in the user's browser. Must be one
	 * of the hexadecimal RGB color values listed <a href=
	 * "http://code.google.com/apis/calendar/data/2.0/reference.html#gCalcolor"
	 * >here</a>.
	 */
	@Key("gCal:color")
	public Value color;

	/**
	 * gCal:hidden tag in Calendar feed
	 */
	@Key("gCal:hidden")
	public Boolean hidden;

	/**
	 * gCal:selected tag in Calendar feed
	 */
	@Key("gCal:selected")
	public Boolean selected;

	/**
	 * gCal:timezone tag in Calendar feed
	 */
	@Key("gCal:timezone")
	public String timezone;

	/**
	 * Calls clone method in superclass
	 */
	  @Override
	  public CalendarEntry clone() {
	    return (CalendarEntry) super.clone();
	  }
	
	/**
	 * Returns the events feed link of the Entry.
	 * @return The events feed link of the Entry or {@code null} if not found.
	 */
	public String getEventsFeedLink() {
		return Link.find(links, EVENTS_FEED);
	}

	/**
	 * Returns the access control list link of the Entry.
	 * @return The access control list link of the Entry or {@code null} if not
	 *         found.
	 */
	public String getAccessControlListLink() {
		return Link.find(links, ACCESS_CONTROL_LIST);
	}
}
