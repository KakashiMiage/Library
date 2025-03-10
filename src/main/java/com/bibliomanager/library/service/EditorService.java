package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Editor;
import com.bibliomanager.library.repository.EditorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditorService {
    private final EditorRepository editorRepository;
    private final AuthService authService;

    public EditorService(EditorRepository editorRepository, AuthService authService) {
        this.editorRepository = editorRepository;
        this.authService = authService;
    }

    public Iterable<Editor> findAllEditors() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.findAll();
    }

    public long countEditors() {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.count();
    }

    public List<Editor> getEditorByName(String editorName) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.findEditorByName(editorName);
    }

    public Editor createEditor(Editor editor) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.save(editor);
    }

    public Optional<Editor> getEditorById(Long editorId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.findById(editorId);
    }

    public Editor updateEditor(Long editorId, Editor updatedEditor) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        if (editorRepository.existsById(editorId)) {
            updatedEditor.setEditorId(editorId);
            return editorRepository.save(updatedEditor);
        }
        return null;
    }

    public void deleteEditor(Long editorId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        this.editorRepository.deleteById(editorId);
    }

    public List<Editor> getEditorsByType(Long typeId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.getEditorsByType(typeId);
    }

    public List<Book> getBooksByEditor(Long editorId) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.getBooksByEditor(editorId);
    }

    public List<Editor> searchEditors(String keyword) {
        if (!authService.isLoggedIn()) {
            throw new RuntimeException("You need to be logged");
        }
        return this.editorRepository.searchEditors(keyword);
    }
}
