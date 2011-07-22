package com.phonegap.calendar.android.model;

import com.google.api.client.util.Key;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Extends Feed and represents the event Feeds
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public class EventFeed extends Feed {
	
	/**
	 * List of EventEntries in feed, corresponding to all EventsEntries in the "Entry" tag in feed 
	 */
  @Key("entry")
  public List<EventEntry> events = Lists.newArrayList();
  
  /* (non-Javadoc)
 * @see com.phonegap.calendar.android.model.Feed#getEntries()
 */
@Override
  public List<EventEntry> getEntries() {
    return events;
  }
}