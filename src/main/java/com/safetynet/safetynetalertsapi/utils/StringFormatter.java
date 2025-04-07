package com.safetynet.safetynetalertsapi.utils;

import org.springframework.stereotype.Service;

@Service
public class StringFormatter {
    public String normalizeString(String string) {
        return string.toLowerCase().replace(" ", "");
    }
}
