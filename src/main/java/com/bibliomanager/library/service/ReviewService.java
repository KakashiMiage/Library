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

    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    public long countReviews() {
        return reviewRepository.count();
    }

    public Review createReview(Review review) {

        User user = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'id " + review.getUser().getUserId()));

        Book book = bookRepository.findById(review.getBook().getIsbn())
                .orElseThrow(() -> new EntityNotFoundException("Livre introuvable avec l'id " + review.getBook().getIsbn()));

        review.setUser(user);
        review.setBook(book);

        return reviewRepository.save(review);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id " + reviewId));
    }

    public Review updateReview(Long reviewId, Review updatedReview) {
        Review existingReview = getReviewById(reviewId);

        existingReview.setReviewRate(updatedReview.getReviewRate());
        existingReview.setReviewDescription(updatedReview.getReviewDescription());

        if (updatedReview.getUser() != null) {
            User user = userRepository.findById(updatedReview.getUser().getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id " + updatedReview.getUser().getUserId()));
            existingReview.setUser(user);
        }

        if (updatedReview.getBook() != null) {
            Book book = bookRepository.findById(updatedReview.getBook().getIsbn())
                    .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + updatedReview.getBook().getIsbn()));
            existingReview.setBook(book);
        }

        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long reviewId) {
        Review existingReview = getReviewById(reviewId);
        reviewRepository.delete(existingReview);
    }

    public List<Review> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBookIsbn(bookId);
    }

    public List<Review> getReviewsByReader(Long userId) {
        return reviewRepository.findByUserUserId(userId);
    }

    public Double getAverageRatingForBook(Long bookId) {
        Double average = reviewRepository.getAverageRatingForBook(bookId);
        if (average == null) {
            throw new EntityNotFoundException("No review found for the book with id " + bookId);
        }
        return average;
    }

    public List<?> getTopRatedBooks(int minRating) {
        return reviewRepository.getTopRatedBooks(minRating);
    }
}
