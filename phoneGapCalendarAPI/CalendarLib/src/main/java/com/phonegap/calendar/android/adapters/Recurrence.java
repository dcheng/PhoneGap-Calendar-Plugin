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

import com.phonegap.calendar.android.utils.DateUtils;

import android.util.Log;

/**
 * This class instances will represent the recurrence Rules in the calendar events
 * aim of all operations we can find here is to parse the Recurrence String info in the
 * EventEntry into an the corresponding attributes of this class for getting an useful 
 * object, in the same way we can do the same, write this class instances like Recurrence
 * rule String.
 * This class attributes and methods are implemented by following the Ical Specifications
 * described by IETF in :  @link http://www.ietf.org/rfc/rfc2445.txt
 * @author Sergio Martinez Rodriguez 
 */
public class Recurrence {

	private static final String TAG = "Recurrence";
	
	/**
	 * Dt object that represents the "DTSART:" rfc2445 label in a recurrence object
	 */
	private Dt dtStart;
	/**
	 * Dt object that represents the "DTEND:" rfc2445 label in a recurrence object
	 */
	private Dt dtEnd;
	/**
	 * Dt object that represents the "DURATION:" rfc2445 label in a recurrence object
	 */
	private Duration duration;
	/**
	 * Dt object that represents the "RRULE:" rfc2445 label in a recurrence object
	 */
	private Rule rule;
	
	/**
	 * Constructor that initialize attributes dtStart, dtEndt, duration and rule to null value 
	 */
	public Recurrence (){
		this.dtStart = null;
		this.dtEnd = null;
		this.duration = null;
		this.rule = null;
	}
	
	/**
	 * Constructor that receives a recurrence object as String with rfc2445 format
	 * and makes an instance of this class as Recurrence object by parsing the received
	 * string
	 * @param recurrence recurrence string with rfc2445 Ical format
	 */
	public Recurrence (String recurrence){
		
//		Log.i(TAG, "REUCRRENCE STRING-->"+recurrence);
		
        String [] recurrenceArray = recurrence.split("\n");
        
        dtStart = null;
        dtEnd = null;
        duration = null;
        rule = null;
        
//        Log.i(TAG, "REUCRRENCE [0]-->"+recurrenceArray[0]);
//        Log.i(TAG, "REUCRRENCE [1]-->"+recurrenceArray[1]);
//        Log.i(TAG, "REUCRRENCE [2]-->"+recurrenceArray[2]);
        
        if (recurrenceArray[0].contains("DTSTART"))
        	dtStart = parseDt(recurrenceArray[0]);
        
        if (recurrenceArray[1].contains("DTEND"))
        	dtEnd = parseDt(recurrenceArray[1]);
        else if (recurrenceArray[1].contains("DURATION"))
        	duration = parseDuration(recurrenceArray[1]);
                
        if (recurrenceArray[2].contains("RRULE:"))
        	rule = parseRule(recurrenceArray[2]);

	}
	
	/**
	 * Gets dtStart attribute
	 * @return Dt Object corresponding to DTSTART rfc2445 label String value
	 */
	public Dt getDtStart() {
		return dtStart;
	}

	/**
	 * Sets dtStart attribute
	 * @param dtStart Dt Object corresponding to DTSTART rfc2445 label String value
	 */
	public void setDtStart(Dt dtStart) {
		this.dtStart = dtStart;
	}

	/**
	 * Gets dtEnd attribute
	 * @return Dt Object corresponding to DTEND rfc2445 label String value
	 */
	public Dt getDtEnd() {
		return dtEnd;
	}

	/**
	 * Sets dtEnd attribute
	 * @param dtEnd Dt Object corresponding to DTEND rfc2445 label String value
	 */
	public void setDtEnd(Dt dtEnd) {
		this.dtEnd = dtEnd;
	}

	/**
	 * Gets duration attribute
	 * @return Duration Object corresponding to DURATION rfc2445 label String value
	 */
	public Duration getDuration() {
		return duration;
	}

	/**
	 * Sets duration attribute
	 * @param duration Duration Object corresponding to DURATION rfc2445 label String value
	 */
	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	/**
	 * Gets rule attribute
	 * @return Rule Object corresponding to RRULE rfc2445 label String value
	 */
	public Rule getRule() {
		return rule;
	}

