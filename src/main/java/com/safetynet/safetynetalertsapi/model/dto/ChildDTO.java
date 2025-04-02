package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.safetynet.safetynetalertsapi.model.Identity;

public class ChildDTO {
	@JsonUnwrapped
	private Identity identity;
	
	private int age;
	
	@JsonProperty
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
		
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public List<FamilyMemberDTO> getHouseHoldMembers() {
		return houseHoldMembers;
	}
	
	public void setHouseHoldMembers(List<FamilyMemberDTO> houseHoldMembers) {
		this.houseHoldMembers = houseHoldMembers;
	}
	
	public String toString() {
		return this.identity + " " + this.age + " " + this.houseHoldMembers;
	}
}
