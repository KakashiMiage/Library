package com.bibliomanager.library.config;

import com.bibliomanager.library.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/genres/**",
                                "/api/types/**",
                                "/api/editors/**",
                                "/api/users/**",
                                "/api/reviews/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasAnyRole("READER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{userId}/favorites/**").hasAnyRole("READER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}/favorites/**").hasAnyRole("READER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{userId}/favorites/**").hasAnyRole("READER", "ADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/genres/**",
                                "/api/editors/**",
                                "/api/types/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/genres/**",
                                "/api/editors/**",
                                "/api/types/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/genres/**",
                                "/api/editors/**",
                                "/api/types/**"
                        ).hasRole("ADMIN")


                        .requestMatchers(
                                "/login.xhtml", "/books.xhtml", "/authors.xhtml",
                                "/genres.xhtml", "/types.xhtml", "/editors.xhtml",
                                "/users.xhtml", "/reviews.xhtml",
                                "/javax.faces.resource/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    }
}
