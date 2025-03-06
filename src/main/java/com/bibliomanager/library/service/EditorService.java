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

    public Optional<Editor> getEditorById(Integer editorId) {
        return this.editorRepository.findById(editorId);
    }

    public Editor updateEditor(Integer editorId, Editor updatedEditor) {
        if (editorRepository.existsById(editorId)) {
            updatedEditor.setEditorId(editorId);
            return editorRepository.save(updatedEditor);
        }
        return null;
    }

    public void deleteEditor(Integer editorId) {
        this.editorRepository.deleteById(editorId);
    }

    public List<Editor> getEditorsByType(Integer typeId) {
        return this.editorRepository.getEditorsByType(typeId);
    }

    public List<Book> getBooksByEditor(Integer editorId) {
        return this.editorRepository.getBooksByEditor(editorId);
    }

    public List<Editor> searchEditors(String keyword) {
        return this.editorRepository.searchEditors(keyword);
    }
}
