package com.safetynet.safetynetalertsapi.model;



/**
 * This class represents a person in the SafetyNet Alerts system.
 * It contains the personal information of individuals.
 * 
 * The information includes the person's name, address, city, zip code, phone number,
 * and email address. This class is used to store and manage contact details for individuals in the system.
 * 
 */
public class Person {
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String zip;
	private String phone;
	private String email;
	
	public Person(String firstName, String lastName, String address, String city, String zip, String phone,
			String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.phone = phone;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}
	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String toString() {
		return "firstname: " + this.firstName 
		+ "\n lastname: " + this.lastName
		+ "\n address: " + this.address  
		+ "\n city: " + this.city
		+ "\n zip: " + this.zip 
		+ "\n phone: " + this.phone 
		+ "\n email: " + this.email; 
	}
}

