package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.model.Review;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        return ResponseEntity.ok(userService.countUsers());
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<User>> getUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.getUsersByName(name));
    }

    @GetMapping("/search/username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.getReviewsByUser(userId));
    }

    @GetMapping("/{id}/books-reviewed")
    public ResponseEntity<List<Book>> getBooksReviewedByUser(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.getBooksReviewedByUser(userId));
    }

    @PutMapping("/{userId}/favorites/{bookId}")
    public ResponseEntity<User> addBookToFavorites(@PathVariable Long userId, @PathVariable Long bookId) {
        User updatedUser = userService.addBookToFavorites(userId, bookId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}/favorites/{bookId}")
    public ResponseEntity<User> removeBookFromFavorites(@PathVariable Long userId, @PathVariable Long bookId) {
        User updatedUser = userService.removeBookFromFavorites(userId, bookId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<Book>> getFavoriteBooks(@PathVariable("userId") Long userId) {
        List<Book> favorites = userService.getFavoriteBooks(userId);
        return ResponseEntity.ok(favorites);
    }


}
