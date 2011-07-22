package com.phonegap.calendar.android.adapters;

import java.util.List;

import com.phonegap.calendar.android.model.Author;
import com.phonegap.calendar.android.model.Comments;
import com.phonegap.calendar.android.model.EventEntry;
import com.phonegap.calendar.android.model.Link;
import com.phonegap.calendar.android.model.Value;
import com.phonegap.calendar.android.model.When;
import com.phonegap.calendar.android.model.Where;
import com.phonegap.calendar.android.model.Who;


/**
 * This class represents a Calendar event object, really is not implementing any operation,
 * apart from build the EventEntry from Event object and viceversa, but this object there 
 * will be used in order to be persisted in the user calendar.  
 * @author Sergio Martinez Rodriguez 
 */
public class Event implements Cloneable{	

	/**
	 * Author of event
	 */
	private Author author;
	/**
	 * Comments in event
	 */
	private Comments comments;
	/**
	 * Content of event
	 */
	private String content;
	/**
	 * Status of event "Confirmed", "Cancelled", "Tentative"
	 */
	private String eventStatus;
	/**
	 * Informs about if guests can modify the event or not
	 */
	private String guestsCanModify;
	/**
	 * Informs about if guests can invite others to event or not
	 */
	private String guestsCanInviteOthers;
	/**
	 * Informs about if anyone can add self to event or not
	 */
	private String anyoneCanAddSelf;
	/**
	 * Event Id
	 */
	private String id;
	/**
	 * links included on the event
	 */
	private List<Link> links;
	/**
	 * Event recurrence options
	 */
	private Recurrence recurrence;
	/**
	 * Event notifications
	 */
	private String sendEventNotifications;
	/**
	 * Event sequence 
	 */
	private String sequence;
	/**
	 * Event summary
	 */
	private String summary;
	/**
	 * Event title
	 */
	private String title;
	/**
	 * Event transparency "Opaque", "Transparent"
	 */
	private String transparency;
	/**
	 * Event uid
	 */
	private String uid;
	/**
	 * Indicates if the event has been modified
	 */
	private String updated;
	/**
	 * Visibility of event 
	 */
	private String visibility;
	/**
	 * List of When objects that indicates when the event takes place
	 */
	private List<When> when;
	/**
	 * List of Where objects that indicates where the event takes place
	 */
	private List<Where> where;
	/**
	 * List of Who objects that indicates who is attending to event
	 */
	private List<Who> who;
	
	
	/**
	 * Constructor 
	 */
	public Event(){
		
	}
	
	/**
	 * This constructor makes an instance of Event using an EventEntry object
	 * @param eventEntry
	 */
	public Event(EventEntry eventEntry) {
		
		this.author = eventEntry.author;
		this.comments = eventEntry.comments;
		this.content = eventEntry.content;
		this.id = eventEntry.id;
		this.links = eventEntry.links;
		this.summary = eventEntry.summary;
		this.title = eventEntry.title;
		this.updated = eventEntry.updated;
		this.when = eventEntry.when;
		this.where = eventEntry.where;
		this.who = eventEntry.who;
		
		this.transparency = (eventEntry.transparency !=null) 
			? eventEntry.transparency.value : null;
		this.uid = (eventEntry.uid.value !=null) 
			? eventEntry.uid.value : null;
		this.visibility = (eventEntry.visibility!=null) 
			? eventEntry.visibility.value : null;
		this.eventStatus = (eventEntry.eventStatus !=null) 
			? eventEntry.eventStatus.value : null;
		this.guestsCanModify = (eventEntry.guestsCanModify !=null) 
			? eventEntry.guestsCanModify.value : null;
		this.guestsCanInviteOthers = (eventEntry.guestsCanInviteOthers !=null) 
			? eventEntry.guestsCanInviteOthers.value : null;
		this.anyoneCanAddSelf = (eventEntry.anyoneCanAddSelf !=null) 
			? eventEntry.anyoneCanAddSelf.value : null;		
		this.sendEventNotifications = (eventEntry.sendEventNotifications!=null) 
			? eventEntry.sendEventNotifications.value : null;
		this.sequence = (eventEntry.sequence !=null) 
			? eventEntry.sequence.value : null;
		
		this.recurrence = (eventEntry.recurrence!=null)
			? new Recurrence(eventEntry.recurrence) : null;
		
	}
	
