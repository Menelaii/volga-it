package com.example.volgaitzhezha.enums.converters;

import com.example.volgaitzhezha.enums.TransportType;
import com.example.volgaitzhezha.exceptions.ApiRequestException;
import org.springframework.core.convert.converter.Converter;

public class TransportTypeConverter implements Converter<String, TransportType> {

    @Override
    public TransportType convert(String source) {
        try {
            return TransportType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid transport type: " + source);
        }
    }
}
