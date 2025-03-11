package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Genre;
import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {

    // Recherche d'un genre par son nom (insensible à la casse)
    Optional<Genre> findByGenreNameIgnoreCase(String genreName);

    // Liste des livres associés à un genre donné (ManyToMany)
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.genreId = :genreId")
    List<Book> findBooksByGenre(@Param("genreId") Long genreId);
}
