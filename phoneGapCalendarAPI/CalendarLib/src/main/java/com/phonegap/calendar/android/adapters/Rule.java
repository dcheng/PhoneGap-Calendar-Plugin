package com.phonegap.calendar.android.adapters;

import java.util.Arrays;
import java.util.Date;

import com.phonegap.calendar.android.utils.DateUtils;

public class Rule {
	
	 private String freq;	
	 private Date until;
	 private int count;
	 private int interval;
	 private String[] bySeconds;
	 private String[] byMinute;
	 private String[] byHour;
	 private String[] byDay;
	 private String[] byMonthDay;
	 private String[] byYearDay;
	 private String[] byWeekNo;
	 private String[] byMonth;
	 private String[] bySetPos;
	 private String wkst;	
	
	 
	 
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
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getWkst() {
		return wkst;
	}
	public void setWkst(String wkst) {
		this.wkst = wkst;
	}
	public Date getUntil() {
		return until;
	}
	public void setUntil(Date until) {
		this.until = until;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public String[] getBySeconds() {
		return bySeconds;
	}
	public void setBySeconds(String[] bySeconds) {
		this.bySeconds = bySeconds;
	}
	public String[] getByMinute() {
		return byMinute;
	}
	public void setByMinute(String[] byMinute) {
		this.byMinute = byMinute;
	}
	public String[] getByHour() {
		return byHour;
	}
	public void setByHour(String[] byHour) {
		this.byHour = byHour;
	}
	public String[] getByDay() {
		return byDay;
	}
	public void setByDay(String[] byDay) {
		this.byDay = byDay;
	}
	public String[] getByMonthDay() {
		return byMonthDay;
	}
	public void setByMonthDay(String[] byMonthDay) {
		this.byMonthDay = byMonthDay;
	}
	public String[] getByYearDay() {
		return byYearDay;
	}
	public void setByYearDay(String[] byYearDay) {
		this.byYearDay = byYearDay;
	}
	public String[] getByWeekNo() {
		return byWeekNo;
	}
	public void setByWeekNo(String[] byWeekNo) {
		this.byWeekNo = byWeekNo;
	}
	public String[] getByMonth() {
		return byMonth;
	}
	public void setByMonth(String[] byMonth) {
		this.byMonth = byMonth;
	}
	public String[] getBySetPos() {
		return bySetPos;
	}
	public void setBySetPos(String[] bySetPos) {
		this.bySetPos = bySetPos;
	}
	
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
