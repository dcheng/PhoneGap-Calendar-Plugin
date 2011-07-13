package com.phonegap.calendar.android.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.phonegap.calendar.android.core.CalendarOps;
import com.phonegap.calendar.android.model.Author;
import com.phonegap.calendar.android.model.CalendarClient;
import com.phonegap.calendar.android.model.CalendarEntry;
import com.phonegap.calendar.android.model.Category;
import com.phonegap.calendar.android.model.EventEntry;
import com.phonegap.calendar.android.model.Link;

import android.util.Log;



public class Calendar implements Cloneable{

	private static final String TAG = "Calendars";
	
	private String summary;
	private String updated;
	private List<Link> links;
	private List<Event> events;
	private Author author;	  
	private String id;
	private String title;
	private Category category;
	private String accessLevel;
	private String color;
	private Boolean hidden;
	private Boolean selected;
	private String timezone;
	private CalendarClient calendarClient;
	private CalendarEntry calendarEntry;
	
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
	
	public boolean createEvent(Event event){
		try{
			CalendarOps.addEvent(calendarClient, getCalendarEntry(), event.getEventEntry());		
		}catch(Exception exception){
			exception.printStackTrace();
			return false;
		}	    
		return true;
	}
	
	public boolean deleteEvent(Event event){
		
		try{
			CalendarOps.deleteEvent(calendarClient, event.getEventEntry());
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return true;
	}
	
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
	
	public List<Event> getCalendarAllEventsList(){
		
		List<Event> result = new ArrayList<Event>();
		result = findCalendarEventsBydate(null, null);

		return result;
	}
	
	public List<Event> findCalendarEventsBydate(Date minStart, Date maxStart) {

		List<Event> result = new ArrayList<Event>();
		List<EventEntry> events = CalendarOps.findUserEvents(calendarClient, minStart, maxStart);

		for (EventEntry eventEntry : events) {
			result.add(new Event(eventEntry));
		}
		return result;
	}
	
	
	
	public boolean addEvents(List<Event> events){
		this.events.addAll(events);
		return false;
	}
	
	public CalendarClient getCalendarClient() {
		return calendarClient;
	}

	public void setCalendarClient(CalendarClient calendarClient) {
		this.calendarClient = calendarClient;
	}

	public CalendarEntry getCalendarEntry() {
		return calendarEntry;
	}

	public void setCalendarEntry(CalendarEntry calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	
	
}

