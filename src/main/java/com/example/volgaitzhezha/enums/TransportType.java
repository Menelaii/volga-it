package com.example.volgaitzhezha.enums;

import com.example.volgaitzhezha.exceptions.ApiRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public enum TransportType {
    ALL("ALL"),
    CAR("CAR"),
    BIKE("BIKE"),
    SCOOTER("SCOOTER");

    private final String value;

    TransportType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TransportType fromValue(String value) {
        for (TransportType type : values()) {
            if (Objects.equals(type.value, value.toUpperCase())) {
                return type;
            }
        }

        throw new ApiRequestException("Нет такого вида транспорта: " + value);
    }
}
