package com.safetynet.safetynetalertsapi.model.dto;

public class FireStationDTO {
	private String address;
	
	private int station;

	public FireStationDTO(String address, int station) {
		this.address = address;
		this.station = station;
	}

	public int getStation() {
		return station;
	}

	public String getAddress() {
		return address;
	}
	
	@Override
	public String toString() {
		return this.station + this.address;  
	}
}
