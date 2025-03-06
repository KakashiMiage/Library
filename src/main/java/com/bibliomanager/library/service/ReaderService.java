package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Reader;
import com.bibliomanager.library.repository.ReaderRepository;
import com.bibliomanager.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Reader> getAllReaders() {
        return (List<Reader>) readerRepository.findAll();
    }

    public Optional<Reader> getReaderById(Long id) {
        return readerRepository.findById(id);
    }

    public Optional<Reader> getReaderByUsername(String username) {
        return readerRepository.findByReaderUsername(username);
    }

    public List<Reader> getReaderByName(String name) {
        return readerRepository.findByReaderName(name);
    }

    public long countReaders() {
        return readerRepository.count();
    }

    public List<Long> getBooksReviewedByReader(Long readerId) {
        return reviewRepository.findByReader_ReaderId(readerId)
                .stream()
                .map(review -> review.getBook().getIsbn())
                .distinct()
                .collect(Collectors.toList());
    }

    public Reader createReader(Reader reader) {
        return readerRepository.save(reader);
    }

    public Reader updateReader(Long id, Reader updatedReader) {
        return readerRepository.findById(id).map(reader -> {
            reader.setReaderName(updatedReader.getReaderName());
            reader.setReaderUsername(updatedReader.getReaderUsername());
            return readerRepository.save(reader);
        }).orElseThrow(() -> new RuntimeException("Reader not found"));
    }

    public void deleteReader(Long id) {
        readerRepository.deleteById(id);
    }
}
