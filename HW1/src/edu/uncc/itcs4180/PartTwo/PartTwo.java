package edu.uncc.itcs4180.PartTwo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* Assignment: Homework 1
 * File name: PartOne.java
 * Group:
 * 	Bradlee Speice
 * 	Brandon Rodenmayer
 */

public class PartTwo {
	
	public static void main(String[] args) {
		
		// Set up reading the actual data
		String filename = "raw_data.csv";
		String outputFilename = "distinct_vehicles.csv";
		List<Vehicle> vehicles;
		
		// Read in all of our data
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			vehicles = new ArrayList<Vehicle>();
			String line;
			while ((line = reader.readLine()) != null)
				vehicles.add(readVehicle(line));
			
			reader.close();			
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file: " + filename);
			return;
		} catch (IOException e) {
			System.out.println("Error reading the CSV file.");
			return;
		}
		
		// If we've made it here, we have the final list of all our vehicles,
		// but we need to find just the unique ones.
		Set<Vehicle> uniqueVehicles = new HashSet<Vehicle>(vehicles);
		
		// Write the unique vehicles to another CSV file
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));
			
			for (Vehicle v : uniqueVehicles)
				writer.write(v.toCSV() + "\n");
			
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing unique vehicles to CSV");
			// Don't return here, we can still complete the rest of the program
		}
		
		// Count the vehicles by modelYear and manufacturerName
		Map<String, Integer> vehicleCount = new HashMap<String, Integer>();
		for (Vehicle v : uniqueVehicles)
			if (vehicleCount.containsKey(vehicleMapString(v)))
				vehicleCount.put(vehicleMapString(v), vehicleCount.get(vehicleMapString(v)) + 1);
		
		// Print out the results of our counting
		// Convert to Set for iteration - http://stackoverflow.com/questions/1066589/java-iterate-through-hashmap
		int counter = 1;
		for (Map.Entry<String, Integer> v : vehicleCount.entrySet()) {
			System.out.println(counter + "-" + v.getKey() + ":" + v.getValue());
			counter++;
		}
		System.out.println("--------------------------------------------------" + 
							"------------------------------");
		System.out.println("Total: " + (counter - 1));
		System.out.println("--------------------------------------------------" + 
				"------------------------------");
		
		// Now we need to sort our counting, and get the top 10 results.
		// The way this is done is to convert the Map to a Set natively, then the Set to a List
		// This way, we preserve all Key-Value pairs during the sort.
		// Finally, Collections.sort() goes in ascending order, so reverse that instead of taking
		// the elements off the end of the list.
		List<Map.Entry<String, Integer>> vehicleCountList = new ArrayList<Map.Entry<String, Integer>>(vehicleCount.entrySet());
		Collections.sort(vehicleCountList, Collections.reverseOrder(new VehicleEntryComparator()));
		
		System.out.println("--------------------------------------------------" +
						"\nTop 10 Models:" +
						"--------------------------------------------------");
		// Go to the top 10 results, or full list, whichever comes first
		for (int i = 0; i < vehicleCountList.size() && i < 10; i++)
			System.out.println(i + "-" + vehicleCountList.get(i).getKey() + ":" +
					vehicleCountList.get(i).getValue());
			
	}
	
	private static Vehicle readVehicle(String line) {
		String[] elements = line.split(",");
		try {
			// Note that there is an extra column in the data - "Car/Truck/Both" in col. 4
			return new Vehicle(Integer.parseInt(elements[0]), // Model Year
					elements[1], // Manufacturer Name
					elements[2], // Model Name
					Integer.parseInt(elements[4]), // Horsepower
					Integer.parseInt(elements[5]), // No. Cylinders
					Integer.parseInt(elements[6])); // No. Gears
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Improperly formatted CSV file.");
			return null;
		}
	}
	
	private static String vehicleMapString(Vehicle v) {
		return v.getModelName() + " - "  + v.getManufacturerName();
	}
	
}
