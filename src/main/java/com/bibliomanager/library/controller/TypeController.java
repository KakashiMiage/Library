package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Type;
import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class TypeController {

    @Autowired
    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    public ResponseEntity<List<Type>> getAllTypes() {
        return ResponseEntity.ok(typeService.findAllTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Type> getTypeById(@PathVariable("id") Long typeId) {
        return ResponseEntity.ok(typeService.getTypeById(typeId));
    }

    @PostMapping
    public ResponseEntity<Type> createType(@RequestBody Type type) {
        Type createdType = typeService.createType(type);
        return new ResponseEntity<>(createdType, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Type> updateType(@PathVariable("id") Long typeId, @RequestBody Type type) {
        Type updatedType = typeService.updateType(typeId, type);
        return ResponseEntity.ok(updatedType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable("id") Long typeId) {
        typeService.deleteType(typeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countTypes() {
        return ResponseEntity.ok(typeService.countTypes());
    }

    @GetMapping("/search/name")
    public ResponseEntity<Type> findTypeByName(@RequestParam String typeName) {
        Type type = typeService.findTypeByName(typeName);
        return ResponseEntity.ok(type);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable("id") Long typeId) {
        List<Book> books = typeService.getBooksByType(typeId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/genre/{genreId}")
    public ResponseEntity<List<Type>> getTypesByGenre(@PathVariable("genreId") Long genreId) {
        List<Type> types = typeService.getTypesByGenre(genreId);
        return ResponseEntity.ok(types);
    }
}
