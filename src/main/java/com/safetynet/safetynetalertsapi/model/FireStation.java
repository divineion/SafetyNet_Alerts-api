package com.safetynet.safetynetalertsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents a fire station in the SafetyNet Alerts system.
 * <p>
 * This class stores the details of a fire station, including the unique station number and the address it covers.
 * Each {@link Person}'s address in the system is associated with a fire station.
 * A fire station can be associated with multiple addresses,
 * meaning that a single station may appear multiple times in the system, each time with a different address.
 * Attributes:
 * - address: the address covered by the fire station
 * - station: the identification number of the fire station
 */
public class FireStation {
    private String address;

    private int station;

    public FireStation(@JsonProperty("address") String address, @JsonProperty("station") int station) {
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
        return this.address + this.station;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || this.getClass() != object.getClass()) return false;
        return Objects.equals(address, ((FireStation) object).getAddress()) &&
                Objects.equals(station, ((FireStation) object).getStation());
    }
}
