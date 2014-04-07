package edu.uncc.itcs4180.hw5.twitter;

import com.google.gson.annotations.SerializedName;

public class TwitterAuthentication {
	@SerializedName("token_type")
	String tokenType;
	
	@SerializedName("access_token")
	String accessToken;
}
