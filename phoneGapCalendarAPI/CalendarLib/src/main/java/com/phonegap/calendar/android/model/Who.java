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

/**
* A person associated with the containing entity. The type of the association
* is determined by the {@link #rel} attribute; the details about the person are
* contained in an embedded or linked-to Contact entry.
* <p>
* The {@link Who} element can be used to specify email senders and recipients,
* calendar event organizers, and so on.
* 
* @author Alain Vongsouvanh
* @author Nicolas Garnier
* @author Sergio Martinez Rodriguez
*/
public class Who {

	/**
	 *  STATUS_ACCEPTED constant URL
	 */
 public static final String STATUS_ACCEPTED = "http://schemas.google.com/g/2005#event.accepted";
 /**
  * STATUS_DECLINED constant URL
  */
 public static final String STATUS_DECLINED = "http://schemas.google.com/g/2005#event.declined";
 /**
  * STATUS_INVITED constant URL
  */
 public static final String STATUS_INVITED = "http://schemas.google.com/g/2005#event.invited";
 /**
  * STATUS_TENTATIVE constant URL
  */
 public static final String STATUS_TENTATIVE = "http://schemas.google.com/g/2005#event.tentative";
/**
 * TYPE_OPTIONAL constant URL
 */
 public static final String TYPE_OPTIONAL = "http://schemas.google.com/g/2005#event.optional";
 /**
  * TYPE_REQUIRED constant URL
  */
 public static final String TYPE_REQUIRED = "http://schemas.google.com/g/2005#event.required";

 /**
  * Email address. This property is typically used when {@link EntryLink} is
  * not specified. The address must comply with <a
  * href="http://www.faqs.org/rfcs/rfc2822.html">RFC 2822</a>, section 3.4.1.
  */
 @Key("@email")
 public String email;

 /**
  * Specifies the relationship between the containing entity and the contained
  * person. See below for possible values.
  */
 @Key("@rel")
 public String rel;

 /** A simple string value that can be used as a representation of this person. */
 @Key("@valueString")
 public String valueString;

 /**
  * Status of event attendee. Valid values are {@link #STATUS_ACCEPTED},
  * {@link #STATUS_DECLINED}, {@link #STATUS_INVITED} and
  * {@link #STATUS_TENTATIVE}.
  */
 @Key("gd:attendeeStatus")
 public AttendeeStatus attendeeStatus;

}
