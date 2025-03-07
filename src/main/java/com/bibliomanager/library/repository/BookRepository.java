package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/*
 @Repository: repository in the persistence layer and makes it eligible for Spring’s exception translation mechanism.
*/
public interface BookRepository extends CrudRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE LOWER(b.bookTitle) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findByBookTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT b FROM Book b WHERE b.author.id = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT b FROM Book b WHERE b.genre.id = :genreId")
    List<Book> findByGenreId(@Param("genreId") Long genreId);

    @Query("SELECT b FROM Book b WHERE b.editor.id = :editorId")
    List<Book> findByEditorId(@Param("editorId") Long editorId);

    @Query("SELECT b FROM Book b WHERE b.type.id = :typeId")
    List<Book> findByTypeId(@Param("typeId") Long typeId);

    @Query("SELECT b FROM Book b WHERE SIZE(b.bookReviews) > 0")
    List<Book> findByBookReviewsIsNotEmpty();

    @Query("SELECT DISTINCT b FROM Book b JOIN b.bookReviews r WHERE r.reviewRate >= :minRating")
    List<Book> findByBookReviewsRatingGreaterThanEqual(@Param("minRating") int minRating);
}

