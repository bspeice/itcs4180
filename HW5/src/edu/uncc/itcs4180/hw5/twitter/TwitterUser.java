package edu.uncc.itcs4180.hw5.twitter;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * TwitterUser.java
 */

// Code copied from:
// https://github.com/Rockncoder/TwitterTutorial/blob/master/src/com/example/TwitterTutorial/TwitterUser.java

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class TwitterUser implements Serializable {

	@SerializedName("screen_name")
	private String screenName;

	@SerializedName("name")
	private String name;

	@SerializedName("profile_image_url")
	private String profileImageUrl;
	
	@SerializedName("profile_background_image_url")
	private String profileBackgroundImageUrl;

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}
	
	public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
		this.profileBackgroundImageUrl = profileBackgroundImageUrl;
	}
}