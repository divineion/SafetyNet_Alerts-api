package com.safetynet.safetynetalertsapi.model.dto;

import java.util.Objects;

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

	@Override
	public boolean equals(Object object) {
		if (this == object ) return true;
		if (object == null || this.getClass() != object.getClass()) return false;
		FireStationDTO fireStation = (FireStationDTO) object;
		return Objects.equals(address, ((FireStationDTO) object).getAddress()) &&
				Objects.equals(station, ((FireStationDTO) object).getStation());
	}
}
