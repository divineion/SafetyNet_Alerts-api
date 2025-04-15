package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Identifiable;
import com.safetynet.safetynetalertsapi.model.Identity;

public class ChildDTO implements Identifiable {
	@JsonUnwrapped
	private Identity identity;
	
	private int age;
	
	@JsonUnwrapped
	private List<FamilyMemberDTO> houseHoldMembers;
	
	public ChildDTO(Identity identity, int age, List<FamilyMemberDTO> familyMembers) {
		this.identity = identity;
		this.age = age;
		this.houseHoldMembers = familyMembers;
	}	
	
	public Identity getIdentity() {
		return identity;
	}

	public int getAge() {
		return age;
	}
	
	public List<FamilyMemberDTO> getHouseHoldMembers() {
		return houseHoldMembers;
	}
	
	public String toString() {
		return this.identity + " " + this.age + " " + this.houseHoldMembers;
	}
}
