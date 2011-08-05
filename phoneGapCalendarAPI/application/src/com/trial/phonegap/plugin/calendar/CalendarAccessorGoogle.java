package com.trial.phonegap.plugin.calendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.util.DateTime;
import com.phonegap.calendar.android.adapters.Calendar;
import com.phonegap.calendar.android.adapters.CalendarsManager;
import com.phonegap.calendar.android.adapters.Dt;
import com.phonegap.calendar.android.adapters.Event;
import com.phonegap.calendar.android.adapters.Recurrence;
import com.phonegap.calendar.android.adapters.Rule;
import com.phonegap.calendar.android.model.EventEntry;
import com.phonegap.calendar.android.model.Reminder;
import com.phonegap.calendar.android.model.When;
import com.phonegap.calendar.android.model.Where;
import com.phonegap.calendar.android.utils.DateUtils;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;


/**
 * An implementation of {@link CalendarAccessor} that uses Google Calendar API.
 * <p>
 * This implementation has several features:
 * <ul>
 * <li>It sees only contacts from the first account found on Google Calendar.
 * <li>It uses an own library to access to Google Calendar.
 * <li>It has an instance variable it used either to call api functions or like
 *     a caché memory with events informations from the last finding
 * </ul>
 */
public class CalendarAccessorGoogle extends CalendarAccessorCreator {

	


	/**
	 * Is a the TAG log for logging
	 */
	private static final String TAG = "[Android:CalendarAccessorCreator.java]";

	/**
	 * This String is used when a parameter given from the interface is null
	 */
	private static final String NOT_SPECIFIED_PARSE_CONSTANT = "  - N / D -  ";
	/**
	 * This String is used when a parameter given from the interface is null
	 */
	private static final String SHORT_NOT_SPECIFIED_PARSE_CONSTANT = "N/D";
	/**
	 * Transparency opaque
	 */
	private static final String TRANSPARENCY_OPAQUE = "Opaque";
	/**
	 * Transparency Transparent
	 */
	private static final String TRANSPARENCY_TRANSPARENT = "Transparent";
	/**
	 * UNKNOWN is used when the Transparency and status  parameter given from the interface is null
	 */
	private static final String UNKNOWN = "Unknown";
	/**
	 *  Event status confirmed
	 */
	private static final String EVENT_STATUS_CONFIRMED = "Confirmed";
	/**
	 * Event status Cacelled
	 */
	private static final String EVENT_STATUS_CANCELED = "Cancelled";
	/**
	 * Event status tentative
	 */
	private static final String EVENT_STATUS_TENTATIVE = "Tentative";

	/**
	 * Is a instance variable of CalendarManager used to retrieve the user calendars
	 */
	private CalendarsManager calendarsManager;
	/**
	 * Is a instance variable of Calendar used to find, save and create events into a calendar
	 */
	private Calendar calendar;
	
	 private static final List<String> dbEventCalendarList = new LinkedList<String>();
	 static{
		 dbEventCalendarList.add("id");
		 dbEventCalendarList.add("description");
		 dbEventCalendarList.add("location");
		 dbEventCalendarList.add("summary");
		 dbEventCalendarList.add("start");
		 dbEventCalendarList.add("end");
		 dbEventCalendarList.add("status");
		 dbEventCalendarList.add("transparency");
		 dbEventCalendarList.add("recurrence");
		 dbEventCalendarList.add("reminder");
	 }

	/**
	 * Constructor of CalendarAccessorGoogle
	 * @param view
	 * @param app
	 */
	public CalendarAccessorGoogle(WebView view, Activity app) {
		mApp = app;
		mView = view;

		calendarsManager = new CalendarsManager(app);

		List<Calendar> calendars = calendarsManager.getUserCalendars();

		calendar = calendars.get(0);
	}

