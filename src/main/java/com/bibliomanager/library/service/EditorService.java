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

    public EditorService(EditorRepository editorRepository) {
        this.editorRepository = editorRepository;
    }

    public Iterable<Editor> findAllEditors() {
        return this.editorRepository.findAll();
    }

    public long countEditors() {
        return this.editorRepository.count();
    }

    public List<Editor> getEditorByName(String editorName) {
        return this.editorRepository.findEditorByName(editorName);
    }

    public Editor createEditor(Editor editor) {
        return this.editorRepository.save(editor);
    }

    public Optional<Editor> getEditorById(Long editorId) {
        return this.editorRepository.findById(editorId);
    }

    public Editor updateEditor(Long editorId, Editor updatedEditor) {
        if (editorRepository.existsById(editorId)) {
            updatedEditor.setEditorId(editorId);
            return editorRepository.save(updatedEditor);
        }
        return null;
    }

    public void deleteEditor(Long editorId) {
        this.editorRepository.deleteById(editorId);
    }

    public List<Editor> getEditorsByType(Long typeId) {
        return this.editorRepository.getEditorsByType(typeId);
    }

    public List<Book> getBooksByEditor(Long editorId) {
        return this.editorRepository.getBooksByEditor(editorId);
    }

    public List<Editor> searchEditors(String keyword) {
        return this.editorRepository.searchEditors(keyword);
    }
}
