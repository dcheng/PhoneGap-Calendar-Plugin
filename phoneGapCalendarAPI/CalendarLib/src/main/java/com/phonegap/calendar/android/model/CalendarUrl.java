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

package com.phonegap.calendar.android.model;

import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.util.Key;
import com.phonegap.calendar.android.core.CalendarClient;

/**
 * With this class we can instanciate a CalendarUrl object and
 * later obtain the requested feeds in each method corresponding to
 * the operation we want perform
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public class CalendarUrl extends GoogleUrl {

  public static final String ROOT_URL = "https://www.google.com/calendar/feeds";

  /**
   * Parameter max-results in CalendarUrl object, 
   * represents the maximum results we can get 
   * for the requested operation
   */
  @Key("max-results")
  public Integer maxResults;

  /**
   * Parameter start-min in CalendarUrl object, represents the 
   * minimum start date of event for the requested search
   */
  @Key("start-min")
  public String startMin;
  
  /**
   * Parameter start-max in CalendarUrl object, represents the 
   * maximum start date of event for the requested search
   */
  @Key("start-max")
  public String startMax;
  
  /**
   * Parameter q in CalendarUrl object, represents that the request
   * performed is going to be like a query at calendar asking just for
   * the events containing the given value for q as search parameter 
   */
  @Key("q")
  public String title;
  

  /**
   * Constructor using an URL String 
   * @param url String with the provided URL
   */
  public CalendarUrl(String url) {
    super(url);
    if (CalendarClient.DEBUG) {
      this.prettyprint = true;
    }
  }

  /**
   * Root user calendar Url
   * @return CalendarUrl
   */
  private static CalendarUrl forRoot() {
    return new CalendarUrl(ROOT_URL);
  }

  /**
   * Default calendar Url
   * @return CalendarUrl
   */
  public static CalendarUrl forCalendarMetafeed() {
    CalendarUrl result = forRoot();
    result.pathParts.add("default");
    return result;
  }

  /**
   * Url for getting all user's calendars
   * @return CalendarUrl
   */
  public static CalendarUrl forAllCalendarsFeed() {
    CalendarUrl result = forCalendarMetafeed();
    result.pathParts.add("allcalendars");
    result.pathParts.add("full");
    return result;
  }

  /**
   * Url for getting owned user's calendars
   * @return CalendarUrl
   */
  public static CalendarUrl forOwnCalendarsFeed() {
    CalendarUrl result = forCalendarMetafeed();
    result.pathParts.add("owncalendars");
    result.pathParts.add("full");
    return result;
  }

  /**
   * Url for getting provided user's calendar events with specified parameters
   * @param userId authenticathed user
   * @param visibility visibility
   * @param  projection projection
   * @return CalendarUrl
   */
  public static CalendarUrl forEventFeed(String userId, String visibility, String projection) {
    CalendarUrl result = forRoot();
    result.pathParts.add(userId);
    result.pathParts.add(visibility);
    result.pathParts.add(projection);
    return result;
  }

  /**
   * Url for getting all authenticathed user events
   * @return CalendarUrl
   */
  public static CalendarUrl forDefaultPrivateFullEventFeed() {
    return forEventFeed("default", "private", "full");
  }
 
  /**
   *  Url for getting authenticathed user events with title search param
   * @return CalendarUrl
   */
  public static CalendarUrl forDefaultPrivateFullEventFeedueriedByTittle(String title) {
	    CalendarUrl result = forDefaultPrivateFullEventFeed();
	    result.title = (title);
	    return result;
	  }
  
  /**
   * Url for getting authenticathed user events between the given dates
   * @param startMin min startDate for retrieved events
   * @param startMax max startDate for retrieved events
   * @return CalendarUrl
   */
  public static CalendarUrl forDefaultPrivateFullEventFeedBetweenDates(String startMin, String startMax) {
	    CalendarUrl result = forDefaultPrivateFullEventFeed();
	    if (startMin!=null)
	    	result.startMin=startMin;
	    if (startMax!=null)
	    	result.startMax=startMax;
	    return result;
	  }
  
}
