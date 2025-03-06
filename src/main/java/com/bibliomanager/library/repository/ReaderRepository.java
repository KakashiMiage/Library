package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.Reader;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends CrudRepository<Reader, Long> {

    Optional<Reader> findByReaderUsername(String readerUsername);

    List<Reader> findByReaderName(String readerName);

}