	/* (non-Javadoc)
	 * @see com.trial.phonegap.plugin.calendar.CalendarAccessorCreator#find(org.json.JSONObject)
	 */
	@Override
	public JSONArray find(JSONObject options) {

		Date dateStartAfter = null;
		Date dateStartBefore = null;
		Date dateEndAfter = null;
		Date dateEndBefore = null;
		JSONArray events = new JSONArray();
		List<Event> eventList = null;
		try {

			JSONObject filter = options.getJSONObject("filter");
			
			//Loggers
			Log.i(TAG, "Date After: " + filter.getString("startAfter"));
			Log.i(TAG, "Date Before: " + filter.getString("startBefore"));
			Log.i(TAG, "Date After: " + filter.getString("endAfter"));
			Log.i(TAG, "Date Before: " + filter.getString("endBefore"));

			
			//obtain date objets for finding event given by parameters
			dateStartAfter = DateUtils.stringCalendarDateToDateGTM(filter.getString("startAfter"), "yyyy-MM-dd HH:mm:ss");
			dateStartBefore = DateUtils.stringCalendarDateToDateGTM(filter.getString("startBefore"), "yyyy-MM-dd HH:mm:ss");
			dateEndAfter = DateUtils.stringCalendarDateToDateGTM(filter.getString("endAfter"), "yyyy-MM-dd HH:mm:ss");
			dateEndBefore = DateUtils.stringCalendarDateToDateGTM(filter.getString("endBefore"), "yyyy-MM-dd HH:mm:ss");
			
			//remove date parameter from filter 
		    filter.remove("startAfter");
		    filter.remove("startBefore");
		    filter.remove("endAfter");
		    filter.remove("endBefore");
		    
			// Google Calendar only provides one method for searching events, dates since dateStartAfter until dateStartBefore, 
			// so we have to configure and adapt the search with the others options, using this method

			// if dateStartAfter is after than dateEndAfter, the search from dateStarAfter 
			// will include all events which end be after dateEndAfter
			if ((dateStartAfter != null) && (dateEndAfter != null) && dateStartAfter.after(dateEndAfter)){
				Log.i(TAG, "Flow enters in : CHANGE dateStartAfter  after than dateEndAfter");
				dateEndAfter = null;
			}			
			
			// if dateStartBefore is after than dateEndbefore, we will search until dateEndBefore
			if((dateStartBefore != null) && (dateEndBefore != null) && dateStartBefore.after(dateEndBefore)){
				Log.i(TAG, "Flow enters in : CHANGE  dateStartBefore  after than dateEndBefore");
				dateStartBefore = dateEndBefore;
			}
			
			//The Find only does with three dates (dateEndBefore and dateStartBefore and dateStartAfter), because with dateEndAfter 
			//like startAfter don't ensures a correct finding
			
			//If dateStartAfter is after than dateStartBefore, No dates matching
			if ((dateStartAfter != null) && (dateStartBefore != null) && dateStartAfter.after(dateStartBefore)){
				Log.i(TAG, "Flow enters in : dateStartAfter  after than dateStartBefore");
				return events;
			//If dateEndAfter is after than dateEndBefore, No dates matching
			}else if ((dateEndAfter != null) && (dateEndBefore != null) && dateEndAfter.after(dateEndBefore)){
				Log.i(TAG, "Flow enters in : dateEndAfter  after than dateEndBefore");
				return events;
			//If dateStartBefore is not null then finds by dateStartBefore like dateStartBefore 
			}else if (dateStartBefore != null){
				Log.i(TAG, "Flow enters in: dateStartBefore");
				eventList = calendar.findCalendarEventsBydate(dateStartAfter, dateStartBefore);
			//If dateEndBefore is not null and dateStartBefore is null then finds by dateEndBefore like dateStartBefore 
			}else if (dateEndBefore != null){
				Log.i(TAG, "Flow enters in : dateEndBefore");
				eventList = calendar.findCalendarEventsBydate(dateStartAfter, dateEndBefore);
			// If dateEndBefore and dateStartBefore	are null, and dateStartAfter is not null the find only by this date
			}else if (dateStartAfter != null){
				Log.i(TAG, "Flow enters in : dateStartAfter");
				eventList = calendar.findCalendarEventsBydate(dateStartAfter, dateStartBefore);
			//Othewise return all Events	
			} else {
				Log.i(TAG, "Flow enters in : Every event");
				eventList = calendar.getCalendarAllEventsList();
			}
			
			//After searching the google calendar by start event dates, the result of the searching must be 
			//filtered by the end of event dates
			eventList = filterEventsByEnd(eventList, dateEndBefore, dateEndAfter);
			
			//Filter the events by remaining parameters.
			if (filter.length() != 0){
				Log.i(TAG,"Start events filtering  by " + filter.length() + " remaining parameters");
				
				JSONArray names = filter.names();			
				for(int i = 0; i < names.length();i++){
					Log.i(TAG,"Filter by '" + names.getString(i) + "' parameter ,with value '" +filter.getString(names.getString(i)) + "'");
					if (dbEventCalendarList.contains(names.getString(i))){
						if (!filter.isNull(names.getString(i))){					
							eventList = filterByParameter(eventList,names.getString(i), filter.getString(names.getString(i)));
						}
					}else{
						throw new Throwable("Parameter '" + names.getString(i) + "' doesn't exist on CalendarEvent");
					}					
				}
			}else{
				Log.i(TAG,"No more remaining parameters");
			}
					
			
			//Finally transforms an eventList on a JSONArray
			int i = 0;
			for (Event event : eventList) {
				i++;
				events.put(eventToJsonEvent(event));
				Log.i(TAG, "Evento [" + i + "]: " + event.getTitle());
			}
			
			calendar.addEvents(eventList);

		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			//TODO Manage exception
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			//TODO Manage exception
		}
		
		return events;
	}
	
