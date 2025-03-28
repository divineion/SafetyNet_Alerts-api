package com.safetynet.safetynetalertsapi.model;

public class Address {
	private String address;
	
	private String city;
	
	private String zip;

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
		return "address: " + this.address +
				"\n city: " + this.city +
				"\n zip: " + this.zip;
	}
}
