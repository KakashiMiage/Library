package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Review;
import com.bibliomanager.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AuthService authService;

    public List<Review> getAllReviews() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return (List<Review>) reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByUser(Long userId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return reviewRepository.findByUser_UserId(userId);
    }

    public List<Review> getReviewsByBook(Long isbn) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return reviewRepository.findByBook_Isbn(isbn);
    }

    public long countReviews() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return reviewRepository.count();
    }

    public Double getAverageRatingForBook(Long bookId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return reviewRepository.getAverageRatingForBook(bookId);
    }

    public List<Object[]> getTopRatedBooks(int minRating) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return reviewRepository.getTopRatedBooks(minRating);
    }

    public Review createReview(Review review) {
        if (!authService.isLoggedIn() || !"READER".equals(authService.getCurrentUserRole())) {
            throw new RuntimeException("You need to have role READER");
        }
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review updatedReview) {
        if (!authService.isLoggedIn() || !"READER".equals(authService.getCurrentUserRole())) {
            throw new RuntimeException("You need to have role READER");
        }
        return reviewRepository.findById(id).map(review -> {
            review.setReviewRate(updatedReview.getReviewRate());
            review.setReviewDescription(updatedReview.getReviewDescription());
            return reviewRepository.save(review);
        }).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public void deleteReview(Long id) {
        if (!authService.isLoggedIn() || !"READER".equals(authService.getCurrentUserRole())) {
            throw new RuntimeException("You need to have role READER");
        }
        reviewRepository.deleteById(id);
    }
}
