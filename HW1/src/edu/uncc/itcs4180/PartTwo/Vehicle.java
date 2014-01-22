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
	
	public Vehicle(int modelYear, String manufacturerName, String modelName,
			int horsePower, int noCylinders, int noGears) {
		this.modelYear = modelYear;
		this.manufacturerName = manufacturerName;
		this.modelName = modelName;
		this.horsePower = horsePower;
		this.noCylinders = noCylinders;
		this.noGears = noGears;
	}
	
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

	public String toCSV() {
		return this.modelYear + "," + this.manufacturerName + "," + this.modelName + "," +  
				this.horsePower + "," + this.noCylinders + "," + this.noGears;
	}
	
	public String toString() {
		return "Model Year: " + this.modelYear + " Manufacturer Name: " + this.manufacturerName +
				" Model Name: " + this.modelName + " Horsepower: " + this.horsePower +
				" No. Cylinders: " + this.noCylinders + " No. Gears: " + this.noGears;
	}
	
	// Need to implement both equals() and hashCode() for guaranteeing unique Sets
	// http://stackoverflow.com/questions/16238182/hashset-contains-duplicate-entries
	// Also:
	// http://www.coderanch.com/t/572755/java-programmer-SCJP/certification/HashSet-adding-duplicates-hashcode-obects
	@Override
	public boolean equals(Object _v2) {
		Vehicle v2 = (Vehicle) _v2;
		return ((this.modelYear == v2.modelYear) &&
				(this.manufacturerName.equals(v2.manufacturerName)) &&
				(this.modelName.equals(v2.modelName)) &&
				(this.horsePower == v2.horsePower) &&
				(this.noCylinders == v2.noCylinders) &&
				(this.noGears == v2.noGears));
	}
	
	@Override
	public int hashCode() {
		return this.modelYear + this.manufacturerName.hashCode() + this.modelName.hashCode() +
				this.horsePower + this.noCylinders + this.noGears;
	}
}
