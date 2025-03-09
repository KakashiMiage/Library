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

    public List<Review> getAllReviews() {
        return (List<Review>) reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByUser_UserId(userId);
    }

    public List<Review> getReviewsByBook(Long isbn) {
        return reviewRepository.findByBook_Isbn(isbn);
    }

    public long countReviews() {
        return reviewRepository.count();
    }

    public Double getAverageRatingForBook(Long bookId) {
        return reviewRepository.getAverageRatingForBook(bookId);
    }

    public List<Object[]> getTopRatedBooks(int minRating) {
        return reviewRepository.getTopRatedBooks(minRating);
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review updatedReview) {
        return reviewRepository.findById(id).map(review -> {
            review.setReviewRate(updatedReview.getReviewRate());
            review.setReviewDescription(updatedReview.getReviewDescription());
            return reviewRepository.save(review);
        }).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
