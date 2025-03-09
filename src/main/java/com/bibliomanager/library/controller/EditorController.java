package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Editor;
import com.bibliomanager.library.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EditorController {

    @Autowired
    private final EditorService editorService;

    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }

    @GetMapping("/editors")
    public ResponseEntity<Iterable<Editor>> findAllEditors() {
        return ResponseEntity.ok(this.editorService.findAllEditors());
    }

    @GetMapping("/editors/count")
    public ResponseEntity<Long> countEditors() {
        return ResponseEntity.ok(this.editorService.countEditors());
    }

    @GetMapping("/editors/name/{editorName}")
    public ResponseEntity<List<Editor>> getEditorByName(@PathVariable String editorName) {
        return ResponseEntity.ok(this.editorService.getEditorByName(editorName));
    }

    @PostMapping("/editors")
    public ResponseEntity<Editor> createEditor(@RequestBody Editor editor) {
        if (editor.getEditorType() == null || editor.getEditorType().getTypeId() == null) {
            throw new IllegalArgumentException("Editor type cannot be null or this editor doesn't exist");
        }
        return ResponseEntity.ok(this.editorService.createEditor(editor));
    }

    @GetMapping("/editors/{id}")
    public ResponseEntity<Optional<Editor>> getEditorById(@PathVariable Long id) {
        return ResponseEntity.ok(this.editorService.getEditorById(id));
    }
    
    @PutMapping("/editors/{id}")
    public ResponseEntity<Editor> updateEditor(@PathVariable Long id, @RequestBody Editor updatedEditor) {
        Editor editor = this.editorService.updateEditor(id, updatedEditor);
        return (editor != null) ? ResponseEntity.ok(editor) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/editors/{id}")
    public ResponseEntity<Void> deleteEditor(@PathVariable Long id) {
        this.editorService.deleteEditor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/editors/type/{typeId}")
    public ResponseEntity<List<Editor>> getEditorsByType(@PathVariable Long typeId) {
        return ResponseEntity.ok(this.editorService.getEditorsByType(typeId));
    }

    @GetMapping("/editors/{id}/books")
    public ResponseEntity<List<Book>> getBooksByEditor(@PathVariable Long id) {
        return ResponseEntity.ok(this.editorService.getBooksByEditor(id));
    }

    @GetMapping("/editors/search")
    public ResponseEntity<List<Editor>> searchEditors(@RequestParam String keyword) {
        return ResponseEntity.ok(this.editorService.searchEditors(keyword));
    }
}
