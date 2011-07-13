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


public class Event implements Cloneable{	

	private Author author;
	private Comments comments;
	private String content;
	private String eventStatus;
	private String guestsCanModify;
	private String guestsCanInviteOthers;
	private String anyoneCanAddSelf;
	private String id;
	private List<Link> links;
	private Recurrence recurrence;
	private String sendEventNotifications;
	private String sequence;
	private String summary;
	private String title;
	private String transparency;
	private String uid;
	private String updated;
	private String visibility;
	private List<When> when;
	private List<Where> where;
	private List<Who> who;
	
	
	public Event(){
		
	}
	
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
	

	public Author getAuthor() {
		return author;
	}


	public void setAuthor(Author author) {
		this.author = author;
	}


	public Comments getComments() {
		return comments;
	}


	public void setComments(Comments comments) {
		this.comments = comments;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getEventStatus() {
		return eventStatus;
	}


	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}


	public String getGuestsCanModify() {
		return guestsCanModify;
	}


	public void setGuestsCanModify(String guestsCanModify) {
		this.guestsCanModify = guestsCanModify;
	}


	public String getGuestsCanInviteOthers() {
		return guestsCanInviteOthers;
	}


	public void setGuestsCanInviteOthers(String guestsCanInviteOthers) {
		this.guestsCanInviteOthers = guestsCanInviteOthers;
	}


	public String getAnyoneCanAddSelf() {
		return anyoneCanAddSelf;
	}


	public void setAnyoneCanAddSelf(String anyoneCanAddSelf) {
		this.anyoneCanAddSelf = anyoneCanAddSelf;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public List<Link> getLinks() {
		return links;
	}


	public void setLinks(List<Link> links) {
		this.links = links;
	}


	public Recurrence getRecurrence() {
		return recurrence;
	}


	public void setRecurrence(Recurrence recurrence) {
		this.recurrence = recurrence;
	}


	public String getSendEventNotifications() {
		return sendEventNotifications;
	}


	public void setSendEventNotifications(String sendEventNotifications) {
		this.sendEventNotifications = sendEventNotifications;
	}


	public String getSequence() {
		return sequence;
	}


	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getTransparency() {
		return transparency;
	}


	public void setTransparency(String transparency) {
		this.transparency = transparency;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getUpdated() {
		return updated;
	}


	public void setUpdated(String updated) {
		this.updated = updated;
	}


	public String getVisibility() {
		return visibility;
	}


	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}


	public List<When> getWhen() {
		return when;
	}


	public void setWhen(List<When> when) {
		this.when = when;
	}


	public List<Where> getWhere() {
		return where;
	}


	public void setWhere(List<Where> where) {
		this.where = where;
	}


	public List<Who> getWho() {
		return who;
	}


	public void setWho(List<Who> who) {
		this.who = who;
	}		

}
