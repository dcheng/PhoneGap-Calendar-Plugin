/*
 *  Copyright 2011 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */

package com.phonegap.calendar.android.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.phonegap.calendar.android.core.CalendarClient;
import com.phonegap.calendar.android.core.CalendarOps;
import com.phonegap.calendar.android.model.Author;
import com.phonegap.calendar.android.model.CalendarEntry;
import com.phonegap.calendar.android.model.Category;
import com.phonegap.calendar.android.model.EventEntry;
import com.phonegap.calendar.android.model.Link;

import android.util.Log;


/**
 * This class represents a calendar object with all the info it can contains and the
 * operations we can execute over this object, most of them related with CRUD event operations.
 * It is also remarkable that we have methods in order to be able to transform this object into
 * a CalendarEntry object an viceversa
 * @author Sergio Martinez Rodriguez
 *
 */
public class Calendar implements Cloneable{

	private static final String TAG = "Calendars";
	
	/**
	 * Summary field in CalendarEntry 
	 */
	private String summary;
	
	/**
	 * updated field in CalendarEntry 
	 */
	private String updated;
	/**
	 * List of links of the calendar
	 */
	private List<Link> links;
	/**
	 * List of events that calendar contains
	 */
	private List<Event> events;
	/**
	 * Author of calendar
	 */
	private Author author;	  
	/**
	 * calendar id
	 */
	private String id;
	/**
	 * calendar title
	 */
	private String title;
	/**
	 * calendar category
	 */
	private Category category;
	/**
	 * calendar accessLevel
	 */
	private String accessLevel;
	/**
	 * calendar color attribute
	 */
	private String color;
	/**
	 * calendar hidden property 
	 */
	private Boolean hidden;
	/**
	 * calendar selected property 
	 */
	private Boolean selected;
	/**
	 * 
	 */
	private String timezone;
	/**
	 * Time zone of calendar
	 */
	private CalendarClient calendarClient;
	/**
	 * Google CalendarClient able to access the user accounts
	 */
	private CalendarEntry calendarEntry;
	
	/**
	 * Constructor using fields
	 * @param calendarClient Google CalendarClient able to access the user accounts
	 * @param summary Summary field in CalendarEntry
	 * @param updated updated field in CalendarEntry 
	 * @param links List of links of the calendar
	 * @param events List of events of the calendar
	 * @param author Author of calendar
	 * @param id calendar id
	 * @param title calendar title
	 * @param category calendar category
	 * @param accessLevel calendar accesslevel
	 * @param color calendar color attribute
	 * @param hidden calendar hidden property
	 * @param selected calendar selected property
	 * @param timezone Time Zone of calendar
	 */
	public Calendar(CalendarClient calendarClient, String summary, String updated, List<Link> links,
			List<Event> events, Author author, String id, String title,
			Category category, String accessLevel, String color,
			Boolean hidden, Boolean selected, String timezone) {
		super();
		this.calendarClient = calendarClient;
		this.summary = summary;
		this.updated = updated;
		this.links = links;
		this.events = events;
		this.author = author;
		this.id = id;
		this.title = title;
		this.category = category;
		this.accessLevel = accessLevel;
		this.color = color;
		this.hidden = hidden;
		this.selected = selected;
		this.timezone = timezone;
		this.calendarEntry = createCalendarEntry();
	}
	
	/**
	 * Creates an instance of Calendar with the given calendarEntry and calendarClient objects
	 * @param calendarEntry CalendarEntry object with calendar fields
	 * @param calendarClient Google CalendarClient able to access the user accounts 
	 */
	public Calendar(CalendarEntry calendarEntry, CalendarClient calendarClient) {
		this.calendarClient = calendarClient;
		this.summary = calendarEntry.summary;
		this.updated = calendarEntry.updated;
		this.links = calendarEntry.links;
		this.author = calendarEntry.author;
		this.id = calendarEntry.id;
		this.title = calendarEntry.title;
		this.category = calendarEntry.category;
		this.accessLevel = calendarEntry.accessLevel.value;
		this.color = calendarEntry.color.value;
		this.hidden = calendarEntry.hidden;
		this.selected = calendarEntry.selected;
		this.timezone = calendarEntry.timezone;
		this.calendarEntry = calendarEntry;
	}

	/**
	 * Returns the current object as CalendarEntry instance
	 * @return CalendarEntry instance of Calendar instance 
	 */
	private CalendarEntry createCalendarEntry(){
		
		CalendarEntry calendarEntry = new CalendarEntry();
		calendarEntry.summary = this.summary;
		calendarEntry.updated = this.updated;
		calendarEntry.links = this.links;
		calendarEntry.author = this.author;
		calendarEntry.id = this.id;
		calendarEntry.title = this.title;
		calendarEntry.category = this.category;
		calendarEntry.accessLevel.value = this.accessLevel;
		calendarEntry.color.value = this.color;
		calendarEntry.hidden = this.hidden;
		calendarEntry.selected = this.selected;
		calendarEntry.timezone = this.timezone;
		
		return calendarEntry;
	}
	
	/**
	 * Creates an event into the user calendar represented by the current object
	 * @param event Event instance that will be created
	 * @return True if success, false otherwise
	 */
	public boolean createEvent(Event event){
		try{
			CalendarOps.addEvent(calendarClient, getCalendarEntry(), event.getEventEntry());		
		}catch(Exception exception){
			exception.printStackTrace();
			return false;
		}	    
		return true;
	}
	
