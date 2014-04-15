package edu.uncc.itcs4180.hw5.twitter;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * TwitterAuthentication.java
 */

import com.google.gson.annotations.SerializedName;

public class TwitterAuthentication {
	@SerializedName("token_type")
	String tokenType;
	
	@SerializedName("access_token")
	String accessToken;
}
