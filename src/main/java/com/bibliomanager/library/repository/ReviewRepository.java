package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    List<Review> findByBookIsbn(Long bookId);

    List<Review> findByUserUserId(Long userId);

    @Query("SELECT AVG(r.reviewRate) FROM Review r WHERE r.book.isbn = :bookId")
    Double getAverageRatingForBook(@Param("bookId") Long bookId);

    @Query("SELECT r.book FROM Review r GROUP BY r.book HAVING AVG(r.reviewRate) >= :minRating")
    List<?> getTopRatedBooks(@Param("minRating") double minRating);
}
