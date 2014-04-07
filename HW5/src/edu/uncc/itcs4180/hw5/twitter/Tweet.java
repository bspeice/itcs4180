package edu.uncc.itcs4180.hw5.twitter;

// Code adapted from:
// https://github.com/Rockncoder/TwitterTutorial/blob/master/src/com/example/TwitterTutorial/Tweet.java
//
// We changed the field names to conform to Java standards

import com.google.gson.annotations.SerializedName;

public class Tweet {

	@SerializedName("created_at")
	private String dateCreated;

	@SerializedName("id")
	private String id;

	@SerializedName("text")
	private String text;

	@SerializedName("in_reply_to_status_id")
	private String inReplyToStatusId;

	@SerializedName("in_reply_to_user_id")
	private String inReplyToUserId;

	@SerializedName("in_reply_to_screen_name")
	private String inReplyToScreenName;

	@SerializedName("user")
	private TwitterUser user;

	public String getDateCreated() {
		return dateCreated;
	}

	public String getId() {
		return id;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public String getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public String getInReplyToUserId() {
		return inReplyToUserId;
	}

	public String getText() {
		return text;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public void setInReplyToStatusId(String inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public void setInReplyToUserId(String inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUser(TwitterUser user) {
		this.user = user;
	}

	public TwitterUser getUser() {
		return user;
	}

	@Override
	public String toString() {
		return getText();
	}
}