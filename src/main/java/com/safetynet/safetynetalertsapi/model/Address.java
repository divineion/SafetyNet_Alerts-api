package com.safetynet.safetynetalertsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
	private String address;
	
	private String city;
	
	private String zip;

	public Address() {}

	public Address(String address, String city, String zip) {
		this.address = address;
		this.city = city;
		this.zip = zip;
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
	
	public String toString() {
		return this.address + " " + this.city + " " + this.zip;
	}
}
