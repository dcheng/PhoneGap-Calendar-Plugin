package com.phonegap.calendar.android.adapters;

public class Duration {

	private long hours;
	private long minutes;
	private long seconds;
	
	public Duration(){
		this.hours = 0;
		this.minutes = 0;
		this.seconds = 0;
	}
	
	public long getHours() {
		return hours;
	}
	public void setHours(long hours) {
		this.hours = hours;
	}
	public long getMinutes() {
		return minutes;
	}
	public void setMinutes(long minutes) {
		this.minutes = minutes;
	}
	public long getSeconds() {
		return seconds;
	}
	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}
	
	public String getICalFormat(){
		
		String result = "DURATION:PT";
		
		result.concat(String.valueOf(hours)+"H");
		result.concat(String.valueOf(hours)+"M");
		result.concat(String.valueOf(hours)+"S");
		result.concat("\n");
		
		return result;
	}
	
	@Override
	public String toString() {
		return "Duration [hours=" + hours + ", minutes=" + minutes
				+ ", seconds=" + seconds + "]";
	}
	
	
	
}
