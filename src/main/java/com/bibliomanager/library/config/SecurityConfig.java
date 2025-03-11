package com.bibliomanager.library.config;

import com.bibliomanager.library.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                .csrf(csrf -> csrf.disable()) // CSRF désactivé (optionnel en dev)
                .authorizeHttpRequests(auth -> auth

                        // ========================
                        // PUBLIC : API lecture et VUES publiques
                        // ========================
                        .requestMatchers(
                                "/api/books",
                                "/api/books/{id}",
                                "/api/books/search/**",
                                "/api/books/reviews/not-empty",
                                "/api/books/top-rated",

                                "/api/authors",
                                "/api/authors/{id}",
                                "/api/authors/search/**",
                                "/api/authors/{id}/books",

                                "/api/genres",
                                "/api/genres/{id}",
                                "/api/genres/search",
                                "/api/genres/{id}/books",

                                "/api/types",
                                "/api/types/{id}",
                                "/api/types/search/**",
                                "/api/types/{id}/books",

                                "/api/editors",
                                "/api/editors/{id}",
                                "/api/editors/search/**",
                                "/api/editors/{id}/books"
                        ).permitAll()

                        // ========================
                        // PUBLIC : VUES JSF ET RESSOURCES
                        // ========================
                        .requestMatchers(
                                "/login.xhtml",
                                "/books.xhtml",
                                "/authors.xhtml",
                                "/genres.xhtml",
                                "/types.xhtml",
                                "/editors.xhtml",
                                "/users.xhtml",
                                "/reviews.xhtml"
                        ).permitAll()

                        .requestMatchers(
                                "/javax.faces.resource/**"  // Ressources statiques JSF (css, js...)
                        ).permitAll()

                        // ========================
                        // READER + ADMIN (actions utilisateurs connectés)
                        // ========================
                        .requestMatchers(
                                "/api/reviews/**",
                                "/api/users/{userId}/favorites/**"
                        ).hasAnyRole("READER", "ADMIN")

                        // ========================
                        // ADMIN ONLY : CRUD complet
                        // ========================
                        .requestMatchers(
                                "/api/books/**",
                                "/api/authors/**",
                                "/api/genres/**",
                                "/api/editors/**",
                                "/api/types/**",
                                "/api/users/**"
                        ).hasRole("ADMIN")

                        // ========================
                        // TOUT AUTRE : authentification obligatoire
                        // ========================
                        .anyRequest().authenticated()
                )
                .userDetailsService(authService);


        return http.build();
    }
}
