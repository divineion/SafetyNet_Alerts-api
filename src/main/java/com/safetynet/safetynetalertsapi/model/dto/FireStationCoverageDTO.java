package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FireStationCoverageDTO {
	@JsonProperty("covered persons")
	private List<CoveredPersonDTO> coveredPersonsDTO;
	
	@JsonProperty("adults")
	int adultsCounter;
	
	@JsonProperty("children")
	long childrenCounter;
	
	public FireStationCoverageDTO(List<CoveredPersonDTO> coveredPersonsDTO, int adultsCounter, long children) {
		this.coveredPersonsDTO = coveredPersonsDTO;
		this.adultsCounter = adultsCounter;
		this.childrenCounter = children;
	}

	public List<CoveredPersonDTO> getCoveredPersonsDTO() {
		return coveredPersonsDTO;
	}

	public void setCoveredPersonsDTO(List<CoveredPersonDTO> coveredPersonsDTO) {
		this.coveredPersonsDTO = coveredPersonsDTO;
	}

	public int getAdultsCounter() {
		return adultsCounter;
	}

	public void setAdultsCounter(int adultsCounter) {
		this.adultsCounter = adultsCounter;
	}

	public long getChildrenCounter() {
		return childrenCounter;
	}

	public void setChildrenCounter(int childrenCounter) {
		this.childrenCounter = childrenCounter;
	}

}
