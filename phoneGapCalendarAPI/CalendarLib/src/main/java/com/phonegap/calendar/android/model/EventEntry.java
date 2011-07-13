package com.phonegap.calendar.android.model;

import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;


/**
 * Represents a base GData Event Entry.
 * 
 * @author Alain Vongsouvanh
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

	  @Key("content")
	  public String content;

	  @Key("gd:comments")
	  public Comments comments;

	  /**
	   * Status of the event. Possible values are {@link #EVENT_STATUS_CANCELED},
	   * {@link #EVENT_STATUS_CONFIRMED} and {@link #EVENT_STATUS_TENTATIVE}.
	   */
	  @Key("gd:eventStatus")
	  public Value eventStatus;

	  @Key("gd:recurrence")
	  public String recurrence;

	  @Key("gd:reminder")
	  private List<Reminder> reminders_;

	  /**
	   * Transparency of the event. corresponding to the TRANSP property defined in
	   * RFC 2445. Possible values are {@link #TRANSPARENCY_OPAQUE} and
	   * {@link Event#TRANSPARENCY_TRANSPARENT}.
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

	  @Key("gd:when")
	  public List<When> when;

	  @Key("gd:where")
	  public List<Where> where;

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