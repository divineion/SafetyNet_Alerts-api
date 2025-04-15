package com.safetynet.safetynetalertsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Address {
	private String address;

	private String city;

	private String zip;

	public Address(@JsonProperty("address") String address, @JsonProperty("city") String city, @JsonProperty("zip") String zip) {
		this.address = address;
		this.city = city;
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getZip() {
		return zip;
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
