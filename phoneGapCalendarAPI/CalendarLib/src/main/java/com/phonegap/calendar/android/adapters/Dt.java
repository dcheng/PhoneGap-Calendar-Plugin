package com.phonegap.calendar.android.adapters;

import java.util.Date;

import com.phonegap.calendar.android.utils.DateUtils;

public class Dt {

	private String TimeZone;
	private Date date;
	
	public String getTimeZone() {
		return TimeZone;
	}
	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getICalFormat(String type){
		
		String result = type;
		
		if (TimeZone!=null)
			result.concat(";"+TimeZone+":");
		else
			result.concat(":");
		
		result.concat(DateUtils.dateToStringCalendarDate(date, "yyyyMMdd'T'HHmmss")+"\n");
		return result;
	}
	
	@Override
	public String toString() {
		return "Dt [TimeZone=" + TimeZone + ", date=" + date + "]";
	}
	
	
	
}
