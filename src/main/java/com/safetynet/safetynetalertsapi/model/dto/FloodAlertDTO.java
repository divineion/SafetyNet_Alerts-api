package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

public class FloodAlertDTO {
    public String address;

    public List<AlertPersonInfoDTO> persons;

    public FloodAlertDTO(String address, List<AlertPersonInfoDTO> persons) {
        this.address = address;
        this.persons = persons;
    }

    public String getAddress() {
        return address;
    }


    public List<AlertPersonInfoDTO> getPersons() {
        return persons;
    }
}