	/**
	 * Filters every event of a list events by the given parameter.
	 * @param eventList The list of events to filter.
	 * @param nameParameter String with the name of the parameter for filtering the events list.
	 * @param valueParameter String with the parameter value. 
	 * @return A List of events filtered
	 */
	private List<Event>  filterByParameter(List<Event> eventList, String nameParameter, String valueParameter) {	
		List<Event> eventToRemove = new ArrayList<Event>();
		
		for(Event event : eventList){		
			if(!isParameterInEvent(event,nameParameter,valueParameter)){
				eventToRemove.add(event);
			}
		}
		
		for(Event event : eventToRemove){		
			eventList.remove(event);
		}
		return eventList;		
	}
	
	/**
	 * Returns a boolean with true value if the event given as parameter contain the value of the parameter also given as parameter
	 * @param event Event object
	 * @param nameParameter String with the name of the parameter to check the value into the event given.
	 * @param valueParameter String with the value for checking in the corresponding parameter
	 * @return A boolean 
	 */
	public boolean isParameterInEvent(Event event, String nameParameter, String valueParameter){		
		boolean res = false;		
		if (nameParameter.equals("description")){
			res = valueParameter.equals(event.getTitle());
		}else if (nameParameter.equals("summary")){
			res = valueParameter.equals(event.getSummary());
		}else if (nameParameter.equals("status")){		
			res = valueParameter.equals(event.getEventStatus());
		}else if (nameParameter.equals("transparency")){			
			res = valueParameter.equals(event.getTransparency());
		}else if (nameParameter.equals("reminder")){		
			for (When when : event.getWhen()){			
				for(Reminder reminder : when.reminders){
					if ((valueParameter.equals(reminder.method)) ||
						(valueParameter.equals(String.valueOf(reminder.minutes)))) {
						res = true;
						break;
					}
				}				
			}
		}else if (nameParameter.equals("location")){
			for (Where where : event.getWhere()){
				if (valueParameter.equals(where.description)){
					res = true;
					break;
				}
			}
		}else{
			res = false;
		}
		return res;
	 }


	/**
	 * this method filter the evenList given by parameter . Events whose ends are Before than dateEndBefore and After than dateEndAfter
	 * @param eventList
	 * @param dateEndBefore 
	 * @param dateEndAfter
	 * @return A list of events filtered by dateEndBefore and dateEndAfter
	 */
	private List<Event> filterEventsByEnd(List<Event> eventList, Date dateEndBefore, Date dateEndAfter) {
		Date end = null;
		List<Event> eventsToRemove = new ArrayList<Event>();
		for (Event event : eventList){
			if (!checkNotNullObject(parseRecurrence(event.getRecurrence())).equals(NOT_SPECIFIED_PARSE_CONSTANT)){
				// If event it is a recurrence event should not have "When" fields and
				// viceversa
				Log.i(TAG, "entramos en : RECURRENCIA parsear end Recurrente");
				end = DateUtils.stringCalendarDateToDateGTM((String) parseEndRecurrenceDate(event.getRecurrence()), "yyyy-MM-dd HH:mm:ss") ;
			}else{	
				// If event it is not recurrence event should not have recurrence
				// object info and have "When" info instead of that
				end = DateUtils.stringCalendarDateToDateGTM(parseEndDate(event.getWhen()), "yyyy-MM-dd HH:mm:ss");			
			}
			
			//If dateEndAfter is not null eventList is filtered by this date
			if ((dateEndAfter != null) && end.before(dateEndAfter)){
				Log.i(TAG, "entramos en : REMOVE EVENT "+ DateUtils.dateToStringCalendarDate(end, "yyyy-MM-dd HH:mm:ss") + "end before: " + DateUtils.dateToStringCalendarDate(dateEndAfter, "yyyy-MM-dd HH:mm:ss"));
				eventsToRemove.add(event);
				
			}

			//If the event has not yet deleted, and dateEndBefore is not null eventList is filtered by this date			
			if (!eventsToRemove.contains(event) && (dateEndBefore != null) && end.after(dateEndBefore)){
				Log.i(TAG, "entramos en : REMOVE EVENT "+ DateUtils.dateToStringCalendarDate(end, "yyyy-MM-dd HH:mm:ss") + "end after: " + DateUtils.dateToStringCalendarDate(dateEndBefore, "yyyy-MM-dd HH:mm:ss"));

				eventsToRemove.add(event);
			}
		}
		
		
		for (Event event : eventsToRemove){
			eventList.remove(event);
		}	
		
		
		return eventList;
	}

