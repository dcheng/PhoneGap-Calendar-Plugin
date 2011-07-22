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

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Abstract class, represents the basic feed, all feeds should extend this
 * class, like EventFeed and CalendarFeed  
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public abstract class Feed {

	/**
	 * List of links in feed, corresponding to all links in the "link" tag in feed 
	 */
  @Key("link")
  public List<Link> links;

  /**
   * Gets the next Link
   * @return Next link as String
   */
  public String getNextLink() {
    return Link.find(links, "next");
  }

  /**
   * Gets bacht link
   * @return String with bacth link
   */
  public String getBatchLink() {
    return Link.find(links, "http://schemas.google.com/g/2005#batch");
  }
  
  /**
   * Returns the list of Entries in the feed
   * @return List of Entries in the feed
   */
  public abstract List<? extends Entry> getEntries();
}
