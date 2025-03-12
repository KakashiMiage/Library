package com.bibliomanager.library.service;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Recherche de l'utilisateur en BDD
        User user = userRepository.findByUserUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // Log dev (tu peux l'enlever après)
        System.out.println("Authentification en cours pour l'utilisateur : " + username);

        // Construction de l'objet UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserUsername())
                .password("{noop}" + user.getUserPassword()) // {noop} -> pas de password encoder
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build();
    }
}
