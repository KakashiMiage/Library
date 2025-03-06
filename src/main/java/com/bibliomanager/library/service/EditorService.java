package com.bibliomanager.library.service;

import com.bibliomanager.library.repository.EditorRepository;
import org.springframework.stereotype.Service;

@Service
public class EditorService {
    private final EditorRepository editorRepository;

    public EditorService(EditorRepository editorRepository) {
        this.editorRepository = editorRepository;
    }
}
