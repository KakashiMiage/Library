package com.bibliomanager.library.service;

import com.bibliomanager.library.model.Editor;
import com.bibliomanager.library.model.Type;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.repository.EditorRepository;
import com.bibliomanager.library.repository.TypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class EditorService {

    @Autowired
    private final EditorRepository editorRepository;

    @Autowired
    private final TypeRepository typeRepository;

    @Autowired
    public EditorService(EditorRepository editorRepository, TypeRepository typeRepository) {
        this.editorRepository = editorRepository;
        this.typeRepository = typeRepository;
    }

    public Editor createEditor(Editor editor) {
        List<Type> completeTypes = new ArrayList<>();

        if (editor.getTypes() != null && !editor.getTypes().isEmpty()) {
            for (Type type : editor.getTypes()) {
                if (type.getTypeId() == null) {
                    throw new IllegalArgumentException("The typeId cannot be null");
                }

                Type loadedType = typeRepository.findById(type.getTypeId())
                        .orElseThrow(() -> new EntityNotFoundException("Type not found with id : " + type.getTypeId()));
                completeTypes.add(loadedType);
            }
        }

        editor.setTypes(completeTypes);
        return editorRepository.save(editor);
    }

    public Editor updateEditor(Long editorId, Editor updatedEditor) {
        Editor existingEditor = getEditorById(editorId);

        existingEditor.setEditorName(updatedEditor.getEditorName());
        existingEditor.setEditorSIRET(updatedEditor.getEditorSIRET());

        List<Type> completeTypes = new ArrayList<>();

        if (updatedEditor.getTypes() != null && !updatedEditor.getTypes().isEmpty()) {
            for (Type type : updatedEditor.getTypes()) {
                if (type.getTypeId() == null) {
                    throw new IllegalArgumentException("The typeId cannot be null");
                }

                Type loadedType = typeRepository.findById(type.getTypeId())
                        .orElseThrow(() -> new EntityNotFoundException("Type not found with id " + type.getTypeId()));
                completeTypes.add(loadedType);
            }
        }

        existingEditor.setTypes(completeTypes);

        return editorRepository.save(existingEditor);
    }

    public void deleteEditor(Long editorId) {
        Editor existingEditor = getEditorById(editorId);
        editorRepository.delete(existingEditor);
    }

    public List<Editor> findAllEditors() {
        return (List<Editor>) editorRepository.findAll();
    }

    public long countEditors() {
        return editorRepository.count();
    }

    public Editor getEditorById(Long editorId) {
        return editorRepository.findById(editorId)
                .orElseThrow(() -> new EntityNotFoundException("Editor not found with id : " + editorId));
    }

    public Editor getEditorByName(String editorName) {
        return editorRepository.findByEditorNameIgnoreCase(editorName)
                .orElseThrow(() -> new EntityNotFoundException("Editor not found with name : " + editorName));
    }

    public Editor getEditorBySiret(Long editorSiret) {
        return editorRepository.findByEditorSIRET(editorSiret)
                .orElseThrow(() -> new EntityNotFoundException("Editor not found with siret : " + editorSiret));
    }

    public List<Editor> getEditorsByType(Long typeId) {
        return editorRepository.findEditorsByType(typeId);
    }

    public List<Book> getBooksByEditor(Long editorId) {
        getEditorById(editorId);
        return editorRepository.findBooksByEditor(editorId);
    }

    public List<Editor> searchEditors(String keyword) {
        return editorRepository.searchEditors(keyword);
    }
}
