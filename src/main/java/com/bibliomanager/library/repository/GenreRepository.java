package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {

    @Query(value = "SELECT COUNT(g) FROM Genre g")
    long countGenres();

    @Query(value = "SELECT g FROM Genre g WHERE g.genreName = :genreName")
    List<Genre> findGenreByName(@Param("genreName") String genreName);

    @Query(value = "SELECT b FROM Book b WHERE b.genre.genreId = :genreId")
    List<Book> getBooksByGenre(@Param("genreId") String genreId);
}

