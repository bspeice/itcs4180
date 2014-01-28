package com.uncc.itcs4180.PartOne;

class User
{
	private String fname;
	private char minit;
	private String lname;
	private int age;
	
	public User()
	{
		fname = "Default";
		minit = 'D';
		lname = "User";
		age = 0;
	}
	
	public User(String fname, char minit, String lname, int age)
	{
		this.fname = fname;
		this.minit = minit;
		this.lname = lname;
		this.age = age;
	}
	
	public String getFname()
	{
		return this.fname;
	}
	
	public char getMinit()
	{
		return this.minit;
	}
	
	public String getLname()
	{
		return this.lname;
	}
	
	public int getAge()
	{
		return this.age;
	}
	
	public boolean equals(User user)
	{
		if(this.fname.equals(user.getFname()) && this.minit==user.getMinit() && this.lname.equals(user.getLname()) && this.age==user.getAge())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		String userString = fname+" "+minit+" "+lname+" "+age;
		return userString;
	}
}