	/**
	 * Constructor using fields
	 * @param author author attribute
	 * @param comments comments attribute
	 * @param content content attribute 
	 * @param eventStatus eventStatus attribute 
	 * @param guestsCanModify guestsCanModify attribute
	 * @param guestsCanInviteOthers guestsCanInviteOthers attribute
	 * @param anyoneCanAddSelf anyoneCanAddSelf attribute
	 * @param id id attribute
	 * @param links links attribute 
	 * @param recurrence recurrence attribute
	 * @param sendEventNotifications sendEventNotifications attribute
	 * @param sequence sequence attribute
	 * @param summary summary attribute
	 * @param title title attribute 
	 * @param transparency transparency attribute
	 * @param uid uid attribute
	 * @param updated updated attribute
	 * @param visibility visibility attribute
	 * @param when when attribute
	 * @param where where attribute
	 * @param who who attribute
	 */
	public Event(Author author, Comments comments, String content,
			String eventStatus, String guestsCanModify,
			String guestsCanInviteOthers, String anyoneCanAddSelf, String id,
			List<Link> links, Recurrence recurrence,
			String sendEventNotifications, String sequence, String summary,
			String title, String transparency, String uid, String updated,
			String visibility, List<When> when, List<Where> where, List<Who> who) {
		super();
		this.author = author;
		this.comments = comments;
		this.content = content;
		this.eventStatus = eventStatus;
		this.guestsCanModify = guestsCanModify;
		this.guestsCanInviteOthers = guestsCanInviteOthers;
		this.anyoneCanAddSelf = anyoneCanAddSelf;
		this.id = id;
		this.links = links;
		this.recurrence = recurrence;
		this.sendEventNotifications = sendEventNotifications;
		this.sequence = sequence;
		this.summary = summary;
		this.title = title;
		this.transparency = transparency;
		this.uid = uid;
		this.updated = updated;
		this.visibility = visibility;
		this.when = when;
		this.where = where;
		this.who = who;
	}

	/**
	 * Creates and return an EventEntry instance using this current
	 * Event object
	 * @return EventEntry instance
	 */
	public EventEntry getEventEntry(){
	    EventEntry eventEntry = new EventEntry();
	    
	    eventEntry.author = this.author;
		eventEntry.comments = this.comments;
		eventEntry.content = this.content;

		eventEntry.id = this.id;
		eventEntry.links = this.links;
		eventEntry.recurrence = (this.recurrence!=null) 
			? this.recurrence.getRecurrenceRuleAsString() : null;
		eventEntry.summary = this.summary;
		if (this.title!=null){
			eventEntry.title = this.title;
		}else throw new NullPointerException("Title field can not be null");
		eventEntry.updated = this.updated;
 
		if (this.when!=null){
			eventEntry.when = this.when;
		}else throw new NullPointerException("When field can not be null");
		eventEntry.where = this.where;
		eventEntry.who = this.who;

		eventEntry.transparency = new Value();
		eventEntry.transparency.value = (this.transparency != null)
			? this.transparency : EventEntry.TRANSPARENCY_OPAQUE;

		eventEntry.eventStatus = new Value();
		eventEntry.eventStatus.value = (this.eventStatus!=null) 
			? this.eventStatus : EventEntry.EVENT_STATUS_CONFIRMED;
		
		eventEntry.visibility = new Value();
		eventEntry.visibility.value = (this.visibility!=null) 
			? this.visibility : EventEntry.VISIBILITY_DEFAULT;
		
		//Those fields are not necessary to be filled in order to create a new event  
//		eventEntry.sendEventNotifications.value = this.sendEventNotifications;
//		eventEntry.sequence.value = this.sequence;
//		eventEntry.uid.value = this.uid;		
//		eventEntry.guestsCanModify.value = (this.guestsCanModify != null)
//			? this.guestsCanModify : null;
//		eventEntry.guestsCanInviteOthers.value = (this.guestsCanInviteOthers!=null)
//			? this.guestsCanInviteOthers : null;
//		eventEntry.anyoneCanAddSelf.value = (this.anyoneCanAddSelf!=null) 
//			? this.anyoneCanAddSelf : null;
		
	    return eventEntry;
		
	}
	

	/**
	 * Gets authos attribute
	 * @return Author object
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * Sets author attribute
	 * @param author Author object
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}

	/**
	 * Gets comments attribute
	 * @return Comments object 
	 */
	public Comments getComments() {
		return comments;
	}

	/**
	 * Sets comments attribute
	 * @param comments Comments object 
	 */
	public void setComments(Comments comments) {
		this.comments = comments;
	}

	/**
	 * Gets content attribute
	 * @return content String 
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets content attribute
	 * @param content content String
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets eventStatus attribute
	 * @return eventStatus String
	 */
	public String getEventStatus() {
		return eventStatus;
	}

