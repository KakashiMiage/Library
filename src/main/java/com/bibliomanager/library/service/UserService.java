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

    // Récupérer tous les users
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    // Compter les users
    public long countUsers() {
        return userRepository.count();
    }

    // Créer un user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Trouver un user par ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id " + userId));
    }

    // Mettre à jour un user
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = getUserById(userId);

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setUserUsername(updatedUser.getUserUsername());
        existingUser.setUserPassword(updatedUser.getUserPassword());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setFavoriteBooks(updatedUser.getFavoriteBooks());

        return userRepository.save(existingUser);
    }

    // Supprimer un user
    public void deleteUser(Long userId) {
        User existingUser = getUserById(userId);
        userRepository.delete(existingUser);
    }

    // Trouver un user par son nom
    public List<User> getUsersByName(String name) {
        List<User> users = userRepository.findByUserNameIgnoreCase(name);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("Aucun utilisateur trouvé avec le nom : " + name);
        }
        return users;
    }

    // Trouver un user par son username (login)
    public User getUserByUsername(String username) {
        return userRepository.findByUserUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec le username : " + username));
    }

    // Récupérer toutes les reviews laissées par un user
    public List<Review> getReviewsByUser(Long userId) {
        getUserById(userId); // Vérifie que l'utilisateur existe
        return userRepository.findReviewsByUser(userId);
    }

    // Récupérer tous les livres sur lesquels un user a laissé une review
    public List<Book> getBooksReviewedByUser(Long userId) {
        getUserById(userId); // Vérifie que l'utilisateur existe
        return userRepository.findBooksReviewedByUser(userId);
    }

    // Ajouter un livre aux favoris d'un utilisateur
    public User addBookToFavorites(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Livre introuvable avec l'id " + bookId));

        // Ajouter le livre à la liste des favoris
        user.getFavoriteBooks().add(book);

        return userRepository.save(user);
    }

    // Retirer un livre des favoris d'un utilisateur
    public User removeBookFromFavorites(Long userId, Long bookId) {
        User user = getUserById(userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Livre introuvable avec l'id " + bookId));

        // Retirer le livre des favoris
        user.getFavoriteBooks().remove(book);

        return userRepository.save(user);
    }

    public List<Book> getFavoriteBooks(Long userId) {
        User user = getUserById(userId);  // vérifie que l'utilisateur existe
        return user.getFavoriteBooks();
    }

}
