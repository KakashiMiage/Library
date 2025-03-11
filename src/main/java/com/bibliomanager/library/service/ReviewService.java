package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Review;
import com.bibliomanager.library.model.User;
import com.bibliomanager.library.repository.BookRepository;
import com.bibliomanager.library.repository.ReviewRepository;
import com.bibliomanager.library.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(
            ReviewRepository reviewRepository,
            BookRepository bookRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // 🔹 Récupérer toutes les reviews
    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    // 🔹 Compter le nombre de reviews
    public long countReviews() {
        return reviewRepository.count();
    }

    // 🔹 Créer une review
    public Review createReview(Review review) {
        // Vérifie que le user existe
        User user = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id " + review.getUser().getUserId()));

        // Vérifie que le book existe
        Book book = bookRepository.findById(review.getBook().getIsbn())
                .orElseThrow(() -> new EntityNotFoundException("Livre introuvable avec l'id " + review.getBook().getIsbn()));

        review.setUser(user);
        review.setBook(book);

        return reviewRepository.save(review);
    }

    // 🔹 Trouver une review par ID
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review introuvable avec l'id " + reviewId));
    }

    // 🔹 Mettre à jour une review
    public Review updateReview(Long reviewId, Review updatedReview) {
        Review existingReview = getReviewById(reviewId);

        existingReview.setReviewRate(updatedReview.getReviewRate());
        existingReview.setReviewDescription(updatedReview.getReviewDescription());

        // Optionnel : si tu permets de changer le User/Book d'une review
        if (updatedReview.getUser() != null) {
            User user = userRepository.findById(updatedReview.getUser().getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id " + updatedReview.getUser().getUserId()));
            existingReview.setUser(user);
        }

        if (updatedReview.getBook() != null) {
            Book book = bookRepository.findById(updatedReview.getBook().getIsbn())
                    .orElseThrow(() -> new EntityNotFoundException("Livre introuvable avec l'id " + updatedReview.getBook().getIsbn()));
            existingReview.setBook(book);
        }

        return reviewRepository.save(existingReview);
    }

    // 🔹 Supprimer une review
    public void deleteReview(Long reviewId) {
        Review existingReview = getReviewById(reviewId);
        reviewRepository.delete(existingReview);
    }

    // 🔹 Récupérer les reviews par livre
    public List<Review> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBookIsbn(bookId);
    }

    // 🔹 Récupérer les reviews par reader/user
    public List<Review> getReviewsByReader(Long userId) {
        return reviewRepository.findByUserUserId(userId);
    }

    // 🔹 Moyenne des ratings pour un livre
    public Double getAverageRatingForBook(Long bookId) {
        Double average = reviewRepository.getAverageRatingForBook(bookId);
        if (average == null) {
            throw new EntityNotFoundException("Aucune review trouvée pour le livre avec l'id " + bookId);
        }
        return average;
    }

    // 🔹 Livres avec une note moyenne >= minRating
    public List<?> getTopRatedBooks(int minRating) {
        return reviewRepository.getTopRatedBooks(minRating);
    }
}