	/**
	 * Gets rule attribute
	 * @param rule Rule Object corresponding to RRULE rfc2445 label String value
	 */
	public void setRule(Rule rule) {
		this.rule = rule;
	}

	/**
	 * Writes the complete Recurrence object as String with the rfc2445 ietf format
	 * for the recurrence fields.
	 * @return result String with rfc2445 format
	 */
	public String getRecurrenceRuleAsString(){
		String result;
		
		result = dtStart.getICalFormat("DTSTART") + 
		((dtEnd != null) ? "" : dtEnd.getICalFormat("DTEND")) + 
		((duration != null) ? "" : duration.getICalFormat()) +
		rule.getICalFormat() + "\n";  
		
		return result;
	}
	
	/**
	 * Parses received String corresponding to "RRULE" rfc2445 label in a recurrence
	 * event into a Rule object. The parsing process is following the detailed 
	 * specifications in  http://www.ietf.org/rfc/rfc2445.txt
	 * @param string "RRULE" String with rfc2445 format
	 * @return Rule instance
	 */
	private Rule parseRule(String string) {
		
    	String [] ruleArray = string.split(":");
    	
    	Rule rule = new Rule();
    	
    	String [] splitedRuleArray = ruleArray[1].concat(";").split(";");
    	
    	for (int i = 0; i<splitedRuleArray.length; i++){
    	if (splitedRuleArray[i].contains("FREQ="))
    		rule.setFreq(getSubstring(splitedRuleArray[i], "FREQ=", null));
    	
    	if (splitedRuleArray[1].contains("UNTIL="))
    		rule.setUntil(DateUtils.stringCalendarDateToDateGTM(getSubstring(splitedRuleArray[i], "UNTIL=", ";"), "yyyyMMdd'T'HHmmss'Z'"));
    	else if (splitedRuleArray[i].contains("COUNT="))
    		rule.setCount(Integer.parseInt(getSubstring(splitedRuleArray[i], "COUNT=", null)));
    	
    	if (splitedRuleArray[i].contains("INTERVAL="))
    		rule.setInterval(Integer.parseInt(getSubstring(splitedRuleArray[i], "INTERVAL=", null)));
    	
    	String substring = null;
    	
    	if (splitedRuleArray[i].contains("BYSECOND=")){
    		substring = getSubstring(splitedRuleArray[i], "BYSECOND=", null);
    		if (substring.contains(",")) rule.setBySeconds(substring.split(","));
    		else rule.setBySeconds(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("BYMINUTE=")){
    		substring = getSubstring(splitedRuleArray[i], "BYMINUTE=", null);
    		if (substring.contains(",")) rule.setByMinute(substring.split(","));
    		else rule.setByMinute(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("BYHOUR=")){
    		substring = getSubstring(splitedRuleArray[i], "BYHOUR=", null);
    		if (substring.contains(",")) rule.setByHour(substring.split(","));
    		else rule.setByHour(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("BYDAY=")){
    		substring = getSubstring(splitedRuleArray[i], "BYDAY=", null);
    		if (substring.contains(",")) rule.setByDay(substring.split(","));
    		else rule.setByDay(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("BYMONTHDAY=")){
    		substring = getSubstring(splitedRuleArray[i], "BYMONTHDAY=", null);
    		if (substring.contains(",")) rule.setByMonthDay(substring.split(","));
    		else rule.setByMonthDay(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("BYYEARDAY=")){
    		substring = getSubstring(splitedRuleArray[i], "BYYEARDAY=", null);
    		if (substring.contains(",")) rule.setByYearDay(substring.split(","));
    		else rule.setByYearDay(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("BYWEEKNO=")){
    		substring = getSubstring(splitedRuleArray[i], "BYWEEKNO=", null);
    		if (substring.contains(",")) rule.setByWeekNo(substring.split(","));
    		else rule.setByWeekNo(new String[]{substring});
    		}
    	if (splitedRuleArray[i].contains("BYMONTH=")){
    		substring = getSubstring(splitedRuleArray[i], "BYMONTH=", null);
    		if (substring.contains(",")) rule.setByMonth(substring.split(","));
    		else rule.setByMonth(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("BYSETPOS=")){
    		substring = getSubstring(splitedRuleArray[i], "BYSETPOS=", null);
    		if (substring.contains(",")) rule.setBySetPos(substring.split(","));
    		else rule.setBySetPos(new String[]{substring});
    	}
    	if (splitedRuleArray[i].contains("WKST=")){
    		rule.setWkst(getSubstring(splitedRuleArray[i], "WKST=", null));
    	}
    	}
    	
		return rule;
	}

	/**
	 * Parses received String corresponding to "DTSTART"/"DTEND" rfc2445 label in a recurrence
	 * event into a DT object. The parsing process is following the detailed 
	 * specifications in  http://www.ietf.org/rfc/rfc2445.txt
	 * @param dtToParse "DTSTART"/"DTEND" String with rfc2445 format
	 * @return DT instance
	 */
	private Dt parseDt(String dtToParse){
    	
    	String [] dtStartArray = dtToParse.split(":");
        
    	Dt dt = new Dt();
        
        if (dtStartArray[0].contains(";")){
        	String []dtTimeZoneArray = null;
        	if (dtStartArray[0].contains(";TZID=")){
        	   dtTimeZoneArray = dtStartArray[0].split(";TZID=");
        	   dt.setTimeZone(dtTimeZoneArray[1]);
        	}
        }
        if (dtStartArray[1].contains("T"))
        	dt.setDate(DateUtils.stringCalendarDateToDateGTM(dtStartArray[1], "yyyyMMdd'T'HHmmss"));
        else 
        	dt.setDate(DateUtils.stringCalendarDateToDateGTM(dtStartArray[1], "yyyyMMdd"));

        	return dt;
    }

	/**
	 * Parses received String corresponding to "DURATION" rfc2445 label in a recurrence
	 * event into a Duration object. The parsing process is following the detailed 
	 * specifications in  http://www.ietf.org/rfc/rfc2445.txt
	 * @param durationToParse "DURATION" String with rfc2445 format
	 * @return Duration instance
	 */
    private Duration parseDuration(String durationToParse){
    	
    	String [] durationArray = durationToParse.split(":PT");
        
    	Duration duration = new Duration();
    	        
        if (durationArray[1].contains("S")){
        	if (durationArray[1].contains("M")){
        		duration.setSeconds(Long.parseLong(getSubstring(durationArray[1], "M", "S")));
        		if (durationArray[1].contains("H")){
        			duration.setMinutes(Long.parseLong(getSubstring(durationArray[1], "H", "M")));
        			duration.setHours(Long.parseLong(getSubstring(durationArray[1], null, "H")));
        		}else{
        			duration.setMinutes(Long.parseLong(getSubstring(durationArray[1], null, "M")));
        		}
        		
        	}else if (durationArray[1].contains("H")){
        		duration.setSeconds(Long.parseLong(getSubstring(durationArray[1], "H", "S")));
    		}else {
    			duration.setSeconds(Long.parseLong(getSubstring(durationArray[1], null, "S")));
    		}
        }else if (durationArray[1].contains("M")){
    		if (durationArray[1].contains("H")){
    			duration.setMinutes(Long.parseLong(getSubstring(durationArray[1], "H", "M")));
    			duration.setHours(Long.parseLong(getSubstring(durationArray[1], null, "H")));
    		}else{
    			duration.setMinutes(Long.parseLong(getSubstring(durationArray[1], null, "M")));
    		}
        }else if (durationArray[1].contains("H")){
			duration.setHours(Long.parseLong(getSubstring(durationArray[1], null, "H")));
		}
        
        return duration;
        
            }
    
    /**
     * Select the substring that matches between the two given strings 
     * EX: getSubstring("Example", "xa", "le") returns : "mp" 
     * @param string string that contains the substring
     * @param begin string that delimit the begin of substring when matches (excluding this string) (0 if null) 
     * @param end string that delimit the end of substring when matches (excluding this string) (length of main string if null)
     * @return
     */
    private String getSubstring(String string, String begin, String end){
    	
    	int intBeg = 0;
    	int intEnd = string.length();
    	if (begin!=null)    	
    	 intBeg = string.indexOf(begin)+begin.length();
    	if (end!=null)
    		intEnd = string.indexOf(end);
    	string = string.substring(intBeg,intEnd);
    	
    	return string;
    	
    }
	
}
