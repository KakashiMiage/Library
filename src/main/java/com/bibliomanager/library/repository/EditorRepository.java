package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Editor;
import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EditorRepository extends CrudRepository<Editor, Long> {

    Optional<Editor> findByEditorNameIgnoreCase(String editorName);

    Optional<Editor> findByEditorSIRET(Long editorSIRET);

    @Query("SELECT e FROM Editor e JOIN e.types t WHERE t.typeId = :typeId")
    List<Editor> findEditorsByType(@Param("typeId") Long typeId);

    @Query("SELECT b FROM Book b WHERE b.editor.editorId = :editorId")
    List<Book> findBooksByEditor(@Param("editorId") Long editorId);

    @Query("SELECT e FROM Editor e WHERE LOWER(e.editorName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Editor> searchEditors(@Param("keyword") String keyword);
}
