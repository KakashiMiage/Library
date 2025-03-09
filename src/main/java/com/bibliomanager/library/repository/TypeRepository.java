package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/*
 @Repository: repository in the persistence layer and makes it eligible for Spring’s exception translation mechanism.

*/
public interface TypeRepository extends CrudRepository<Type, Long> {
    @Query("SELECT COUNT(t) FROM Type t")
    long countTypes();

    @Query("SELECT t FROM Type t WHERE t.typeName = :typeName")
    List<Type> findTypeByName(@Param("typeName") String typeName);

    @Query("SELECT b FROM Book b WHERE b.type.typeId = :typeId")
    List<Book> getBooksByType(@Param("typeId") Long typeId);
}