	/* (non-Javadoc)
	 * @see com.trial.phonegap.plugin.calendar.CalendarAccessorCreator#save(org.json.JSONObject)
	 */
	@Override
	public boolean save(JSONObject jsonEvent) {
		try {
			if (jsonEvent.isNull("id")){
				calendar.createEvent(jsonEventToEvent(jsonEvent));
			}else{
				calendar.updateEvent(jsonEventToEvent(jsonEvent),
									 getEventByIdIntoEventList(calendar.getEvents(), jsonEvent.getString("id")));
			}
			
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			return false;
			// TODO Manage exception
		}
		return true;
	}
	
	private Event getEventByIdIntoEventList(List<Event> listEvents, String id){
		Event res = null;
		for(Event event :  listEvents){
			if (id.equals(event.getId())){
				res = event;
				break;
			}
		}
		return res;
	}
	
	@Override
	
	/* (non-Javadoc)
	 *  
	 */
	public boolean remove(JSONObject jsonEvent) {
		try {
			if (jsonEvent.isNull("id")){
				return false;
			}else{
				calendar.deleteEvent(jsonEventToEvent(jsonEvent));
			}			
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			return false;
			// TODO Manage exception
		}
		return true;
	}
	
			///////////////////PRIVATE METHODS//////////////////////	


	/**
	 * Transforms a Event object in a JSONObject
	 * @param event Event bject
	 * @return a JSONObject with the information within the event parameter given
	 * @throws JSONException
	 */
	private JSONObject eventToJsonEvent(Event event) throws JSONException {

		JSONObject jsonEvent = new JSONObject();

			// This field can't be null
			jsonEvent.put("id", event.getId());

			// This field should be never null
			jsonEvent.put("description", checkNotNullObject(event.getTitle()));
			jsonEvent.put("location",checkNotNullObject(parseLocation(event.getWhere())));
			jsonEvent.put("summary", checkNotNullObject(event.getSummary()));
			jsonEvent.put("status",	checkNotNullObject(parseStatus(event.getEventStatus())));
			jsonEvent.put("transparency", checkNotNullObject(parseTransparency(event.getTransparency())));

			
			if (!checkNotNullObject(parseRecurrence(event.getRecurrence())).equals(NOT_SPECIFIED_PARSE_CONSTANT)){
				// If event it is a recurrence event should not have "When" fields and
				// viceversa
				jsonEvent.put("recurrence",checkNotNullObject(parseRecurrence(event.getRecurrence())));
				jsonEvent.put("start",checkNotNullObject(parseStartRecurrenceDate(event.getRecurrence().getDtStart())));
				jsonEvent.put("end",checkNotNullObject(parseEndRecurrenceDate(event.getRecurrence())));
				jsonEvent.put("reminder",checkNotNullObject(parseReminder(event.getWhen())));
			}else{
				
				// If event it is not recurrence event should not have recurrence
				// object info and have "When" info instead of that
				jsonEvent.put("start",checkNotNullObject(parseStartDate(event.getWhen())));
				jsonEvent.put("end",checkNotNullObject(parseEndDate(event.getWhen())));
				jsonEvent.put("reminder",checkNotNullObject(parseReminder(event.getWhen())));
			}

		return jsonEvent;
	}
	
