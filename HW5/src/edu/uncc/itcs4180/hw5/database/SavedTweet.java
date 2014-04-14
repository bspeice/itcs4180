package edu.uncc.itcs4180.hw5.database;

public class SavedTweet 
{
	private long id;
	private String username;
	private String text;
	private String time;
	private String profileImageUrl;
	
	public SavedTweet()
	{
		this.id= 0;
		this.username = "";
		this.text = "";
		this.time = "";
		this.profileImageUrl = "";
	}
	
	public SavedTweet(long id, String username, String text, String time, String profileImageUrl)
	{
		this.id= id;
		this.username = username;
		this.text = text;
		this.time = time;
		this.profileImageUrl = profileImageUrl;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	@Override
	public String toString() {
		return "SavedTweet [id=" + id + ", username=" + username + ", text="
				+ text + ", time=" + time + ", profileImageUrl="
				+ profileImageUrl + "]";
	}
}
