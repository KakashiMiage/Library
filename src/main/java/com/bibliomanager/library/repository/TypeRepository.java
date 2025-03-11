package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Type;
import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends CrudRepository<Type, Long> {

    // Recherche d'un type par son nom (insensible à la casse)
    Optional<Type> findByTypeNameIgnoreCase(String typeName);

    // Tous les livres d'un type spécifique
    @Query("SELECT b FROM Book b WHERE b.type.typeId = :typeId")
    List<Book> findBooksByType(@Param("typeId") Long typeId);

    // Optionnel : récupérer les types en fonction de leur association avec un genre
    @Query("SELECT t FROM Type t JOIN t.genres g WHERE g.genreId = :genreId")
    List<Type> findTypesByGenre(@Param("genreId") Long genreId);
}
