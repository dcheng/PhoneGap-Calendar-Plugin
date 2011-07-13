package com.phonegap.calendar.android.model;

import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public class When {

	@Key("@startTime")
	public DateTime startTime;

	@Key("@endTime")
	public DateTime endTime;
	
	@Key("@valueString")
	public String valueString;

	@Key("gd:reminder")
	public List<Reminder> reminders;
}
