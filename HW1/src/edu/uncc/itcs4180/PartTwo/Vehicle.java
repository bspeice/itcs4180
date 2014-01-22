package edu.uncc.itcs4180.PartTwo;

/* Assignment: Homework 1
 * File name: Vehicle.java
 * Group:
 * 	Bradlee Speice
 * 	Brandon Rodenmayer
 */

public class Vehicle {
	
	private int modelYear;
	private String manufacturerName;
	private String modelName;
	private int horsePower;
	private int noCylinders;
	private int noGears;
	
	public int getModelYear() {
		return modelYear;
	}
	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public int getHorsePower() {
		return horsePower;
	}
	public void setHorsePower(int horsePower) {
		this.horsePower = horsePower;
	}
	public int getNoCylinders() {
		return noCylinders;
	}
	public void setNoCylinders(int noCylinders) {
		this.noCylinders = noCylinders;
	}
	public int getNoGears() {
		return noGears;
	}
	public void setNoGears(int noGears) {
		this.noGears = noGears;
	}

}
