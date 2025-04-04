package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

public class CoveredPhoneDTO {
	private int stationNumber;
	
	private List<String> phones;

	public CoveredPhoneDTO(int stationNumber, List<String> phones) {
		this.stationNumber = stationNumber;
		this.phones = phones;
	}

	public int getStationNumber() {
		return stationNumber;
	}

	public void setStationNumber(int stationNumber) {
		this.stationNumber = stationNumber;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
}
