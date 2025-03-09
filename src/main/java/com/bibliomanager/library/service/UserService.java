package com.bibliomanager.library.service;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.repository.UserRepository;
import com.bibliomanager.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUserUsername(username);
    }

    public List<User> getUserByName(String name) {
        return userRepository.findByUserName(name);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public List<Long> getBooksReviewedByUser(Long userId) {
        return reviewRepository.findByUser_UserId(userId)
                .stream()
                .map(review -> review.getBook().getIsbn())
                .distinct()
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUserName(updatedUser.getUserName());
            user.setUserUsername(updatedUser.getUserUsername());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
