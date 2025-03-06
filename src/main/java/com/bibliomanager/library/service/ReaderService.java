package com.bibliomanager.library.service;

import com.bibliomanager.library.repository.ReaderRepository;
import org.springframework.stereotype.Service;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }
}
