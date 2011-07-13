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

package com.phonegap.calendar.android.model;

import com.google.api.client.util.Key;

/**
 * Represents a logically nested feed. For example, a calendar feed might have a
 * nested feed representing all comments on entries.
 * <p>
 * <h2>Restrictions</h2>
 * <ul>
 * <li>Either or both of {@link #url} or {@link #feed} must be present.</li>
 * <li>If atom:feed is not present, the client can fetch the contents from the
 * URI in {@link #url}. If it is present, the contents must the same as what
 * would be retrieved from {@link #url} at the time the feed was generated.</li>
 * <li>When a PUT or a POST contains a <gd:feedLink> and the {@link #url}
 * attribute is specified, the attribute's value is used to create a link; if
 * the {@link #feed} element is also present, it is ignored. If the {@link #url}
 * attribute is not specified, then the {@link #feed} element is stored as an
 * embedded feed. Note that some services may not support all of these options.</li>
 * </ul>
 * 
 * @author Nicolas Garnier
 */
public class FeedLink {

  /**
   * Specifies the feed URI. If the nested feed is embedded and not linked, this
   * attribute may be omitted.
   */
  @Key("@href")
  public String url;

}