package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

public class FireAlertDTO {
	private int stationNumber;
	private List<AlertPersonInfoDTO> persons;
	
	public FireAlertDTO(int stationNumber, List<AlertPersonInfoDTO> persons) {
		this.stationNumber = stationNumber;
		this.persons = persons;
	}
	
	public int getStationNumber() {
		return stationNumber;
	}
	
	public List<AlertPersonInfoDTO> getPersons() {
		return persons;
	}
}
