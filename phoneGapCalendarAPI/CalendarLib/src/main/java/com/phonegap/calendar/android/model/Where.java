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
 * A place (such as an event location) associated with the containing entity.
 * The type of the association is determined by the {@link #rel} attribute; the details
 * of the location are contained in an embedded or linked-to Contact entry. A
 * {@link Where} element is more general than a {@link GeoPt} element. The former
 * identifies a place using a text description and/or a Contact entry, while the
 * latter identifies a place using a specific geographic location.
 * 
 * @since 2.2
 * @author Nicolas Garnier
 * @author Sergio Martinez Rodriguez
 */
public class Where {

  /** Place where the enclosing event takes place. */
  public static final String REL_EVENT = "http://schemas.google.com/g/2005#event";

  /**
   * A secondary location. For example, a remote site with a videoconference
   * link to the main site.
   */
  public static final String REL_EVENT_ALTERNATE = "http://schemas.google.com/g/2005#event.alternate";

  /** A nearby parking lot. */
  public static final String REL_EVENT_PARKING = "http://schemas.google.com/g/2005#event.parking";

  /**
   * Specifies a user-readable label to distinguish this location from other
   * locations.
   */
  @Key("@label")
  public String label;

  /**
   * Specifies the relationship between the containing entity and the contained
   * location. Possible values (see below) are defined by other elements. For
   * example, {@link When} defines {@link #REL_EVENT}. Valid values are {@link #REL_EVENT}, {@link #REL_EVENT_ALTERNATE} and {@link #REL_EVENT_PARKING}.
   */
  @Key("@rel")
  public String rel;

  /**
   * A simple string value that can be used as a representation of this
   * location.
   */
  @Key("@valueString")
  public String description;

}
