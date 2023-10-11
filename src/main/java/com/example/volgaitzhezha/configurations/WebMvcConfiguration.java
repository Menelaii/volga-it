package com.example.volgaitzhezha.configurations;

import com.example.volgaitzhezha.enums.converters.RentTypeConverter;
import com.example.volgaitzhezha.enums.converters.TransportTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TransportTypeConverter());
        registry.addConverter(new RentTypeConverter());
    }
}
