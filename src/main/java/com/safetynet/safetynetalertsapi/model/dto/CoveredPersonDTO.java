package com.safetynet.safetynetalertsapi.model.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Address;
import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;

public class CoveredPersonDTO implements Identifiable {
	@JsonUnwrapped
	private Identity identity;
	
	@JsonUnwrapped
	private Address address;
	
	private String phone;

	public CoveredPersonDTO(Identity identity, Address address, String phone) {
		this.identity = identity;
		this.address = address;
		this.phone = phone;
	}

	public Identity getIdentity() {
		return identity;
	}

	public String getAddress() {
		return address.getAddress();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
