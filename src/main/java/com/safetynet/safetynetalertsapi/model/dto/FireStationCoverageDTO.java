package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FireStationCoverageDTO {
	@JsonProperty("covered persons")
	private List<CoveredPersonDTO> coveredPersonsDTO;
	
	@JsonProperty("adults")
	long adultsCounter;
	
	@JsonProperty("children")
	long childrenCounter;

	@JsonCreator
	public FireStationCoverageDTO(List<CoveredPersonDTO> coveredPersonsDTO, long adultsCounter, long children) {
		this.coveredPersonsDTO = coveredPersonsDTO;
		this.adultsCounter = adultsCounter;
		this.childrenCounter = children;
	}

	public List<CoveredPersonDTO> getCoveredPersonsDTO() {
		return coveredPersonsDTO;
	}

	public long getAdultsCounter() {
		return adultsCounter;
	}

	public long getChildrenCounter() {
		return childrenCounter;
	}
}
