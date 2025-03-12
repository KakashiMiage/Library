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

                        // PUBLIC (lecture uniquement)
                        .requestMatchers(HttpMethod.GET,
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/genres/**",
                                "/api/types/**",
                                "/api/editors/**",
                                "/api/users/**",
                                "/api/reviews/**"
                        ).permitAll()

                        // PUBLIC : création utilisateur (inscription)
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // READER et ADMIN (gestion reviews et favoris)
                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasAnyRole("READER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{userId}/favorites/**").hasAnyRole("READER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}/favorites/**").hasAnyRole("READER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{userId}/favorites/**").hasAnyRole("READER", "ADMIN")

                        // ADMIN uniquement pour CRUD (création, modif, suppression)
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


                        // JSF + ressources statiques publiques
                        .requestMatchers(
                                "/login.xhtml", "/books.xhtml", "/authors.xhtml",
                                "/genres.xhtml", "/types.xhtml", "/editors.xhtml",
                                "/users.xhtml", "/reviews.xhtml",
                                "/javax.faces.resource/**"
                        ).permitAll()

                        // Autres requêtes authentifiées
                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    }
}
