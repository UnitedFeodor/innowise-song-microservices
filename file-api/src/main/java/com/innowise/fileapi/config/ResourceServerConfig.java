package com.innowise.fileapi.config;

import com.innowise.fileapi.converter.JwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());

        return httpSecurity
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("/files/**").authenticated())
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(jwtCustomizer -> jwtCustomizer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }
}
