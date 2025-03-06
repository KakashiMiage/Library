package com.bibliomanager.library.controller;

import com.bibliomanager.library.model.Book;
import com.bibliomanager.library.model.Type;
import com.bibliomanager.library.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TypeController {

    @Autowired
    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping("/types")
    public ResponseEntity<Iterable<Type>> getAllTypes() {
        return ResponseEntity.ok(this.typeService.getAllTypes());
    }

    @GetMapping("/types/count")
    public ResponseEntity<Long> countTypes() {
        return ResponseEntity.ok(this.typeService.countTypes());
    }

    @GetMapping("/types/name/{typeName}")
    public ResponseEntity<List<Type>> findTypeByName(@PathVariable String typeName) {
        return ResponseEntity.ok(this.typeService.findTypeByName(typeName));
    }

    @PostMapping("/types")
    public ResponseEntity<Type> createType(@RequestBody Type type) {
        return ResponseEntity.ok(this.typeService.createType(type));
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<Optional<Type>> getTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.typeService.getTypeById(id));
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<Type> updateType(@PathVariable Integer id, @RequestBody Type updatedType) {
        Type type = this.typeService.updateType(id, updatedType);
        return (type != null) ? ResponseEntity.ok(type) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Integer id) {
        this.typeService.deleteType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types/{id}/books")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable Long id) {
        return ResponseEntity.ok(this.typeService.getBooksByType(id));
    }
}
