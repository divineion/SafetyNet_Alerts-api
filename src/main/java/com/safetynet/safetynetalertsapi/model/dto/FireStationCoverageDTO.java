package com.safetynet.safetynetalertsapi.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FireStationCoverageDTO {
    @JsonProperty("covered persons")
    private List<CoveredPersonDTO> coveredPersonsDTO;
    long adults;
    long children;

    public FireStationCoverageDTO(List<CoveredPersonDTO> coveredPersonsDTO, long adults, long children) {
        this.coveredPersonsDTO = coveredPersonsDTO;
        this.adults = adults;
        this.children = children;
    }

    public List<CoveredPersonDTO> getCoveredPersonsDTO() {
        return coveredPersonsDTO;
    }

    public long getAdults() {
        return adults;
    }

    public long getChildren() {
        return children;
    }
}
