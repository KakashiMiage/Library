package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Genre;
import org.springframework.stereotype.Repository;

@Repository
/*
 @Repository: repository in the persistence layer and makes it eligible for Spring’s exception translation mechanism.

*/
public interface GenreRepository extends CrudRepository<Genre, Long> {

}

