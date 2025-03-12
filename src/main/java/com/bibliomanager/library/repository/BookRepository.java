package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT b FROM Book b WHERE b.bookTitleNormalized LIKE CONCAT('%', :keyword, '%')")
    List<Book> searchBooks(@Param("keyword") String keyword);


    // Retourne les livres avec un rating moyen >= minRating
    @Query(
            "SELECT b FROM Book b JOIN b.bookReviews r " +
                    "GROUP BY b " +
                    "HAVING AVG(r.reviewRate) >= :minRating")
    List<Book> findTopRatedBooks(@Param("minRating") double minRating);
}
