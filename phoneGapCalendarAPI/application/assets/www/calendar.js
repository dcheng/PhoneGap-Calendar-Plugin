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




var CalendarFindOptions = function (filter, multiple){
	  this.filter = filter || '';
	  this.multiple = multiple || true;	  
}



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