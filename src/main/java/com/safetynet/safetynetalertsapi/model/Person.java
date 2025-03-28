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
	private Identity identity;
	
	private Address address;
	
	private String phone;
	
	private String email;

	public Identity getIdentity() {
		return identity;
	}
	
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
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
	
	@Override
	public String toString() {
		return this.identity
		+ "\n" + this.address 
		+ "\n phone: " + this.phone 
		+ "\n email: " + this.email; 
	}
}

