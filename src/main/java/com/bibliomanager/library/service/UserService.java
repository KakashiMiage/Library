package com.bibliomanager.library.service;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Review;
import com.bibliomanager.library.repository.UserRepository;
import com.bibliomanager.library.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookRepository bookRepository;

    public UserService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public long countUsers() {
        return userRepository.count();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id " + userId));
    }

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = getUserById(userId);

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setUserUsername(updatedUser.getUserUsername());
        existingUser.setUserPassword(updatedUser.getUserPassword());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setFavoriteBooks(updatedUser.getFavoriteBooks());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        User existingUser = getUserById(userId);
        userRepository.delete(existingUser);
    }

    public List<User> getUsersByName(String name) {
        List<User> users = userRepository.findByUserNameIgnoreCase(name);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Aucun utilisateur trouvé avec le nom : " + name);
        }
        return users;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUserUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec le username : " + username));
    }

    public List<Review> getReviewsByUser(Long userId) {
        getUserById(userId);
        return userRepository.findReviewsByUser(userId);
    }

    public List<Book> getBooksReviewedByUser(Long userId) {
        getUserById(userId);
        return userRepository.findBooksReviewedByUser(userId);
    }

    public User addBookToFavorites(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Livre introuvable avec l'id " + bookId));

        user.getFavoriteBooks().add(book);

        return userRepository.save(user);
    }

    public User removeBookFromFavorites(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Livre introuvable avec l'id " + bookId));

        user.getFavoriteBooks().remove(book);

        return userRepository.save(user);
    }

    public List<Book> getFavoriteBooks(Long userId) {
        User user = getUserById(userId);
        return user.getFavoriteBooks();
    }

}
