/*
 * Copyright (c) 2011 Google Inc.
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

package com.phonegap.calendar.android.core;

import android.util.Log;

import com.google.api.client.googleapis.xml.atom.AtomPatchRelativeToOriginalContent;
import com.google.api.client.googleapis.xml.atom.GoogleAtom;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.xml.atom.AtomContent;
import com.google.api.client.http.xml.atom.AtomFeedContent;
import com.google.api.client.http.xml.atom.AtomParser;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.phonegap.calendar.android.model.CalendarEntry;
import com.phonegap.calendar.android.model.CalendarFeed;
import com.phonegap.calendar.android.model.CalendarUrl;
import com.phonegap.calendar.android.model.Entry;
import com.phonegap.calendar.android.model.EventEntry;
import com.phonegap.calendar.android.model.EventFeed;
import com.phonegap.calendar.android.model.Feed;

import java.io.IOException;

/**
 * CalendarClient instances are the objects that can access to GDATA
 * services via Http request in order to execute the requested operation    
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public class CalendarClient {

  /** Whether to enable debugging. */
  public static final boolean DEBUG = true;

  static final XmlNamespaceDictionary DICTIONARY =
      new XmlNamespaceDictionary().set("", "http://www.w3.org/2005/Atom").set(
          "batch", "http://schemas.google.com/gdata/batch").set(
          "gd", "http://schemas.google.com/g/2005");


  /**
   * HttpRequestFactory that will provide the httpRequests we will need
   */
  private final HttpRequestFactory requestFactory;

  /**
   * This constructor initialize requestFactory attribute
   * @param requestFactory RequestFactory object
   */
  public CalendarClient(HttpRequestFactory requestFactory) {
    this.requestFactory = requestFactory;
  }

  /**
   * Adds an atom parser into the given HttpRequest object
   * @param request HttpRequest object in which new AtomParser will be added
   */
  public void initializeParser(HttpRequest request) {
    AtomParser parser = new AtomParser();
    parser.namespaceDictionary = DICTIONARY;
    request.addParser(parser);
  }
  
  /**
   * Called when the client has not more operation to perform
   * @throws IOException
   */
  public void shutdown() throws IOException {
	   //TODO implement this method
	  }

  /**
   * Performs the delete operation into user calendar
   * @param entry Calendar or Event Entry that will be deleted
   * @throws IOException
   */
  public void executeDelete(Entry entry) throws IOException {
    HttpRequest request = requestFactory.buildDeleteRequest(new GenericUrl(entry.getEditLink()));
    request.execute().ignore();
  }

  /**
   * Performs the Insert operation into user calendar
   * @param entry Calendar or Event Entry that will be created
   * @param url CalendarUrl in which the operation will be performed
   * @return entry Created entry
   * @throws IOException
   */
  Entry executeInsert(Entry entry, CalendarUrl url) throws IOException {
    AtomContent content = new AtomContent();
    content.namespaceDictionary = DICTIONARY;
    content.entry = entry;
    
    HttpRequest request = requestFactory.buildPostRequest(url, content);
    return request.execute().parseAs(entry.getClass());
  }

  /**
   * Performs the Update operation into user calendar
   * @param updated Updated Entry object
   * @param original Original Entry object
   * @return Updated entry Object
   * @throws IOException
   */
  Entry executePatchRelativeToOriginal(Entry updated, Entry original) throws IOException {
    AtomPatchRelativeToOriginalContent content = new AtomPatchRelativeToOriginalContent();
    content.namespaceDictionary = DICTIONARY;
    content.originalEntry = original;
    content.patchedEntry = updated;
    HttpRequest request =
        requestFactory.buildPatchRequest(new GenericUrl(updated.getEditLink()), content);
    return request.execute().parseAs(updated.getClass());
  }

  /**
   * Executes the operations in order to get calendar info from feeds  
   * @param <F> generic for type of feed we are requesting for
   * @param url CalendarUrl where the request will be executed
   * @param feedClass type of feed class we are requesting for
   * @return feed class with the requested information
   * @throws IOException
   */
  <F extends Feed> F executeGetFeed(CalendarUrl url, Class<F> feedClass) throws IOException {
    url.fields = GoogleAtom.getFieldsFor(feedClass);
    HttpRequest request = requestFactory.buildGetRequest(url);
    Log.i("REQUEST", url.toString());
    return request.execute().parseAs(feedClass);
  }
  
  /**
   * Creates several events into the provided calendar in CalendarEntry
   * @param eventFeed EventFeed including the events we want to create
   * @param calendar CalendarEntry CalendarEntry with calendar in which 
   * the events will be included 
   * @return EventFeed with the new events
   * @throws IOException
   */
	public EventFeed executeBatchEventFeed(EventFeed eventFeed,
			CalendarEntry calendar) throws IOException {
		// batch link
		CalendarUrl eventFeedUrl = new CalendarUrl(calendar.getEventsFeedLink());
		eventFeedUrl.maxResults = 0;
		CalendarUrl url = new CalendarUrl(executeGetEventFeed(eventFeedUrl)
				.getBatchLink());
		AtomFeedContent content = new AtomFeedContent();
		content.namespaceDictionary = DICTIONARY;
		content.feed = eventFeed;
		// execute request
		HttpRequest request = requestFactory.buildPostRequest(url, content);
		return request.execute().parseAs(EventFeed.class);
	}

	

	/**
	 * Performs the Insert operation for calendarEntries
	   * @param entry Calendar Entry that will be created
	   * @param url CalendarUrl in which the operation will be performed
	   * @return CalendarEntry object with Created entry
	 * @throws IOException
	 */
  public CalendarEntry executeInsertCalendar(CalendarEntry entry, CalendarUrl url)
      throws IOException {
    return (CalendarEntry) executeInsert(entry, url);
  }

  /**
   * Performs the Update operation for CalendarEntries into user calendar
   * @param updated Updated CalendarEntry object
   * @param original Original CalendarEntry object
   * @return Updated CalendarEntry Object
   * @throws IOException
   */
  public CalendarEntry executePatchCalendarRelativeToOriginal(
      CalendarEntry updated, CalendarEntry original) throws IOException {
    return (CalendarEntry) executePatchRelativeToOriginal(updated, original);
  }
  
  /**
   * Performs the Update operation for EventEntries into user calendar
   * @param updated Updated EventEntry object
   * @param original Original EventEntry object
   * @return Updated EventEntry Object
   * @throws IOException
   */  public EventEntry executePatchEventRelativeToOriginal(
		  EventEntry updated, EventEntry original) throws IOException {
	    return (EventEntry) executePatchRelativeToOriginal(updated, original);
	  }

   
   /**
    * Executes the operation for getting the info about User calendars
    * @param url CalendarUrl where the request will be executed
    * @return CalendarFeed with the retrieved info about user calendars 
    * @throws IOException
    */
  public CalendarFeed executeGetCalendarFeed(CalendarUrl url) throws IOException {
    return executeGetFeed(url, CalendarFeed.class);
  }
  
  /**
   * Executes the operation for getting the info about User calendar events
   * @param url CalendarUrl where the request will be executed
   * @return EventFeed with the retrieved info about user events 
   * @throws IOException
   */
  public EventFeed executeGetEventFeed(CalendarUrl url) throws IOException {
	    return executeGetFeed(url, EventFeed.class);
	  }
  
	/**
	 * Performs the Insert operation for EventEntries
	   * @param event Event Entry that will be created
	   * @param url CalendarUrl in which the operation will be performed
	   * @return EventEntry object with Created entry
	 * @throws IOException
	 */
  public EventEntry executeInsertEvent(EventEntry event, CalendarUrl url) throws IOException {
	    return (EventEntry) executeInsert(event, url);
	  }
}
