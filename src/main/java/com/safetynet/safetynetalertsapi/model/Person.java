package com.safetynet.safetynetalertsapi.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Objects;

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
	
	private String phone;
	
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Person person = (Person) o;

		return (Objects.equals(identity, person.identity) &&
				Objects.equals(address, person.address) &&
				Objects.equals(phone, person.phone) &&
				Objects.equals(email, person.email)
		);
	}
}

