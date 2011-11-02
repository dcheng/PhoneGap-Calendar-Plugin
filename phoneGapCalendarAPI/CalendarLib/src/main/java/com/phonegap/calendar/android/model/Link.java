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

import java.util.List;

/**
 * @author Yaniv Inbar
 * @author Sergio Martinez Rodriguez
 */
public class Link {

	/**
	 * href string corresponding to href tag in Link
	 */
  @Key("@href")
  public String href;

  /**
   * type string corresponding to type tag in Link
   */
  @Key("@type")
  public String type;
  
  /**
   * rel string corresponding to rel tag in Link
   */
  @Key("@rel")
  public String rel;

  /**
   * Finds the href url for the given links that matches given rel string
   * @param links List of Links objects
   * @param rel string with rel type
   * @return href string 
   */
  public static String find(List<Link> links, String rel) {
    if (links != null) {
      for (Link link : links) {
        if (rel.equals(link.rel)) {
          return link.href;
        }
      }
    }
    return null;
  }
}
