package com.example.volgaitzhezha.security;

import com.example.volgaitzhezha.security.jwt.JwtFilter;
import com.example.volgaitzhezha.security.userDetails.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(conf -> conf.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/Account/Me").authenticated()
                        .requestMatchers("/api/Account/SignOut").authenticated()
                        .requestMatchers("/api/Account/Update").authenticated()
                        .requestMatchers("/api/Admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/Payment/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/Transport/**").authenticated()
                        .requestMatchers("/api/Rent/MyHistory").authenticated()
                        .requestMatchers("/api/Rent/TransportHistory").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/Rent/**").authenticated()
                        .requestMatchers("/swagger-ui.html", "/v2/api-docs", "/webjars/**").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(
                        (sessionManagement) -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder builder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return builder.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
