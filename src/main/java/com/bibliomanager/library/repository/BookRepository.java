package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    // Recherche basique par titre
    List<Book> findByBookTitleContainingIgnoreCase(String bookTitle);

    // Recherche par auteur (Spring Data génère la query)
    List<Book> findByAuthorAuthorId(Long authorId);

    // Recherche par genre (relation ManyToMany)
    List<Book> findByGenresGenreId(Long genreId);

    // Recherche par éditeur
    List<Book> findByEditorEditorId(Long editorId);

    // Recherche par type
    List<Book> findByTypeTypeId(Long typeId);

    // Recherche des livres ayant des reviews (pas besoin de @Query ici)
    List<Book> findByBookReviewsIsNotEmpty();

    // Recherche par mot clé (requête personnalisée obligatoire)
    @org.springframework.data.jpa.repository.Query(
            "SELECT b FROM Book b WHERE LOWER(b.bookTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "OR LOWER(b.bookDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@org.springframework.data.repository.query.Param("keyword") String keyword);

    // Retourne les livres avec un rating moyen >= minRating
    @org.springframework.data.jpa.repository.Query(
            "SELECT b FROM Book b JOIN b.bookReviews r " +
                    "GROUP BY b " +
                    "HAVING AVG(r.reviewRate) >= :minRating")
    List<Book> findTopRatedBooks(@org.springframework.data.repository.query.Param("minRating") double minRating);
}
