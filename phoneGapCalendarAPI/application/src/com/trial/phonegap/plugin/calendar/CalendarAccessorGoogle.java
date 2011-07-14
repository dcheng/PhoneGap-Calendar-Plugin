package com.trial.phonegap.plugin.calendar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.util.DateTime;
import com.phonegap.calendar.android.adapters.Calendar;
import com.phonegap.calendar.android.adapters.CalendarsManager;
import com.phonegap.calendar.android.adapters.Dt;
import com.phonegap.calendar.android.adapters.Event;
import com.phonegap.calendar.android.adapters.Recurrence;
import com.phonegap.calendar.android.model.EventEntry;
import com.phonegap.calendar.android.model.Reminder;
import com.phonegap.calendar.android.model.When;
import com.phonegap.calendar.android.model.Where;
import com.phonegap.calendar.android.utils.DateUtils;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

public class CalendarAccessorGoogle extends CalendarAccessorCreator {

	private static final String TAG = "[Android:CalendarAccessorCreator.java]";

	private static final String NOT_SPECIFIED_PARSE_CONSTANT = "  - N / D -  ";
	private static final String SHORT_NOT_SPECIFIED_PARSE_CONSTANT = "N/D";
	private static final String TRANSPARENCY_OPAQUE = "Opaque";
	private static final String TRANSPARENCY_TRANSPARENT = "Transparent";
	private static final String UNKNOWN = "Unknown";
	private static final String EVENT_STATUS_CONFIRMED = "Confirmed";
	private static final String EVENT_STATUS_CANCELED = "Cancelled";
	private static final String EVENT_STATUS_TENTATIVE = "Tentative";

	private CalendarsManager calendarsManager;
	private Calendar calendar;

	public CalendarAccessorGoogle(WebView view, Activity app) {
		mApp = app;
		mView = view;

		calendarsManager = new CalendarsManager(app);

		List<Calendar> calendars = calendarsManager.getUserCalendars();

		calendar = calendars.get(0);
	}

	@Override
	public JSONArray find(JSONObject options) {

		Date dateAfter = null;
		Date dateBefore = null;
		JSONArray events = new JSONArray();
		try {

			JSONObject filter = options.getJSONObject("filter");
	
			Log.i(TAG, "Date After: " + filter.getString("startAfter"));
			Log.i(TAG, "Date Before: " + filter.getString("startBefore"));
			
			dateAfter = DateUtils.stringCalendarDateToDate(filter.getString("startAfter"), "yyyy-MM-dd HH:mm:ss");
			dateBefore = DateUtils.stringCalendarDateToDate(filter.getString("startBefore"), "yyyy-MM-dd HH:mm:ss");

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

	@Override
	public boolean save(JSONObject jsonEvent) {
		try {
			calendar.createEvent(JsonEventToEvent(jsonEvent));
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			// TODO Manage exception
		}
		return false;
	}
	
			///////////////////PRIVATE METHODS//////////////////////	

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
	
	private Event JsonEventToEvent(JSONObject JsonEvent) throws JSONException {

		Event event = new Event();
		
		event.setRecurrence(null); //TODO ADD SOMETHING
		event.setTitle(JsonEvent.getString("description"));
		
		List<Where> whereList = new ArrayList<Where>();
		Where where = new Where();
		where.description = JsonEvent.getString("location");
		whereList.add(where);
		
		event.setWhere(whereList);
		
		event.setSummary(JsonEvent.getString("summary"));
		event.setEventStatus(constantSelector(JsonEvent.getString("status")));
		event.setTransparency(constantSelector(JsonEvent.getString("transparency")));
		
		List<When> whenList = new ArrayList<When>();
		When when = new When();
		when.startTime = new DateTime(DateUtils.stringCalendarDateToDate(JsonEvent.getString("start"), "yyyy-MM-dd HH:mm:ss"));
		when.endTime = new DateTime(DateUtils.stringCalendarDateToDate(JsonEvent.getString("end"), "yyyy-MM-dd HH:mm:ss"));
		when.reminders = null; //TODO ADD SOMETHING
		whenList.add(when);		
		
		event.setWhen(whenList);
	
		return event;
		
		}

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

	private Object parseStartRecurrenceDate(Dt dtStart) {		
		if (dtStart.getDate()!=null)
			return DateUtils.dateToStringCalendarDate(dtStart.getDate(),"yyyy-MM-dd HH:mm:ss");		
		return null;
	}

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

	private String parseEndDate(List<When> when) {
		if (when != null) {
			if (when.size() > 1) {
				// Missing information
				printMissedWhens(when);
			}
			Log.i("FECHACABRONA","FECHA---->"+when.get(0).endTime.toStringRfc3339());
			return DateUtils.dateToStringCalendarDate(
					DateUtils.stringCalendarDateToDate(
							when.get(0).endTime.toStringRfc3339(), 
							"yyyy-MM-dd'T'HH:mm:ss.SSSZ"), 
							"yyyy-MM-dd HH:mm:ss");
		} else
			return null;
	}

	private String parseStartDate(List<When> when) {
		if (when != null) {
			if (when.size() > 1) {
				// Missing information
				printMissedWhens(when);
			}
			return DateUtils.dateToStringCalendarDate(
					DateUtils.stringCalendarDateToDate(
							when.get(0).startTime.toStringRfc3339(), 
							"yyyy-MM-dd'T'HH:mm:ss.SSSZ"), 
							"yyyy-MM-dd HH:mm:ss");
		} else
			return null;
	}

	private Object checkNotNullObject(Object object) {
		return (object == null || object.equals("")) ? NOT_SPECIFIED_PARSE_CONSTANT : object;
	}

	private Object checkNotNullObjectShort(Object object) {
		return (object == null || object.equals("")) ? SHORT_NOT_SPECIFIED_PARSE_CONSTANT : object;
	}

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

	private Object parseExpireDate(Recurrence recurrenceRule) {
		if (recurrenceRule.getRule().getUntil()!=null){
			return DateUtils.dateToStringCalendarDate(recurrenceRule.getRule().getUntil(),null);
		}else if (recurrenceRule.getRule().getCount()!=0){
			return "repeat "+String.valueOf(recurrenceRule.getRule().getCount())+" times";
		}
		else return null;
	}
}
