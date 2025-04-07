package com.safetynet.safetynetalertsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safetynet.safetynetalertsapi.model.dto.FireStationDTO;

import java.util.Objects;

/**
 * Represents a fire station in the SafetyNet Alerts system.
 * 
 * This class stores the details of a fire station, including the unique station number and the address it covers.
 * Each {@link Person}'s address in the system is associated with a fire station. 
 * A fire station can be associated with multiple addresses,
 * meaning that a single station may appear multiple times in the system, each time with a different address.  
 * Attributes:
 * - address: the address covered by the fire station
 * - station: the identification number of the fire station
 */
public class FireStation {
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("station")
	private int station;

	public FireStation() {}

	public FireStation(@JsonProperty("address") String address, @JsonProperty("station") int station) {
		this.address = address;
		this.station = station;
	}
	
	public int getStation() {
		return station;
	}
	
    public void setStation(int station) {
        this.station = station;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "station: " + this.station + "\n address: " + this.address;  
	}

	@Override
	public boolean equals(Object object) {
		if (this == object ) return true;
		if (object == null || this.getClass() != object.getClass()) return false;
		FireStation fireStation = (FireStation) object;
		return Objects.equals(address, ((FireStation) object).getAddress()) &&
				Objects.equals(station, ((FireStation) object).getStation());
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, station);
	}
}
