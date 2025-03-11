package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Author;
import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE LOWER(a.authorFirstName) = LOWER(:firstName) AND LOWER(a.authorLastName) = LOWER(:lastName)")
    Optional<Author> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT b FROM Book b WHERE b.author.authorId = :authorId")
    List<Book> findBooksByAuthor(@Param("authorId") Long authorId);

    // Recherche d'auteurs par nom de famille uniquement
    List<Author> findByAuthorLastNameIgnoreCase(String lastName);
}
