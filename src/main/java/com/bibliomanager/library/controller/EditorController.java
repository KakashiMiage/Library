package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Editor;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editors")
public class EditorController {

    @Autowired
    private final EditorService editorService;

    public EditorController(EditorService editorService) {
        this.editorService = editorService;
    }

    @GetMapping
    public ResponseEntity<List<Editor>> getAllEditors() {
        return ResponseEntity.ok(editorService.findAllEditors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editor> getEditorById(@PathVariable("id") Long editorId) {
        return ResponseEntity.ok(editorService.getEditorById(editorId));
    }

    @PostMapping
    public ResponseEntity<Editor> createEditor(@RequestBody Editor editor) {
        Editor createdEditor = editorService.createEditor(editor);
        return new ResponseEntity<>(createdEditor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editor> updateEditor(@PathVariable("id") Long editorId, @RequestBody Editor editor) {
        Editor updatedEditor = editorService.updateEditor(editorId, editor);
        return ResponseEntity.ok(updatedEditor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditor(@PathVariable("id") Long editorId) {
        editorService.deleteEditor(editorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countEditors() {
        return ResponseEntity.ok(editorService.countEditors());
    }

    @GetMapping("/search/name")
    public ResponseEntity<Editor> getEditorByName(@RequestParam String name) {
        Editor editor = editorService.getEditorByName(name);
        return ResponseEntity.ok(editor);
    }

    @GetMapping("/search/type/{typeId}")
    public ResponseEntity<List<Editor>> getEditorsByType(@PathVariable("typeId") Long typeId) {
        List<Editor> editors = editorService.getEditorsByType(typeId);
        return ResponseEntity.ok(editors);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByEditor(@PathVariable("id") Long editorId) {
        List<Book> books = editorService.getBooksByEditor(editorId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Editor>> searchEditors(@RequestParam String keyword) {
        List<Editor> editors = editorService.searchEditors(keyword);
        return ResponseEntity.ok(editors);
    }
}
