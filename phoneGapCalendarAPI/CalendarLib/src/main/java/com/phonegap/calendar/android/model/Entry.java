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

import com.google.api.client.util.Data;
import com.google.api.client.util.Key;

import java.util.List;

/**
 * @author Yaniv Inbar
 * @author Sergio Martínez Rodríguez
 */
public class Entry implements Cloneable {

	/**
	 * summary string corresponding to summary tag in Entry
	 */
  @Key
  public String summary;

  /**
	 * updated String corresponding to updated tag in Entry
	 */
  @Key
  public String updated;

  /**
	 * List of links corresponding to links tags in Entry
	 */
  @Key("link")
  public List<Link> links;

  /**
	 * Author object corresponding to author tag in Entry
	 */
  @Key("author")
  public Author author;
  
  /**
   * id String corresponding to id tag in Entry
   */
  @Key
  public String id;

  /**
   * title String corresponding to title tag in Entry
   */
  @Key("title")
  public String title;
  
  /**
   * Returns the self link of the Entry.
   * 
   * @return The self link of the Entry or {@code null} if not found.
   */
public String getSelfLink() {
    return Link.find(links, "self");
  }
  
  /* (non-Javadoc)
 * @see java.lang.Object#clone()
 */
@Override
  protected Entry clone() {
    try {
      @SuppressWarnings("unchecked")
      Entry result = (Entry) super.clone();
      Data.deepCopy(this, result);
      return result;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Find liks with the "edit" value in the "rel" field
   * @return String with the href link
   */
public String getEditLink() {
    return Link.find(links, "edit");
  }
}
