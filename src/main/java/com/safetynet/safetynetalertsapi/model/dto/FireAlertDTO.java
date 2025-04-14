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
	
	public void setStationNumber(int stationNumber) {
		this.stationNumber = stationNumber;
	}
	
	public List<AlertPersonInfoDTO> getResidents() {
		return persons;
	}
	
	public void setResidents(List<AlertPersonInfoDTO> persons) {
		this.persons = persons;
	}
}
