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

import java.util.Arrays;
import java.util.Date;

import com.phonegap.calendar.android.utils.DateUtils;

/**
 * This class instances are objects that represent the info relative to
 * RRULE tag in the recurrence string of EventEntry, we have here parse
 * methods for the conversion EventEntry - > Event and viceversa
 * This class attributes and methods are implemented by following the Ical Specifications
 * described by IETF in :  @link http://www.ietf.org/rfc/rfc2445.txt  
 * @author Sergio Martinez Rodriguez
 */
public class Rule {
	
	 /**
	 * Repetition frequency of rule
	 */
	private String freq;	
	 /**
	 * Date until rule is active
	 */
	private Date until;
	 /**
	 * Times the event can repeat 
	 */
	private int count;
	 /**
	 * Interval between repetitions
	 */
	private int interval;
	 /**
	 * Seconds in which the event is taking place 
	 */
	private String[] bySeconds;
	 /**
	 * Minutes in which the event is taking place
	 */
	private String[] byMinute;
	 /**
	 * Hours in which the event is taking place
	 */
	private String[] byHour;
	 /**
	 * Days in which the event is taking place 
	 */
	private String[] byDay;
	 /**
	 * Month day in which the event is taking place
	 */
	private String[] byMonthDay;
	 /**
	 * Year day in which the event is taking place
	 */
	private String[] byYearDay;
	 /**
	 * Year week in which the event is taking place
	 */
	private String[] byWeekNo;
	 /**
	 * year Month in which the event is taking place
	 */
	private String[] byMonth;
	 /**
	 *  the nth occurrence of the specific occurrence within the set of events specified by the rule
	 */
	private String[] bySetPos;
	 /**
	 * Day of week represented as string "SA", "SU",... in which the event is taking place
	 */
	private String wkst;	
	
	 
	 
	 /**
	 * Builder using fields
	 */
	public Rule() {
		this.freq = null;
		this.until = null;
		this.count = 0;
		this.interval = 0;
		this.bySeconds = null;
		this.byMinute = null;
		this.byHour = null;
		this.byDay = null;
		this.byMonthDay = null;
		this.byYearDay = null;
		this.byWeekNo = null;
		this.byMonth = null;
		this.bySetPos = null;
		this.wkst = null;
	}
	/**
	 * Gets the frequency attribute
	 * @return frequency string 
	 */
	public String getFreq() {
		return freq;
	}
	
	/**
	 * Sets the Frequency String
	 * @param freq Frecuency String
	 */
	public void setFreq(String freq) {
		this.freq = freq;
	}
	
	/**
	 * Gets Day of week Attribute
	 * @return wkst String
	 */
	public String getWkst() {
		return wkst;
	}
	/**
	 * Sets Day of week Attribute
	 * @param wkst wkst String
	 */
	public void setWkst(String wkst) {
		this.wkst = wkst;
	}
	/**
	 * Gets until attribute
	 * @return until Date
	 */
	public Date getUntil() {
		return until;
	}
	/**
	 * Sets until attribute
	 * @param until until Date
	 */
	public void setUntil(Date until) {
		this.until = until;
	}
	/**
	 * Gets count Attribute  
	 * @return count int
	 */
	public int getCount() {
		return count;
	}
	/**
	 * Sets count attribute
	 * @param count count int
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * Gets interval attribute
	 * @return interval int
	 */
	public int getInterval() {
		return interval;
	}
	/**
	 * Sets interval attribute
	 * @param interval interval int
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	/**
	 * Gets the array of Seconds in which the event is taking place  
	 * @return Seconds in which the event is taking place as String array
	 */
	public String[] getBySeconds() {
		return bySeconds;
	}
	/**
	 * Sets the array of Seconds in which the event is taking place
	 * @param bySeconds Seconds in which the event is taking place as String array
	 */
	public void setBySeconds(String[] bySeconds) {
		this.bySeconds = bySeconds;
	}
	/**
	 * Gets the array of Minutes in which the event is taking place  
	 * @return Minutes in which the event is taking place as String array
	 */
	public String[] getByMinute() {
		return byMinute;
	}
	/**
	 * Sets the array of Minutes in which the event is taking place
	 * @param byMinute Minutes in which the event is taking place as String array
	 */
	public void setByMinute(String[] byMinute) {
		this.byMinute = byMinute;
	}
	/**
	 * Gets the array of Hours in which the event is taking place  
	 * @return Hours in which the event is taking place as String array
	 */
	public String[] getByHour() {
		return byHour;
	}
	/**
	 * Sets the array of hours in which the event is taking place
	 * @param byHour hours in which the event is taking place as String array
	 */
	public void setByHour(String[] byHour) {
		this.byHour = byHour;
	}
	/**
	 * Gets the array of days in which the event is taking place  
	 * @return days in which the event is taking place as String array
	 */
	public String[] getByDay() {
		return byDay;
	}
	/**
	 * Sets the array of days in which the event is taking place
	 * @param byDay days in which the event is taking place as String array
	 */
	public void setByDay(String[] byDay) {
		this.byDay = byDay;
	}
	/**
	 * Gets the array of days in month in which the event is taking place  
	 * @return days in month in which the event is taking place as String array
	 */
	public String[] getByMonthDay() {
		return byMonthDay;
	}
	/**
	 * Sets the array of days in month in which the event is taking place
	 * @param byMonthDay days in month in which the event is taking place as String array
	 */	
	public void setByMonthDay(String[] byMonthDay) {
		this.byMonthDay = byMonthDay;
	}
	/**
	 * Gets the array of days in Year in which the event is taking place  
	 * @return days in year in which the event is taking place as String array
	 */
	public String[] getByYearDay() {
		return byYearDay;
	}
	/**
	 * Sets the array of days in year in which the event is taking place
	 * @param byYearDay days in year in which the event is taking place as String array
	 */	
	public void setByYearDay(String[] byYearDay) {
		this.byYearDay = byYearDay;
	}
	/**
	 * Gets the array of weeks in Year in which the event is taking place  
	 * @return weeks in year in which the event is taking place as String array
	 */
	public String[] getByWeekNo() {
		return byWeekNo;
	}
	/**
	 * Sets the array of weeks in year in which the event is taking place
	 * @param byWeekNo weeks in year in which the event is taking place as String array
	 */	
	public void setByWeekNo(String[] byWeekNo) {
		this.byWeekNo = byWeekNo;
	}
	/**
	 * Gets the array of months in Year in which the event is taking place  
	 * @return months in year in which the event is taking place as String array
	 */
	public String[] getByMonth() {
		return byMonth;
	}
	/**
	 * Sets the array of months in year in which the event is taking place
	 * @param byMonth months in year in which the event is taking place as String array
	 */
	public void setByMonth(String[] byMonth) {
		this.byMonth = byMonth;
	}
	/**
	 * Gets nth occurrence of the specific occurrence within the set of events specified by the rule
	 * @return nth occurrence of the specific occurrence within the set of events specified by the rule
	 */
	public String[] getBySetPos() {
		return bySetPos;
	}
	/**
	 * Sets nth occurrence of the specific occurrence within the set of events specified by the rule
	 * @param bySetPos nth occurrence of the specific occurrence within the set of events specified by the rule
	 */
	public void setBySetPos(String[] bySetPos) {
		this.bySetPos = bySetPos;
	}
	
