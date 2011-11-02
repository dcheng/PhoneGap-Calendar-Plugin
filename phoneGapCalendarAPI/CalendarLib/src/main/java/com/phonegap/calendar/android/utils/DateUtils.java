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

package com.phonegap.calendar.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;


/**
 * This class give some useful operations in order to work with
 * dates
 * @author Sergio Martinez Rodriguez
 *
 */
public class DateUtils {

	private static final String TAG = "DateUtils";
	
	 /**
	 * Parse any string date with given format or default "yyyy-MM-dd'T'HH:mm:ss" into a Date element
	 * if there is any error in the parser process this method launch an exception,
	 * the returned element has GTM time 
	 * and returns null.
	 * @param dateString date as string
	 * @param format String format of given String date 
	 * @return GTM Date corresponding to the given String date
	 */
	public static Date stringCalendarDateToDateGTM(String dateString, String format) {
		Date date = null;
		SimpleDateFormat dformat;
		if (format==null)
			dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		else
			dformat = new SimpleDateFormat(format);
		try {
				date = dformat.parse(dateString);
		} catch (ParseException parseException) {
			parseException.printStackTrace();
			return null;			
		}
		return date;
	}
	
	/**
	 * Parse any string date with given or default "yyyy-MM-dd'T'HH:mm:ss" format into a Date element
	 * if there is any error in the parser process this method launch an exception,
	 * the returned element has device Locale time 
	 * and returns null.
	 * @param dateString date as string
	 * @param format String format of given String date 
	 * @return Locale Date corresponding to the given String date
	 */
	public static Date stringCalendarDateToDateLocale(String dateString, String format) {
		Date date = null;
		SimpleDateFormat dformat;
		if (format==null)
			dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		else
			dformat = new SimpleDateFormat(format);
		try {
				date = dformat.parse(dateString);
			
		} catch (ParseException parseException) {
			parseException.printStackTrace();
			return null;			
		}

		TimeZone tm = TimeZone.getDefault();
		
		tm.setID(Locale.getDefault().getISO3Country());
		date = new Date(date.getTime()+tm.getOffset(date.getTime()));

		return date;
	}
	
	/**
	 * Parse any date into given string or default "yyyy-MM-dd'T'HH:mm:ss" format 
	 * @param date Date object we want to transform into String
	 * @param format String format of desired String date
	 * @return String date corresponding with given String Date object
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