	/**
	 * Sets eventStatus attribute
	 * @param eventStatus eventStatus String
	 */
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	/**
	 * Gets guestsCanModify attribute
	 * @return guestsCanModify String
	 */
	public String getGuestsCanModify() {
		return guestsCanModify;
	}

	/** 
	 * Sets guestsCanModify attribute
	 * @param guestsCanModify guestsCanModify String
	 */
	public void setGuestsCanModify(String guestsCanModify) {
		this.guestsCanModify = guestsCanModify;
	}

	/**
	 * Gets guestsCanInviteOthers attribute
	 * @return guestsCanInviteOthers String
	 */
	public String getGuestsCanInviteOthers() {
		return guestsCanInviteOthers;
	}

	/**
	 * Sets guestsCanInviteOthers attribute
	 * @param guestsCanInviteOthers guestsCanInviteOthers String
	 */
	public void setGuestsCanInviteOthers(String guestsCanInviteOthers) {
		this.guestsCanInviteOthers = guestsCanInviteOthers;
	}

	/**
	 * Gets anyoneCanAddSelf attribute
	 * @return anyoneCanAddSelf String
	 */
	public String getAnyoneCanAddSelf() {
		return anyoneCanAddSelf;
	}

	/**
	 * Sets anyoneCanAddSelf attribute
	 * @param anyoneCanAddSelf anyoneCanAddSelf String
	 */
	public void setAnyoneCanAddSelf(String anyoneCanAddSelf) {
		this.anyoneCanAddSelf = anyoneCanAddSelf;
	}


	/**
	 * Gets id attribute
	 * @return id String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id attribute
	 * @param id id String
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets links attribute  
	 * @return List of Links objects
	 */
	public List<Link> getLinks() {
		return links;
	}

	/**
	 * Gets links attribute  
	 * @param links List of Links objects
	 */
	public void setLinks(List<Link> links) {
		this.links = links;
	}

	/**
	 * Gets recurrence attribute
	 * @return Recurrence object  
	 */
	public Recurrence getRecurrence() {
		return recurrence;
	}

	/**
	 * Sets recurrence attribute
	 * @param recurrence Recurrence object
	 */
	public void setRecurrence(Recurrence recurrence) {
		this.recurrence = recurrence;
	}

	/**
	 * Gets sendEventNotifications attribute
	 * @return sendEventNotifications String
	 */
	public String getSendEventNotifications() {
		return sendEventNotifications;
	}

	/**
	 * Sets sendEventNotifications attribute
	 * @param sendEventNotifications sendEventNotifications String
	 */
	public void setSendEventNotifications(String sendEventNotifications) {
		this.sendEventNotifications = sendEventNotifications;
	}

	/**
	 * Gets sequence attribute
	 * @return sequence String
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * Sets sequence attribute
	 * @param sequence sequence String
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * Gets summary attribute
	 * @return summary String
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Sets summary attribute
	 * @param summary summary String
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Gets title attribute
	 * @return title String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets title attribute
	 * @param title title String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets transparency attribute
	 * @return transparency String
	 */
	public String getTransparency() {
		return transparency;
	}

	/**
	 * Sets transparency attribute
	 * @param transparency transparency String
	 */
	public void setTransparency(String transparency) {
		this.transparency = transparency;
	}

	/**
	 * Gets uid attribute
	 * @return uid String
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Sets uid attribute
	 * @param uid uid String
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Gets updated Attribute
	 * @return updated String
	 */
	public String getUpdated() {
		return updated;
	}

	/**
	 * Sets updated Attribute
	 * @param updated updated String
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	/**
	 * Gets visibility attribute
	 * @return visibility String 
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * Sets visibility attribute
	 * @param visibility visibility String
	 */
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	/**
	 * Gets when attribute
	 * @return List of all the when objects linked to this Event
	 */
	public List<When> getWhen() {
		return when;
	}

	/**
	 * Sets when attribute
	 * @param when List of all the when objects you want link to this Event
	 */
	public void setWhen(List<When> when) {
		this.when = when;
	}

	/**
	 * Gets where attribute
	 * @return List of all the where objects linked to this Event
	 */
	public List<Where> getWhere() {
		return where;
	}

	/**
	 * Sets where attribute
	 * @param where List of all the where objects you want link to this Event
	 */
	public void setWhere(List<Where> where) {
		this.where = where;
	}

	/**
	 * Gets who attribute
	 * @return List of all the who objects linked to this Event
	 */
	public List<Who> getWho() {
		return who;
	}

	/**
	 * Sets who attribute 
	 * @param who List of all the who objects you want link to this Event
	 */
	public void setWho(List<Who> who) {
		this.who = who;
	}		

}
