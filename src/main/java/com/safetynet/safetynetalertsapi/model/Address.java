package com.safetynet.safetynetalertsapi.model;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address1 = (Address) o;
		return Objects.equals(address, address1.address) &&
				Objects.equals(city, address1.city) &&
				Objects.equals(zip, address1.zip);
	}
}
