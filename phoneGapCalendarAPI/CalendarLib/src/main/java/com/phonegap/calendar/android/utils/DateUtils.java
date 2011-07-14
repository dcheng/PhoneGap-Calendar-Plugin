package com.phonegap.calendar.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class DateUtils {

	private static final String TAG = "DateUtils";
	
	 /**
	 * Parse any string date with given format into a Date element
	 * if there is any error in the parser process this method launch an exception 
	 * and returns null.
	 * @param dateString
	 * @return Date object
	 */
	public static Date stringCalendarDateToDate(String dateString, String format) {
		Date date = null;
		SimpleDateFormat dformat;
		if (format==null)
			dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		else
			dformat = new SimpleDateFormat(format);
		try {
				date = dformat.parse(dateString);
			//its normal having an parsing exception here, for instance, with "No Credit" as string
		} catch (ParseException parseException) {
			parseException.printStackTrace();
			return null;			
		}
		return date;
	}
	
	/**
	 * Parse any date into given string format 
	 * @param date
	 * @return String date
	 */
	public static String dateToStringCalendarDate(Date date, String format){
		SimpleDateFormat dformat;
		if (format==null)
			dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		else
			dformat = new SimpleDateFormat(format);
		if (date != null) {
			return dformat.format(date);
		} else {
			return "NOT VALID STRING FORMAT";
		}
	}
	
}
