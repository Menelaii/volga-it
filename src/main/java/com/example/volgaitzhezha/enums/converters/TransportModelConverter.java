package com.example.volgaitzhezha.enums.converters;

import com.example.volgaitzhezha.enums.TransportModel;
import org.springframework.core.convert.converter.Converter;

public class TransportModelConverter implements Converter<String, TransportModel> {

    @Override
    public TransportModel convert(String source) {
        try {
            return TransportModel.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transport model: " + source);
        }
    }
}
