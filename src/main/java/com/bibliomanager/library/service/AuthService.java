package com.bibliomanager.library.service;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // ✅ Génère une clé sécurisée

    private static final long EXPIRATION_TIME = 7200 * 1000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String connect(String username, String password) {
        Optional<User> userRepo = userRepository.findByUserUsername(username);
        if (userRepo.isPresent()) {
            User user = userRepo.get();
            if (passwordEncoder.matches(password, user.getUserPassword())) {
                return generateToken(user);
            }
        }
        throw new RuntimeException("Invalid username or password");
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserUsername())
                .claim("groups", List.of("ROLE_" + user.getRole().name()))
                .claim("sessionId", UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
