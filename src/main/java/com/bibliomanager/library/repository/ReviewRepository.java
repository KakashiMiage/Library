package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    List<Review> findByReader_ReaderId(Long readerId);

    List<Review> findByBook_Isbn(Long isbn);

    long count();

    @Query("SELECT AVG(r.reviewRate) FROM Review r WHERE r.book.isbn = :bookId")
    Double getAverageRatingForBook(Long bookId);

    @Query("SELECT r.book FROM Review r GROUP BY r.book HAVING AVG(r.reviewRate) >= :minRating")
    List<Object[]> getTopRatedBooks(int minRating);
}
