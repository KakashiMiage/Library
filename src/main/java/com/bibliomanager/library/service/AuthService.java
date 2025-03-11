package com.bibliomanager.library.service;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private static String currentUser = null;
    private static String currentUserRole = null;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage du mot de passe", e);
        }
    }

    public boolean login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUserUsernameIgnoreCase(username);
        if (userOpt.isPresent()) {
            String hashedPassword = hashPassword(password);
            if (hashedPassword.equals(userOpt.get().getUserPassword())) {
                currentUser = username;
                currentUserRole = userOpt.get().getRole().name();
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
        currentUserRole = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUsername() {
        return currentUser;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }

    public boolean isAdmin() {
        return false;
    }
}
