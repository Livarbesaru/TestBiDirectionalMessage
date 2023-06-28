package com.message.bidirectional.config;

import com.message.bidirectional.util.JwtCustomAuthenticationConverter;
import com.message.bidirectional.util.JwtCustomDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {

    private JwtCustomDecoder jwtCustomDecoder;
    private JwtCustomAuthenticationConverter jwtCustomAuthenticationConverter;
    @Autowired
    public WebSecurity(JwtCustomDecoder jwtCustomDecoder, JwtCustomAuthenticationConverter jwtCustomAuthenticationConverter){
        this.jwtCustomDecoder = jwtCustomDecoder;
        this.jwtCustomAuthenticationConverter = jwtCustomAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz)->{
                    authz.anyRequest().authenticated();
                });
        http.
                oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtCustomDecoder)
                                .jwtAuthenticationConverter(jwtCustomAuthenticationConverter)
                        )
                );
        return http.build();
    }
}
