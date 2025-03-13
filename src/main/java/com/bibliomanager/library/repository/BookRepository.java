package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByBookTitleContainingIgnoreCase(String bookTitle);

    List<Book> findByAuthorAuthorId(Long authorId);

    List<Book> findByGenresGenreId(Long genreId);

    List<Book> findByEditorEditorId(Long editorId);

    List<Book> findByTypeTypeId(Long typeId);

    List<Book> findByBookReviewsIsNotEmpty();

    @Query("SELECT b FROM Book b WHERE b.bookTitleNormalized LIKE CONCAT('%', :keyword, '%')")
    List<Book> searchBooks(@Param("keyword") String keyword);


    @Query(
            "SELECT b FROM Book b JOIN b.bookReviews r " +
                    "GROUP BY b " +
                    "HAVING AVG(r.reviewRate) >= :minRating")
    List<Book> findTopRatedBooks(@Param("minRating") double minRating);
}
