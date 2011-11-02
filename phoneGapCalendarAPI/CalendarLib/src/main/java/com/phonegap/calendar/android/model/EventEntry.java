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

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;


/**
 * Represents a base GData EventEntry.
 * 
 * @author Alain Vongsouvanh
 * @author Sergio Martinez Rodriguez
 */
public class EventEntry extends Entry {

	  /** The event has been canceled */
	  public static final String EVENT_STATUS_CANCELED = "http://schemas.google.com/g/2005#event.canceled";
	  /** The event is planned */
	  public static final String EVENT_STATUS_CONFIRMED = "http://schemas.google.com/g/2005#event.confirmed";
	  /** The event is only tentatively scheduled */
	  public static final String EVENT_STATUS_TENTATIVE = "http://schemas.google.com/g/2005#event.tentative";

	  /**
	   * Indicates event consumes time on calendar; event time will be marked as
	   * busy in a free/busy search.
	   */
	  public static final String TRANSPARENCY_OPAQUE = "http://schemas.google.com/g/2005#event.opaque";
	  /**
	   * Indicates event doesn't consume any time on calendar; event time will not
	   * be marked as busy in a free/busy search.
	   */
	  public static final String TRANSPARENCY_TRANSPARENT = "http://schemas.google.com/g/2005#event.transparent";

	  /** Allow some readers to see the event */
	  public static final String VISIBILITY_CONFIDENTIAL = "http://schemas.google.com/g/2005#event.confidential";
	  /**
	   * Inherit the behavior from the preferences of the owner of the calendar on
	   * which the event appears.
	   */
	  public static final String VISIBILITY_DEFAULT = "http://schemas.google.com/g/2005#event.default";
	  /** Allow fewest readers to see the event. */
	  public static final String VISIBILITY_PRIVATE = "http://schemas.google.com/g/2005#event.private";
	  /** Allow most readers to see the event. */
	  public static final String VISIBILITY_PUBLIC = "http://schemas.google.com/g/2005#event.public";

	  /**
	   * Content attribute, represents the content tag info in GData Event
	   */
	  @Key("content")
	  public String content;

	  /**
	   * Comments attribute, represents the gd:comments tag info in GData Event
	   */
	  @Key("gd:comments")
	  public Comments comments;

	  /**
	   * Status of the event. Possible values are {@link #EVENT_STATUS_CANCELED},
	   * {@link #EVENT_STATUS_CONFIRMED} and {@link #EVENT_STATUS_TENTATIVE}.
	   */
	  @Key("gd:eventStatus")
	  public Value eventStatus;

	  /**
	   * Recurrence attribute, represents the gd:recurrence tag info in GData Event, is a string with rfc2445 Ical recurrence format
	   */
	  @Key("gd:recurrence")
	  public String recurrence;

	  /**
	   * Reminder attribute, represents the gd:reminder tag info in GData Event
	   */
	  @Key("gd:reminder")
	  private List<Reminder> reminders_;

	  /**
	   * Transparency of the event. corresponding to the TRANSP property defined in
	   * RFC 2445. Possible values are {@link #TRANSPARENCY_OPAQUE} and
	   * {@link #TRANSPARENCY_TRANSPARENT}.
	   */
	  @Key("gd:transparency")
	  public Value transparency;

	  /**
	   * Visibility of the event. Possible values are
	   * {@link #VISIBILITY_CONFIDENTIAL}, {@link #VISIBILITY_DEFAULT},
	   * {@link #VISIBILITY_PRIVATE} and {@link #VISIBILITY_PUBLIC}.
	   */
	  @Key("gd:visibility")
	  public Value visibility;

	  /**
	   * When attribute, represents the gd:when tag info in GData Event
	   */
	  @Key("gd:when")
	  public List<When> when;

	  /**
	   * where attribute, represents the gd:where tag info in GData Event
	   */
	  @Key("gd:where")
	  public List<Where> where;

	  /**
	   * who attribute, represents the gd:who tag info in GData Event
	   */
	  @Key("gd:who")
	  public List<Who> who;

	  /**
	   * TODO(alainv): Add description. Boolean, possible values are {@code true} or
	   * {@code false}.
	   */
	  @Key("gCal:anyoneCanAddSelf")
	  public Value anyoneCanAddSelf;

	  /**
	   * TODO(alainv): Add description. Boolean, possible values are {@code true} or
	   * {@code false}.
	   */
	  @Key("gCal:guestsCanInviteOthers")
	  public Value guestsCanInviteOthers;

	  /**
	   * TODO(alainv): Add description. Boolean, possible values are {@code true} or
	   * {@code false}.
	   */
	  @Key("gCal:guestsCanModify")
	  public Value guestsCanModify;

	  /**
	   * TODO(alainv): Add description. Boolean, possible values are {@code true} or
	   * {@code false}.
	   */
	  @Key("gCal:sendEventNotifications")
	  public Value sendEventNotifications;

	  /**
	   * TODO(alainv): Add description. Boolean, possible values are {@code true} or
	   * {@code false}.
	   */
	  @Key("gCal:guestsCanSeeGuests")
	  public Value guestsCanSeeGuests;

	  /**
	   * TODO(alainv): Add description. Integer.
	   */
	  @Key("gCal:sequence")
	  public Value sequence;

	  /**
	   * id attribute, represents the gCal:uid tag info in GData Event
	   */
	  @Key("gCal:uid")
	  public Value uid;

  public String getEventFeedLink() {
    return Link.find(links, "self");
  }

  @Override
  public EventEntry clone() {
    return (EventEntry) super.clone();
  }
  
  /**
   * Returns the event reminder.
   * 
   * @return The event reminder
   */
  public List<Reminder> getReminders() {
    if (recurrence != null && !recurrence.isEmpty()) {
      // recurrence event, g:reminder is in top level
      return reminders_;
    } else if (when.size() > 0) {
      // single event, g:reminder is inside g:when
      return when.get(0).reminders;
    } else
      return null;
  }

  /**
   * Adds a reminder into Event reminder list
   * @param reminder new reminder to be added
   */
  public void addReminder(Reminder reminder) {
    List<Reminder> reminders = null;

    if (recurrence != null && !recurrence.isEmpty()) {
      // recurrence event, g:reminder is in top level
      if (reminders_ == null)
        reminders = new ArrayList<Reminder>();
      reminders = reminders_;
    } else if (when.size() > 0) {
      // single event, g:reminder is inside g:when
      if (when.get(0).reminders == null)
        when.get(0).reminders = new ArrayList<Reminder>();
      reminders = when.get(0).reminders;
    }
    // TODO(alainv): add exception if reminders == null.
    reminders.add(reminder);
  }
  
}