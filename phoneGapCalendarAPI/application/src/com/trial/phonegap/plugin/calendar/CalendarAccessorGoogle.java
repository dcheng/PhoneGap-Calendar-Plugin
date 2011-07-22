package com.trial.phonegap.plugin.calendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

public class CalendarAccessorGoogle extends CalendarAccessorCreator {

	/**
	 * 
	 */
	private static final String TAG = "[Android:CalendarAccessorCreator.java]";

	/**
	 * 
	 */
	private static final String NOT_SPECIFIED_PARSE_CONSTANT = "  - N / D -  ";
	/**
	 * 
	 */
	private static final String SHORT_NOT_SPECIFIED_PARSE_CONSTANT = "N/D";
	/**
	 * 
	 */
	private static final String TRANSPARENCY_OPAQUE = "Opaque";
	/**
	 * 
	 */
	private static final String TRANSPARENCY_TRANSPARENT = "Transparent";
	/**
	 * 
	 */
	private static final String UNKNOWN = "Unknown";
	/**
	 * 
	 */
	private static final String EVENT_STATUS_CONFIRMED = "Confirmed";
	/**
	 * 
	 */
	private static final String EVENT_STATUS_CANCELED = "Cancelled";
	/**
	 * 
	 */
	private static final String EVENT_STATUS_TENTATIVE = "Tentative";

	/**
	 * 
	 */
	private CalendarsManager calendarsManager;
	/**
	 * 
	 */
	private Calendar calendar;

	/**
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

		Date dateAfter = null;
		Date dateBefore = null;
		JSONArray events = new JSONArray();
		try {

			JSONObject filter = options.getJSONObject("filter");
	
			Log.i(TAG, "Date After: " + filter.getString("startAfter"));
			Log.i(TAG, "Date Before: " + filter.getString("startBefore"));
			
			dateAfter = DateUtils.stringCalendarDateToDateGTM(filter.getString("startAfter"), "yyyy-MM-dd HH:mm:ss");
			dateBefore = DateUtils.stringCalendarDateToDateGTM(filter.getString("startBefore"), "yyyy-MM-dd HH:mm:ss");

			// List<Event> events = calendar.getCalendarAllEventsList();

			List<Event> eventList = calendar.findCalendarEventsBydate(dateAfter, dateBefore);

			int i = 0;
			for (Event event : eventList) {
				i++;
				events.put(eventToJsonEvent(event));
				Log.i(TAG, "Evento [" + i + "]: " + event.getTitle());
			}

		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			//TODO Manage exception
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			//TODO Manage exception
		}
		
		return events;
	}

	/* (non-Javadoc)
	 * @see com.trial.phonegap.plugin.calendar.CalendarAccessorCreator#save(org.json.JSONObject)
	 */
	@Override
	public boolean save(JSONObject jsonEvent) {
		try {
			calendar.createEvent(JsonEventToEvent(jsonEvent));
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			return false;
			// TODO Manage exception
		}
		return true;
	}
	
			///////////////////PRIVATE METHODS//////////////////////	

	/**
	 * @param event
	 * @return
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
				// If event it is a recurrence event should not have When fields and
				// viceversa
				jsonEvent.put("recurrence",checkNotNullObject(parseRecurrence(event.getRecurrence())));
				jsonEvent.put("start",checkNotNullObject(parseStartRecurrenceDate(event.getRecurrence().getDtStart())));
				jsonEvent.put("end",checkNotNullObject(parseEndRecurrenceDate(event.getRecurrence())));
				jsonEvent.put("reminder",checkNotNullObject(parseReminder(event.getWhen())));
			}else{
				
				// If event it is not recurrence event should not have recurrence
				// object info and have when info instead of that
				jsonEvent.put("start",checkNotNullObject(parseStartDate(event.getWhen())));
				jsonEvent.put("end",checkNotNullObject(parseEndDate(event.getWhen())));
				jsonEvent.put("reminder",checkNotNullObject(parseReminder(event.getWhen())));
			}

		return jsonEvent;
	}
	
	/**
	 * @param jsonEvent
	 * @return
	 * @throws JSONException
	 */
	private Event JsonEventToEvent(JSONObject jsonEvent) throws JSONException {

		Event event = new Event();
		
		
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
	 * @param string
	 * @return
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
	 * @param recurrenceJson
	 * @return
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
	 * @param jsonArray
	 * @return
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
	 * @param jsonArray
	 * @return
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
	
	
	

	/**
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

			//////////// PARSERS FORM EVENT INTO JSON EVENT /////////////////

	/**
	 * @param recurrence
	 * @return
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
	 * @param dtStart
	 * @return
	 */
	private Object parseStartRecurrenceDate(Dt dtStart) {		
		if (dtStart.getDate()!=null)
			return DateUtils.dateToStringCalendarDate(dtStart.getDate(),"yyyy-MM-dd HH:mm:ss");		
		return null;
	}

	/**
	 * @param transparency
	 * @return
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
	 * @param eventStatus
	 * @return
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
	 * @param where
	 * @return
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
	 * @param when
	 * @return
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
	 * @param when
	 * @return
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
	 * @param when
	 * @return
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
	 * @param object
	 * @return
	 */
	private Object checkNotNullObject(Object object) {
		return (object == null || object.equals("")) ? NOT_SPECIFIED_PARSE_CONSTANT : object;
	}

	/**
	 * @param object
	 * @return
	 */
	private Object checkNotNullObjectShort(Object object) {
		return (object == null || object.equals("")) ? SHORT_NOT_SPECIFIED_PARSE_CONSTANT : object;
	}

	/**
	 * @param recurrenceRule
	 * @return
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
	 * @param recurrenceRule
	 * @return
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
