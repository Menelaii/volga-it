package com.example.volgaitzhezha.enums.converters;

import com.example.volgaitzhezha.enums.TransportType;
import org.springframework.core.convert.converter.Converter;

public class TransportTypeConverter implements Converter<String, TransportType> {

    @Override
    public TransportType convert(String source) {
        return TransportType.fromValue(source);
    }
}
