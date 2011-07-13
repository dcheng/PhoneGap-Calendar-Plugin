package com.phonegap.calendar.android.model;

import com.google.api.client.util.Key;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Yaniv Inbar
 */
public class EventFeed extends Feed {

  @Key("entry")
  public List<EventEntry> events = Lists.newArrayList();
  
  @Override
  public List<EventEntry> getEntries() {
    return events;
  }
}