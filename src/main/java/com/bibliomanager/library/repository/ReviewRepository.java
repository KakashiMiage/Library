package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

    // Récupérer toutes les reviews d'un livre
    List<Review> findByBookIsbn(Long bookId);

    // Récupérer toutes les reviews d'un user
    List<Review> findByUserUserId(Long userId);

    // Moyenne des notes pour un livre donné
    @Query("SELECT AVG(r.reviewRate) FROM Review r WHERE r.book.isbn = :bookId")
    Double getAverageRatingForBook(@Param("bookId") Long bookId);

    // Retourner les livres avec un rating moyen >= minRating
    @Query("SELECT r.book FROM Review r GROUP BY r.book HAVING AVG(r.reviewRate) >= :minRating")
    List<?> getTopRatedBooks(@Param("minRating") double minRating);
}
