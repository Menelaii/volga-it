package com.example.volgaitzhezha.enums;

import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public enum RentType {
    MINUTES("MINUTES"),
    DAYS("DAYS");

    private final String value;

    RentType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RentType fromValue(String value) {
        for (RentType type : values()) {
            if (Objects.equals(type.value, value.toUpperCase())) {
                return type;
            }
        }

        throw new ApiRequestException("Нет такого вида аренды: " + value);
    }
}