	/**
	 * Transforms a JSONObject object with information about an event in an event object
	 * @param JsonEvent an JSONObject object with data about an event
	 * @return an event object with te information within JSONObject object given by parameter
	 * @throws JSONException
	 */
	private Event jsonEventToEvent(JSONObject jsonEvent) throws JSONException {


		Event event = new Event();
		
		if(!jsonEvent.isNull("id")){
			event.setId(jsonEvent.getString("id"));
		}
		
		if (!jsonEvent.isNull("recurrence")){
			Recurrence recurrence = new Recurrence();
			recurrence.setRule(parseJsonRecurrenceRule(jsonEvent.getJSONObject("recurrence")));
				if (!(jsonEvent.isNull("start")) && (!jsonEvent.isNull("end"))){
					Dt dt = new Dt();
					dt.setDate(DateUtils.stringCalendarDateToDateLocale(jsonEvent.getString("start"), "yyyy-MM-dd HH:mm:ss"));
					
					TimeZone tm = TimeZone.getDefault();			
					tm.setID(Locale.getDefault().getISO3Country());
					
					dt.setTimeZone(tm.getDisplayName());
					recurrence.setDtStart(dt);
					dt.setDate(DateUtils.stringCalendarDateToDateLocale(jsonEvent.getString("end"), "yyyy-MM-dd HH:mm:ss"));
					recurrence.setDtEnd(dt);
				}
			event.setRecurrence(recurrence);	
		}		
		
		if (!jsonEvent.isNull("description")){
			event.setTitle(jsonEvent.getString("description"));	
		}		
		
		if (!jsonEvent.isNull("location")){
			List<Where> whereList = new ArrayList<Where>();
			Where where = new Where();
			where.description = jsonEvent.getString("location");
			whereList.add(where);
			
			event.setWhere(whereList);
		}
		
		if (jsonEvent.isNull("summary"))
			event.setSummary(jsonEvent.getString("summary"));
		if (jsonEvent.isNull("status"))
			event.setEventStatus(constantSelector(jsonEvent.getString("status")));
		if (jsonEvent.isNull("transparency"))
			event.setTransparency(constantSelector(jsonEvent.getString("transparency")));

		if (jsonEvent.isNull("recurrence")){
			List<When> whenList = new ArrayList<When>();
			When when = new When();
			if (!jsonEvent.isNull("start"))
				when.startTime = new DateTime(DateUtils.stringCalendarDateToDateLocale(jsonEvent.getString("start"), "yyyy-MM-dd HH:mm:ss"));
			if (!jsonEvent.isNull("end"))
				when.endTime = new DateTime(DateUtils.stringCalendarDateToDateLocale(jsonEvent.getString("end"), "yyyy-MM-dd HH:mm:ss"));
			if (!jsonEvent.isNull("reminder"))
				when.reminders = parseJsonReminder(jsonEvent.getString("reminder"));
			whenList.add(when);					
			event.setWhen(whenList);
		}
		
		return event;
		
		}

	

		/**
		 * Parser W3C->Google. Returns a List of Reminders object by a string with the information about the reminders. 
		 * @param string the string 
		 * @return A List<Reminder>
		 */
		private List<Reminder> parseJsonReminder(String string) {
			Reminder reminder = new Reminder();
			if (string.contains("alert"))
				reminder.method = Reminder.METHOD_ALERT;
			if (string.contains("mail"))
				reminder.method = Reminder.METHOD_EMAIL;
			if (string.contains("sms"))
				reminder.method = Reminder.METHOD_SMS;
			reminder.minutes = 10;
			List<Reminder> remiderList = new ArrayList<Reminder>();
			remiderList.add(reminder);
			return remiderList;
		}

