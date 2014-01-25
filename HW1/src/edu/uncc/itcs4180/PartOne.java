/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 1
 * PartOne.java
 */
package edu.uncc.itcs4180;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PartOne 
{
	public static void main(String[] args)
	{
		SortedSet<User> uniqueUsers = new TreeSet<User>(new UserComparator());
		
		//Add all new users to uniqueUsers assuming they are not old users
		String filePath = "userListNew.txt";
		//Using modified code provided in assignment instructions
		// Lets make sure the file path is not empty or null 
		if (filePath == null || filePath.isEmpty()) 
		{ 
			System.out.println("Invalid File Path"); 
			return; 
		} 
		 
		BufferedReader inputStream = null; 
		//We need a try catch block so we can handle any potential IO errors 
		try 
		{ 
			//Try block so we can use ÅefinallyÅf and close BufferedReader 
			try 
			{ 
				inputStream = new BufferedReader(new FileReader(filePath)); 
				String lineContent = null; 
				//Loop will iterate over each line within the file. 
				//It will stop when no new lines are found. 
				while ((lineContent = inputStream.readLine()) != null) 
				{ 
					//Add user to the list of users 
					String[] resultingTokens = lineContent.split(","); 
					User tempUser= new User(resultingTokens[0].trim(), resultingTokens[1].trim().charAt(0), resultingTokens[2].trim(), Integer.parseInt(resultingTokens[3].trim()));
					uniqueUsers.add(tempUser);
				} 
			} 
			//Make sure we close the buffered reader. 
			finally 
			{ 
				if (inputStream != null) 
					inputStream.close(); 
			} 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		
		//Remove all of the old users from uniqueUsers, leaving only new users
		filePath = "userListOld.txt";
		//Using modified code provided in assignment instructions
		// Lets make sure the file path is not empty or null 
		if (filePath == null || filePath.isEmpty()) 
		{ 
			System.out.println("Invalid File Path"); 
			return; 
		} 
		 
		inputStream = null; 
		//We need a try catch block so we can handle any potential IO errors 
		try 
		{ 
			//Try block so we can use ÅefinallyÅf and close BufferedReader 
			try 
			{ 
				inputStream = new BufferedReader(new FileReader(filePath)); 
				String lineContent = null; 
				//Loop will iterate over each line within the file. 
				//It will stop when no new lines are found. 
				while ((lineContent = inputStream.readLine()) != null) 
				{ 
					//Remove the user if they exist
					String[] resultingTokens = lineContent.split(","); 
					User tempUser= new User(resultingTokens[0].trim(), resultingTokens[1].trim().charAt(0), resultingTokens[2].trim(), Integer.parseInt(resultingTokens[3].trim()));
					/*Print users as they are removed
					 * if(uniqueUsers.remove(tempUser));
					 *	System.out.println("REMOVED USER: "+tempUser);
					*/
					uniqueUsers.remove(tempUser);
				} 
			} 
			//Make sure we close the buffered reader. 
			finally 
			{ 
				if (inputStream != null) 
					inputStream.close(); 
			} 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 

		//iterator to print out all users in uniqueUsers (Sorted by age)
		for (User user : uniqueUsers) 
		{
		    System.out.print(user.getFname()+ " ");
		    System.out.print(user.getMinit()+ " ");
		    System.out.print(user.getLname()+ " ");
		    System.out.println(user.getAge());
		}
	}
}

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

class UserComparator implements Comparator<User>
{
	public int compare(User a, User b)
	{
		if(a.getFname().equals(b.getFname()) && a.getMinit()==b.getMinit() && a.getLname().equals(b.getLname()) && a.getAge()==b.getAge())
		{
			return 0;
		}
		else
		{
			if(a.getAge()>b.getAge())
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
	}
}