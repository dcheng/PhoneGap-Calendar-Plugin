/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


/*
 *  Modified by Sergio Martinez. Copyright 2011 Vodafone Group Services Ltd.
 * 
 */

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