		/**
		 * Parser W3C->Google. Return a recurrence rule by a JSONObject with recurrence data given by parameter
		 * @param recurrenceJson
		 * @return a Rule object with the data of the recurrence
		 * @throws JSONException
		 */
		private Rule parseJsonRecurrenceRule(JSONObject recurrenceJson) throws JSONException {
			
			Rule recRule = new Rule();
						
			if (!recurrenceJson.isNull("frecuency"))
				recRule.setFreq(recurrenceJson.getString("frecuency"));
			if (!recurrenceJson.isNull("interval"))
				recRule.setInterval(recurrenceJson.getInt("interval"));
			if (!recurrenceJson.isNull("expires"))
				recRule.setUntil(DateUtils.stringCalendarDateToDateGTM(recurrenceJson.getString("expires"), "yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
			if (!recurrenceJson.isNull("exceptionDates"))
				//Not defined in Ical Spec, so it's going to be ignored here
				Log.i(TAG,"Ignoring exceptionDates values:" + recurrenceJson.getJSONArray("exceptionDates").toString());
			if (!recurrenceJson.isNull("daysInWeek"))
				recRule.setByDay(parseDaysInWeek(recurrenceJson.getJSONArray("daysInWeek")));
			if (!recurrenceJson.isNull("daysInMonth"))
				//BYMONTHDAY Ical Spec			
				recRule.setByMonthDay(parseStringArray(recurrenceJson.getJSONArray("daysInMonth")));
			if (!recurrenceJson.isNull("daysInYear"))
				//BYYEARDAY Ical Spec
				recRule.setByMonthDay(parseStringArray(recurrenceJson.getJSONArray("daysInYear")));
			if (!recurrenceJson.isNull("weeksInMonth"))
				//Not defined in Ical Spec, so it's going to be ignored here
				Log.i(TAG,"Ignoring weeksInMonth values:" + recurrenceJson.getJSONArray("weeksInMonth").toString());
			if (!recurrenceJson.isNull("monthsInYear"))
				//BYMONTH Ical Spec
				recRule.setByMonthDay(parseStringArray(recurrenceJson.getJSONArray("monthsInYear")));		
			
			return recRule;
		}

		/**
		 * Parser W3C->Google. Return an Array of String by a JSONArray given by parameter
		 * @param jsonArray
		 * @return an Array of String
		 * @throws JSONException
		 */
		private String[] parseStringArray(JSONArray jsonArray) throws JSONException {
			String [] stringArray = new String[jsonArray.length()]; 
			for (int i =0; i<jsonArray.length(); i++){
				stringArray[i] = jsonArray.getString(i);
			}			
			return stringArray;
		
		}

		/**
		 * Parser W3C->Google. Return an array of String with days of weeks on google format by a 
		 * JSONArray with information of the day of the weeks in w3c format
		 * @param jsonArray
		 * @return An Array of String
		 * @throws JSONException
		 */
		private String[] parseDaysInWeek(JSONArray jsonArray) throws JSONException {
			/*
			 * BYDAY Ical Spec for ("SU", "SO",...) 
			 * ex:
				   bywdaylist = weekdaynum / ( weekdaynum *("," weekdaynum) )
			   weekdaynum = [([plus] ordwk / minus ordwk)] weekday
			   whewe weekday = daysInWeek
			   
			 * And first part, included in [***] will not be filled 
			 */
			
			String [] daysInWeek = new String[jsonArray.length()]; 
			for (int i =0; i<jsonArray.length(); i++){
				int day = jsonArray.getInt(i);
				if (day==0) daysInWeek[i] = "SU";
				if (day==1) daysInWeek[i] = "MO";
				if (day==2) daysInWeek[i] = "TU";
				if (day==3) daysInWeek[i] = "WE";
				if (day==4) daysInWeek[i] = "TH";
				if (day==5) daysInWeek[i] = "FR";
				if (day==6) daysInWeek[i] = "SU";
			}			
			return daysInWeek;
		}
		

			//////////// PARSERS FORM EVENT INTO JSON EVENT /////////////////
	
	/**
	 * W3C uses constants different from the ones used google. 
	 * This method Transforms constants used by w3c API to constants used by google calendar
	 * @param string
	 * @return
	 */
	private String constantSelector(String string) {
		if (string.equals(EVENT_STATUS_CANCELED))
			return EventEntry.EVENT_STATUS_CANCELED;
		else if (string.equals(EVENT_STATUS_CONFIRMED))
			return EventEntry.EVENT_STATUS_CONFIRMED;
		else if (string.equals(EVENT_STATUS_TENTATIVE))
			return EventEntry.EVENT_STATUS_TENTATIVE;
		else if (string.equals(TRANSPARENCY_OPAQUE))
			return EventEntry.TRANSPARENCY_OPAQUE;
		else if (string.equals(TRANSPARENCY_TRANSPARENT))
			return EventEntry.TRANSPARENCY_TRANSPARENT;
		else return null;

	}
	
	

	/**
	 * Parser Google->W3C. Returns a string with the information about the ending date of the event with recurrence 
	 * given the recurrence object of this event
	 * @param recurrence A recurrence object
	 * @return a string with the ending date of the event
	 */
	private Object parseEndRecurrenceDate(Recurrence recurrence) {
		
		if (recurrence.getDtEnd().getDate()!=null)
			return DateUtils.dateToStringCalendarDate(recurrence.getDtEnd().getDate(), "yyyy-MM-dd HH:mm:ss");
		else if (recurrence.getDuration()!=null){
			long duration = recurrence.getDuration().getMinutes()*60000+recurrence.getDuration().getHours()*3600000+recurrence.getDuration().getSeconds()*1000;
			if (recurrence.getDtStart().getDate()!=null){
				long date = recurrence.getDtStart().getDate().getTime()+duration;				
				return DateUtils.dateToStringCalendarDate(new Date(date),"yyyy-MM-dd HH:mm:ss");
			}
			else 
				return null;
		}		
		return null;
	}


	/**
	 * Parser Google->W3C. Returns a String with the starting date of an event given by the Dt object of the event
	 * @param dtStart A Dt object
	 * @return A String with the stating date of an event
	 */
	private Object parseStartRecurrenceDate(Dt dtStart) {		
		if (dtStart.getDate()!=null)
			return DateUtils.dateToStringCalendarDate(dtStart.getDate(),"yyyy-MM-dd HH:mm:ss");		
		return null;
	}
	

	
	/**
	 * Parser Google->W3C. Returns a String with the W3C Transparency value
	 * @param transparency String with the Google Transparency value
	 * @return String with transparency value
	 */
	private Object parseTransparency(String transparency) {
		if (transparency != null) {
			if (transparency.equals(EventEntry.TRANSPARENCY_OPAQUE))
				return TRANSPARENCY_OPAQUE;
			else if (transparency.equals(EventEntry.TRANSPARENCY_TRANSPARENT))
				return TRANSPARENCY_TRANSPARENT;
			else
				return UNKNOWN;
		}
		return null;
	}

	/**
	 * Parser Google->W3C. Returns a String with the W3C Status value
	 * @param eventStatus String with the Google status value
	 * @return string with status value
	 */
	private Object parseStatus(String eventStatus) {
		if (eventStatus != null) {
			if (eventStatus.equals(EventEntry.EVENT_STATUS_CANCELED))
				return EVENT_STATUS_CANCELED;
			else if (eventStatus.equals(EventEntry.EVENT_STATUS_CONFIRMED))
				return EVENT_STATUS_CONFIRMED;
			else if (eventStatus.equals(EVENT_STATUS_TENTATIVE))
				return EVENT_STATUS_TENTATIVE;
			else
				return UNKNOWN;
		}
		return null;
	}

	
	/**
	 * Return an object with the description of the first Where object in the List given by parameter
	 * @param where List<Where> 
	 * @return An object with the description of a where object
	 */
	private Object parseLocation(List<Where> where) {
		if (where != null) {
			if (where.size() > 1) {
				// Missing information
				printMissedwheres(where);
			}
			return where.get(0).description;
		} else
			return null;

	}

	
	/**
	 * Private method to print Logs for developers 
	 * @param where
	 */
	private void printMissedwheres(List<Where> where) {
		Log.w(TAG, "The parsing process is missing some info:");
		int i = 0;
		for (Where whereObject : where) {
			i++;
			Log.w(TAG, i + " Object Missed Where parsing into JSON object");
			Log.w(TAG, "Location: " + whereObject.description);
			Log.w(TAG, "Label: " + whereObject.label);
			Log.w(TAG, "link: " + whereObject.rel);

		}

	}

	/**
	 * Parser Google->W3C. Returns a String with a list of reminders given within when parameter
	 * @param when 
	 * @return A String
	 */
	private String parseReminder(List<When> when) {
		if (when != null) {
			if (when.size() > 1) {
				// Missing information
				printMissedWhens(when);

			}
			if (when.get(0).reminders != null) {
				String reminders = "";
				List<Reminder> reminderList = when.get(0).reminders;
				for (Reminder reminder : reminderList) {
					reminders = reminders.concat(reminder.minutes + " min to event: " + reminder.method + "; ");
				}
				//Log.i(TAG,"REMINDER--->"+reminders);
				return reminders;
			}
		}
		return null;

	}

	
	/**
	 * Private method to print Logs for developers 
	 * @param when
	 */
	private void printMissedWhens(List<When> when) {
		Log.w(TAG, "The parsing process is missing some info:");
		int i = 0;
		for (When whenObject : when) {
			i++;
			Log.w(TAG, i + " Object Missed When parsing into JSON object");
			Log.w(TAG, "Start: " + whenObject.startTime.toString());
			Log.w(TAG, "End: " + whenObject.endTime.toString());
			Log.w(TAG, "Reminders: " + ((whenObject.reminders !=null )? whenObject.reminders.size():0) + " elements");
		}
	}

	
	/**
	 * Parser Google->W3C. Returns a String  with the ending event date within "when" given by parameter
	 * @param when A List of When objects
	 * @return A String with a ending event date
	 */
	private String parseEndDate(List<When> when) {
		if (when != null) {
			if (when.size() > 1) {
				// Missing information
				printMissedWhens(when);
			}
			Log.i("FECHACABRONA","FECHA---->"+when.get(0).endTime.toStringRfc3339());
			return DateUtils.dateToStringCalendarDate(
					DateUtils.stringCalendarDateToDateGTM(
							when.get(0).endTime.toStringRfc3339(), 
							"yyyy-MM-dd'T'HH:mm:ss.SSSZ"), 
							"yyyy-MM-dd HH:mm:ss");
		} else
			return null;
	}


	/**
	 * Parser Google->W3C. Returns a String  with the starting event date within "when" given by parameter
	 * @param when A List of When objects
	 * @return A String with a starting event date
	 */
	private String parseStartDate(List<When> when) {
		if (when != null) {
			if (when.size() > 1) {
				// Missing information
				printMissedWhens(when);
			}
			return DateUtils.dateToStringCalendarDate(
					DateUtils.stringCalendarDateToDateGTM(
							when.get(0).startTime.toStringRfc3339(), 
							"yyyy-MM-dd'T'HH:mm:ss.SSSZ"), 
							"yyyy-MM-dd HH:mm:ss");
		} else
			return null;
	}


	/**
	 * Checks if the object given by parameter is null. If it is null return a String if not return the same object given by parameter
	 * @param object Object to check
	 * @return An object with the result of checking
	 */
	private Object checkNotNullObject(Object object) {
		return (object == null || object.equals("")) ? NOT_SPECIFIED_PARSE_CONSTANT : object;
	}


	/**
	 * Checks if the object given by parameter is null. If it is null return a String if not return the same object given by parameter
	 * @param object Object to check
	 * @return An object with the result of checking
	 */
	private Object checkNotNullObjectShort(Object object) {
		return (object == null || object.equals("")) ? SHORT_NOT_SPECIFIED_PARSE_CONSTANT : object;
	}


	/**
	 * Parser Google->W3C. Transform a recurrenceRule object given by parameter to a JSONObject object 
	 * @param recurrenceRule A Recurrence object
	 * @return JSONObject object
	 */
	private JSONObject parseRecurrence(Recurrence recurrenceRule) {

		JSONObject recurrenceJson = new JSONObject();

		if (recurrenceRule != null) {
			try {
				// This field can't be null
				recurrenceJson.put("frequency", recurrenceRule.getRule().getFreq());
				recurrenceJson.put("daysInMonth", checkNotNullObjectShort(recurrenceRule.getRule().getByMonthDay()));
				recurrenceJson.put("monthsInYear",checkNotNullObjectShort(recurrenceRule.getRule().getByMonth()));
				recurrenceJson.put("expires", checkNotNullObjectShort(parseExpireDate(recurrenceRule)));
			} catch (JSONException jsonException) {
				jsonException.printStackTrace();
				return null;
			}
			return recurrenceJson;
		}
		return null;

	}

	
	/**
	 * Parser Google->W3C. Return a String with the expire date of the recurrenceRule given by parameter
	 * @param recurrenceRule
	 * @return String object String with the expire date.
	 */
	private Object parseExpireDate(Recurrence recurrenceRule) {
		if (recurrenceRule.getRule().getUntil()!=null){
			return DateUtils.dateToStringCalendarDate(recurrenceRule.getRule().getUntil(),null);
		}else if (recurrenceRule.getRule().getCount()!=0){
			return "repeat "+String.valueOf(recurrenceRule.getRule().getCount())+" times";
		}
		else return null;
	}
	
}
