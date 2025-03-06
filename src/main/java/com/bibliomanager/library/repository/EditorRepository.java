package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Editor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/*
 @Repository: repository in the persistence layer and makes it eligible for Spring’s exception translation mechanism.

*/
public interface EditorRepository extends CrudRepository<Editor, Integer> {

    @Query("SELECT COUNT(e) FROM Editor e")
    long countEditors();

    @Query("SELECT e FROM Editor e WHERE e.editorName = :editorName")
    List<Editor> findEditorByName(@Param("editorName") String editorName);

    @Query("SELECT e FROM Editor e WHERE e.editorType.id = :typeId")
    List<Editor> getEditorsByType(@Param("typeId") Integer typeId);

    @Query("SELECT b FROM Book b WHERE b.editor.editorId = :editorId")
    List<Book> getBooksByEditor(@Param("editorId") Integer editorId);

    @Query("SELECT e FROM Editor e WHERE LOWER(e.editorName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Editor> searchEditors(@Param("keyword") String keyword);
}

