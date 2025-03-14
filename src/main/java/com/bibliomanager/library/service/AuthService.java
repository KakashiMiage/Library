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

        User user = userRepository.findByUserUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + username));

        System.out.println("User authentication in progress : " + username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserUsername())
                .password("{noop}" + user.getUserPassword()) // {noop} -> pas de password encoder
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build();
    }
}
