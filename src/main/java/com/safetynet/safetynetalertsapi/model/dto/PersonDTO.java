package com.safetynet.safetynetalertsapi.model.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Address;
import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;

public class PersonDTO implements Identifiable {
	@JsonUnwrapped
	private Identity identity;
	
	@JsonUnwrapped
	private Address address;
	
	private String phone;
	
	private String email;

	public PersonDTO() {}

    public PersonDTO(Identity identity, Address address, String phone, String email) {
    	this.identity = identity;
    	this.address = address;
    	this.phone = phone;
    	this.email = email;
	}

	public Identity getIdentity() {
		return identity;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	@Override
	public String toString() {
		return this.identity
		+ " " + this.address 
		+ " " + this.phone 
		+ " " + this.email; 
	}
}
