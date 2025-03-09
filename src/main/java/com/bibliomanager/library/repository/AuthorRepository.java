package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
/*
 @Repository: repository in the persistence layer and makes it eligible for Spring’s exception translation mechanism.

*/
public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE LOWER(a.authorFirstName) = LOWER(:firstName) AND LOWER(a.authorLastName) = LOWER(:lastName)")
    Optional<Author> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}

