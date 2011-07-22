package com.phonegap.calendar.android.adapters;

/**
 * This class instances are objects that represent the info relative to
 * Duration tag in the recurrence string of EventEntry, we have here parse
 * methods for the conversion EventEntry - > Event and viceversa
 * This class attributes and methods are implemented by following the Ical Specifications
 * described by IETF in :  @link http://www.ietf.org/rfc/rfc2445.txt  
 * @author Sergio Martinez Rodriguez
 */
public class Duration {

	/**
	 * Hours in duration
	 */
	private long hours;
	/**
	 * Minutes in duration
	 */
	private long minutes;
	/**
	 * Seconds in duration
	 */
	private long seconds;
	
	/**
	 *  Constructor initialize the hours,minutes and seconds at 0. 
	 */
	public Duration(){
		this.hours = 0;
		this.minutes = 0;
		this.seconds = 0;
	}
	
	/**
	 * Gets hours attribute
	 * @return hours long
	 */
	public long getHours() {
		return hours;
	}
	/**
	 * Sets the hours attribute
	 * @param hours hours long
	 */
	public void setHours(long hours) {
		this.hours = hours;
	}
	/**
	 * Gets minutes attribute
	 * @return minutes long
	 */
	public long getMinutes() {
		return minutes;
	}
	/** 
	 * Sets the minutes attribute
	 * @param minutes minutes long
	 */
	public void setMinutes(long minutes) {
		this.minutes = minutes;
	}
	/**
	 * Gets seconds attribute
	 * @return seconds long 
	 */	
	public long getSeconds() {
		return seconds;
	}
	/**
	 * Sets the seconds attribute
	 * @param seconds seconds long
	 */
	public void setSeconds(long seconds) {
		this.seconds = seconds;
	}
	
	/**
	 * Writes the Duration object as String with the rfc2445 ietf format
	 * for the DURATION label in ICal spec format
	 * @return result String with rfc2445 format
	 */
	public String getICalFormat(){
		
		String result = "DURATION:PT";
		
		result.concat(String.valueOf(hours)+"H");
		result.concat(String.valueOf(hours)+"M");
		result.concat(String.valueOf(hours)+"S");
		result.concat("\n");
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Duration [hours=" + hours + ", minutes=" + minutes
				+ ", seconds=" + seconds + "]";
	}
	
	
	
}
