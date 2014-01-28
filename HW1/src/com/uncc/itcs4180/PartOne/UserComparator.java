package com.uncc.itcs4180.PartOne;

import java.util.Comparator;

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