	/**
	 * Deletes an event into the user calendar represented by the current object
	 * @param event Event instance that will be deleted
	 * @return True if success, false otherwise
	 */
	public boolean deleteEvent(Event event){
		
		try{
			CalendarOps.deleteEvent(calendarClient, event.getEventEntry());
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Updates an event into the user calendar represented by the current object
	 * @param event Event instance with modifications
	 * @param original Event instance
	 * @return True if success, false otherwise
	 */
	public boolean updateEvent(Event event, Event original){
		
		try{
			EventEntry result = CalendarOps.updateEvent(calendarClient, event.getEventEntry(), original.getEventEntry());
			if (result== null ) throw new NullPointerException("Not updated event:" + original.getTitle());
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Retrieves all events into the user calendar represented by the current object
	 * @return List of all events into the user calendar represented by the current object
	 */
	public List<Event> getCalendarAllEventsList(){
		
		List<Event> result = new ArrayList<Event>();
		result = findCalendarEventsBydate(null, null);

		return result;
	}
	
	/**
	 * Retrieves filtered events by provided dates into the user calendar represented by the current object
	 * @param minStart Date object with the minimum start date (if null, not filter)
	 * @param maxStart Date object with the maximum start date (if null, not filter)
	 @return List of all matching events into the user calendar represented by the current object
	 */
	public List<Event> findCalendarEventsBydate(Date minStart, Date maxStart) {

		List<Event> result = new ArrayList<Event>();
		List<EventEntry> events = CalendarOps.findUserEvents(calendarClient, minStart, maxStart);

		for (EventEntry eventEntry : events) {
			result.add(new Event(eventEntry));
		}
		return result;
	}
	
	
	
	/**
	 * Add events into the current Calendar object (not in the User calendar) 
	 * @param events List of events will be added
	 */
	public void addEvents(List<Event> events){
		if (this.events == null){
			this.events = new ArrayList<Event>();
		}
		this.events.addAll(events);
	}
	
	/**
	 * Returns Google CalendarClient object able to access the user accounts
	 * @return Google CalendarClient able to access the user accounts
	 */
	public CalendarClient getCalendarClient() {
		return calendarClient;
	}

	/**
	 * sets Google CalendarClient able to access the user accounts
	 * @param calendarClient Google CalendarClient able to access the user accounts
	 */
	public void setCalendarClient(CalendarClient calendarClient) {
		this.calendarClient = calendarClient;
	}

	/**
	 * Gets the calendarEntry object attribute
	 * @return CalendarEntry object
	 */
	public CalendarEntry getCalendarEntry() {
		return calendarEntry;
	}

	/**
	 * Sets the calendarEntry object attribute
	 * @param CalendarEntry CalendarEntry object
	 */
	public void setCalendarEntry(CalendarEntry calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	/**
	 * Gets the summary attribute
	 * @return summary String  
	 */
	public String getSummary() {
		return summary;
	}
	
	/**
	 * Sets the summary attribute
	 * @param summary String
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	/**
	 * Gets the updated attribute
	 * @return updated String
	 */
	public String getUpdated() {
		return updated;
	}
	/**
	 * Sets the updated attribute
	 * @param updated String
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	/**
	 * Gets the Links Attribute
	 * @return List of Links 
	 */
	public List<Link> getLinks() {
		return links;
	}
	/**
	 * Sets the Links Attribute
	 * @param links List of Links
	 */
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	/**
	 * Gets the Events Attribute
	 * @return List of Events 
	 */
	public List<Event> getEvents() {
		return events;
	}
	
	/**
	 * Sets the Events attribute
	 * @param events List of Events
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	/**
	 * Gets the author attribute
	 * @return Author object
	 */
	public Author getAuthor() {
		return author;
	}
	/**
	 * Gets the author attribute
	 * @param author Author object 
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	/**
	 * Gets id attribute
	 * @return id String
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets id Attribute
	 * @param id id String
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Gets title attribute 
	 * @return title String 
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Sets title attribute
	 * @param title title String
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Get category attribute
	 * @return category Category
	 */
	public Category getCategory() {
		return category;
	}
	/**
	 * Sets the category attribute
	 * @param category Category object
	 */
	public void setCategory(Category category) {
		this.category = category;
	}
	/**
	 * Gets the accessLevel attribute 
	 * @return accessLevel String
	 */
	public String getAccessLevel() {
		return accessLevel;
	}
	/**
	 * Sets the accessLevel attribute
	 * @param accessLevel accessLevel String
	 */
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	/**
	 * Gets color attribute
	 * @return color String 
	 */
	public String getColor() {
		return color;
	}
	/**
	 * Sets color Attribute
	 * @param color color String
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * Gets hidden attribute state
	 * @return hidden boolean
	 */
	public Boolean getHidden() {
		return hidden;
	}
	/**
	 * Sets hidden attribute state
	 * @param hidden hidden boolean
	 */
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	/**
	 * Gets selected attribute state
	 * @return selected boolean
	 */
	public Boolean getSelected() {
		return selected;
	}
	/**
	 * Sets selected attribute state
	 * @param selected selected boolean
	 */
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	/**
	 * Gets the timezone String
	 * @return Timezone String
	 */
	public String getTimezone() {
		return timezone;
	}
	/**
	 * Sets timeZone attribute
	 * @param timezone Timezone String
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
		
}

