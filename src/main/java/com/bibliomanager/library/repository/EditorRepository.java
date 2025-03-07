package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Editor;
import org.springframework.stereotype.Repository;

@Repository
/*
 @Repository: repository in the persistence layer and makes it eligible for Spring’s exception translation mechanism.

*/
public interface EditorRepository extends CrudRepository<Editor, Long> {

}

