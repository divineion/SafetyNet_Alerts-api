package com.safetynet.safetynetalertsapi.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * This class represents a person in the SafetyNet Alerts system.
 * It contains the personal information of individuals.
 * 
 * The information includes the person's name, address, city, zip code, phone number,
 * and email address. This class is used to store and manage contact details for individuals in the system.
 * 
 */
public class Person implements Identifiable {	
	@JsonUnwrapped
	private Identity identity;
	
	@JsonUnwrapped
	private Address address;
	
	@JsonProperty("phone")
	private String phone;
	
	@JsonProperty("email")
	private String email;

	public Person(Identity identity, Address address, String phone, String email) {
		this.identity = identity;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}

	public Person() {
	}

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
		+ " " + this.address 
		+ " " + this.phone 
		+ " " + this.email; 
	}
}

