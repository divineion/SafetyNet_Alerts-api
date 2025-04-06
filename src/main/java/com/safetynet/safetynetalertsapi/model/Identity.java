package com.safetynet.safetynetalertsapi.model;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Identity {
	private String firstName;
	
	private String lastName;

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
	
	@Override
	public String toString() {
		return this.firstName + " " + this.lastName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Identity identity = (Identity) o;
		return Objects.equals(firstName, ((Identity) o).getFirstName()) &&
				Objects.equals(lastName, ((Identity) o).getLastName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
	}
}
