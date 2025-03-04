package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/*
 @Repository: repository in the persistence layer and makes it eligible for Spring’s exception translation mechanism.
*/
public interface BookRepository extends CrudRepository<Book, Integer> {

}

