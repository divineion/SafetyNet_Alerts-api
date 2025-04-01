package com.safetynet.safetynetalertsapi.model.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Identity;

public class FamilyMemberDTO {
	@JsonUnwrapped
	private Identity identity;
	
	
	public FamilyMemberDTO(Identity identity) {
		this.identity = identity;
	}
	
	public Identity getIdentity() {
		return identity;
	}
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	
	@Override
	public String toString() {
		return this.identity.toString();
	}
}
