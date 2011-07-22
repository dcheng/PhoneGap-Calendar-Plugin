/**
* Contains information about the calendar Event.
* @constructor
* @param {DOMString} id unique identifier
* @param {DOMString} description A description of the event.
* @param {DOMString} location A plain text description of the location of the event.
* @param {DOMString} summary A summary of the event.
* @param {DOMString} start The start date and time of the event as a valid date or time string.
* @param {DOMString} end The end date and time of the event as a valid date or time string.
* @param {DOMString} status An indication of the user's status of the event.
* @param {DOMString} transparency An indication of the display status to set for the event.
* @param {CalendarRepeatRule} recurrence The recurrence or repetition rule for this event
* @param {DOMString} reminder A reminder for the event.
*/
var CalendarEvent = function (	id, description, location, summary, start, end, status, transparency, recurrence, reminder){
	this.id = id || null;
	this.description = description || null;
	this.location = location || null;
	this.summary = summary || null;
	this.start = start || null;
	this.end = end || null;
	this.status = status || null;
	this.transparency = transparency || null;
	this.recurrence = recurrence || null;
	this.reminder = reminder || null;
}
	

/**
* Contains information about the recurrence of a calendar event item.
* @constructor
* @param {DOMString} frecuency The frequency of the CalendarRepeatRule. Must be 'daily', 'weekly', 'monthly', 'yearly'.
* @param {unsigned short} interval A positive integer defining how often the recurrence rule must repeat.
* @param {DOMString} expires The date and time to which the CalendarRepeatRule applies as a valid date or time string.
* @param {DOMString} exceptionDates One or more dates and times to which the CalendarRepeatRule does not apply as valid date or time string strings.
* @param {short[]} daysInWeek The day or days of the week for which the CalendarRepeatRule applies.
* @param {short[]} daysInMonth The day or days of the month for which the CalendarRepeatRule applies.
* @param {short[]} daysInYear The day or days of the year for which the CalendarRepeatRule applies.
* @param {short[]} weeksInMonth he day or days of the month for which the CalendarRepeatRule applies.
* @param {short[]} monthsInYear The month or months of the year for which the CalendarRepeatRule applies.
*/
var CalendarRepeatRule = function ( frecuency, interval, expires, exceptionDates, daysInWeek, daysInMonth, daysInYear, weeksInMonth, monthsInYear){
	this.frecuency = frecuency || null;
	this.interval = interval || null;
	this.expires = expires || null;
	this.exceptionDates = exceptionDates || null;
	this.daysInWeek = daysInWeek || null;
	this.daysInMonth = daysInMonth || null;
	this.daysInYear = daysInYear || null;
	this.weeksInMonth = weeksInMonth || null;
	this.monthInYear = monthsInYear || null;	
}	



/**
 * describes the options that can be applied to calendar searching.
 * @param {CalendarEventFilter} filter A search filter with which to search and initially filter the Calendar database.
 * @param {boolean} multiple A boolean value to indicate whether multiple Calendar objects are returnable as part of the associated Calendar findEvents() operation.
 */
var CalendarFindOptions = function (filter, multiple){
	  this.filter = filter || '';
	  this.multiple = multiple || true;	  
}

/**
 * captures the searchable parameters for finding calendar event items.
 * @param {DOMString} startBefore Search for Calendar Events that start before the time provided as a valid date or time string.
 * @param {DOMString} startAfter Search for Calendar Events that start after the time provided as a valid date or time string.
 * @param {DOMString} endBefore Search for Calendar Events that end before the time provided as a valid date or time string.
 * @param {DOMString} endAfter Search for Calendar Events that end after the time provided as a valid date or time string.
 */
var CalendarEventfilter = function (startBefore, startAfter, endBefore, endAfter){
	this.startBefore = startBefore;
	this.startAfter = startAfter;
	this.endBefore = endBefore;
	this.endAfter = endAfter;
}


/**
 *  CalendarError.
 *  An error code assigned by an implementation when an error has occurreds
 * @constructor
 */
var CalendarError = function() {
    this.code=null;
};

/**
 * Error codes
 */
CalendarError.UNKNOWN_ERROR = 0;
CalendarError.INVALID_ARGUMENT_ERROR = 1;
CalendarError.TIMEOUT_ERROR = 2;
CalendarError.PENDING_OPERATION_ERROR = 3;
CalendarError.IO_ERROR = 45;
CalendarError.NOT_SUPPORTED_ERROR = 5;
CalendarError.PERMISSION_DENIED_ERROR = 20;



/**
 *  Calendar  
 * @constructor
 */
var Calendar = function() { 

}


/**
* This function creates a new event, but it does not persist the event
* to device storage. To persist the event to device storage, invoke
* calendar.save().
* @param properties an object who's properties will be examined to create a new Contact
* @returns new Contact object
*/
Calendar.prototype.create = function(properties) {
    var i;
	var event = new CalendarEvent();
    for (i in properties) {
        if (event[i] !== 'undefined') {
        	event[i] = properties[i];
        }
    }
    return event;
};

/**
* Removes calendar from device storage.
* @param successCB success callback
* @param errorCB error callback
*/
CalendarEvent.prototype.remove = function(successCB, errorCB) {
    if (this.id === null) {
        var errorObj = new ContactError();
        errorObj.code = ContactError.NOT_FOUND_ERROR;
        errorCB(errorObj);
    }
    else {
        PhoneGap.exec(successCB, errorCB, "CalendarPlugin", "remove", [this.id]);
    }
};


/**
* Persists calendar to device storage.
* @param successCB success callback
* @param errorCB error callback
*/
CalendarEvent.prototype.save = function(successCB, errorCB) {		
    PhoneGap.exec(successCB, errorCB, "CalendarPlugin", "save", [this]);
};


/**
* This function returns and array of events of calendar.  It is required as we need to convert raw
* JSON objects into concrete Calendar objects.  Currently this method is called after
* navigator.service.calendar.find but before the find methods success call back.
*
* @param jsonArray an array of JSON Objects that need to be converted to Contact objects.
* @returns an array of Contact objects
*/
Calendar.prototype.cast = function(pluginResult) {
	var events = [];
	var i;
	for (i=0; i<pluginResult.message.length; i++) {
		events.push(window.plugins.calendar.create(pluginResult.message[i]));
	}
	pluginResult.message = events;
	return pluginResult;
};
/**
* @param options The options for the search
* @param successCallback The callback which will be called when calendar is successful
* @param failureCallback The callback which will be called when calendar encouters an error
*/
Calendar.prototype.find = function(options,successCallback, failureCallback) {
	
   return PhoneGap.exec(successCallback,    //Callback which will be called when calendar is successful
   					failureCallback,     //Callback which will be called when calendar encounters an error
   					'CalendarPlugin',  //Telling PhoneGap that we want to run "Calendar" Plugin
   					'find',              //Telling the plugin, which action we want to perform
   					[options]);        //Passing a list of arguments to the plugin
};

/**
* <ul>
* <li>Register the Calendar Javascript plugin.</li>
* <li>Also register native call which will be called when this plugin runs</li>
* </ul>
*/
PhoneGap.addConstructor(function() {
	//Register the javascript plugin with PhoneGap
	PhoneGap.addPlugin('calendar', new Calendar());
	
	//Register the native class of plugin with PhoneGap
	PluginManager.addService("CalendarPlugin","com.trial.phonegap.plugin.calendar.CalendarPlugin");
});