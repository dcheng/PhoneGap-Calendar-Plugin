package com.phonegap.calendar.android.adapters;

import com.phonegap.calendar.android.utils.DateUtils;

import android.util.Log;

public class Recurrence {

	private static final String TAG = "Recurrence";
	
	private Dt dtStart;
	private Dt dtEnd;
	private Duration duration;
	private Rule rule;
	
	public Recurrence (){
		this.dtStart = null;
		this.dtEnd = null;
		this.duration = null;
		this.rule = null;
	}
	
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
	
	public Dt getDtStart() {
		return dtStart;
	}

	public void setDtStart(Dt dtStart) {
		this.dtStart = dtStart;
	}

	public Dt getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(Dt dtEnd) {
		this.dtEnd = dtEnd;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public String getRecurrenceRuleAsString(){
		String result;
		
		result = dtStart.getICalFormat("DTSTART") + 
		((dtEnd != null) ? "" : dtEnd.getICalFormat("DTEND")) + 
		((duration != null) ? "" : duration.getICalFormat()) +
		rule.getICalFormat() + "\n";  
		
		return result;
	}
	
	private Rule parseRule(String string) {
		
    	String [] ruleArray = string.split(":");
    	
    	Rule rule = new Rule();
    	
    	String [] splitedRuleArray = ruleArray[1].concat(";").split(";");
    	
    	for (int i = 0; i<splitedRuleArray.length; i++){
    	if (splitedRuleArray[i].contains("FREQ="))
    		rule.setFreq(getSubstring(splitedRuleArray[i], "FREQ=", null));
    	
    	if (splitedRuleArray[1].contains("UNTIL="))
    		rule.setUntil(DateUtils.stringCalendarDateToDate(getSubstring(splitedRuleArray[i], "UNTIL=", ";"), "yyyyMMdd'T'HHmmss'Z'"));
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
        	dt.setDate(DateUtils.stringCalendarDateToDate(dtStartArray[1], "yyyyMMdd'T'HHmmss"));
        else 
        	dt.setDate(DateUtils.stringCalendarDateToDate(dtStartArray[1], "yyyyMMdd"));

        	return dt;
    }
    
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
