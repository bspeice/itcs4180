package edu.uncc.itcs4180.PartTwo;

import java.util.Comparator;
import java.util.Map.Entry;

// Create a comparator that can be used to easily sort our vehicle map
class VehicleEntryComparator implements Comparator<Entry<String, Integer>> {

	@Override
	public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		if (o1.getValue() > o2.getValue())
			return 1;
		else if (o1.getValue() < o2.getValue())
			return -1;
		else
			return 0;
	}
	
}