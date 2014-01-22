package edu.uncc.itcs4180.PartTwo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		List<Vehicle> vehicles;
		
		// Read in all of our data
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			vehicles = new ArrayList<Vehicle>();
			String line;
			while ((line = reader.readLine()) != null)
				vehicles.add(readVehicle(line));
			
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file: " + filename);
			return;
		} catch (IOException e) {
			System.out.println("Error reading the CSV file.");
			return;
		}
		
		// If we've made it here, we have the final list of all our vehicles
	}
	
	private static Vehicle readVehicle(String line) {
		String[] elements = line.split(",");
		try {
			return new Vehicle(Integer.parseInt(elements[0]), // Model Year
					elements[1], // Manufacturer Name
					elements[2], // Model Name
					Integer.parseInt(elements[3]), // Horsepower
					Integer.parseInt(elements[4]), // No. Cylinders
					Integer.parseInt(elements[5])); // No. Gears
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Improperly formatted CSV file.");
			return null;
		}
	}

}
