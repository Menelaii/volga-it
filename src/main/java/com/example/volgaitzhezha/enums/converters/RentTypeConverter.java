package com.example.volgaitzhezha.enums.converters;

import com.example.volgaitzhezha.enums.RentType;
import org.springframework.core.convert.converter.Converter;

public class RentTypeConverter implements Converter<String, RentType> {

    @Override
    public RentType convert(String source) {
        try {
            return RentType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid rent type: " + source);
        }
    }
}
