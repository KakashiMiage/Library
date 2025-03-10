package com.bibliomanager.library.service;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.repository.UserRepository;
import com.bibliomanager.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AuthService authService;


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

    public List<User> getAllUsers() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return userRepository.findByUserUsername(username);
    }

    public List<User> getUserByName(String name) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return userRepository.findByUserName(name);
    }

    public long countUsers() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return userRepository.count();
    }

    public List<Long> getBooksReviewedByUser(Long userId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return reviewRepository.findByUser_UserId(userId)
                .stream()
                .map(review -> review.getBook().getIsbn())
                .distinct()
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        user.setUserPassword(hashPassword(user.getUserPassword())); // 🔥 Hachage du mot de passe SHA-256
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUserName(updatedUser.getUserName());
            user.setUserUsername(updatedUser.getUserUsername());
            user.setRole(updatedUser.getRole());
            if (!hashPassword(updatedUser.getUserPassword()).equals(user.getUserPassword())) {
                user.setUserPassword(hashPassword(updatedUser.getUserPassword()));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        userRepository.deleteById(id);
    }
}