	/**
	 * Writes the Rule object as String with the rfc2445 ietf format
	 * for the RRULE label in ICal spec format
	 * @return result String with rfc2445 format
	 */
	public String getICalFormat(){
		
		String result = "RRULE:FREQ=";
		
		result.concat(this.freq);
		if (count!=0)
			result.concat(";COUNT="+String.valueOf(count));
		if (until!=null)
			result.concat(";UNTIL="+DateUtils.dateToStringCalendarDate(until, "yyyyMMdd'T'HHmmss'Z'"));
		if (interval!=0)
			result.concat(";INTERVAL="+String.valueOf(count));
		
		if (bySeconds!=null){
			result.concat(";BYSECOND=");
			result.concat(bySeconds[0]);
			if (bySeconds.length>1){
			for (int i=0; i<bySeconds.length; i++)
				result.concat(","+bySeconds[i]);	
			}
		}
		
		if (byMinute!=null){
			result.concat(";BYMINUTE=");
			result.concat(byMinute[0]);
			if (byMinute.length>1){
			for (int i=0; i<byMinute.length; i++)
				result.concat(","+byMinute[i]);	
			}
		}
		
		if (byHour!=null){
			result.concat(";BYHOUR=");
			result.concat(byHour[0]);
			if (byHour.length>1){
			for (int i=0; i<byHour.length; i++)
				result.concat(","+byHour[i]);	
			}
		}
		
		if (byDay!=null){
			result.concat(";BYDAY=");
			result.concat(byDay[0]);
			if (byDay.length>1){
			for (int i=0; i<byDay.length; i++)
				result.concat(","+byDay[i]);	
			}
		}
		
		if (byMonthDay!=null){
			result.concat(";BYMONTHDAY=");
			result.concat(byMonthDay[0]);
			if (byMonthDay.length>1){
			for (int i=0; i<byMonthDay.length; i++)
				result.concat(","+byMonthDay[i]);	
			}
		}
		
		if (byYearDay!=null){
			result.concat(";BYYEARDAY=");
			result.concat(byYearDay[0]);
			if (byYearDay.length>1){
			for (int i=0; i<byYearDay.length; i++)
				result.concat(","+byYearDay[i]);	
			}
		}
		
		if (byWeekNo!=null){
			result.concat(";BYWEEKNO=");
			result.concat(byWeekNo[0]);
			if (byWeekNo.length>1){
			for (int i=0; i<byWeekNo.length; i++)
				result.concat(","+byWeekNo[i]);	
			}
		}
		
		if (byMonth!=null){
			result.concat(";BYMONTH=");
			result.concat(byMonth[0]);
			if (byMonth.length>1){
				for (int i=0; i<byMonth.length; i++)
					result.concat(","+byMonth[i]);	
			}
		}
			
		if (bySetPos!=null){
			result.concat(";BYSETPOS=");
			result.concat(bySetPos[0]);
			if (bySetPos.length>1){
				for (int i=0; i<bySetPos.length; i++)
					result.concat(","+bySetPos[i]);	
			}
		}
		
		if (wkst!=null)
			result.concat(";WKST="+wkst);
		
		result.concat("\n");
		
		return result;
	}
	
	/* (non-Javadoc) 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rule [freq=" + freq + ", until=" + until + ", count=" + count
				+ ", interval=" + interval + ", bySeconds="
				+ Arrays.toString(bySeconds) + ", byMinute="
				+ Arrays.toString(byMinute) + ", byHour="
				+ Arrays.toString(byHour) + ", byDay=" + Arrays.toString(byDay)
				+ ", byMonthDay=" + Arrays.toString(byMonthDay)
				+ ", byYearDay=" + Arrays.toString(byYearDay) + ", byWeekNo="
				+ Arrays.toString(byWeekNo) + ", byMonth="
				+ Arrays.toString(byMonth) + ", bySetPos="
				+ Arrays.toString(bySetPos) + ", wkst=" + wkst + "]";
	};
	 
	 
}
